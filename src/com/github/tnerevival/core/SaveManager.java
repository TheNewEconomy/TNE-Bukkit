package com.github.tnerevival.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.SQLite;
import com.github.tnerevival.core.db.flat.Article;
import com.github.tnerevival.core.db.flat.Entry;
import com.github.tnerevival.core.db.flat.Section;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;

public class SaveManager {
	
	//FlatFile ff = new FlatFile(TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.FlatFile.File"));
	File file = new File(TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.FlatFile.File"));
	
	String prefix = TNE.instance.getConfig().getString("Core.Database.Prefix");
	String type = TNE.instance.getConfig().getString("Core.Database.Type");
	String sqliteFile = TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.SQLite.File");
	Double currentSaveVersion = 2.0;
	Double saveVersion = 0.0;
	
	public SaveManager() {
		if(firstRun()) {
			initiate();
		} else {
			getVersion();
			TNE.instance.getLogger().info("Save file of version: " + saveVersion + " detected.");
			load();
			convert();
		}
	}
	
	public Boolean firstRun() {
		if(type.equalsIgnoreCase("flatfile")) {
			return !file.exists();
		} else if(type.equalsIgnoreCase("mysql")) {
			MySQL mysql = new MySQL(TNE.instance.getConfig().getString("Core.Database.MySQL.Host"), TNE.instance.getConfig().getInt("Core.Database.MySQL.Port"), TNE.instance.getConfig().getString("Core.Database.MySQL.Database"), TNE.instance.getConfig().getString("Core.Database.MySQL.User"), TNE.instance.getConfig().getString("Core.Database.MySQL.Password"));
			mysql.connect();
			try {
				DatabaseMetaData meta = ((Connection)mysql.connection()).getMetaData();
				ResultSet tables = meta.getTables(null, null, prefix + "_INFO", null);
				
				return !tables.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mysql.close();
		} else if(type.equalsIgnoreCase("sqlite")) {
			SQLite sqlite = new SQLite(sqliteFile);
			sqlite.connect();
			try {
				DatabaseMetaData meta = ((Connection)sqlite.connection()).getMetaData();
				ResultSet tables = meta.getTables(null, null, prefix + "_INFO", null);
				
				return !tables.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sqlite.close();
		}
		return !file.exists();
	}
	
	public void initiate() {
		if(type.equalsIgnoreCase("flatfile")) {
			try {
				TNE.instance.getDataFolder().mkdir();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(type.equalsIgnoreCase("mysql")) {
			createMySQLTables();
		} else if(type.equalsIgnoreCase("sqlite")) {
			createSQLiteTables();
		}
	}
	
	public void getVersion() {
		if(type.equalsIgnoreCase("flatfile")) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				saveVersion = ois.readDouble();
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(type.equalsIgnoreCase("mysql")) {
			MySQL mysql = new MySQL(TNE.instance.getConfig().getString("Core.Database.MySQL.Host"), TNE.instance.getConfig().getInt("Core.Database.MySQL.Port"), TNE.instance.getConfig().getString("Core.Database.MySQL.Database"), TNE.instance.getConfig().getString("Core.Database.MySQL.User"), TNE.instance.getConfig().getString("Core.Database.MySQL.Password"));
			mysql.connect();
			Statement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				statement = ((Connection) mysql.connection()).createStatement();
				result = statement.executeQuery("SELECT version FROM " + table + " WHERE id = 1;");
				if(result.first()) {
					saveVersion = Double.valueOf(result.getString("version"));
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			mysql.close();
		} else if(type.equalsIgnoreCase("sqlite")) {
			SQLite sqlite = new SQLite(sqliteFile);
			sqlite.connect();
			Statement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				statement = ((Connection) sqlite.connection()).createStatement();
				result = statement.executeQuery("SELECT version FROM " + table + " WHERE id = 1;");
				if(result.first()) {
					saveVersion = Double.valueOf(result.getString("version"));
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			sqlite.close();
		}
	}
	
	public void convert() {
		if(saveVersion == 1.0) {
			SaveConversion.alphaTwo();
		}
	}
	
	public void load() {
		if(type.equalsIgnoreCase("flatfile")) {
			loadFlatFile();
		} else if(type.equalsIgnoreCase("mysql")) {
			loadMySQL();
		} else if(type.equalsIgnoreCase("sqlite")) {
			loadSQLite();
		}
	}
	
	public void save() {
		if(type.equalsIgnoreCase("flatfile")) {
			saveFlatFile();
		} else if(type.equalsIgnoreCase("mysql")) {
			saveMySQL();
		} else if(type.equalsIgnoreCase("sqlite")) {
			saveSQLite();
		}
	}
	
	
	//Actual Save/Load Methods
	
	//FlatFile Methods
	@SuppressWarnings("unchecked")
	public void loadFlatFile() {
		if(saveVersion == 1.0 || saveVersion == 1.1) {			
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				saveVersion = ois.readDouble();
				TNE.instance.manager.accounts = (HashMap<String, Account>) ois.readObject();
				TNE.instance.manager.banks = (HashMap<String, Bank>) ois.readObject();
				ois.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(saveVersion == 2.0) {
			Section accounts = null;
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				saveVersion = ois.readDouble();
				accounts = (Section) ois.readObject();
				ois.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Iterator<java.util.Map.Entry<String, Article>> it = accounts.getArticle().entrySet().iterator();
			
			while(it.hasNext()) {
				java.util.Map.Entry<String, Article> entry = it.next();
				Account account = new Account(entry.getKey());
				HashMap<String, Double> balanceMap = new HashMap<String, Double>();
				HashMap<String, Bank> bankMap = new HashMap<String, Bank>();
				
				Entry info = entry.getValue().getEntry("info");
				Entry balances = entry.getValue().getEntry("balances");
				Entry banks = entry.getValue().getEntry("banks");
				
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
				
				TNE.instance.manager.accounts.put(entry.getKey(), account);
			}
		}
	}
	
	public void saveFlatFile() {
		if(currentSaveVersion == 2.0) {
			Iterator<java.util.Map.Entry<String, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();
			
			Section accounts = new Section("accounts");
			
			while(it.hasNext()) {
				java.util.Map.Entry<String, Account> entry = it.next();
				
				Account acc = entry.getValue();
				Article account = new Article(entry.getKey());
				//Info
				Entry info = new Entry("info");
				info.addData("accountnumber", acc.getAccountNumber());
				info.addData("owner", acc.getOwner());
				info.addData("company", acc.getCompany());
				info.addData("status", acc.getStatus());
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
				
				accounts.addArticle(entry.getKey(), account);
			}
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				oos.writeDouble(currentSaveVersion);
				oos.writeObject(accounts);
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//MySQL Methods
	public void loadMySQL() {
		MySQL mysql = new MySQL(TNE.instance.getConfig().getString("Core.Database.MySQL.Host"), TNE.instance.getConfig().getInt("Core.Database.MySQL.Port"), TNE.instance.getConfig().getString("Core.Database.MySQL.Database"), TNE.instance.getConfig().getString("Core.Database.MySQL.User"), TNE.instance.getConfig().getString("Core.Database.MySQL.Password"));
		mysql.connect();
		if(saveVersion == 2.0) {
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_USERS";
			
			try {
				statement = ((Connection) mysql.connection()).prepareStatement("SELECT * FROM " + table + ";");
				result = statement.executeQuery();
				
				while(result.next()) {
					Account account = new Account(result.getString("username"));
					account.balancesFromString(result.getString("balances"));
					account.setCompany(result.getString("company"));
					account.setAccountNumber(result.getInt("accountnumber"));
					account.setStatus(result.getString("accountstatus"));
					account.setJoined(result.getString("joinedDate"));
					account.setOverflow(AccountUtils.overflowFromString(result.getString("overflow")));
					
					String bankTable = prefix + "_BANKS";
					PreparedStatement bankStatement = ((Connection) mysql.connection()).prepareStatement("SELECT * FROM " + bankTable + " WHERE username = ?;");
					bankStatement.setString(1, account.getOwner());
					ResultSet banks = bankStatement.executeQuery();
					
					while(banks.next()) {
						account.getBanks().put(banks.getString("world"), BankUtils.fromString(banks.getString("bank")));
					}
					TNE.instance.manager.accounts.put(account.getOwner(), account);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		mysql.close();
	}
	
	public void saveMySQL() {
		MySQL mysql = new MySQL(TNE.instance.getConfig().getString("Core.Database.MySQL.Host"), TNE.instance.getConfig().getInt("Core.Database.MySQL.Port"), TNE.instance.getConfig().getString("Core.Database.MySQL.Database"), TNE.instance.getConfig().getString("Core.Database.MySQL.User"), TNE.instance.getConfig().getString("Core.Database.MySQL.Password"));
		mysql.connect();
		if(currentSaveVersion == 2.0) {
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				statement = ((Connection) mysql.connection()).prepareStatement("Update " + table + " SET version = ? WHERE id = 1;");
				statement.setString(1, String.valueOf(currentSaveVersion));
				statement.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			Iterator<java.util.Map.Entry<String, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();
			
			while(it.hasNext()) {
				java.util.Map.Entry<String, Account> entry = it.next();
				
				Iterator<java.util.Map.Entry<String, Bank>> bankIterator = entry.getValue().getBanks().entrySet().iterator();
				
				while(bankIterator.hasNext()) {
					java.util.Map.Entry<String, Bank> bankEntry = bankIterator.next();
					
					table = prefix + "_BANKS";
					
					try {
						statement = ((Connection) mysql.connection()).prepareStatement("SELECT * FROM " + table + " WHERE username = ? AND world = ?;");
						statement.setString(1, entry.getKey());
						statement.setString(2, bankEntry.getKey());
						result = statement.executeQuery();
						if(result.first()) {
							statement = ((Connection) mysql.connection()).prepareStatement("UPDATE " + table + " SET bank = ? WHERE username = ? AND world = ?;");
							statement.setString(1, bankEntry.getValue().toString());
							statement.setString(2, entry.getKey());
							statement.setString(3, bankEntry.getKey());
						} else {
							statement = ((Connection) mysql.connection()).prepareStatement("INSERT INTO " + table + " (id, username, world, bank) VALUES ('', ?, ?, ?);");
							statement.setString(1, entry.getKey());
							statement.setString(2, bankEntry.getKey());
							statement.setString(3, bankEntry.getValue().toString());
							statement.executeQuery();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				table = prefix + "_USERS";
				
				try {
					statement = ((Connection) mysql.connection()).prepareStatement("SELECT * FROM " + table + " WHERE username = ?;");
					statement.setString(1, entry.getKey());
					result = statement.executeQuery();
					if(result.first()) {
						statement = ((Connection) mysql.connection()).prepareStatement("UPDATE " + table + " SET balances = ?, joinedDate = ?, accountnumber = ?, company = ?, accountstatus = ?, overflow = ? WHERE username = ?;");
						statement.setString(1, entry.getValue().balancesToString());
						statement.setString(2, entry.getValue().getJoined());
						statement.setInt(3, entry.getValue().getAccountNumber());
						statement.setString(4, entry.getValue().getCompany());
						statement.setString(5, entry.getValue().getStatus());
						statement.setString(6, entry.getValue().overflowToString());
						statement.executeUpdate();
					} else {
						statement = ((Connection) mysql.connection()).prepareStatement("INSERT INTO " + table + " (username, balances, joinedDate, accountnumber, company, accountstatus, overflow) VALUES (?, ?, ?, ?, ?, ?, ?);");
						statement.setString(1, entry.getKey());
						statement.setString(2, entry.getValue().balancesToString());
						statement.setString(3, entry.getValue().getJoined());
						statement.setInt(4, entry.getValue().getAccountNumber());
						statement.setString(5, entry.getValue().getCompany());
						statement.setString(6, entry.getValue().getStatus());
						statement.setString(7, entry.getValue().overflowToString());
						statement.executeQuery();
					}					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		mysql.close();
	}
	
	//SQLite Methods
	public void loadSQLite() {
		SQLite sqlite = new SQLite(sqliteFile);
		sqlite.connect();
		if(saveVersion == 2.0) {
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_USERS";
			
			try {
				statement = ((Connection) sqlite.connection()).prepareStatement("SELECT * FROM " + table + ";");
				result = statement.executeQuery();
				
				while(result.next()) {
					Account account = new Account(result.getString("username"));
					account.balancesFromString(result.getString("balances"));
					account.setCompany(result.getString("company"));
					account.setAccountNumber(result.getInt("accountnumber"));
					account.setStatus(result.getString("accountstatus"));
					account.setJoined(result.getString("joinedDate"));
					account.setOverflow(AccountUtils.overflowFromString(result.getString("overflow")));
					
					String bankTable = prefix + "_BANKS";
					PreparedStatement bankStatement = ((Connection) sqlite.connection()).prepareStatement("SELECT * FROM " + bankTable + " WHERE username = ?;");
					bankStatement.setString(1, account.getOwner());
					ResultSet banks = bankStatement.executeQuery();
					
					while(banks.next()) {
						account.getBanks().put(banks.getString("world"), BankUtils.fromString(banks.getString("bank")));
					}
					TNE.instance.manager.accounts.put(account.getOwner(), account);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		sqlite.close();
	}
	
	public void saveSQLite() {
		SQLite sqlite = new SQLite(sqliteFile);
		sqlite.connect();
		if(currentSaveVersion == 2.0) {
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				statement = ((Connection) sqlite.connection()).prepareStatement("Update " + table + " SET version = ? WHERE id = 1;");
				statement.setString(1, String.valueOf(currentSaveVersion));
				statement.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			Iterator<java.util.Map.Entry<String, Account>> it = TNE.instance.manager.accounts.entrySet().iterator();
			
			while(it.hasNext()) {
				java.util.Map.Entry<String, Account> entry = it.next();
				
				Iterator<java.util.Map.Entry<String, Bank>> bankIterator = entry.getValue().getBanks().entrySet().iterator();
				
				while(bankIterator.hasNext()) {
					java.util.Map.Entry<String, Bank> bankEntry = bankIterator.next();
					
					table = prefix + "_BANKS";
					
					try {
						statement = ((Connection) sqlite.connection()).prepareStatement("SELECT * FROM " + table + " WHERE username = ? AND world = ?;");
						statement.setString(1, entry.getKey());
						statement.setString(2, bankEntry.getKey());
						result = statement.executeQuery();
						if(result.first()) {
							statement = ((Connection) sqlite.connection()).prepareStatement("UPDATE " + table + " SET bank = ? WHERE username = ? AND world = ?;");
							statement.setString(1, bankEntry.getValue().toString());
							statement.setString(2, entry.getKey());
							statement.setString(3, bankEntry.getKey());
						} else {
							statement = ((Connection) sqlite.connection()).prepareStatement("INSERT INTO " + table + " (id, username, world, bank) VALUES ('', ?, ?, ?);");
							statement.setString(1, entry.getKey());
							statement.setString(2, bankEntry.getKey());
							statement.setString(3, bankEntry.getValue().toString());
							statement.executeQuery();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				table = prefix + "_USERS";
				
				try {
					statement = ((Connection) sqlite.connection()).prepareStatement("SELECT * FROM " + table + " WHERE username = ?;");
					statement.setString(1, entry.getKey());
					result = statement.executeQuery();
					if(result.first()) {
						statement = ((Connection) sqlite.connection()).prepareStatement("UPDATE " + table + " SET balances = ?, joinedDate = ?, accountnumber = ?, company = ?, accountstatus = ?, overflow = ? WHERE username = ?;");
						statement.setString(1, entry.getValue().balancesToString());
						statement.setString(2, entry.getValue().getJoined());
						statement.setInt(3, entry.getValue().getAccountNumber());
						statement.setString(4, entry.getValue().getCompany());
						statement.setString(5, entry.getValue().getStatus());
						statement.setString(6, entry.getValue().overflowToString());
						statement.executeUpdate();
					} else {
						statement = ((Connection) sqlite.connection()).prepareStatement("INSERT INTO " + table + " (username, balances, joinedDate, accountnumber, company, accountstatus, overflow) VALUES (?, ?, ?, ?, ?, ?, ?);");
						statement.setString(1, entry.getKey());
						statement.setString(2, entry.getValue().balancesToString());
						statement.setString(3, entry.getValue().getJoined());
						statement.setInt(4, entry.getValue().getAccountNumber());
						statement.setString(5, entry.getValue().getCompany());
						statement.setString(6, entry.getValue().getStatus());
						statement.setString(7, entry.getValue().overflowToString());
						statement.executeQuery();
					}					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		sqlite.close();
	}
	
	public void createMySQLTables() {
		MySQL mysql = new MySQL(TNE.instance.getConfig().getString("Core.Database.MySQL.Host"), TNE.instance.getConfig().getInt("Core.Database.MySQL.Port"), TNE.instance.getConfig().getString("Core.Database.MySQL.Database"), TNE.instance.getConfig().getString("Core.Database.MySQL.User"), TNE.instance.getConfig().getString("Core.Database.MySQL.Password"));
		mysql.connect();
		Statement statement;
		String table = prefix + "_INFO";
		try {
			statement = ((Connection) mysql.connection()).createStatement();
			
			statement.executeQuery("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "id INTEGER NOT NULL," +
								   "version VARCHAR(10)" +
								   ");");
			
			table = prefix + "_USERS";
			statement.executeQuery("CREATE TABLE IF NOT EXISTS " + table + " (" +
								"username VARCHAR(40) NOT NULL," +
								"balances LONGTEXT," +
								"joinedDate VARCHAR(60)," +
								"accountnumber INTEGER," +
								"company VARCHAR(60)," +
								"accountstatus VARCHAR(60)," +
								"overflow LONGTEXT," +
								"PRIMARY KEY (`username`)" +
								");");
			
			table = prefix + "_BANKS";
			statement.executeQuery("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "id INTEGER NOT NULL AUTO_INCREMENT," +
								   "username VARCHAR(40) NOT NULL," +
								   "world VARCHAR(50) NOT NULL," +
								   "bank LONGTEXT," +
								   "PRIMARY KEY ('id')" +
								   ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mysql.close();
	}
	
	public void createSQLiteTables() {
		SQLite sqlite = new SQLite(sqliteFile);
		sqlite.connect();
		Statement statement;
		String table = prefix + "_INFO";
		try {
			statement = ((Connection) sqlite.connection()).createStatement();
			
			statement.executeQuery("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "id INTEGER NOT NULL," +
								   "version CHAR(10)" +
								   ");");
			
			table = prefix + "_USERS";
			statement.executeQuery("CREATE TABLE IF NOT EXISTS " + table + " (" +
								"username CHAR(40) PRIMARY KEY NOT NULL," +
								"balances LONGTEXT," +
								"joinedDate CHAR(60)," +
								"accountnumber INTEGER," +
								"company CHAR(60)," +
								"accountstatus CHAR(60)" +
								"overflow LONGTEXT," +
								");");
			
			table = prefix + "_BANKS";
			statement.executeQuery("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "id INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT," +
								   "username CHAR(40) NOT NULL," +
								   "world CHAR(50) NOT NULL," +
								   "bank LONGTEXT" +
								   ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sqlite.close();
	}
}