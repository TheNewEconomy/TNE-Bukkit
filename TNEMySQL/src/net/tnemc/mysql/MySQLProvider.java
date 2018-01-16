package net.tnemc.mysql;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DatabaseConnector;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 9/7/2017.
 */
public class MySQLProvider extends TNEDataProvider {

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

      return !result.next();
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
  public void initialize() {
    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_INFO` (" +
        "`id` INTEGER NOT NULL UNIQUE," +
        "`version` VARCHAR(10)," +
        "`server_name` VARCHAR(250)" +
        ") ENGINE = INNODB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
    mysql().executePreparedUpdate("INSERT INTO `" + manager.getPrefix() + "_INFO` (id, version, server_name) VALUES (?, ?, ?);",
        new Object[] {
            1,
            TNELib.instance().currentSaveVersion,
            TNE.instance().getServer().getName()
        });

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_ECOIDS` (" +
        "`username` VARCHAR(250)," +
        "`uuid` VARCHAR(36) UNIQUE" +
        ") ENGINE = INNODB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_USERS` (" +
        "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
        "`display_name` VARCHAR(250)," +
        "`joined_date` BIGINT(60)," +
        "`last_online` BIGINT(60)," +
        "`account_number` INTEGER," +
        "`account_status` VARCHAR(60)," +
        "`account_player` BOOLEAN" +
        ") ENGINE = INNODB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_BALANCES` (" +
        "`uuid` VARCHAR(36) NOT NULL," +
        "`server_name` VARCHAR(250) NOT NULL," +
        "`world` VARCHAR(50) NOT NULL," +
        "`currency` VARCHAR(250) NOT NULL," +
        "`balance` VARCHAR(41)," +
        "PRIMARY KEY(uuid, server_name, world, currency)" +
        ") ENGINE = INNODB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_TRANSACTIONS` (" +
        "`trans_id` VARCHAR(36) NOT NULL," +
        "`trans_initiator` VARCHAR(36)," +
        "`trans_initiator_balance` VARCHAR(41)," +
        "`trans_recipient` VARCHAR(36) NOT NULL," +
        "`trans_recipient_balance` VARCHAR(41)," +
        "`trans_type` VARCHAR(36) NOT NULL," +
        "`trans_world` VARCHAR(36) NOT NULL," +
        "`trans_time` BIGINT(60) NOT NULL," +
        "`trans_voided` BOOLEAN NOT NULL," +
        "PRIMARY KEY(trans_id)" +
        ") ENGINE = INNODB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + manager.getPrefix() + "_CHARGES` (" +
        "`charge_transaction` VARCHAR(36) NOT NULL," +
        "`charge_player` VARCHAR(36) NOT NULL," +
        "`charge_currency` VARCHAR(250) NOT NULL," +
        "`charge_world` VARCHAR(36) NOT NULL," +
        "`charge_amount` VARCHAR(41) NOT NULL," +
        "`charge_type` VARCHAR(20) NOT NULL," +
        "PRIMARY KEY(charge_transaction, charge_player)" +
        ") ENGINE = INNODB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");

    mysql().close(manager);
  }

  @Override
  public void update(Double version) {
    //Nothing to convert(?)
  }

  public MySQL mysql() {
    return ((MySQL)connector());
  }

  @Override
  public DatabaseConnector connector() {
    sql.connect(manager);
    return sql;
  }

  @Override
  public void save(Double version) {
    mysql().executePreparedUpdate("Update " + manager.getPrefix() + "_INFO SET version = ? WHERE id = 1;", new Object[] { String.valueOf(version) });
    super.save(version);
  }

  @Override
  public Boolean backupData() {
    return false;
  }

  @Override
  public UUID loadID(String username) {
    String table = manager.getPrefix() + "_ECOIDS";
    try {
      int idIndex = mysql().executePreparedQuery("SELECT uuid FROM " + table + " WHERE username = ? LIMIT 1", new Object[] {
          username
      });
      if(mysql().results(idIndex).next()) {
        UUID id = UUID.fromString(mysql().results(idIndex).getString("uuid"));
        mysql().close(manager);
        return id;
      }
    } catch(Exception e) {
      TNE.debug(e);
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
      mysql().close(manager);
    } catch(Exception e) {
      TNE.debug(e);
    }
    TNE.debug("Finished loading Eco IDS. Total: " + ids.size());
    return ids;
  }

  @Override
  public void saveID(String username, UUID id) {
    String table = manager.getPrefix() + "_ECOIDS";
    mysql().executePreparedUpdate("INSERT INTO `" + table + "` (username, uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?",
        new Object[] {
            username,
            id.toString(),
            username
        });
    mysql().close(manager);
  }

  @Override
  public void removeID(String username) {
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_ECOIDS WHERE username = ?", new Object[] { username });
    mysql().close(manager);
  }

  @Override
  public void removeID(UUID id) {
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_ECOIDS WHERE uuid = ?", new Object[] { id.toString() });
    mysql().close(manager);
  }

  @Override
  public Collection<TNEAccount> loadAccounts() {
    List<TNEAccount> accounts = new ArrayList<>();
    TNE.debug("Loading TNE Accounts");

    String table = manager.getPrefix() + "_USERS";
    try {
      int accountIndex = mysql().executeQuery("SELECT uuid FROM " + table + ";");
      while (mysql().results(accountIndex).next()) {
        TNE.debug("Loading account with UUID of " + mysql().results(accountIndex).getString("uuid"));
        TNEAccount account = loadAccount(UUID.fromString(mysql().results(accountIndex).getString("uuid")));
        if(account != null) accounts.add(account);
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return accounts;
  }

  @Override
  public TNEAccount loadAccount(UUID id) {
    String table = manager.getPrefix() + "_USERS";
    try {
      int accountIndex = mysql().executePreparedQuery("SELECT uuid, display_name, account_number, account_status, joined_date, last_online, account_player FROM " + table + " WHERE uuid = ? LIMIT 1", new Object[]{
          id.toString()
      });
      if (mysql().results(accountIndex).next()) {
        TNEAccount account = new TNEAccount(UUID.fromString(mysql().results(accountIndex).getString("uuid")),
                                            mysql().results(accountIndex).getString("display_name"));
        account.setAccountNumber(mysql().results(accountIndex).getInt("account_number"));
        account.setStatus(AccountStatus.fromName(mysql().results(accountIndex).getString("account_status")));
        account.setJoined(mysql().results(accountIndex).getLong("joined_date"));
        account.setLastOnline(mysql().results(accountIndex).getLong("last_online"));
        account.setPlayerAccount(mysql().results(accountIndex).getBoolean("account_player"));

        String balancesTable = manager.getPrefix() + "_BALANCES";
        int balancesIndex = mysql().executePreparedQuery("SELECT world, currency, balance FROM " + balancesTable + " WHERE uuid = ?", new Object[]{account.identifier().toString()});
        while (mysql().results(balancesIndex).next()) {
          account.setHoldings(mysql().results(balancesIndex).getString("world"), mysql().results(balancesIndex).getString("currency"), new BigDecimal(mysql().results(balancesIndex).getString("balance")));
        }
        mysql().close(manager);
        return account;
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return null;
  }

  @Override
  public void saveAccount(TNEAccount account) {
    String table = manager.getPrefix() + "_USERS";
    mysql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, display_name, joined_date, last_online, account_number, account_status, account_player) VALUES(?, ?, ?, ?, ?, ?, ?)" +
            " ON DUPLICATE KEY UPDATE display_name = ?, joined_date = ?, last_online = ?, account_number = ?, account_status = ?, account_player = ?",
        new Object[]{
            account.identifier().toString(),
            account.displayName(),
            account.getJoined(),
            account.getLastOnline(),
            account.getAccountNumber(),
            account.getStatus().getName(),
            account.playerAccount(),
            account.displayName(),
            account.getJoined(),
            account.getLastOnline(),
            account.getAccountNumber(),
            account.getStatus().getName(),
            account.playerAccount(),
        }
    );

    final String balTable = manager.getPrefix() + "_BALANCES";

    account.getWorldHoldings().forEach((world, holdings)->{

      holdings.getHoldings().forEach((currency, balance)->{
        mysql().executePreparedUpdate("INSERT INTO `" + balTable + "` (uuid, server_name, world, currency, balance) " +
                "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?",
            new Object[]{
                account.identifier().toString(),
                TNE.instance().getServer().getServerName(),
                world,
                currency,
                balance.toPlainString(),
                balance.toPlainString()
            }
        );
      });
    });
  }

  @Override
  public void deleteAccount(UUID id) {
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_ECOIDS WHERE uuid = ?", new Object[] { id.toString() });
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_USERS WHERE uuid = ? ", new Object[] { id.toString() });
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_BALANCES WHERE uuid = ? ", new Object[] { id.toString() });
    mysql().close(manager);
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
            mysql().results(transIndex).getString("trans_initiator"),
            mysql().results(transIndex).getString("trans_recipient"),
            mysql().results(transIndex).getString("trans_world"),
            TNE.transactionManager().getType(mysql().results(transIndex).getString("trans_type")),
            mysql().results(transIndex).getLong("trans_time"));

        String chargesTable = manager.getPrefix() + "_CHARGES";
        int chargesIndex = mysql().executePreparedQuery("SELECT charge_player, charge_world, charge_amount, charge_type, charge_currency FROM " + chargesTable + " WHERE charge_transaction = ?", new Object[]{transaction.transactionID().toString()});
        while (mysql().results(chargesIndex).next()) {
          String player = mysql().results(chargesIndex).getString("charge_player");
          boolean initiator = player.equalsIgnoreCase(transaction.initiator());
          String world = mysql().results(chargesIndex).getString("charge_world");
          BigDecimal amount = new BigDecimal(mysql().results(chargesIndex).getString("charge_amount"));
          String chargeType = mysql().results(chargesIndex).getString("charge_type");
          String currency = mysql().results(chargesIndex).getString("charge_currency");

          TransactionCharge charge = new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency), amount, TransactionChargeType.valueOf(chargeType));

          if(initiator) {
            transaction.setInitiatorCharge(charge);
            transaction.setInitiatorBalance(new CurrencyEntry(world, TNE.manager().currencyManager().get(world, currency),
                new BigDecimal(mysql().results(transIndex).getString("trans_initiator_balance"))));
          } else {
            transaction.setRecipientCharge(charge);
            transaction.setRecipientBalance(new CurrencyEntry(world, TNE.manager().currencyManager().get(world, currency),
                new BigDecimal(mysql().results(transIndex).getString("trans_recipient_balance"))));
          }
        }
        mysql().close(manager);
        return transaction;
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return null;
  }

  @Override
  public Collection<TNETransaction> loadTransactions() {
    List<TNETransaction> transactions = new ArrayList<>();

    String table = manager.getPrefix() + "_TRANSACTIONS";
    try {
      int accountIndex = mysql().executeQuery("SELECT trans_id FROM " + table + ";");
      while (mysql().results(accountIndex).next()) {
        TNETransaction transaction = loadTransaction(UUID.fromString(mysql().results(accountIndex).getString("trans_id")));
        if(transaction != null) transactions.add(transaction);
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return transactions;
  }

  @Override
  public void saveTransaction(TNETransaction transaction) {
    String table = manager.getPrefix() + "_TRANSACTIONS";
    mysql().executePreparedUpdate("INSERT INTO `" + table + "` (trans_id, trans_initiator, trans_initiator_balance, trans_recipient, trans_recipient_balance, trans_type, trans_world, trans_time, trans_voided) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE trans_recipient = ?, trans_world = ?, trans_voided = ?",
        new Object[]{
            transaction.transactionID().toString(),
            transaction.initiator(),
            (transaction.initiatorBalance() != null)? transaction.initiatorBalance().getAmount().toPlainString() : "0.0",
            transaction.recipient(),
            (transaction.recipientBalance() != null)? transaction.recipientBalance().getAmount().toPlainString() : "0.0",
            transaction.type().name(),
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
              transaction.initiatorCharge().getAmount().toPlainString(),
              transaction.initiatorCharge().getType().name(),
              transaction.initiatorCharge().getWorld(),
              transaction.initiatorCharge().getAmount().toPlainString(),
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
              transaction.recipientCharge().getAmount().toPlainString(),
              transaction.recipientCharge().getType().name(),
              transaction.recipientCharge().getWorld(),
              transaction.recipientCharge().getAmount().toPlainString(),
              transaction.recipientCharge().getType().name()
          }
      );
    }
    mysql().close(manager);
  }

  @Override
  public void deleteTransaction(UUID id) {
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_TRANSACTIONS WHERE trans_id = ? ", new Object[] { id.toString() });
    mysql().executePreparedUpdate("DELETE FROM " + manager.getPrefix() + "_CHARGES WHERE charge_transaction = ? ", new Object[] { id.toString() });
  }
}