package com.github.tnerevival.core.version;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.auction.Claim;
import com.github.tnerevival.core.db.*;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.core.transaction.Record;
import com.github.tnerevival.core.transaction.TransactionHistory;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public abstract class Version {
  public String mysqlHost = TNE.configurations.getString("Core.Database.MySQL.Host");
  public Integer mysqlPort = TNE.configurations.getInt("Core.Database.MySQL.Port");
  public String mysqlDatabase = TNE.configurations.getString("Core.Database.MySQL.Database");
  public String mysqlUser = TNE.configurations.getString("Core.Database.MySQL.User");
  public String mysqlPassword = TNE.configurations.getString("Core.Database.MySQL.Password");
  public String prefix = TNE.configurations.getString("Core.Database.Prefix");
  public String h2File = TNE.instance().getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.H2.File");
  public String sqliteFile = TNE.instance().getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.SQLite.File");

  protected Database db;

  protected void createDB() {
    createDB(TNE.instance().saveManager.type.toLowerCase());
  }

  protected void createDB(String type) {
    switch(type) {
      case "mysql":
        db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
        break;
      case "h2":
        db = new H2(h2File, mysqlUser, mysqlPassword);
        break;
      case "sqlite":
        db = new H2(h2File, mysqlUser, mysqlPassword);
        break;
      default:
        db = new FlatFile(TNE.instance().getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
    }
  }

  //Helper methods to automatically cast db to proper database class
  public SQLDatabase sql() {
    if(db == null) createDB();
    return (SQLDatabase)db;
  }

  public MySQL mysql() {
    if(db == null) createDB();
    return (MySQL)db;
  }

  public SQLite sqlite() {
    if(db == null) createDB();
    return (SQLite)db;
  }

  public H2 h2() {
    if(db == null) createDB();
    return (H2)db;
  }


  public FlatFile flatfile() {
    if(db == null) createDB();
    return (FlatFile)db;
  }

  //abstract methods to be implemented by each child class
  public abstract double versionNumber();
  public abstract void update(double version, String type);
  public abstract Map<String, TransactionHistory> loadTransactions();
  public abstract TransactionHistory loadHistory(UUID id);
  public abstract void saveTransaction(Record record);
  public abstract void deleteTransaction(UUID id);
  public abstract Collection<Account> loadAccounts();
  public abstract Account loadAccount(UUID id);
  public abstract void saveAccount(Account acc);
  public abstract void deleteAccount(UUID id);
  public abstract Collection<Shop> loadShops();
  public abstract Shop loadShop(String name, String world);
  public abstract void saveShop(Shop shop);
  public abstract void deleteShop(Shop shop);
  public abstract Collection<TNESign> loadSigns();
  public abstract TNESign loadSign(String location);
  public abstract void saveSign(TNESign sign);
  public abstract void deleteSign(TNESign sign);
  public abstract Collection<Auction> loadAuctions();
  public abstract Auction loadAuction(Integer lot);
  public abstract void saveAuction(Auction auction);
  public abstract void deleteAuction(Auction auction);
  public abstract Collection<Claim> loadClaims();
  public abstract Claim loadClaim(UUID owner, Integer lot);
  public abstract void saveClaim(Claim claim);
  public abstract void deleteClaim(Claim claim);
  public abstract Map<String, UUID> loadIDS();
  public abstract UUID loadID(String username);
  public abstract void saveID(String username, UUID id);
  public abstract void removeID(String username);
  public abstract void removeID(UUID id);
  public abstract void loadFlat(File file);
  public abstract void saveFlat(File file);
  public abstract void loadMySQL();
  public abstract void saveMySQL();
  public abstract void loadSQLite();
  public abstract void saveSQLite();
  public abstract void loadH2();
  public abstract void saveH2();
  public abstract void createTables(String type);
}