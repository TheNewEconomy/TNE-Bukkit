package com.github.tnerevival.core.version;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.db.FlatFile;
import com.github.tnerevival.core.db.H2;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.flat.Article;
import com.github.tnerevival.core.db.flat.Entry;
import com.github.tnerevival.core.db.flat.FlatFileConnection;
import com.github.tnerevival.core.db.flat.Section;
import com.github.tnerevival.utils.BankUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class Alpha3_0 extends Version {

	@Override
	public double versionNumber() {
		return 3.0;
	}

	@Override
	public void update(double version, String type) {
		String table = prefix + "_ECOIDS";
		if(type.equalsIgnoreCase("mysql")) {
			db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
			
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "username VARCHAR(20)," +
					   "uuid VARCHAR(36)" +
					   ");");

      //TODO: Shops Table.

      table = prefix + "_USERS";
      mysql().executeUpdate("ALTER TABLE " + table + " DROP COLUMN `overflow`");
      mysql().executeUpdate("ALTER TABLE " + table + " ADD COLUMN `acc_pin` VARCHAR(30)," +
                            "ADD COLUMN `command_credits` LONGTEXT," +
                            "ADD COLUMN `inventory_credits` LONGTEXT AFTER `uuid`");
		}
	}

	@Override
	public void loadFlat(File file) {
    db = new FlatFile(TNE.instance.getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
    FlatFileConnection connection = (FlatFileConnection)db.connection();
    Section accounts = null;
    Section ids = null;
    //TODO: Shops Table
    try {
      connection.getOIS().readDouble();
      accounts = (Section) connection.getOIS().readObject();
      ids = (Section) connection.getOIS().readObject();
      connection.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Iterator<java.util.Map.Entry<String, Article>> it = accounts.getArticle().entrySet().iterator();

    while(it.hasNext()) {
      java.util.Map.Entry<String, Article> entry = it.next();
      UUID uid = UUID.fromString(entry.getKey());
      Entry info = entry.getValue().getEntry("info");
      Entry balances = entry.getValue().getEntry("balances");
      Entry banks = entry.getValue().getEntry("banks");

      Account account = new Account(uid, (Integer) info.getData("accountnumber"));
      HashMap<String, Double> balanceMap = new HashMap<String, Double>();
      HashMap<String, Bank> bankMap = new HashMap<String, Bank>();

      account.setAccountNumber((Integer) info.getData("accountnumber"));
      account.setStatus((String) info.getData("status"));
      account.setPin((String) info.getData("pin"));
      account.creditsFromString((String)info.getData("inventory_credits"));
      account.commandsFromString((String)info.getData("command_credits"));

      Iterator<java.util.Map.Entry<String, Object>> balanceIterator = balances.getData().entrySet().iterator();

      while(balanceIterator.hasNext()) {
        java.util.Map.Entry<String, Object> balanceEntry = balanceIterator.next();

        balanceMap.put(balanceEntry.getKey(), (Double)balanceEntry.getValue());
      }
      account.setBalances(balanceMap);

      Iterator<java.util.Map.Entry<String, Object>> bankIterator = banks.getData().entrySet().iterator();

      while(bankIterator.hasNext()) {
        java.util.Map.Entry<String, Object> bankEntry = bankIterator.next();

        bankMap.put(bankEntry.getKey(), BankUtils.fromString((String)bankEntry.getValue()));
      }
      account.setBanks(bankMap);

      TNE.instance.manager.accounts.put(uid, account);
    }

    Iterator<Map.Entry<String, Article>> idsIterator = ids.getArticle().entrySet().iterator();

    while(idsIterator.hasNext()) {
      Map.Entry<String, Article> idEntry = idsIterator.next();

      Entry info = idEntry.getValue().getEntry("info");

      TNE.instance.manager.ecoIDs.put((String)info.getData("username"), UUID.fromString((String)info.getData("uuid")));
    }
	}

	@Override
	public void saveFlat(File file) {
    Iterator<java.util.Map.Entry<UUID, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();

    Section accounts = new Section("accounts");

    while(it.hasNext()) {
      java.util.Map.Entry<UUID, Account> entry = it.next();

      Account acc = entry.getValue();
      Article account = new Article(entry.getKey().toString());
      //Info
      Entry info = new Entry("info");
      info.addData("accountnumber", acc.getAccountNumber());
      info.addData("uuid", acc.getUid());
      info.addData("status", acc.getStatus().getName());
      info.addData("inventory_credits", acc.creditsToString());
      info.addData("command_credits", acc.commandsToString());
      info.addData("pin", acc.getPin());
      account.addEntry(info);
      //Balances
      Entry balances = new Entry("balances");
      Iterator<java.util.Map.Entry<String, Double>> balanceIterator = acc.getBalances().entrySet().iterator();

      while(balanceIterator.hasNext()) {
        java.util.Map.Entry<String, Double> balanceEntry = balanceIterator.next();
        balances.addData(balanceEntry.getKey(), balanceEntry.getValue());
      }
      account.addEntry(balances);

      Entry banks = new Entry("banks");

      Iterator<java.util.Map.Entry<String, Bank>> bankIterator = acc.getBanks().entrySet().iterator();

      while(bankIterator.hasNext()) {
        java.util.Map.Entry<String, Bank> bankEntry = bankIterator.next();
        banks.addData(bankEntry.getKey(), bankEntry.getValue().toString());
      }
      account.addEntry(banks);

      accounts.addArticle(entry.getKey().toString(), account);
    }

    Iterator<Map.Entry<String, UUID>> idsIterator = TNE.instance.manager.ecoIDs.entrySet().iterator();

    Section ids = new Section("IDS");

    while(idsIterator.hasNext()) {
      Map.Entry<String, UUID> idEntry = idsIterator.next();

      Article a = new Article(idEntry.getKey());
      Entry e = new Entry("info");

      e.addData("username", idEntry.getKey());
      e.addData("uuid", idEntry.getValue().toString());
      a.addEntry(e);

      ids.addArticle(idEntry.getKey(), a);
    }

    //TODO: Shops table
    try {
      db = new FlatFile(TNE.instance.getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
      FlatFileConnection connection = (FlatFileConnection)db.connection();
      connection.getOOS().writeDouble(versionNumber());
      connection.getOOS().writeObject(accounts);
      connection.getOOS().writeObject(ids);
      connection.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
	}

	@Override
	public void loadMySQL() {

    db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);

    String table = prefix + "_USERS";

    try {
      mysql().executeQuery("SELECT * FROM " + table + ";");

      while(mysql().results().next()) {
        Account account = new Account(UUID.fromString(mysql().results().getString("uuid")));
        account.balancesFromString(mysql().results().getString("balances"));
        account.setAccountNumber(mysql().results().getInt("accountnumber"));
        account.setStatus(mysql().results().getString("accountstatus"));
        account.setJoined(mysql().results().getString("joinedDate"));
        account.creditsFromString(mysql().results().getString("inventory_credits"));
        account.commandsFromString(mysql().results().getString("command_credits"));
        account.setPin(mysql().results().getString("acc_pin"));

        String bankTable = prefix + "_BANKS";
        mysql().executePreparedQuery("SELECT * FROM " + bankTable + " WHERE uuid = ?;", new Object[] { account.getUid().toString() }, false);

        while(mysql().secondary().next()) {
          account.getBanks().put(mysql().secondary().getString("world"), BankUtils.fromString(mysql().secondary().getString("bank")));
        }
        TNE.instance.manager.accounts.put(account.getUid(), account);
      }

      table = prefix + "_ECOIDS";
      mysql().executeQuery("SELECT * FROM " + table + ";");
      while(mysql().results().next()) {
        TNE.instance.manager.ecoIDs.put(mysql().results().getString("username"), UUID.fromString(mysql().results().getString("uuid")));
      }

      //TODO: Shops table.
      mysql().close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
	}

	@Override
	public void saveMySQL() {
    String table = prefix + "_INFO";
    db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
    mysql().executePreparedUpdate("Update " + table + " SET version = ? WHERE id = 1;", new Object[] { String.valueOf(versionNumber()) });

    Iterator<Map.Entry<UUID, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();

    while(it.hasNext()) {
      java.util.Map.Entry<UUID, Account> entry = it.next();

      Iterator<java.util.Map.Entry<String, Bank>> bankIterator = entry.getValue().getBanks().entrySet().iterator();

      while(bankIterator.hasNext()) {
        java.util.Map.Entry<String, Bank> bankEntry = bankIterator.next();

        table = prefix + "_BANKS";

        try {
          mysql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ? AND world = ?;",
              new Object[] {
                  entry.getKey().toString(),
                  bankEntry.getKey()
              });
          if(mysql().results().first()) {
            mysql().executePreparedUpdate("UPDATE " + table + " SET bank = ? WHERE uuid = ? AND world = ?;",
                new Object[] {
                    bankEntry.getValue().toString(),
                    entry.getKey().toString(),
                    bankEntry.getKey()
                });
          } else {
            mysql().executePreparedUpdate("INSERT INTO " + table + " (uuid, world, bank) VALUES (?, ?, ?);",
                new Object[] {
                    entry.getKey().toString(),
                    bankEntry.getKey(),
                    bankEntry.getValue().toString(),
                });
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      table = prefix + "_USERS";

      try {
        mysql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?;", new Object[] { entry.getKey().toString() });
        if(mysql().results().first()) {
          mysql().executePreparedUpdate("UPDATE " + table + " SET balances = ?, acc_pin = ?, inventory_credits = ?, command_credits = ?, joinedDate = ?, accountnumber = ?, accountstatus = ? WHERE uuid = ?;",
              new Object[] {
                  entry.getValue().balancesToString(),
                  entry.getValue().getPin(),
                  entry.getValue().creditsToString(),
                  entry.getValue().commandsToString(),
                  entry.getValue().getJoined(),
                  entry.getValue().getAccountNumber(),
                  entry.getValue().getStatus().getName(),
                  entry.getKey().toString()
              });
        } else {
          mysql().executePreparedUpdate("INSERT INTO " + table + " (uuid, balances, acc_pin, inventory_credits, command_credits, joinedDate, accountnumber, accountstatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
              new Object[] {
                  entry.getKey().toString(),
                  entry.getValue().balancesToString(),
                  entry.getValue().getPin(),
                  entry.getValue().creditsToString(),
                  entry.getValue().commandsToString(),
                  entry.getValue().getJoined(),
                  entry.getValue().getAccountNumber(),
                  entry.getValue().getStatus().getName(),
                  entry.getValue().overflowToString()
              });
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    Iterator<Map.Entry<String, UUID>> ecoIDSIterator = TNE.instance.manager.ecoIDs.entrySet().iterator();

    while(it.hasNext()) {
      Map.Entry<String, UUID> idEntry = ecoIDSIterator.next();

      mysql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?", new Object[] { idEntry.getValue().toString() });
      try {
        if(mysql().results().first()) {
          mysql().executePreparedUpdate("UPDATE " + table + " SET username = ? WHERE uuid = ?",
              new Object[] {
                  idEntry.getValue(),
                  idEntry.getValue().toString()
              });
        } else {
          mysql().executePreparedQuery("INSERT INTO " + table + " (username, uuid) VALUES(?, ?)",
              new Object[]{
                  idEntry.getValue(),
                  idEntry.getValue().toString()
              });
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

      //TODO: Shops Table
    }

    mysql().close();
	}

	@Override
	public void loadSQLite() {
		loadH2();
	}

	@Override
	public void saveSQLite() {
		saveH2();
	}

	@Override
	public void loadH2() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveH2() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createTables(String type) {
		String table = prefix + "_INFO";
		
		if(type.equalsIgnoreCase("mysql")) {
			db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
			
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "id INTEGER NOT NULL," +
					   "version VARCHAR(10)" +
					   ");");
			mysql().executeUpdate("INSERT INTO " + table + " (id, version) VALUES(1, " + versionNumber() + ");");

      table = prefix + "_ECOIDS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
          "username VARCHAR(20)," +
          "uuid VARCHAR(36)" +
          ");");

      table = prefix + "_USERS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
          "uuid VARCHAR(36) NOT NULL," +
          "inventory_credits LONGTEXT," +
          "command_credits LONGTEXT," +
          "acc_pin VARCHAR(30)," +
          "balances LONGTEXT," +
          "joinedDate VARCHAR(60)," +
          "accountnumber INTEGER," +
          "accountstatus VARCHAR(60)," +
          ");");

      table = prefix + "_SHOPS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
          "shop_owner VARCHAR(36)," +
          "shop_name VARCHAR(60) NOT NULL," +
          "shop_hidden INTEGER(1)," +
          "shop_admin INTEGER(1)," +
          "shop_items LONGTEXT," +
          "shop_blacklist LONGTEXT," +
          "shop_whitelist LONGTEXT," +
          "shop_shares LONGTEXT," +
          ");");
			
			table = prefix + "_BANKS";
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "uuid VARCHAR(36) NOT NULL," +
								   "world VARCHAR(50) NOT NULL," +
								   "bank LONGTEXT" +
								   ");");
			mysql().close();
		} else {
			File h2DB = new File(sqliteFile);
			if(!h2DB.exists()) {
				try {
          h2DB.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			db = new H2(sqliteFile, mysqlUser, mysqlPassword);
			
			h2().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "id INTEGER NOT NULL," +
					   "version CHAR(10)" +
					   ");");
      h2().executeUpdate("INSERT INTO " + table + " (id, version) VALUES(1, " + versionNumber() + ");");

			table = prefix + "_USERS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								"uuid CHAR(36) NOT NULL," +
					      "inventory_credits LONGTEXT," +
					      "command_credits LONGTEXT," +
					      "acc_pin VARCHAR(30)," +
								"balances LONGTEXT," +
								"joinedDate CHAR(60)," +
								"accountnumber INTEGER," +
								"accountstatus CHAR(60)," +
								");");
			
			table = prefix + "_BANKS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "uuid CHAR(36) NOT NULL," +
								   "world CHAR(50) NOT NULL," +
								   "bank LONGTEXT" +
								   ");");
      h2().close();
		}
	}

}