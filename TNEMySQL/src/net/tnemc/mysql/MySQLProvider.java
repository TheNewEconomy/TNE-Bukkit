package net.tnemc.mysql;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DatabaseConnector;
import com.github.tnerevival.core.db.sql.MySQL;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.AccountStatus;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldHoldings;
import net.tnemc.core.common.data.TNEDataProvider;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.currency.CurrencyEntry;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
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
public class MySQLProvider extends TNEDataProvider {

  private String prefix = manager.getPrefix();

  private final String ID_LOAD = "SELECT uuid FROM " + prefix + "_ECOIDS WHERE username = ? LIMIT 1";
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
  private final String BALANCE_LOAD = "SELECT world, currency, balance FROM " + prefix + "_BALANCES WHERE uuid = ?";
  private final String BALANCE_SAVE = "INSERT INTO " + prefix + "_BALANCES (uuid, server_name, world, currency, balance) " +
                                      "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?";
  private final String BALANCE_DELETE = "DELETE FROM " + prefix + "_BALANCES WHERE uuid = ?";
  private final String HISTORY_SAVE = "INSERT INTO " + prefix + "_BALANCES_HISTORY (uuid, server_name, world, currency, balance) VALUES(?, ?, ?, ?, ?)";
  private final String TRANSACTION_LOAD = "";
  private final String TRANSACTION_SAVE = "";
  private final String TRANSACTIONS_DELETE = "";

  private MySQL sql;

  public MySQLProvider(DataManager manager) {
    super(manager);
    sql = new MySQL(manager);
  }

  @Override
  public String identifier() {
    return "mysql";
  }

  @Override
  public boolean supportUpdate() {
    return true;
  }

