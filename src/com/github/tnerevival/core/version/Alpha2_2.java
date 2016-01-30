package com.github.tnerevival.core.version;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.db.FlatFile;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.SQLite;
import com.github.tnerevival.core.db.flat.Article;
import com.github.tnerevival.core.db.flat.Entry;
import com.github.tnerevival.core.db.flat.FlatFileConnection;
import com.github.tnerevival.core.db.flat.Section;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;

public class Alpha2_2 extends Version {

	@Override
	public double versionNumber() {
		return 2.2;
	}

	@Override
	public void update(double version, String type) {
		//we're not going to update old TNE versions since we haven't changed any data since around Alpha 2.1...
	}

	@Override
	public void loadFlat(File file) {
		db = new FlatFile(TNE.instance.getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
		FlatFileConnection connection = (FlatFileConnection)db.connection();
		Section accounts = null;
		try {
			connection.getOIS().readDouble();
			accounts = (Section) connection.getOIS().readObject();
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
			account.setCompany((String) info.getData("company"));
			account.setStatus((String) info.getData("status"));
			account.setOverflow(AccountUtils.overflowFromString((String)info.getData("overflow")));
			
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
			info.addData("company", acc.getCompany());
			info.addData("status", acc.getStatus().getName());
			info.addData("overflow", acc.overflowToString());
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
		try {
			db = new FlatFile(TNE.instance.getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
			FlatFileConnection connection = (FlatFileConnection)db.connection();
			connection.getOOS().writeDouble(versionNumber());
			connection.getOOS().writeObject(accounts);
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
				account.setCompany(mysql().results().getString("company"));
				account.setAccountNumber(mysql().results().getInt("accountnumber"));
				account.setStatus(mysql().results().getString("accountstatus"));
				account.setJoined(mysql().results().getString("joinedDate"));
				account.setOverflow(AccountUtils.overflowFromString(mysql().results().getString("overflow")));
				
				String bankTable = prefix + "_BANKS";
				mysql().executePreparedQuery("SELECT * FROM " + bankTable + " WHERE uuid = ?;", new Object[] { account.getUid().toString() }, false);
				
				while(mysql().secondary().next()) {
					account.getBanks().put(mysql().secondary().getString("world"), BankUtils.fromString(mysql().secondary().getString("bank")));
				}
				TNE.instance.manager.accounts.put(account.getUid(), account);
			}
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
		
		Iterator<java.util.Map.Entry<UUID, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();
		
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
					mysql().executePreparedUpdate("UPDATE " + table + " SET balances = ?, joinedDate = ?, accountnumber = ?, company = ?, accountstatus = ?, overflow = ? WHERE uuid = ?;", 
							new Object[] {
								entry.getValue().balancesToString(),
								entry.getValue().getJoined(),
								entry.getValue().getAccountNumber(),
								entry.getValue().getCompany(),
								entry.getValue().getStatus().getName(),
								entry.getValue().overflowToString(),
								entry.getKey().toString()
							});
				} else {
					mysql().executePreparedUpdate("INSERT INTO " + table + " (uuid, balances, joinedDate, accountnumber, company, accountstatus, overflow) VALUES (?, ?, ?, ?, ?, ?, ?);", 
							new Object[] {
								entry.getKey().toString(),
								entry.getValue().balancesToString(),
								entry.getValue().getJoined(),
								entry.getValue().getAccountNumber(),
								entry.getValue().getCompany(),
								entry.getValue().getStatus().getName(),
								entry.getValue().overflowToString()
							});
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		mysql().close();
	}

	@Override
	public void loadSQLite() {
		String table = prefix + "_USERS";
		db = new SQLite(sqliteFile);
		try {
			sqlite().executeQuery("SELECT * FROM " + table + ";");
			
			while(sqlite().results().next()) {
				UUID uid = UUID.fromString(sqlite().results().getString("uuid"));
				
				Account account = new Account(uid, sqlite().results().getInt("accountnumber"));
				account.balancesFromString(sqlite().results().getString("balances"));
				account.setCompany(sqlite().results().getString("company"));
				account.setStatus(sqlite().results().getString("accountstatus"));
				account.setJoined(sqlite().results().getString("joinedDate"));
				account.setOverflow(AccountUtils.overflowFromString(sqlite().results().getString("overflow")));
				
				String bankTable = prefix + "_BANKS";
				sqlite().executePreparedQuery("SELECT * FROM " + bankTable + " WHERE uuid = ?;", new Object[] { uid.toString() }, false);
				
				while(sqlite().secondary().next()) {
					account.getBanks().put(sqlite().secondary().getString("world"), BankUtils.fromString(sqlite().secondary().getString("bank")));
				}
				TNE.instance.manager.accounts.put(uid, account);
			}
			sqlite().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveSQLite() {
		String table = prefix + "_INFO";
		db = new SQLite(sqliteFile);
		sqlite().executePreparedUpdate("Update " + table + " SET version = ? WHERE id = 1;", new Object[] { String.valueOf(versionNumber()) });
		
		Iterator<java.util.Map.Entry<UUID, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();
		
		while(it.hasNext()) {
			java.util.Map.Entry<UUID, Account> entry = it.next();
			
			Iterator<java.util.Map.Entry<String, Bank>> bankIterator = entry.getValue().getBanks().entrySet().iterator();
			
			while(bankIterator.hasNext()) {
				java.util.Map.Entry<String, Bank> bankEntry = bankIterator.next();
				
				table = prefix + "_BANKS";
				try {
					sqlite().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ? AND world = ?;", 
						new Object[] {
							entry.getKey().toString(),
							bankEntry.getKey()
						});
					if(sqlite().results().next()) {
						sqlite().executePreparedUpdate("UPDATE " + table + " SET bank = ? WHERE uuid = ? AND world = ?;", 
								new Object[] {
									bankEntry.getValue().toString(),
									entry.getKey().toString(),
									bankEntry.getKey()
								});
					} else {
						sqlite().executePreparedUpdate("INSERT INTO " + table + " (uuid, world, bank) VALUES (?, ?, ?);", 
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
				sqlite().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?;", new Object[] { entry.getKey().toString() });
				if(sqlite().results().next()) {
					sqlite().executePreparedUpdate("UPDATE " + table + " SET balances = ?, joinedDate = ?, accountnumber = ?, company = ?, accountstatus = ?, overflow = ? WHERE uuid = ?;", 
							new Object[] {
								entry.getValue().balancesToString(),
								entry.getValue().getJoined(),
								entry.getValue().getAccountNumber(),
								entry.getValue().getCompany(),
								entry.getValue().getStatus().getName(),
								entry.getValue().overflowToString(),
								entry.getKey().toString()
							});
				} else {
					sqlite().executePreparedUpdate("INSERT INTO " + table + " (uuid, balances, joinedDate, accountnumber, company, accountstatus, overflow) VALUES (?, ?, ?, ?, ?, ?, ?);", 
							new Object[] {
								entry.getKey().toString(),
								entry.getValue().balancesToString(),
								entry.getValue().getJoined(),
								entry.getValue().getAccountNumber(),
								entry.getValue().getCompany(),
								entry.getValue().getStatus().getName(),
								entry.getValue().overflowToString()
							});
				}
				sqlite().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void loadYAML() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveYAML() {
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

			table = prefix + "_USERS";
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   			"uuid VARCHAR(36) NOT NULL," +
								"balances LONGTEXT," +
								"joinedDate VARCHAR(60)," +
								"accountnumber INTEGER," +
								"company VARCHAR(60)," +
								"accountstatus VARCHAR(60)," +
								"overflow LONGTEXT" +
								");");
			
			table = prefix + "_BANKS";
			mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "uuid VARCHAR(36) NOT NULL," +
								   "world VARCHAR(50) NOT NULL," +
								   "bank LONGTEXT" +
								   ");");
			mysql().close();
		} else {
			File sqliteDB = new File(sqliteFile);
			if(!sqliteDB.exists()) {
				try {
					sqliteDB.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			db = new SQLite(sqliteFile);
			
			sqlite().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   "id INTEGER NOT NULL," +
					   "version CHAR(10)" +
					   ");");
			sqlite().executeUpdate("INSERT INTO " + table + " (id, version) VALUES(1, " + versionNumber() + ");");

			table = prefix + "_USERS";
			sqlite().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								"uuid CHAR(36) NOT NULL," +
								"balances LONGTEXT," +
								"joinedDate CHAR(60)," +
								"accountnumber INTEGER," +
								"company CHAR(60)," +
								"accountstatus CHAR(60)," +
								"overflow LONGTEXT" +
								");");
			
			table = prefix + "_BANKS";
			sqlite().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "uuid CHAR(36) NOT NULL," +
								   "world CHAR(50) NOT NULL," +
								   "bank LONGTEXT" +
								   ");");
			sqlite().close();
		}
	}

}