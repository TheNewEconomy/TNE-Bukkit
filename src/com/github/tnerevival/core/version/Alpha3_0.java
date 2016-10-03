package com.github.tnerevival.core.version;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.db.FlatFile;
import com.github.tnerevival.core.db.H2;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.SQLDatabase;
import com.github.tnerevival.core.db.flat.Article;
import com.github.tnerevival.core.db.flat.Entry;
import com.github.tnerevival.core.db.flat.FlatFileConnection;
import com.github.tnerevival.core.db.flat.Section;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.SignUtils;

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
			
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
					   "`username` VARCHAR(20)," +
					   "`uuid` VARCHAR(36) UNIQUE" +
					   ");");

      table = prefix + "_SHOPS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_owner` VARCHAR(36)," +
          "`shop_name` VARCHAR(60) NOT NULL UNIQUE," +
          "`shop_hidden` TINYINT(1)," +
          "`shop_admin` TINYINT(1)," +
          "`shop_items` LONGTEXT," +
          "`shop_blacklist` LONGTEXT," +
          "`shop_whitelist` LONGTEXT," +
          "`shop_shares` LONGTEXT," +
          ");");

      table = prefix + "_USERS";
      mysql().executeUpdate("ALTER TABLE `" + table + "` DROP COLUMN `overflow`");
      mysql().executeUpdate("ALTER TABLE `" + table + "` ADD COLUMN `acc_pin` VARCHAR(30)," +
                            "ADD COLUMN `command_credits` LONGTEXT," +
                            "ADD COLUMN `inventory_credits` LONGTEXT AFTER `uuid`");
      mysql().executeUpdate("ALTER TABLE `" + table + "` ADD UNIQUE(uuid)");

      table = prefix + "_BANKS";
      mysql().executeUpdate("ALTER TABLE `" + table + "` ADD UNIQUE(uuid)");
      mysql().executeUpdate("ALTER TABLE `" + table + "` ADD UNIQUE(world)");

      table = prefix + "_SIGNS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_owner` VARCHAR(36)," +
          "`sign_type` VARCHAR(30) NOT NULL," +
          "`sign_location` LONGTEXT NOT NULL UNIQUE," +
          "`sign_meta` LONGTEXT" +
          ");");
		}
	}

	@Override
	public void loadFlat(File file) {
    db = new FlatFile(TNE.instance.getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
    FlatFileConnection connection = (FlatFileConnection)db.connection();
    Section accounts = null;
    Section ids = null;
    Section shops = null;
    Section signs = null;
    try {
      connection.getOIS().readDouble();
      accounts = (Section) connection.getOIS().readObject();
      ids = (Section) connection.getOIS().readObject();
      shops = (Section) connection.getOIS().readObject();
      signs = (Section) connection.getOIS().readObject();
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    Iterator<Map.Entry<String, Article>> it = accounts.getArticle().entrySet().iterator();

    while(it.hasNext()) {
      Map.Entry<String, Article> entry = it.next();
      UUID uid = UUID.fromString(entry.getKey());
      Entry info = entry.getValue().getEntry("info");
      Entry balances = entry.getValue().getEntry("balances");
      Entry banks = entry.getValue().getEntry("banks");

      Account account = new Account(uid, (Integer) info.getData("accountnumber"));
      Map<String, Double> balanceMap = new HashMap<>();
      Map<String, Bank> bankMap = new HashMap<>();

      account.setAccountNumber((Integer) info.getData("accountnumber"));
      account.setStatus((String) info.getData("status"));
      account.setPin((String) info.getData("pin"));
      account.creditsFromString((String)info.getData("inventory_credits"));
      account.commandsFromString((String)info.getData("command_credits"));

      Iterator<Map.Entry<String, Object>> balanceIterator = balances.getData().entrySet().iterator();

      while(balanceIterator.hasNext()) {
        java.util.Map.Entry<String, Object> balanceEntry = balanceIterator.next();

        balanceMap.put(balanceEntry.getKey(), (Double)balanceEntry.getValue());
      }
      account.setBalances(balanceMap);

      Iterator<java.util.Map.Entry<String, Object>> bankIterator = banks.getData().entrySet().iterator();

      while(bankIterator.hasNext()) {
        java.util.Map.Entry<String, Object> bankEntry = bankIterator.next();

        bankMap.put(bankEntry.getKey(), Bank.fromString((String)bankEntry.getValue()));
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

    Iterator<Map.Entry<String, Article>> shopsIterator = shops.getArticle().entrySet().iterator();
    while(shopsIterator.hasNext()) {
      Map.Entry<String, Article> shopEntry = shopsIterator.next();

      Shop s = new Shop(shopEntry.getKey());
      Entry info = shopEntry.getValue().getEntry("info");

      s.setOwner(UUID.fromString((String)info.getData("owner")));
      s.setHidden((boolean)info.getData("hidden"));
      s.setAdmin((boolean)info.getData("admin"));
      s.listFromString((String)info.getData("blacklist"), true);
      s.listFromString((String)info.getData("whitelist"), false);
      s.sharesFromString((String)info.getData("shares"));
      MISCUtils.debug("Items:" + info.getData("items"));

      if(((String)info.getData("items")).trim() != "") {
        s.itemsFromString((String) info.getData("items"));
      }

      TNE.instance.manager.shops.put(shopEntry.getKey(), s);
    }

    Iterator<Map.Entry<String, Article>> signsIterator = signs.getArticle().entrySet().iterator();
    while(signsIterator.hasNext()) {
      Map.Entry<String, Article> signEntry = signsIterator.next();
      Entry info = signEntry.getValue().getEntry("info");

      TNESign sign = SignUtils.instance((String)info.getData("type"), UUID.fromString((String)info.getData("owner")));
      sign.setLocation(SerializableLocation.fromString((String)info.getData("location")));
      sign.loadMeta((String)info.getData("meta"));

      TNE.instance.manager.signs.put(sign.getLocation(), sign);
    }
	}

	@Override
	public void saveFlat(File file) {
    Iterator<java.util.Map.Entry<UUID, Account>> accIT = TNE.instance.manager.accounts.entrySet().iterator();

    Section accounts = new Section("accounts");

    while(accIT.hasNext()) {
      java.util.Map.Entry<UUID, Account> entry = accIT.next();

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
      Iterator<java.util.Map.Entry<String, Double>> balIT = acc.getBalances().entrySet().iterator();

      while(balIT.hasNext()) {
        java.util.Map.Entry<String, Double> balanceEntry = balIT.next();
        balances.addData(balanceEntry.getKey(), balanceEntry.getValue());
      }
      account.addEntry(balances);

      Entry banks = new Entry("banks");

      Iterator<java.util.Map.Entry<String, Bank>> bankIT = acc.getBanks().entrySet().iterator();

      while(bankIT.hasNext()) {
        java.util.Map.Entry<String, Bank> bankEntry = bankIT.next();
        banks.addData(bankEntry.getKey(), bankEntry.getValue().toString());
      }
      account.addEntry(banks);

      accounts.addArticle(entry.getKey().toString(), account);
    }

    Iterator<Map.Entry<String, UUID>> idsIT = TNE.instance.manager.ecoIDs.entrySet().iterator();

    Section ids = new Section("IDS");

    while(idsIT.hasNext()) {
      Map.Entry<String, UUID> idEntry = idsIT.next();

      Article a = new Article(idEntry.getKey());
      Entry e = new Entry("info");

      e.addData("username", idEntry.getKey());
      e.addData("uuid", idEntry.getValue().toString());
      a.addEntry(e);

      ids.addArticle(idEntry.getKey(), a);
    }

    Iterator<Map.Entry<String, Shop>> shopIT = TNE.instance.manager.shops.entrySet().iterator();
    Section shops = new Section("SHOPS");

    while(shopIT.hasNext()) {
      Map.Entry<String, Shop> shopEntry = shopIT.next();
      Shop s = shopEntry.getValue();

      Article a = new Article(s.getName());
      Entry info = new Entry("info");

      info.addData("owner", s.getOwner().toString());
      info.addData("hidden", s.isHidden());
      info.addData("admin", s.isAdmin());
      MISCUtils.debug("Items:" + s.itemsToString());
      info.addData("items", s.itemsToString());
      info.addData("blacklist", s.listToString(true));
      info.addData("whitelist", s.listToString(false));
      info.addData("shares", s.sharesToString());
      a.addEntry(info);

      shops.addArticle(s.getName(), a);
    }

    Iterator<Map.Entry<SerializableLocation, TNESign>> signIT = TNE.instance.manager.signs.entrySet().iterator();
    Section signs = new Section("SIGNS");

    while(signIT.hasNext()) {
      Map.Entry<SerializableLocation, TNESign> signEntry = signIT.next();
      TNESign sign = signEntry.getValue();

      Article a = new Article(sign.getLocation().toString());
      Entry info = new Entry("info");

      info.addData("owner", sign.getOwner().toString());
      info.addData("type", sign.getType().getName());
      info.addData("extra", sign.getMeta());
      info.addData("location", sign.getLocation().toString());
      a.addEntry(info);

      signs.addArticle(sign.getLocation().toString(), a);
    }

    try {
      db = new FlatFile(TNE.instance.getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
      FlatFileConnection connection = (FlatFileConnection)db.connection();
      connection.getOOS().writeDouble(versionNumber());
      connection.getOOS().writeObject(accounts);
      connection.getOOS().writeObject(ids);
      connection.getOOS().writeObject(shops);
      connection.getOOS().writeObject(signs);
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
          account.getBanks().put(mysql().secondary().getString("world"), Bank.fromString(mysql().secondary().getString("bank")));
        }
        TNE.instance.manager.accounts.put(account.getUid(), account);
      }

      table = prefix + "_ECOIDS";
      mysql().executeQuery("SELECT * FROM " + table + ";");
      while(mysql().results().next()) {
        TNE.instance.manager.ecoIDs.put(mysql().results().getString("username"), UUID.fromString(mysql().results().getString("uuid")));
      }

      table = prefix + "_SHOPS";
      mysql().executeQuery("SELECT * FROM `" + table + "`;");
      while(mysql().results().next()) {
        Shop s = new Shop(mysql().results().getString("shop_name"));
        s.setOwner(UUID.fromString(mysql().results().getString("shop_owner")));
        s.setHidden(SQLDatabase.boolFromDB(mysql().results().getInt("shop_hidden")));
        s.setAdmin(SQLDatabase.boolFromDB(mysql().results().getInt("shop_admin")));
        s.itemsFromString(mysql().results().getString("shop_items"));
        s.listFromString(mysql().results().getString("shop_blacklist"), true);
        s.listFromString(mysql().results().getString("shop_whitelist"), false);
        s.sharesFromString(mysql().results().getString("shop_shares"));
        TNE.instance.manager.shops.put(s.getName(), s);
      }
      mysql().close();

      table = prefix + "_SIGNS";
      mysql().executeQuery("SELECT * FROM `" + table + "`;");
      while(mysql().results().next()) {
        TNESign sign = SignUtils.instance(mysql().results().getString("sign_type"), UUID.fromString(mysql().results().getString("sign_owner")));
        sign.setLocation(SerializableLocation.fromString(mysql().results().getString("sign_location")));
        sign.loadMeta(mysql().results().getString("sign_meta"));
        TNE.instance.manager.signs.put(sign.getLocation(), sign);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
	}

	@Override
	public void saveMySQL() {
    String table = prefix + "_INFO";
    db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
    mysql().executePreparedUpdate("Update " + table + " SET version = ? WHERE id = 1;", new Object[] { String.valueOf(versionNumber()) });

    Iterator<Map.Entry<UUID, Account>> accIT = TNE.instance.manager.accounts.entrySet().iterator();

    while(accIT.hasNext()) {
      java.util.Map.Entry<UUID, Account> entry = accIT.next();

      Iterator<java.util.Map.Entry<String, Bank>> bankIT = entry.getValue().getBanks().entrySet().iterator();

      while(bankIT.hasNext()) {
        java.util.Map.Entry<String, Bank> bankEntry = bankIT.next();

        table = prefix + "_BANKS";

        mysql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, world, bank) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE bank = ?",
            new Object[] {
                entry.getKey().toString(),
                bankEntry.getKey(),
                bankEntry.getValue().toString(),
                bankEntry.getValue().toString(),
            });
      }
      table = prefix + "_USERS";

      mysql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, balances, acc_pin, inventory_credits, command_credits, joinedDate, accountnumber, accountstatus) VALUES(?, ?, ?, ?, ?, ?, ?, ?)" +
                                    " ON DUPLICATE KEY UPDATE balances = ?, acc_pin = ?, inventory_credits = ?, command_credits = ?, joinedDate = ?, accountnumber = ?, accountstatus = ?",
          new Object[] {
              entry.getKey().toString(),
              entry.getValue().balancesToString(),
              entry.getValue().getPin(),
              entry.getValue().creditsToString(),
              entry.getValue().commandsToString(),
              entry.getValue().getJoined(),
              entry.getValue().getAccountNumber(),
              entry.getValue().getStatus().getName(),
              entry.getValue().balancesToString(),
              entry.getValue().getPin(),
              entry.getValue().creditsToString(),
              entry.getValue().commandsToString(),
              entry.getValue().getJoined(),
              entry.getValue().getAccountNumber(),
              entry.getValue().getStatus().getName()
          });
    }
    Iterator<Map.Entry<String, UUID>> idsIT = TNE.instance.manager.ecoIDs.entrySet().iterator();

    while(idsIT.hasNext()) {
      Map.Entry<String, UUID> idEntry = idsIT.next();

      mysql().executePreparedUpdate("INSERT INTO `" + table + "` (username, uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?",
          new Object[] {
              idEntry.getKey(),
              idEntry.getValue().toString(),
              idEntry.getKey()
          });
    }

    Iterator<Map.Entry<String, Shop>> shopIT = TNE.instance.manager.shops.entrySet().iterator();

    while(shopIT.hasNext()) {
      Map.Entry<String, Shop> shopEntry = shopIT.next();

      Shop s = shopEntry.getValue();

      table = prefix + "_SHOPS";
      mysql().executePreparedUpdate("INSERT INTO `" + table + "` (shop_name, shop_owner, shop_hidden, shop_admin, shop_items, shop_blacklist, shop_whitelist, shop_shares) VALUES(?, ?, ?, ?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE shop_owner = ?, shop_hidden = ?, shop_admin = ?, shop_items = ?, shop_blacklist = ?, shop_whitelist = ?, shop_shares = ?",
          new Object[] {
              shopEntry.getKey(),
              s.getOwner(),
              SQLDatabase.boolToDB(s.isHidden()),
              SQLDatabase.boolToDB(s.isAdmin()),
              s.itemsToString(),
              s.listToString(true),
              s.listToString(false),
              s.sharesToString(),
              s.getOwner(),
              SQLDatabase.boolToDB(s.isHidden()),
              SQLDatabase.boolToDB(s.isAdmin()),
              s.itemsToString(),
              s.listToString(true),
              s.listToString(false),
              s.sharesToString()
          });
    }

    Iterator<Map.Entry<SerializableLocation, TNESign>> signIT = TNE.instance.manager.signs.entrySet().iterator();

    while(signIT.hasNext()) {
      Map.Entry<SerializableLocation, TNESign> signEntry = signIT.next();

      TNESign sign = signEntry.getValue();

      table = prefix + "_SIGNS";
      mysql().executePreparedUpdate("INSERT INTO `" + table + "` (sign_owner, sign_type, sign_location, sign_meta) VALUES(?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE sign_owner = ?, sign_type = ?, sign_meta = ?",
          new Object[] {
              sign.getOwner().toString(),
              sign.getType().getName(),
              sign.getLocation().toString(),
              sign.getMeta(),
              sign.getOwner().toString(),
              sign.getType().getName(),
              sign.getMeta()
          });
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
    db = new H2(h2File, mysqlUser, mysqlPassword);

    String table = prefix + "_USERS";

    try {
      h2().executeQuery("SELECT * FROM " + table + ";");

      while(h2().results().next()) {
        Account account = new Account(UUID.fromString(h2().results().getString("uuid")));
        account.balancesFromString(h2().results().getString("balances"));
        account.setAccountNumber(h2().results().getInt("accountnumber"));
        account.setStatus(h2().results().getString("accountstatus"));
        account.setJoined(h2().results().getString("joinedDate"));
        account.creditsFromString(h2().results().getString("inventory_credits"));
        account.commandsFromString(h2().results().getString("command_credits"));
        account.setPin(h2().results().getString("acc_pin"));

        String bankTable = prefix + "_BANKS";
        h2().executePreparedQuery("SELECT * FROM " + bankTable + " WHERE uuid = ?;", new Object[] { account.getUid().toString() }, false);

        while(h2().secondary().next()) {
          account.getBanks().put(h2().secondary().getString("world"), Bank.fromString(h2().secondary().getString("bank")));
        }
        TNE.instance.manager.accounts.put(account.getUid(), account);
      }

      table = prefix + "_ECOIDS";
      h2().executeQuery("SELECT * FROM " + table + ";");
      while(h2().results().next()) {
        TNE.instance.manager.ecoIDs.put(h2().results().getString("username"), UUID.fromString(h2().results().getString("uuid")));
      }

      table = prefix + "_SHOPS";
      h2().executeQuery("SELECT * FROM `" + table + "`;");
      while(h2().results().next()) {
        Shop s = new Shop(h2().results().getString("shop_name"));
        s.setOwner(UUID.fromString(h2().results().getString("shop_owner")));
        s.setHidden(SQLDatabase.boolFromDB(h2().results().getInt("shop_hidden")));
        s.setAdmin(SQLDatabase.boolFromDB(h2().results().getInt("shop_admin")));
        s.itemsFromString(h2().results().getString("shop_items"));
        s.listFromString(h2().results().getString("shop_blacklist"), true);
        s.listFromString(h2().results().getString("shop_whitelist"), false);
        s.sharesFromString(h2().results().getString("shop_shares"));
        TNE.instance.manager.shops.put(s.getName(), s);
      }

      table = prefix + "_SIGNS";
      h2().executeQuery("SELECT * FROM `" + table + "`;");
      while(h2().results().next()) {
        TNESign sign = SignUtils.instance(h2().results().getString("sign_type"), UUID.fromString(h2().results().getString("sign_owner")));
        sign.setLocation(SerializableLocation.fromString(h2().results().getString("sign_location")));
        sign.loadMeta(h2().results().getString("sign_meta"));

        TNE.instance.manager.signs.put(sign.getLocation(), sign);
      }
      h2().close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
	}

	@Override
	public void saveH2() {
    String table = prefix + "_INFO";
    db = new H2(h2File, mysqlUser, mysqlPassword);

    h2().executePreparedUpdate("Update " + table + " SET version = ? WHERE id = 1;", new Object[] { String.valueOf(versionNumber()) });

    Iterator<Map.Entry<UUID, Account>> accIT = TNE.instance.manager.accounts.entrySet().iterator();

    while(accIT.hasNext()) {
      java.util.Map.Entry<UUID, Account> entry = accIT.next();

      Iterator<java.util.Map.Entry<String, Bank>> bankIT = entry.getValue().getBanks().entrySet().iterator();

      while(bankIT.hasNext()) {
        java.util.Map.Entry<String, Bank> bankEntry = bankIT.next();

        table = prefix + "_BANKS";

        h2().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, world, bank) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE bank = ?",
            new Object[] {
                entry.getKey().toString(),
                bankEntry.getKey(),
                bankEntry.getValue().toString(),
                bankEntry.getValue().toString(),
            });
      }
      table = prefix + "_USERS";

      h2().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, balances, acc_pin, inventory_credits, command_credits, joinedDate, accountnumber, accountstatus) VALUES(?, ?, ?, ?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE balances = ?, acc_pin = ?, inventory_credits = ?, command_credits = ?, joinedDate = ?, accountnumber = ?, accountstatus = ?",
          new Object[] {
              entry.getKey().toString(),
              entry.getValue().balancesToString(),
              entry.getValue().getPin(),
              entry.getValue().creditsToString(),
              entry.getValue().commandsToString(),
              entry.getValue().getJoined(),
              entry.getValue().getAccountNumber(),
              entry.getValue().getStatus().getName(),
              entry.getValue().balancesToString(),
              entry.getValue().getPin(),
              entry.getValue().creditsToString(),
              entry.getValue().commandsToString(),
              entry.getValue().getJoined(),
              entry.getValue().getAccountNumber(),
              entry.getValue().getStatus().getName()
          });
    }
    Iterator<Map.Entry<String, UUID>> idsIT = TNE.instance.manager.ecoIDs.entrySet().iterator();

    while(idsIT.hasNext()) {
      Map.Entry<String, UUID> idEntry = idsIT.next();

      h2().executePreparedUpdate("INSERT INTO `" + table + "` (username, uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?",
          new Object[] {
              idEntry.getKey(),
              idEntry.getValue().toString(),
              idEntry.getKey()
          });
    }

    Iterator<Map.Entry<String, Shop>> shopIT = TNE.instance.manager.shops.entrySet().iterator();

    while(shopIT.hasNext()) {
      Map.Entry<String, Shop> shopEntry = shopIT.next();

      Shop s = shopEntry.getValue();

      table = prefix + "_SHOPS";
      h2().executePreparedUpdate("INSERT INTO `" + table + "` (shop_name, shop_owner, shop_hidden, shop_admin, shop_items, shop_blacklist, shop_whitelist, shop_shares) VALUES(?, ?, ?, ?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE shop_owner = ?, shop_hidden = ?, shop_admin = ?, shop_items = ?, shop_blacklist = ?, shop_whitelist = ?, shop_shares = ?",
          new Object[] {
              shopEntry.getKey(),
              s.getOwner(),
              SQLDatabase.boolToDB(s.isHidden()),
              SQLDatabase.boolToDB(s.isAdmin()),
              s.itemsToString(),
              s.listToString(true),
              s.listToString(false),
              s.sharesToString(),
              s.getOwner(),
              SQLDatabase.boolToDB(s.isHidden()),
              SQLDatabase.boolToDB(s.isAdmin()),
              s.itemsToString(),
              s.listToString(true),
              s.listToString(false),
              s.sharesToString()
          });
    }

    Iterator<Map.Entry<SerializableLocation, TNESign>> signIT = TNE.instance.manager.signs.entrySet().iterator();

    while(signIT.hasNext()) {
      Map.Entry<SerializableLocation, TNESign> signEntry = signIT.next();

      TNESign sign = signEntry.getValue();

      table = prefix + "_SIGNS";
      h2().executePreparedUpdate("INSERT INTO `" + table + "` (sign_owner, sign_type, sign_location, sign_meta) VALUES(?, ?, ?, ?)" +
            " ON DUPLICATE KEY UPDATE sign_owner = ?, sign_type = ?, sign_meta = ?",
          new Object[] {
              sign.getOwner().toString(),
              sign.getType().getName(),
              sign.getLocation().toString(),
              sign.getMeta(),
              sign.getOwner().toString(),
              sign.getType().getName(),
              sign.getMeta()
          });
    }
    h2().close();
	}

	@Override
	public void createTables(String type) {
		String table = prefix + "_INFO";
		
		if(type.equalsIgnoreCase("mysql")) {
			db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
			
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
					   "`id` INTEGER NOT NULL," +
					   "`version` VARCHAR(10)" +
					   ");");
			mysql().executeUpdate("INSERT INTO " + table + " (id, version) VALUES(1, " + versionNumber() + ");");

      table = prefix + "_ECOIDS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
          "`username` VARCHAR(20)," +
          "`uuid` VARCHAR(36) UNIQUE" +
          ");");

      table = prefix + "_USERS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
          "`inventory_credits` LONGTEXT," +
          "`command_credits` LONGTEXT," +
          "`acc_pin` VARCHAR(30)," +
          "`balances` LONGTEXT," +
          "`joinedDate` VARCHAR(60)," +
          "`accountnumber` INTEGER," +
          "`accountstatus` VARCHAR(60)," +
          ");");

      table = prefix + "_SHOPS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_owner` VARCHAR(36)," +
          "`shop_name` VARCHAR(60) NOT NULL UNIQUE," +
          "`shop_hidden` TINYINT(1)," +
          "`shop_admin` TINYINT(1)," +
          "`shop_items` LONGTEXT," +
          "`shop_blacklist` LONGTEXT," +
          "`shop_whitelist` LONGTEXT," +
          "`shop_shares` LONGTEXT," +
          ");");
			
			table = prefix + "_BANKS";
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
					"`world` VARCHAR(50) NOT NULL UNIQUE," +
					"`bank` LONGTEXT" +
					");");

      table = prefix + "_SIGNS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_owner` VARCHAR(36)," +
          "`sign_type` VARCHAR(30) NOT NULL," +
          "`sign_location` LONGTEXT NOT NULL UNIQUE," +
          "`sign_meta` LONGTEXT" +
                   ");");
			mysql().close();
		} else {
			File h2DB = new File(h2File);
			if(!h2DB.exists()) {
				try {
          h2DB.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			db = new H2(h2File, mysqlUser, mysqlPassword);

      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`id` INTEGER NOT NULL," +
          "`version` VARCHAR(10)" +
          ");");
      h2().executeUpdate("INSERT INTO " + table + " (id, version) VALUES(1, " + versionNumber() + ");");

      table = prefix + "_ECOIDS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
          "`username` VARCHAR(20)," +
          "`uuid` VARCHAR(36) UNIQUE" +
          ");");

      table = prefix + "_USERS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
          "`inventory_credits` LONGTEXT," +
          "`command_credits` LONGTEXT," +
          "`acc_pin` VARCHAR(30)," +
          "`balances` LONGTEXT," +
          "`joinedDate` VARCHAR(60)," +
          "`accountnumber` INTEGER," +
          "`accountstatus` VARCHAR(60)," +
          ");");

      table = prefix + "_SHOPS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_owner` VARCHAR(36)," +
          "`shop_name` VARCHAR(60) NOT NULL UNIQUE," +
          "`shop_hidden` TINYINT(1)," +
          "`shop_admin` TINYINT(1)," +
          "`shop_items` LONGTEXT," +
          "`shop_blacklist` LONGTEXT," +
          "`shop_whitelist` LONGTEXT," +
          "`shop_shares` LONGTEXT," +
          ");");

      table = prefix + "_BANKS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
          "`world` VARCHAR(50) NOT NULL UNIQUE," +
          "`bank` LONGTEXT" +
          ");");

      table = prefix + "_SIGNS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_owner` VARCHAR(36)," +
          "`sign_type` VARCHAR(30) NOT NULL," +
          "`sign_location` LONGTEXT NOT NULL UNIQUE," +
          "`sign_meta` LONGTEXT" +
          ");");
      h2().close();
		}
	}

}