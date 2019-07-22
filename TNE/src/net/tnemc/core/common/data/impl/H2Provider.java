package net.tnemc.core.common.data.impl;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DatabaseConnector;
import com.github.tnerevival.core.db.SQLDatabase;
import com.github.tnerevival.core.db.sql.H2;
import com.github.tnerevival.core.db.sql.MySQL;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.AccountStatus;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.data.TNEDataProvider;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.currency.CurrencyEntry;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 9/7/2017.
 */
public class H2Provider extends TNEDataProvider {

  private String prefix = manager.getPrefix();

  private final String ID_LOAD = "SELECT uuid FROM " + prefix + "_ECOIDS WHERE username = ? LIMIT 1";
  private final String ID_LOAD_USERNAME = "SELECT username FROM " + prefix + "_ECOIDS WHERE uuid = ? LIMIT 1";
  private final String ID_SAVE = "INSERT INTO " + prefix + "_ECOIDS (username, uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?";
  private final String ID_DELETE = "DELETE FROM " + prefix + "_ECOIDS WHERE uuid = ?";
  private final String ACCOUNT_LOAD = "SELECT uuid, display_name, account_number, account_status, account_language, " +
      "joined_date, last_online, account_player FROM " + prefix + "_USERS WHERE " +
      "uuid = ? LIMIT 1";
  private final String ACCOUNT_SAVE = "INSERT INTO " + prefix + "_USERS (uuid, display_name, joined_date, " +
      "last_online, account_number, account_status, account_language, account_player) " +
      "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE display_name = ?, " +
      "joined_date = ?, last_online = ?, account_number = ?, account_status = ?, account_language = ?, " +
      "account_player = ?";
  private final String ACCOUNT_DELETE = "DELETE FROM " + prefix + "_USERS WHERE uuid = ?";
  private final String BALANCE_LOAD_INDIVIDUAL = "SELECT balance FROM " + prefix + "_BALANCES WHERE uuid = ? AND world = ? AND currency = ?";
  private final String BALANCE_LOAD_ALL = "SELECT world, currency, balance FROM " + prefix + "_BALANCES WHERE uuid = ?";
  private final String BALANCE_DELETE_INDIVIDUAL = "DELETE FROM " + prefix + "_BALANCES WHERE uuid = ? AND world = ? AND currency = ?";
  private final String BALANCE_SAVE = "INSERT INTO " + prefix + "_BALANCES (uuid, server_name, world, currency, balance) " +
      "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?";
  private final String BALANCE_SET_ALL = "UPDATE " + prefix + "_BALANCES SET balance = ? WHERE world = ?";
  private final String BALANCE_DELETE = "DELETE FROM " + prefix + "_BALANCES WHERE uuid = ?";

  private H2 sql;

  public H2Provider(DataManager manager) {
    super(manager);
    sql = new H2(manager);
  }

  @Override
  public String identifier() {
    return "h2";
  }

  @Override
  public boolean supportUpdate() {
    return true;
  }