  @Override
  public Boolean first() {
    Connection connection;
    ResultSet result;
    String table = manager.getPrefix() + "_INFO";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://" + manager.getHost() + ":" + manager.getPort() + "/" + manager.getDatabase() + "?useSSL=false", manager.getUser(), manager.getPassword());


      result = connection.getMetaData().getTables(null, null, table, null);
      boolean first = !result.next();
      connection.close();
      return first;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public Double version() {
    Double version = 0.0;
    Connection connection;
    Statement statement;
    ResultSet result;
    String table = manager.getPrefix() + "_INFO";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://" + manager.getHost() + ":" + manager.getPort() + "/" + manager.getDatabase() + "?useSSL=false", manager.getUser(), manager.getPassword());
      statement = connection.createStatement();
      result = statement.executeQuery("SELECT version FROM " + table + " WHERE id = 1 LIMIT 1;");
      if(result.first()) {
        version = Double.valueOf(result.getString("version"));
      }
      connection.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return version;
  }

  @Override
  public void initialize() throws SQLException {
    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_INFO` (" +
        "`id` INTEGER NOT NULL UNIQUE," +
        "`version` VARCHAR(10)," +
        "`server_name` VARCHAR(100)" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
    mysql().executePreparedUpdate("INSERT INTO `" + manager.getPrefix() + "_INFO` (id, version, server_name) VALUES (?, ?, ?);",
        new Object[] {
            1,
            TNELib.instance().currentSaveVersion,
            TNE.instance().getServerName()
        });

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_ECOIDS` (" +
        "`username` VARCHAR(100)," +
        "`uuid` VARCHAR(36) UNIQUE" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_USERS` (" +
        "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
        "`display_name` VARCHAR(100)," +
        "`joined_date` BIGINT(60)," +
        "`last_online` BIGINT(60)," +
        "`account_number` INTEGER," +
        "`account_status` VARCHAR(60)," +
        "`account_language` VARCHAR(10) NOT NULL DEFAULT 'default'," +
        "`account_player` BOOLEAN" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_BALANCES` (" +
        "`uuid` VARCHAR(36) NOT NULL," +
        "`server_name` VARCHAR(100) NOT NULL," +
        "`world` VARCHAR(50) NOT NULL," +
        "`currency` VARCHAR(100) NOT NULL," +
        "`balance` DECIMAL(49,4)," +
        "PRIMARY KEY(uuid, server_name, world, currency)" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_TRANSACTIONS` (" +
        "`trans_id` VARCHAR(36) NOT NULL," +
        "`trans_initiator` VARCHAR(36)," +
        "`trans_initiator_balance` DECIMAL(49,4)," +
        "`trans_recipient` VARCHAR(36) NOT NULL," +
        "`trans_recipient_balance` DECIMAL(49,4)," +
        "`trans_type` VARCHAR(36) NOT NULL," +
        "`trans_world` VARCHAR(36) NOT NULL," +
        "`trans_time` BIGINT(60) NOT NULL," +
        "`trans_voided` BOOLEAN NOT NULL," +
        "PRIMARY KEY(trans_id)" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_CHARGES` (" +
        "`charge_transaction` VARCHAR(36) NOT NULL," +
        "`charge_player` VARCHAR(36) NOT NULL," +
        "`charge_currency` VARCHAR(100) NOT NULL," +
        "`charge_world` VARCHAR(36) NOT NULL," +
        "`charge_amount` DECIMAL(49,4) NOT NULL," +
        "`charge_type` VARCHAR(20) NOT NULL," +
        "PRIMARY KEY(charge_transaction, charge_player)" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_BALANCES_HISTORY` (" +
        "`id` INTEGER NOT NULL AUTO_INCREMENT," +
        "`uuid` VARCHAR(36) NOT NULL," +
        "`server_name` VARCHAR(100) NOT NULL," +
        "`world` VARCHAR(50) NOT NULL," +
        "`currency` VARCHAR(100) NOT NULL," +
        "`balance` DECIMAL(49,4)," +
        "PRIMARY KEY(id)" +
        ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    close();
  }

  @Override
  public void update(Double version) throws SQLException {
    //Nothing to convert(?)
    if(version == 11.0) {

      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_BALANCES_HISTORY` (" +
          "`id` INTEGER NOT NULL AUTO_INCREMENT," +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`server_name` VARCHAR(100) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`currency` VARCHAR(100) NOT NULL," +
          "`balance` DECIMAL(49,4)" +
          ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
      close();
    }
  }

  public MySQL mysql() throws SQLException {
    return ((MySQL)connector());
  }

  public void close() {
    if(sql != null) {
      sql.close(manager, true);
    }
  }

  @Override
  public DatabaseConnector connector() throws SQLException {
    if(!sql.connected(manager)) {
      TNE.debug("Connecting to MySQL");
      sql.connect(manager);
    }
    return sql;
  }

  @Override
  public void save(Double version) throws SQLException {
    mysql().executePreparedUpdate("Update " + manager.getPrefix() + "_INFO SET version = ? WHERE id = 1;", new Object[] { String.valueOf(version) });
    close();
    super.save(version);
  }

  @Override
  public void delete(Double version) throws SQLException {
    mysql().executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_ECOIDS;");
    mysql().executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_USERS;");
    mysql().executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_BALANCES;");
    mysql().executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_TRANSACTIONS;");
    mysql().executeUpdate("TRUNCATE TABLE " + manager.getPrefix() + "_CHARGES;");
    close();
  }

  @Override
  public Boolean backupData() {
    return false;
  }

  @Override
  public UUID loadID(String username) {
    try {
      int idIndex = mysql().executePreparedQuery(ID_LOAD, new Object[] {
          username
      });
      if(mysql().results(idIndex).next()) {
        UUID id = UUID.fromString(mysql().results(idIndex).getString("uuid"));
        mysql().closeResult(idIndex);
        return id;
      }
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      close();
    }
    return null;
  }

  @Override
  public Map<String, UUID> loadEconomyIDS() {
    Map<String, UUID> ids = new HashMap<>();

    String table = manager.getPrefix() + "_ECOIDS";
    try {
      TNE.debug("MySQL Connected? " + mysql().connected(manager));
      int idIndex = mysql().executeQuery("SELECT username, uuid FROM " + table + ";");
      TNE.debug("Predicted IDs: " + mysql().results(idIndex).getFetchSize());
      while (mysql().results(idIndex).next()) {
        TNE.debug("Loading EcoID for " + mysql().results(idIndex).getString("username"));
        ids.put(mysql().results(idIndex).getString("username"), UUID.fromString(mysql().results(idIndex).getString("uuid")));
      }
      mysql().closeResult(idIndex);
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      close();
    }
    TNE.debug("Finished loading Eco IDS. Total: " + ids.size());
    return ids;
  }

  @Override
  public void saveIDS(Map<String, UUID> ids) {
    PreparedStatement statement = null;
    try {
      statement = mysql().connection(manager).prepareStatement(ID_SAVE);
      for(Map.Entry<String, UUID> entry : ids.entrySet()) {
        if(entry.getKey() == null) {
          System.out.println("Attempted saving id with null display name.");
          continue;
        }
        statement.setString(1, entry.getKey());
        statement.setString(2, entry.getValue().toString());
        statement.setString(3, entry.getKey());
        statement.addBatch();
      }
      statement.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
  }

  @Override
  public void createTables(List<String> tables) throws SQLException {
    for(String table : tables) {
      mysql().executeUpdate(table);
    }
    close();
  }

  @Override
  public void saveID(String username, UUID id) throws SQLException {
    if(username == null) {
      System.out.println("Attempted saving id with null display name.");
      return;
    }
    mysql().executePreparedUpdate(ID_SAVE,
        new Object[] {
            username,
            id.toString(),
            username
        });
    close();
  }

  @Override
  public void removeID(String username) throws SQLException {
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_ECOIDS WHERE username = ?", new Object[] { username });
    close();
  }

  @Override
  public void removeID(UUID id) throws SQLException {
    mysql().executePreparedUpdate(ID_DELETE, new Object[] { id.toString() });
    close();
  }

  @Override
  public Collection<TNEAccount> loadAccounts() {
    List<TNEAccount> accounts = new ArrayList<>();
    TNE.debug("Loading TNE Accounts");

    String table = manager.getPrefix() + "_USERS";
    List<UUID> userIDS = new ArrayList<>();
    try {
      TNE.debug("MySQL Connected? " + mysql().connected(manager));
      int accountIndex = mysql().executeQuery("SELECT uuid FROM " + table + ";");
      while (mysql().results(accountIndex).next()) {
        TNE.debug("Loading account with UUID of " + mysql().results(accountIndex).getString("uuid"));
        userIDS.add(UUID.fromString(mysql().results(accountIndex).getString("uuid")));
      }
      mysql().closeResult(accountIndex);

      userIDS.forEach((id)->{
        TNEAccount account = loadAccount(id);
        if(account != null) accounts.add(account);
      });
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      close();
    }
    TNE.debug("Finished loading Accounts. Total: " + accounts.size());
    return accounts;
  }

  @Override
  public TNEAccount loadAccount(UUID id) {
    TNEAccount account = null;
    try {
      int accountIndex = mysql().executePreparedQuery(ACCOUNT_LOAD, new Object[]{
          id.toString()
      });
      if (mysql().results(accountIndex).next()) {
        account = new TNEAccount(UUID.fromString(mysql().results(accountIndex).getString("uuid")),
                                            mysql().results(accountIndex).getString("display_name"));

        account.setAccountNumber(mysql().results(accountIndex).getInt("account_number"));
        account.setStatus(AccountStatus.fromName(mysql().results(accountIndex).getString("account_status")));
        account.setLanguage(mysql().results(accountIndex).getString("account_language"));
        account.setJoined(mysql().results(accountIndex).getLong("joined_date"));
        account.setLastOnline(mysql().results(accountIndex).getLong("last_online"));
        account.setPlayerAccount(mysql().results(accountIndex).getBoolean("account_player"));

        int balancesIndex = mysql().executePreparedQuery(BALANCE_LOAD, new Object[]{account.identifier().toString()});
        while (mysql().results(balancesIndex).next()) {
          account.setHoldings(mysql().results(balancesIndex).getString("world"), mysql().results(balancesIndex).getString("currency"), mysql().results(balancesIndex).getBigDecimal("balance"), true);
        }
        mysql().closeResult(balancesIndex);
      }
      mysql().closeResult(accountIndex);
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      close();
    }
    return account;
  }

  @Override
  public void saveAccounts(List<TNEAccount> accounts) {
    PreparedStatement accountStatement = null;
    PreparedStatement balanceStatement = null;
    PreparedStatement historyStatement = null;
    try {
      accountStatement = mysql().connection(manager).prepareStatement(ACCOUNT_SAVE);
      balanceStatement = mysql().connection(manager).prepareStatement(BALANCE_SAVE);
      historyStatement = mysql().connection(manager).prepareStatement(HISTORY_SAVE);
      for(TNEAccount account : accounts) {
        if(account.displayName() == null) {
          System.out.println("Attempted saving account with null display name.");
          continue;
        }
        accountStatement.setString(1, account.identifier().toString());
        accountStatement.setString(2, account.displayName());
        accountStatement.setLong(3, account.getJoined());
        accountStatement.setLong(4, account.getLastOnline());
        accountStatement.setInt(5, account.getAccountNumber());
        accountStatement.setString(6, account.getStatus().getName());
        accountStatement.setString(7, account.getLanguage());
        accountStatement.setBoolean(8, account.playerAccount());
        accountStatement.setString(9, account.displayName());
        accountStatement.setLong(10, account.getJoined());
        accountStatement.setLong(11, account.getLastOnline());
        accountStatement.setInt(12, account.getAccountNumber());
        accountStatement.setString(13, account.getStatus().getName());
        accountStatement.setString(14, account.getLanguage());
        accountStatement.setBoolean(15, account.playerAccount());
        accountStatement.addBatch();

        for(Map.Entry<String, WorldHoldings> holdingsEntry : account.getWorldHoldings().entrySet()) {
          for(Map.Entry<String, BigDecimal> entry : holdingsEntry.getValue().getHoldings().entrySet()) {
            final String server = (TNE.manager().currencyManager().get(holdingsEntry.getKey(), entry.getKey()) != null)?
                TNE.manager().currencyManager().get(holdingsEntry.getKey(), entry.getKey()).getServer() :
                TNE.instance().getServerName();
            balanceStatement.setString(1, account.identifier().toString());
            balanceStatement.setString(2, server);
            balanceStatement.setString(3, holdingsEntry.getKey());
            balanceStatement.setString(4, entry.getKey());
            balanceStatement.setBigDecimal(5, entry.getValue());
            balanceStatement.setBigDecimal(6, entry.getValue());
            balanceStatement.addBatch();

            //history
            historyStatement.setString(1, account.identifier().toString());
            historyStatement.setString(2, server);
            historyStatement.setString(3, holdingsEntry.getKey());
            historyStatement.setString(4, entry.getKey());
            historyStatement.setBigDecimal(5, entry.getValue());
            historyStatement.addBatch();
          }
        }
      }
      balanceStatement.executeBatch();
      historyStatement.executeBatch();
      accountStatement.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
  }

  @Override
  public void saveAccount(TNEAccount account) throws SQLException {
    if(account.displayName() == null) {
      System.out.println("Attempted saving account with null display name.");
      return;
    }
    TNE.debug("Saving account: " + account.displayName());
    mysql().executePreparedUpdate(ACCOUNT_SAVE,
        new Object[]{
            account.identifier().toString(),
            account.displayName(),
            account.getJoined(),
            account.getLastOnline(),
            account.getAccountNumber(),
            account.getStatus().getName(),
            account.getLanguage(),
            account.playerAccount(),
            account.displayName(),
            account.getJoined(),
            account.getLastOnline(),
            account.getAccountNumber(),
            account.getStatus().getName(),
            account.getLanguage(),
            account.playerAccount(),
        }
    );

    for(Map.Entry<String, WorldHoldings> entry : account.getWorldHoldings().entrySet()) {
      for(Map.Entry<String, BigDecimal> curEntry : entry.getValue().getHoldings().entrySet()) {
        final String server = (TNE.manager().currencyManager().get(entry.getKey(), curEntry.getKey()) != null)?
            TNE.manager().currencyManager().get(entry.getKey(), curEntry.getKey()).getServer() :
            TNE.instance().getServerName();
        mysql().executePreparedUpdate(BALANCE_SAVE,
            new Object[]{
                account.identifier().toString(),
                server,
                entry.getKey(),
                curEntry.getKey(),
                curEntry.getValue(),
                curEntry.getValue()
            }
        );
        mysql().executePreparedUpdate(HISTORY_SAVE,
            new Object[]{
                account.identifier().toString(),
                server,
                entry.getKey(),
                curEntry.getKey(),
                curEntry.getValue()
            }
        );
      }
    }
    close();
  }

  @Override
  public void deleteAccount(UUID id) throws SQLException {
    mysql().executePreparedUpdate(ID_DELETE, new Object[] { id.toString() });
    mysql().executePreparedUpdate(ACCOUNT_DELETE, new Object[] { id.toString() });
    mysql().executePreparedUpdate(BALANCE_DELETE, new Object[] { id.toString() });
    close();
  }

  @Override
  public TNETransaction loadTransaction(UUID id) {
    String table = manager.getPrefix() + "_TRANSACTIONS";
    try {
      int transIndex = mysql().executePreparedQuery("SELECT trans_id, trans_initiator, trans_recipient, trans_world, trans_type, trans_time, trans_initiator_balance, trans_recipient_balance FROM " + table + " WHERE trans_id = ? LIMIT 1", new Object[]{
          id.toString()
      });
      if (mysql().results(transIndex).next()) {
        TNETransaction transaction = new TNETransaction(UUID.fromString(mysql().results(transIndex).getString("trans_id")),
            TNE.manager().getAccount(UUID.fromString(mysql().results(transIndex).getString("trans_initiator"))),
            TNE.manager().getAccount(UUID.fromString(mysql().results(transIndex).getString("trans_recipient"))),
            mysql().results(transIndex).getString("trans_world"),
            TNE.transactionManager().getType(mysql().results(transIndex).getString("trans_type").toLowerCase()),
            mysql().results(transIndex).getLong("trans_time"));

        String chargesTable = manager.getPrefix() + "_CHARGES";
        int chargesIndex = mysql().executePreparedQuery("SELECT charge_player, charge_world, charge_amount, charge_type, charge_currency FROM " + chargesTable + " WHERE charge_transaction = ?", new Object[]{transaction.transactionID().toString()});
        while (mysql().results(chargesIndex).next()) {
          String player = mysql().results(chargesIndex).getString("charge_player");
          boolean initiator = player.equalsIgnoreCase(transaction.initiator());
          String world = mysql().results(chargesIndex).getString("charge_world");
          BigDecimal amount = mysql().results(chargesIndex).getBigDecimal("charge_amount");
          String chargeType = mysql().results(chargesIndex).getString("charge_type");
          String currency = mysql().results(chargesIndex).getString("charge_currency");

          TransactionCharge charge = new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency), amount, TransactionChargeType.valueOf(chargeType));

          if(initiator) {
            transaction.setInitiatorCharge(charge);
            transaction.setInitiatorBalance(new CurrencyEntry(world, TNE.manager().currencyManager().get(world, currency),
                mysql().results(transIndex).getBigDecimal("trans_initiator_balance")));
          } else {
            transaction.setRecipientCharge(charge);
            transaction.setRecipientBalance(new CurrencyEntry(world, TNE.manager().currencyManager().get(world, currency),
                mysql().results(transIndex).getBigDecimal("trans_recipient_balance")));
          }
        }
        mysql().closeResult(transIndex);
        mysql().closeResult(chargesIndex);
        return transaction;
      }
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      close();
    }
    return null;
  }

  @Override
  public Collection<TNETransaction> loadTransactions() {
    List<TNETransaction> transactions = new ArrayList<>();

    String table = manager.getPrefix() + "_TRANSACTIONS";
    List<UUID> transactionIDS = new ArrayList<>();
    try {
      int accountIndex = mysql().executeQuery("SELECT trans_id FROM " + table + ";");
      while (mysql().results(accountIndex).next()) {
        transactionIDS.add(UUID.fromString(mysql().results(accountIndex).getString("trans_id")));
      }
      mysql().closeResult(accountIndex);
      transactionIDS.forEach((id)->{
        TNETransaction transaction = loadTransaction(id);
        if(transaction != null) transactions.add(transaction);
      });
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      close();
    }
    return transactions;
  }

  @Override
  public void saveTransaction(TNETransaction transaction) throws SQLException {
    String table = manager.getPrefix() + "_TRANSACTIONS";
    mysql().executePreparedUpdate("INSERT INTO `" + table + "` (trans_id, trans_initiator, trans_initiator_balance, trans_recipient, trans_recipient_balance, trans_type, trans_world, trans_time, trans_voided) " +
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
      mysql().executePreparedUpdate("INSERT INTO `" + table + "` (charge_transaction, charge_player, charge_currency, charge_world, charge_amount, charge_type) " +
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
      mysql().executePreparedUpdate("INSERT INTO `" + table + "` (charge_transaction, charge_player, charge_currency, charge_world, charge_amount, charge_type) " +
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
    close();
  }

  @Override
  public void deleteTransaction(UUID id) throws SQLException {
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_TRANSACTIONS WHERE trans_id = ? ", new Object[] { id.toString() });
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_CHARGES WHERE charge_transaction = ? ", new Object[] { id.toString() });
    close();
  }

  @Override
  public String nullAccounts() throws SQLException {
    String userTable = manager.getPrefix() + "_USERS";
    String idTable = manager.getPrefix() + "_ECOIDS";
    int index = mysql().executeQuery("SELECT count(*) FROM " + userTable + " WHERE display_name is null;");
    int userIndex = mysql().executeQuery("SELECT count(*) FROM " + idTable + " WHERE username is null;");

    String counts = "";
    try {
      while(mysql().results(index).next()) {
        counts += " Accounts: " + mysql().results(index).getInt(1);
      }
      mysql().closeResult(index);
      while(mysql().results(userIndex).next()) {
        counts += " IDS: " + mysql().results(userIndex).getInt(1);
      }
      mysql().closeResult(userIndex);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return counts;
  }

  @Override
  public int balanceCount(String world, String currency, int limit) throws SQLException {
    String balanceTable = manager.getPrefix() + "_BALANCES";
    int index = mysql().executePreparedQuery("SELECT count(*) FROM " + balanceTable + " WHERE world = ? AND currency = ?;",
        new Object[] { world, currency });
    int count = 0;
    try {
      while(mysql().results(index).next()) {
        count = mysql().results(index).getInt(1);
      }
      mysql().closeResult(index);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
    if(count > 0) {
      return (int)Math.ceil(count / limit);
    }
    return count;
  }

  //Page 1 = 1 -> 10
  //Page 2 = 11 -> 20
  //Page 3 = 21 -> 30
  //Page 4 = 30 -> 39
  @Override
  public LinkedHashMap<UUID, BigDecimal> topBalances(String world, String currency, int limit, int page) throws SQLException {
    LinkedHashMap<UUID, BigDecimal> balances = new LinkedHashMap<>();

    String balanceTable = manager.getPrefix() + "_BALANCES";

    int start = 1;
    if(page > 1) start = ((page - 1) * limit) + 1;
    int index = mysql().executePreparedQuery("SELECT uuid, balance FROM " + balanceTable + " WHERE world = ? AND currency = ? ORDER BY balance DESC LIMIT ?,?;",
        new Object[] { world, currency, start, limit });
    try {
      while (mysql().results(index).next()) {
        balances.put(UUID.fromString(mysql().results(index).getString("uuid")), new BigDecimal(mysql().results(index).getString("balance")));
      }
      mysql().closeResult(index);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return balances;
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

    int index = mysql().executePreparedQuery(queryBuilder.toString(), values.toArray());
    int count = 0;
    try {
      while(mysql().results(index).next()) {
        count = mysql().results(index).getInt(1);
      }
      mysql().closeResult(index);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
    if(count > 0) {
      return (int)Math.ceil(count / limit);
    }
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
    int index = mysql().executePreparedQuery(queryBuilder.toString(), values.toArray());
    try {
      while (mysql().results(index).next()) {
        UUID id = UUID.fromString(mysql().results(index).getString("trans_id"));
        transactions.put(id, loadTransaction(id));
      }
      mysql().closeResult(index);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return transactions;
  }
}