  @Override
  public Boolean first() throws SQLException {
    String table = manager.getPrefix() + "_INFO";
    boolean first = true;

    SQLDatabase.open();
    try {
      first = !SQLDatabase.getDb().getConnection().getMetaData().getTables(null, null, table, null).next();
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return first;
  }

  @Override
  public Double version() throws SQLException {
    final String table = manager.getPrefix() + "_INFO";
    Double version = 0.0;

    SQLDatabase.open();
    try(ResultSet results = SQLDatabase.getDb().getConnection().createStatement().executeQuery("SELECT version FROM " + table + " WHERE id = 1 LIMIT 1;")) {

      if(results.first()) {
        version = Double.parseDouble(results.getString("version"));
      }
    }
    SQLDatabase.close();
    return version;
  }

  @Override
  public void initialize() throws SQLException {
    executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_INFO` (" +
        "`id` INTEGER NOT NULL UNIQUE," +
        "`version` VARCHAR(10)," +
        "`server_name` VARCHAR(100)" +
        ") ENGINE = INNODB;");
    executePreparedUpdate("INSERT INTO `" + manager.getPrefix() + "_INFO` (id, version, server_name) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE version = ?;",
        new Object[]{
            1,
            TNELib.instance().currentSaveVersion,
            TNE.instance().getServerName(),
            TNELib.instance().currentSaveVersion,
        });

    executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_ECOIDS` (" +
        "`username` VARCHAR(100)," +
        "`uuid` VARCHAR(36) UNIQUE" +
        ") ENGINE = INNODB;");

    executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_USERS` (" +
        "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
        "`display_name` VARCHAR(100)," +
        "`joined_date` BIGINT(60)," +
        "`last_online` BIGINT(60)," +
        "`account_number` INTEGER," +
        "`account_status` VARCHAR(60)," +
        "`account_language` VARCHAR(10) NOT NULL DEFAULT 'default'," +
        //"`account_pin` VARCHAR(60) NOT NULL DEFAULT 'TNEPIN'," +
        "`account_player` BOOLEAN" +
        ") ENGINE = INNODB;");

    executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_BALANCES` (" +
        "`uuid` VARCHAR(36) NOT NULL," +
        "`server_name` VARCHAR(100) NOT NULL," +
        "`world` VARCHAR(50) NOT NULL," +
        "`currency` VARCHAR(100) NOT NULL," +
        "`balance` DECIMAL(49,4)," +
        ") ENGINE = INNODB;");
    executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_BALANCES` ADD PRIMARY KEY(uuid, server_name, world, currency);");

    executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_TRANSACTIONS` (" +
        "`trans_id` VARCHAR(36) NOT NULL," +
        "`trans_initiator` VARCHAR(36)," +
        "`trans_initiator_balance` DECIMAL(49,4)," +
        "`trans_recipient` VARCHAR(36) NOT NULL," +
        "`trans_recipient_balance` DECIMAL(49,4)," +
        "`trans_type` VARCHAR(36) NOT NULL," +
        "`trans_world` VARCHAR(36) NOT NULL," +
        "`trans_time` BIGINT(60) NOT NULL," +
        "`trans_voided` BOOLEAN NOT NULL" +
        ") ENGINE = INNODB;");
    executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_TRANSACTIONS` ADD PRIMARY KEY(trans_id);");

    executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_CHARGES` (" +
        "`charge_transaction` VARCHAR(36) NOT NULL," +
        "`charge_player` VARCHAR(36) NOT NULL," +
        "`charge_currency` VARCHAR(100) NOT NULL," +
        "`charge_world` VARCHAR(36) NOT NULL," +
        "`charge_amount` DECIMAL(49,4) NOT NULL," +
        "`charge_type` VARCHAR(20) NOT NULL" +
        ") ENGINE = INNODB;");

    executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_BALANCES_HISTORY` (" +
        "`id` INTEGER NOT NULL auto_increment," +
        "`uuid` VARCHAR(36) NOT NULL," +
        "`server_name` VARCHAR(100) NOT NULL," +
        "`world` VARCHAR(50) NOT NULL," +
        "`currency` VARCHAR(100) NOT NULL," +
        "`balance` DECIMAL(49,4)" +
        ") ENGINE = INNODB;");
    executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_CHARGES` ADD PRIMARY KEY(charge_transaction, charge_player);");
    executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_BALANCES_HISTORY` ADD PRIMARY KEY(id);");
  }

  @Override
  public void update(Double version) throws SQLException {
    //Nothing to convert(?)
    if(version < TNE.instance().currentSaveVersion) {

      if(version < 1114.0) {
        H2.executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_BALANCES_HISTORY` (" +
            "`id` INTEGER NOT NULL AUTO_INCREMENT," +
            "`uuid` VARCHAR(36) NOT NULL," +
            "`server_name` VARCHAR(100) NOT NULL," +
            "`world` VARCHAR(50) NOT NULL," +
            "`currency` VARCHAR(100) NOT NULL," +
            "`balance` DECIMAL(49,4)" +
            ") ENGINE = INNODB;");
        H2.executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_BALANCES_HISTORY` ADD PRIMARY KEY(id);");

        H2.executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_BALANCES` ALTER COLUMN `balance` DECIMAL(49,4)");
        H2.executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_TRANSACTIONS` ALTER COLUMN `trans_initiator_balance` DECIMAL(49,4)");
        H2.executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_TRANSACTIONS` ALTER COLUMN `trans_recipient_balance` DECIMAL(49,4)");
        H2.executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_CHARGES` ALTER COLUMN `charge_amount` DECIMAL(49,4)");
      }

      /*if(version < 1116.0) {
        executeUpdate("ALTER TABLE `" + manager.getPrefix() + "_USERS ADD`account_pin` VARCHAR(60) NOT NULL DEFAULT 'TNEPIN' AFTER `account_language`");
      }*/
    }
  }

  @Override
  public DatabaseConnector connector() throws SQLException {
    return sql;
  }

  @Override
  public void save(Double version) throws SQLException {
    executePreparedUpdate("UPDATE " + manager.getPrefix() + "_INFO SET version = ? WHERE id = 1;",
        new Object[] { version });
    super.save(version);
  }

  @Override
  public void delete(Double version) throws SQLException {
    executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_ECOIDS;");
    executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_USERS;");
    executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_BALANCES;");
    executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_BALANCES_HISTORY;");
    executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_TRANSACTIONS;");
    executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_CHARGES;");
  }

  @Override
  public Boolean backupData() {
    return false;
  }

  /*@Override
  public void extract() {
    File file = new File(TNE.instance().getDataFolder(), "extracted.yml");
    if(!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        TNE.debug(e);
      }
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

    SQLDatabase.open();
    final String balanceTable = manager.getPrefix() + "_BALANCES";
    final String userTable = manager.getPrefix() + "_USERS";
    final String query = "SELECT " + balanceTable + ".uuid, " + userTable + ".display_name, " + balanceTable + ".balance, " + balanceTable + ".world, " + balanceTable + ".currency FROM " + balanceTable + " LEFT JOIN " + userTable + " ON " + balanceTable +
        ".uuid = " + userTable + ".uuid";

    try {
      try (Statement balStatement = SQLDatabase.getDb().getConnection().createStatement()) {


        try (ResultSet balResults = balStatement.executeQuery(query)) {

          while (balResults.next()) {
            ResultSetMetaData rsmd = balResults.getMetaData();

            System.out.println("Column: " + rsmd.getColumnName(0));
            System.out.println("Column: " + rsmd.getColumnName(1));
            System.out.println("Column: " + rsmd.getColumnName(2));
            System.out.println("Column: " + rsmd.getColumnName(3));
            System.out.println("Column: " + rsmd.getColumnName(4));

            final String username = balResults.getString(2);
            final BigDecimal balance = balResults.getBigDecimal("balance");
            final String world = balResults.getString("world");
            final String currency = balResults.getString("currency");
            String formattedUser = username.replaceAll("\\.", "!").replaceAll("\\-", "@").replaceAll("\\_", "%");

            BigDecimal finalBalance = balance;
            if (configuration.contains("Accounts." + formattedUser + ".Balances." + world + "." + currency)) {
              finalBalance = finalBalance.add(new BigDecimal(configuration.getString("Accounts." + formattedUser + ".Balances." + world + "." + currency)));
            }

            configuration.set("Accounts." + formattedUser + ".Balances." + world + "." + currency, finalBalance.toPlainString());
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    SQLDatabase.close();
  }*/

  @Override
  public String loadUsername(String identifier) throws SQLException {
    SQLDatabase.open();
    String username = null;
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(ID_LOAD_USERNAME)) {

      try(ResultSet results = H2.executePreparedQuery(statement, new Object[] {
          identifier
      })) {

        if(results.next()) {
          username = results.getString("username");
        }
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return username;
  }

  @Override
  public UUID loadID(String username) {
    UUID id = null;
    SQLDatabase.open();
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    StackTraceElement element = stackTrace[2];
    TNELib.debug("loadID called by: [Class: " + element.getClassName() + " via Method: " + element.getMethodName() + " at Line: " + element.getLineNumber());
    TNELib.debug("Username: " + username);
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(ID_LOAD)) {

      try(ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
          username
      })) {

        if(results.next()) {
          TNELib.debug("UUID IN DB: " + results.getString("uuid"));
          id = UUID.fromString(results.getString("uuid"));
        }
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    TNELib.debug("UUID TO RETURN: " + ((id == null)? "null" : id.toString()));
    SQLDatabase.close();
    return id;
  }

  @Override
  public Map<String, UUID> loadEconomyIDS() {
    Map<String, UUID> ids = new HashMap<>();

    SQLDatabase.open();

    String table = manager.getPrefix() + "_ECOIDS";
    try(Statement statement = SQLDatabase.getDb().getConnection().createStatement()) {

      try(ResultSet results = statement.executeQuery("SELECT username, uuid FROM " + table + ";")) {

        while (results.next()) {

          TNE.debug("Loading EcoID for " + results.getString("username"));
          ids.put(results.getString("username"), UUID.fromString(results.getString("uuid")));
        }
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    TNE.debug("Finished loading Eco IDS. Total: " + ids.size());
    return ids;
  }

  @Override
  public Map<String, UUID> loadEconomyAccountIDS() throws SQLException {
    Map<String, UUID> ids = new HashMap<>();

    SQLDatabase.open();

    String table = manager.getPrefix() + "_USERS";
    try(Statement statement = SQLDatabase.getDb().getConnection().createStatement()) {

      try(ResultSet results = statement.executeQuery("SELECT display_name, uuid FROM " + table + ";")) {

        while (results.next()) {

          TNE.debug("Loading EcoID for " + results.getString("display_name"));
          ids.put(results.getString("display_name"), UUID.fromString(results.getString("uuid")));
        }
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    TNE.debug("Finished loading Eco IDS. Total: " + ids.size());
    return ids;
  }

  @Override
  public int accountCount(String username) {
    StringBuilder builder = new StringBuilder();
    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement("SELECT uuid FROM " + manager.getPrefix() + "_USERS WHERE display_name = ?");
        ResultSet results = H2.executePreparedQuery(statement, new Object[] {
            username
        })) {

      while(results.next()) {
        if(builder.length() > 0) {
          builder.append(",");
        }
        builder.append(results.getString("uuid"));
      }
    } catch(SQLException ignore) {

    }
    SQLDatabase.close();
    return builder.toString().split(",").length;
  }

  @Override
  public void saveIDS(Map<String, UUID> ids) throws SQLException {
    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(ID_SAVE)) {
      for (Map.Entry<String, UUID> entry : ids.entrySet()) {
        if (entry.getKey() == null) {
          TNE.debug("Attempted saving id with null display name.");
          continue;
        }
        statement.setString(1, entry.getKey());
        statement.setString(2, entry.getValue().toString());
        statement.setString(3, entry.getKey());
        statement.addBatch();
      }
      statement.executeBatch();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
  }

  @Override
  public void saveID(String username, UUID id) throws SQLException {
    if(username == null) {
      TNE.debug("Attempted saving id with null display name.");
      return;
    }

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(ID_SAVE)) {
      statement.setObject(1, username);
      statement.setObject(2, id.toString());
      statement.setObject(3, username);
      statement.execute();
    }
    SQLDatabase.close();
  }

  @Override
  public void removeID(String username) throws SQLException {
    executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_ECOIDS WHERE username = ?", new Object[] { username });
  }

  @Override
  public void removeID(UUID id) throws SQLException {
    executePreparedUpdate(ID_DELETE, new Object[] { id.toString() });
  }

  @Override
  public Collection<TNEAccount> loadAccounts() {
    List<TNEAccount> accounts = new ArrayList<>();
    TNE.debug("Loading TNE Accounts");

    String table = manager.getPrefix() + "_USERS";
    List<UUID> userIDS = new ArrayList<>();

    SQLDatabase.open();
    try(Statement statement = SQLDatabase.getDb().getConnection().createStatement()) {

      try(ResultSet results = statement.executeQuery("SELECT uuid FROM " + table + ";")) {
        while (results.next()) {
          TNE.debug("Loading account with UUID of " + results.getString("uuid"));
          userIDS.add(UUID.fromString(results.getString("uuid")));
        }

        userIDS.forEach((id)->{
          TNEAccount account = loadAccount(id);
          if(account != null) accounts.add(account);
        });
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    TNE.debug("Finished loading Accounts. Total: " + accounts.size());
    return accounts;
  }

  @Override
  public TNEAccount loadAccount(UUID id) {
    TNEAccount account = null;

    TNE.debug("Load Account Timing");
    long startTime = System.nanoTime();
    SQLDatabase.open();
    try {
      try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(ACCOUNT_LOAD)) {
        try(ResultSet results = H2.executePreparedQuery(statement, new Object[] { id.toString() })) {

          if (results.next()) {
            account = new TNEAccount(UUID.fromString(results.getString("uuid")),
                results.getString("display_name"));

            account.setAccountNumber(results.getInt("account_number"));
            account.setStatus(AccountStatus.fromName(results.getString("account_status")));
            account.setLanguage(results.getString("account_language"));
            //account.setPin(results.getString("account_pin"));
            account.setJoined(results.getLong("joined_date"));
            account.setLastOnline(results.getLong("last_online"));
            account.setPlayerAccount(results.getBoolean("account_player"));
          }
        }
      }

      TNE.debug("Load account info time: " + ((System.nanoTime() - startTime) / 1000000));

    } catch(Exception e) {
      TNE.debug(e);
    }
    TNE.debug("Load account time: " + ((System.nanoTime() - startTime) / 1000000));
    SQLDatabase.close();
    return account;
  }

  @Override
  public void saveAccounts(List<TNEAccount> accounts) {
    SQLDatabase.open();
    try(PreparedStatement accountStatement = SQLDatabase.getDb().getConnection().prepareStatement(ACCOUNT_SAVE)) {
      for (TNEAccount account : accounts) {
        if (account.displayName() == null) {
          TNE.debug("Attempted saving account with null display name.");
          continue;
        }
        accountStatement.setString(1, account.identifier().toString());
        accountStatement.setString(2, account.displayName());
        accountStatement.setLong(3, account.getJoined());
        accountStatement.setLong(4, account.getLastOnline());
        accountStatement.setInt(5, account.getAccountNumber());
        accountStatement.setString(6, account.getStatus().getName());
        accountStatement.setString(7, account.getLanguage());
        //accountStatement.setString(8, account.getPin());
        accountStatement.setBoolean(8, account.playerAccount());
        accountStatement.setString(9, account.displayName());
        accountStatement.setLong(10, account.getJoined());
        accountStatement.setLong(11, account.getLastOnline());
        accountStatement.setInt(12, account.getAccountNumber());
        accountStatement.setString(13, account.getStatus().getName());
        accountStatement.setString(14, account.getLanguage());
        //accountStatement.setString(16, account.getPin());
        accountStatement.setBoolean(15, account.playerAccount());
        accountStatement.addBatch();
      }
      accountStatement.executeBatch();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
  }

  @Override
  public void saveAccount(TNEAccount account) throws SQLException {
    if(account.displayName() == null) {
      TNE.debug("Attempted saving account with null display name.");
      return;
    }
    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(ACCOUNT_SAVE)) {
      statement.setObject(1, account.identifier().toString());
      statement.setObject(2, account.displayName());
      statement.setObject(3, account.getJoined());
      statement.setObject(4, account.getLastOnline());
      statement.setObject(5, account.getAccountNumber());
      statement.setObject(6, account.getStatus().getName());
      statement.setObject(7, account.getLanguage());
      //statement.setObject(8, account.getPin());
      statement.setObject(8, account.playerAccount());
      statement.setObject(9, account.displayName());
      statement.setObject(10, account.getJoined());
      statement.setObject(11, account.getLastOnline());
      statement.setObject(12, account.getAccountNumber());
      statement.setObject(13, account.getStatus().getName());
      statement.setObject(14, account.getLanguage());
      //statement.setObject(16, account.getPin());
      statement.setObject(15, account.playerAccount());

      statement.executeUpdate();
    } catch(Exception ignore) {

    }
    SQLDatabase.close();
  }

  @Override
  public Map<String, BigDecimal> loadAllBalances(UUID id) throws SQLException {
    Map<String, BigDecimal> balances = new HashMap<>();

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(BALANCE_LOAD_ALL)) {

      statement.setObject(1, id.toString());

      try(ResultSet results = statement.executeQuery()) {

        while(results.next()) {
          String builder = results.getString("world") +
              "@" +
              results.getString("currency");
          balances.put(builder, results.getBigDecimal("balance"));
        }
      }
    }
    SQLDatabase.close();
    return balances;
  }

  @Override
  public BigDecimal loadBalance(UUID id, String world, String currency) throws SQLException {
    BigDecimal balance = null;
    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(BALANCE_LOAD_INDIVIDUAL)) {

      statement.setObject(1, id.toString());
      statement.setObject(2, world);
      statement.setObject(3, currency);

      try(ResultSet results = statement.executeQuery()) {

        if(results.next()) {
          balance = results.getBigDecimal("balance");
        }
      }
    }
    SQLDatabase.close();
    return balance;
  }

  @Override
  public void saveBalance(UUID id, String world, String currency, BigDecimal balance) throws SQLException {
    SQLDatabase.open();
    TNE.debug("Start saveBalance");
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(BALANCE_SAVE)) {
      final String server = (TNE.manager().currencyManager().get(world, currency) != null)?
          TNE.manager().currencyManager().get(world, currency).getServer() :
          TNE.instance().getServerName();

      statement.setObject(1, id.toString());
      statement.setObject(2, server);
      statement.setObject(3, world);
      statement.setObject(4, currency);
      statement.setObject(5, balance);
      statement.setObject(6, balance);
      statement.executeUpdate();
    }
    SQLDatabase.close();
    TNE.debug("End saveBalance");
  }

  @Override
  public void setAllBalance(String world, BigDecimal balance) throws SQLException {
    SQLDatabase.open();

    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(BALANCE_SET_ALL)) {
      final String server = TNE.instance().getServerName();

      statement.setObject(1, balance);
      statement.setObject(2, world);
      statement.setObject(3, server);
      statement.executeUpdate();
    }

    SQLDatabase.close();
  }

  @Override
  public void deleteBalance(UUID id, String world, String currency) throws SQLException {
    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(BALANCE_DELETE_INDIVIDUAL)) {
      statement.setObject(1, id.toString());
      statement.setObject(2, world);
      statement.setObject(3, currency);

      statement.executeUpdate();
    }
    SQLDatabase.close();
  }

  @Override
  public void deleteAccount(UUID id) throws SQLException {
    executePreparedUpdate(ID_DELETE, new Object[] { id.toString() });
    executePreparedUpdate(ACCOUNT_DELETE, new Object[] { id.toString() });
    executePreparedUpdate(BALANCE_DELETE, new Object[] { id.toString() });
  }

  @Override
  public TNETransaction loadTransaction(UUID id) {
    String table = manager.getPrefix() + "_TRANSACTIONS";
    String chargesTable = manager.getPrefix() + "_CHARGES";
    TNETransaction transaction = null;

    TNE.debug("Load Transaction: " + id.toString());

    SQLDatabase.open();
    try(PreparedStatement transactionStatement = SQLDatabase.getDb().getConnection().prepareStatement("SELECT trans_id, trans_initiator, trans_recipient, trans_world, trans_type, trans_time, trans_initiator_balance, trans_recipient_balance FROM `" + table + "` WHERE trans_id = ? LIMIT 1");
        ResultSet transResults = MySQL.executePreparedQuery(transactionStatement, new Object[] { id.toString() });
        PreparedStatement chargeStatement = SQLDatabase.getDb().getConnection().prepareStatement("SELECT charge_player, charge_world, charge_amount, charge_type, charge_currency FROM `" + chargesTable + "` WHERE charge_transaction = ?");
        ResultSet chargesResults = MySQL.executePreparedQuery(chargeStatement, new Object[] {
            id.toString()
        })) {

      if (transResults.next()) {
        TNE.debug("transResults.next()");
        transaction = new TNETransaction(UUID.fromString(transResults.getString("trans_id")),
            UUID.fromString(transResults.getString("trans_initiator")),
            UUID.fromString(transResults.getString("trans_recipient")),
            transResults.getString("trans_world"),
            TNE.transactionManager().getType(transResults.getString("trans_type").toLowerCase()),
            transResults.getLong("trans_time"));
        TNE.debug("Returning null transaction?: " + (transaction == null));

        while (chargesResults.next()) {
          TNE.debug("chargesResults.next()");
          String player = chargesResults.getString("charge_player");
          boolean initiator = player.equalsIgnoreCase(transaction.initiator());
          String world = chargesResults.getString("charge_world");
          BigDecimal amount = chargesResults.getBigDecimal("charge_amount");
          String chargeType = chargesResults.getString("charge_type");
          String currency = chargesResults.getString("charge_currency");

          TransactionCharge charge = new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency), amount, TransactionChargeType.valueOf(chargeType));

          if(initiator) {
            transaction.setInitiatorCharge(charge);
            transaction.setInitiatorBalance(new CurrencyEntry(world, TNE.manager().currencyManager().get(world, currency),
                transResults.getBigDecimal("trans_initiator_balance")));
          } else {
            transaction.setRecipientCharge(charge);
            transaction.setRecipientBalance(new CurrencyEntry(world, TNE.manager().currencyManager().get(world, currency),
                transResults.getBigDecimal("trans_recipient_balance")));
          }
        }
        TNE.debug("Returning null transaction?: " + (transaction == null));
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    TNE.debug("Returning null transaction?: " + (transaction == null));
    return transaction;
  }

  @Override
  public Collection<TNETransaction> loadTransactions() {
    List<TNETransaction> transactions = new ArrayList<>();

    String table = manager.getPrefix() + "_TRANSACTIONS";
    List<UUID> transactionIDS = new ArrayList<>();

    SQLDatabase.open();
    try(Statement statement = SQLDatabase.getDb().getConnection().createStatement()) {
      try(ResultSet results = H2.executeQuery(statement,"SELECT trans_id FROM " + table + ";")) {

        while (results.next()) {
          transactionIDS.add(UUID.fromString(results.getString("trans_id")));
        }
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    transactionIDS.forEach((id)->{
      TNETransaction transaction = loadTransaction(id);
      if(transaction != null) transactions.add(transaction);
    });
    return transactions;
  }

  @Override
  public void saveTransaction(TNETransaction transaction) throws SQLException {
    String table = manager.getPrefix() + "_TRANSACTIONS";
    executePreparedUpdate("INSERT INTO `" + table + "` (trans_id, trans_initiator, trans_initiator_balance, trans_recipient, trans_recipient_balance, trans_type, trans_world, trans_time, trans_voided) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE trans_recipient = ?, trans_world = ?, trans_voided = ?",
        new Object[]{
            transaction.transactionID().toString(),
            transaction.initiator(),
            (transaction.initiatorBalance() != null)? transaction.initiatorBalance().getAmount() : BigDecimal.ZERO,
            transaction.recipient(),
            (transaction.recipientBalance() != null)? transaction.recipientBalance().getAmount() : BigDecimal.ZERO,
            transaction.type().name().toLowerCase(),
            transaction.getWorld(),
            transaction.time(),
            transaction.voided(),
            transaction.recipient(),
            transaction.getWorld(),
            transaction.voided()
        }
    );

    table = manager.getPrefix() + "_CHARGES";
    if(transaction.initiatorCharge() != null) {
      executePreparedUpdate("INSERT INTO `" + table + "` (charge_transaction, charge_player, charge_currency, charge_world, charge_amount, charge_type) " +
              "VALUES(?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE charge_world = ?, charge_amount = ?, charge_type = ?",
          new Object[]{
              transaction.transactionID().toString(),
              transaction.initiator(),
              transaction.initiatorCharge().getCurrency().name(),
              transaction.initiatorCharge().getWorld(),
              transaction.initiatorCharge().getAmount(),
              transaction.initiatorCharge().getType().name(),
              transaction.initiatorCharge().getWorld(),
              transaction.initiatorCharge().getAmount(),
              transaction.initiatorCharge().getType().name()
          }
      );
    }

    if(transaction.recipientCharge() != null) {
      executePreparedUpdate("INSERT INTO `" + table + "` (charge_transaction, charge_player, charge_currency, charge_world, charge_amount, charge_type) " +
              "VALUES(?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE charge_world = ?, charge_amount = ?, charge_type = ?",
          new Object[]{
              transaction.transactionID().toString(),
              transaction.recipient(),
              transaction.recipientCharge().getCurrency().name(),
              transaction.recipientCharge().getWorld(),
              transaction.recipientCharge().getAmount(),
              transaction.recipientCharge().getType().name(),
              transaction.recipientCharge().getWorld(),
              transaction.recipientCharge().getAmount(),
              transaction.recipientCharge().getType().name()
          }
      );
    }
  }

  @Override
  public void deleteTransaction(UUID id) throws SQLException {
    executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_TRANSACTIONS WHERE trans_id = ? ", new Object[] { id.toString() });
    executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_CHARGES WHERE charge_transaction = ? ", new Object[] { id.toString() });
  }

  @Override
  public String nullAccounts() throws SQLException {
    return "0";
  }

  @Override
  public int balanceCount(String world, String currency, int limit) throws SQLException {
    final String balanceTable = manager.getPrefix() + "_BALANCES";
    final String userTable = manager.getPrefix() + "_USERS";
    int count = 0;

    SQLDatabase.open();
    final String complex = "SELECT count(*) FROM " + balanceTable + " LEFT JOIN " + userTable + " ON " + balanceTable +
        ".uuid = " + userTable + ".uuid WHERE " + balanceTable + ".world = ? AND " + balanceTable +
        ".currency = ? AND " + userTable + ".display_name NOT LIKE '" + TNE.instance().api().getString("Core.Server.ThirdParty.Town") +
        "%' AND " + userTable + ".display_name NOT LIKE '" + TNE.instance().api().getString("Core.Server.ThirdParty.Nation") + "%' AND "
        + userTable + ".display_name NOT LIKE '" + TNE.instance().api().getString("Core.Server.ThirdParty.Faction") +
        "%' AND " + userTable + ".display_name NOT LIKE 'towny-%';";

    final String query = (TNE.instance().api().getBoolean("Core.Server.ThirdParty.TopThirdParty"))?
        "SELECT count(*) FROM " + balanceTable + " WHERE world = ? AND currency = ?;" :
        complex;

    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(query);
        ResultSet results = H2.executePreparedQuery(statement, new Object[] { world, currency })) {

      while(results.next()) {
        count = results.getInt(1);
      }
    } catch (SQLException e) {
      TNE.debug(e);
    }

    SQLDatabase.close();
    if(count > 0) {
      return (count % limit > 0)? (count / limit) + 1 : count / limit;
    }
    return count;
  }

  //Page 1 = 0 -> 9
  //Page 2 = 10 -> 19
  //Page 3 = 20 -> 29
  //Page 4 = 30 -> 39
  @Override
  public LinkedHashMap<UUID, BigDecimal> topBalances(String world, String currency, int limit, int page) throws SQLException {
    LinkedHashMap<UUID, BigDecimal> balances = new LinkedHashMap<>();

    final String balanceTable = manager.getPrefix() + "_BALANCES";
    final String userTable = manager.getPrefix() + "_USERS";

    int start = 0;
    if(page > 1) start = ((page - 1) * limit);

    final String complex = "SELECT TNE_BALANCES.uuid, TNE_USERS.display_name, TNE_BALANCES.balance, TNE_BALANCES.world, TNE_BALANCES.currency FROM " + balanceTable + " LEFT JOIN " + userTable + " ON " + balanceTable +
        ".uuid = " + userTable + ".uuid WHERE " + balanceTable + ".world = ? AND " + balanceTable +
        ".currency = ? AND " + userTable + ".display_name NOT LIKE '" + TNE.instance().api().getString("Core.Server.ThirdParty.Town") +
        "%' AND " + userTable + ".display_name NOT LIKE '" + TNE.instance().api().getString("Core.Server.ThirdParty.Nation") + "%' AND "
        + userTable + ".display_name NOT LIKE '" + TNE.instance().api().getString("Core.Server.ThirdParty.Faction") +
        "%' AND " + userTable + ".display_name NOT LIKE 'towny-%' ORDER BY TNE_BALANCES.balance DESC LIMIT ?,?;";

    final String query = (TNE.instance().api().getBoolean("Core.Server.ThirdParty.TopThirdParty"))?
        "SELECT uuid, balance FROM " + balanceTable + " WHERE world = ? AND currency = ? ORDER BY balance DESC LIMIT ?,?;" :
        complex;

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(query);
        ResultSet results = H2.executePreparedQuery(statement, new Object[] {
            world, currency, start, limit
        })) {

      while (results.next()) {
        balances.put(UUID.fromString(results.getString("uuid")), results.getBigDecimal("balance"));
      }
    } catch (SQLException e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return balances;
  }

  @Override
  public void createTables(List<String> tables) throws SQLException {
    for(String table : tables) {
      executeUpdate(table);
    }
  }

  @Override
  public int transactionCount(UUID recipient, String world, String type, String time, int limit) throws SQLException {
    StringBuilder queryBuilder = new StringBuilder();
    LinkedList<Object> values = new LinkedList<>();
    queryBuilder.append("SELECT count(*) FROM " + manager.getPrefix() + "_TRANSACTIONS WHERE trans_recipient = ?");
    values.add(recipient.toString());
    if(!world.equalsIgnoreCase("all")) {
      queryBuilder.append(" AND trans_world = ?");
      values.add(world);
    }

    if(!type.equalsIgnoreCase("all") && TNE.transactionManager().getType(type.toLowerCase()) != null) {
      queryBuilder.append(" AND trans_type = ?");
      values.add(type);
    }

    if(!time.equalsIgnoreCase("all")) {
      queryBuilder.append(" AND trans_time >= ?");
      values.add(time);
    }

    int count = 0;
    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(queryBuilder.toString());
        ResultSet results = H2.executePreparedQuery(statement, values.toArray())) {

      while(results.next()) {
        count = results.getInt(1);
      }
    } catch (SQLException e) {
      TNE.debug(e);
    }

    if(count > 0) {
      SQLDatabase.close();
      return (int)Math.ceil(count / limit);
    }
    SQLDatabase.close();
    return count;
  }

  @Override
  public LinkedHashMap<UUID, TNETransaction> transactionHistory(UUID recipient, String world, String type, String time, int limit, int page) throws SQLException {
    LinkedHashMap<UUID, TNETransaction> transactions = new LinkedHashMap<>();

    StringBuilder queryBuilder = new StringBuilder();
    LinkedList<Object> values = new LinkedList<>();
    queryBuilder.append("SELECT trans_id FROM " + manager.getPrefix() + "_TRANSACTIONS WHERE trans_recipient = ?");
    values.add(recipient.toString());
    if(!world.equalsIgnoreCase("all")) {
      queryBuilder.append(" AND trans_world = ?");
      values.add(world);
    }

    if(!type.equalsIgnoreCase("all") && TNE.transactionManager().getType(type.toLowerCase()) != null) {
      queryBuilder.append(" AND trans_type = ?");
      values.add(type);
    }

    if(!time.equalsIgnoreCase("all")) {
      queryBuilder.append(" AND trans_time >= ?");
      values.add(time);
    }


    int start = 1;
    if(page > 1) start = ((page - 1) * limit) + 1;
    queryBuilder.append(" ORDER BY trans_time DESC LIMIT ?,?;");
    values.add(start);
    values.add(limit);

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(queryBuilder.toString())) {

      final Object[] valuesArray = values.toArray(new Object[values.size()]);

      for (int i = 0; i < valuesArray.length; i++) {
        statement.setObject((i + 1), valuesArray[i]);
      }

      ResultSet results = statement.executeQuery();

      while (results.next()) {
        UUID id = UUID.fromString(results.getString("trans_id"));
        transactions.put(id, loadTransaction(id));
      }
      results.close();
    } catch (SQLException ignore) {}
    SQLDatabase.close();
    return transactions;
  }

  public void executeUpdate(String query) {
    SQLDatabase.open();
    try(Statement statement = SQLDatabase.getDb().getConnection().createStatement()) {
      statement.executeUpdate(query);
    } catch (SQLException e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
  }

  public void executePreparedUpdate(String query, Object[] variables) {
    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(query)) {

      for (int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      statement.executeUpdate();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
  }
}