package com.github.tnerevival.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.db.flat.Article;
import com.github.tnerevival.core.db.flat.Entry;
import com.github.tnerevival.core.db.flat.Section;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;

public class SaveManager {
	
	File file = new File(TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.FlatFile.File"));

	//MySQL Variables
	String mysqlHost = TNE.instance.getConfig().getString("Core.Database.MySQL.Host");
	Integer mysqlPort = TNE.instance.getConfig().getInt("Core.Database.MySQL.Port");
	String mysqlDatabase = TNE.instance.getConfig().getString("Core.Database.MySQL.Database");
	String mysqlUser = TNE.instance.getConfig().getString("Core.Database.MySQL.User");
	String mysqlPassword = TNE.instance.getConfig().getString("Core.Database.MySQL.Password").trim();
	
	String prefix = TNE.instance.getConfig().getString("Core.Database.Prefix");
	String type = TNE.instance.getConfig().getString("Core.Database.Type");
	String sqliteFile = TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.SQLite.File");
	Double currentSaveVersion = 2.1;
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
			Connection connection;
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase, mysqlUser, mysqlPassword);
				statement = connection.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema = ? AND table_name = ?;");
				statement.setString(1, mysqlDatabase);
				statement.setString(2, table);
				result = statement.executeQuery();
				Boolean toReturn = result.first();
				connection.close();
				return !toReturn;
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if(type.equalsIgnoreCase("sqlite")) {
			File sqliteDB = new File(sqliteFile);
			if(!sqliteDB.exists()) {
				return true;
			}
			Connection connection;
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
				statement = connection.prepareStatement("SELECT * FROM sqlite_master WHERE type='table' AND name = ?;");
				statement.setString(1, table);
				result = statement.executeQuery();
				Boolean toReturn = result.next();
				connection.close();
				return !toReturn;
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
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
			File sqliteDB = new File(sqliteFile);
			if(!sqliteDB.exists()) {
				try {
					sqliteDB.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
			Connection connection;
			Statement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase, mysqlUser, mysqlPassword);
				statement = connection.createStatement();
				result = statement.executeQuery("SELECT version FROM " + table + " WHERE id = 1;");
				if(result.first()) {
					saveVersion = Double.valueOf(result.getString("version"));
				}
				connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if(type.equalsIgnoreCase("sqlite")) {
			Connection connection;
			Statement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
				statement = connection.createStatement();
				result = statement.executeQuery("SELECT version FROM " + table + " WHERE id = 1;");
				if(result.next()) {
					saveVersion = Double.valueOf(result.getString("version"));
				}
				connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
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
				TNE.instance.manager.legacy = (HashMap<String, Account>) ois.readObject();
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
				
				TNE.instance.manager.legacy.put(entry.getKey(), account);
			}
		}
	}
	
	public void saveFlatFile() {
		if(currentSaveVersion == 2.0) {
			Iterator<java.util.Map.Entry<String, Account>> it = TNE.instance.manager.legacy.entrySet().iterator();
			
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
		Connection connection;
		if(saveVersion == 2.0) {
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_USERS";
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase, mysqlUser, mysqlPassword);
				statement = connection.prepareStatement("SELECT * FROM " + table + ";");
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
					PreparedStatement bankStatement = connection.prepareStatement("SELECT * FROM " + bankTable + " WHERE username = ?;");
					bankStatement.setString(1, account.getOwner());
					ResultSet banks = bankStatement.executeQuery();
					
					while(banks.next()) {
						account.getBanks().put(banks.getString("world"), BankUtils.fromString(banks.getString("bank")));
					}
					TNE.instance.manager.legacy.put(account.getOwner(), account);
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveMySQL() {
		Connection connection = null;
		if(currentSaveVersion == 2.0) {
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase, mysqlUser, mysqlPassword);
				if(saveVersion != 0.0) {
					statement = connection.prepareStatement("Update " + table + " SET version = ? WHERE id = 1;");
					statement.setString(1, String.valueOf(currentSaveVersion));
				} else {
					statement = connection.prepareStatement("INSERT INTO " + table +" (id, version) VALUES(?, ?);");
					statement.setInt(1, 1);
					statement.setString(2, String.valueOf(currentSaveVersion));
				}
				statement.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			Iterator<java.util.Map.Entry<String, Account>> it = TNE.instance.manager.legacy.entrySet().iterator();
			
			while(it.hasNext()) {
				java.util.Map.Entry<String, Account> entry = it.next();
				
				Iterator<java.util.Map.Entry<String, Bank>> bankIterator = entry.getValue().getBanks().entrySet().iterator();
				
				while(bankIterator.hasNext()) {
					java.util.Map.Entry<String, Bank> bankEntry = bankIterator.next();
					
					table = prefix + "_BANKS";
					
					try {
						statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE username = ? AND world = ?;");
						statement.setString(1, entry.getKey());
						statement.setString(2, bankEntry.getKey());
						result = statement.executeQuery();
						if(result.first()) {
							statement = connection.prepareStatement("UPDATE " + table + " SET bank = ? WHERE username = ? AND world = ?;");
							statement.setString(1, bankEntry.getValue().toString());
							statement.setString(2, entry.getKey());
							statement.setString(3, bankEntry.getKey());
							statement.executeUpdate();
						} else {
							statement = connection.prepareStatement("INSERT INTO " + table + " (username, world, bank) VALUES (?, ?, ?);");
							statement.setString(1, entry.getKey());
							statement.setString(2, bankEntry.getKey());
							statement.setString(3, bankEntry.getValue().toString());
							statement.executeUpdate();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				table = prefix + "_USERS";
				
				try {
					statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE username = ?;");
					statement.setString(1, entry.getKey());
					result = statement.executeQuery();
					if(result.first()) {
						statement = connection.prepareStatement("UPDATE " + table + " SET balances = ?, joinedDate = ?, accountnumber = ?, company = ?, accountstatus = ?, overflow = ? WHERE username = ?;");
						statement.setString(1, entry.getValue().balancesToString());
						statement.setString(2, entry.getValue().getJoined());
						statement.setInt(3, entry.getValue().getAccountNumber());
						statement.setString(4, entry.getValue().getCompany());
						statement.setString(5, entry.getValue().getStatus());
						statement.setString(6, entry.getValue().overflowToString());
						statement.setString(7, entry.getKey());
						statement.executeUpdate();
					} else {
						statement = connection.prepareStatement("INSERT INTO " + table + " (username, balances, joinedDate, accountnumber, company, accountstatus, overflow) VALUES (?, ?, ?, ?, ?, ?, ?);");
						statement.setString(1, entry.getKey());
						statement.setString(2, entry.getValue().balancesToString());
						statement.setString(3, entry.getValue().getJoined());
						statement.setInt(4, entry.getValue().getAccountNumber());
						statement.setString(5, entry.getValue().getCompany());
						statement.setString(6, entry.getValue().getStatus());
						statement.setString(7, entry.getValue().overflowToString());
						statement.executeUpdate();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//SQLite Methods
	public void loadSQLite() {
		if(saveVersion == 2.0) {
			Connection connection;
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_USERS";
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
				statement = connection.prepareStatement("SELECT * FROM " + table + ";");
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
					PreparedStatement bankStatement = connection.prepareStatement("SELECT * FROM " + bankTable + " WHERE username = ?;");
					bankStatement.setString(1, account.getOwner());
					ResultSet banks = bankStatement.executeQuery();
					
					while(banks.next()) {
						account.getBanks().put(banks.getString("world"), BankUtils.fromString(banks.getString("bank")));
					}
					TNE.instance.manager.legacy.put(account.getOwner(), account);
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveSQLite() {
		if(currentSaveVersion == 2.0) {
			Connection connection = null;
			PreparedStatement statement;
			ResultSet result;
			String table = prefix + "_INFO";
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
				if(saveVersion != 0.0) {
					statement = connection.prepareStatement("Update " + table + " SET version = ? WHERE id = 1;");
					statement.setString(1, String.valueOf(currentSaveVersion));
				} else {
					statement = connection.prepareStatement("INSERT INTO " + table +" (id, version) VALUES(?, ?);");
					statement.setInt(1, 1);
					statement.setString(2, String.valueOf(currentSaveVersion));
				}
				statement.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			Iterator<java.util.Map.Entry<String, Account>> it = TNE.instance.manager.legacy.entrySet().iterator();
			
			while(it.hasNext()) {
				java.util.Map.Entry<String, Account> entry = it.next();
				
				Iterator<java.util.Map.Entry<String, Bank>> bankIterator = entry.getValue().getBanks().entrySet().iterator();
				
				while(bankIterator.hasNext()) {
					java.util.Map.Entry<String, Bank> bankEntry = bankIterator.next();
					
					table = prefix + "_BANKS";
					
					try {
						statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE username = ? AND world = ?;");
						statement.setString(1, entry.getKey());
						statement.setString(2, bankEntry.getKey());
						result = statement.executeQuery();
						if(result.next()) {
							statement = connection.prepareStatement("UPDATE " + table + " SET bank = ? WHERE username = ? AND world = ?;");
							statement.setString(1, bankEntry.getValue().toString());
							statement.setString(2, entry.getKey());
							statement.setString(3, bankEntry.getKey());
							statement.executeUpdate();
						} else {
							statement = connection.prepareStatement("INSERT INTO " + table + " (username, world, bank) VALUES (?, ?, ?);");
							statement.setString(1, entry.getKey());
							statement.setString(2, bankEntry.getKey());
							statement.setString(3, bankEntry.getValue().toString());
							statement.executeUpdate();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				table = prefix + "_USERS";
				
				try {
					statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE username = ?;");
					statement.setString(1, entry.getKey());
					result = statement.executeQuery();
					if(result.next()) {
						statement = connection.prepareStatement("UPDATE " + table + " SET balances = ?, joinedDate = ?, accountnumber = ?, company = ?, accountstatus = ?, overflow = ? WHERE username = ?;");
						statement.setString(1, entry.getValue().balancesToString());
						statement.setString(2, entry.getValue().getJoined());
						statement.setInt(3, entry.getValue().getAccountNumber());
						statement.setString(4, entry.getValue().getCompany());
						statement.setString(5, entry.getValue().getStatus());
						statement.setString(6, entry.getValue().overflowToString());
						statement.setString(7, entry.getKey());
						statement.executeUpdate();
					} else {
						statement = connection.prepareStatement("INSERT INTO " + table + " (username, balances, joinedDate, accountnumber, company, accountstatus, overflow) VALUES (?, ?, ?, ?, ?, ?, ?);");
						statement.setString(1, entry.getKey());
						statement.setString(2, entry.getValue().balancesToString());
						statement.setString(3, entry.getValue().getJoined());
						statement.setInt(4, entry.getValue().getAccountNumber());
						statement.setString(5, entry.getValue().getCompany());
						statement.setString(6, entry.getValue().getStatus());
						statement.setString(7, entry.getValue().overflowToString());
						statement.executeUpdate();
					}
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void createMySQLTables() {
		Connection connection;
		Statement statement;
		String table = prefix + "_INFO";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase, mysqlUser, mysqlPassword);
			statement = connection.createStatement();
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "id INTEGER NOT NULL," +
								   "version VARCHAR(10)" +
								   ");");
			
			table = prefix + "_USERS";
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   			"id INTEGER NOT NULL AUTO_INCREMENT," +
					   			"uuid varchar(36) NOT NULL," +
								"username VARCHAR(40) NOT NULL," +
								"balances LONGTEXT," +
								"joinedDate VARCHAR(60)," +
								"accountnumber INTEGER," +
								"company VARCHAR(60)," +
								"accountstatus VARCHAR(60)," +
								"overflow LONGTEXT," +
								"PRIMARY KEY (`id`)" +
								");");
			
			table = prefix + "_BANKS";
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "id INTEGER NOT NULL AUTO_INCREMENT," +
								   "username VARCHAR(40) NOT NULL," +
								   "world VARCHAR(50) NOT NULL," +
								   "bank LONGTEXT," +
								   "PRIMARY KEY (`id`)" +
								   ");");
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void createSQLiteTables() {
		Connection connection;
		Statement statement;
		String table = prefix + "_INFO";
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
			statement = connection.createStatement();
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "id INTEGER NOT NULL," +
								   "version CHAR(10)" +
								   ");");
			
			table = prefix + "_USERS";
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
					   			"id INTEGER PRIMARY KEY NOT NULL," +
								"uuid CHAR(36) NOT NULL," +
								"username CHAR(40) NOT NULL," +
								"balances LONGTEXT," +
								"joinedDate CHAR(60)," +
								"accountnumber INTEGER," +
								"company CHAR(60)," +
								"accountstatus CHAR(60)," +
								"overflow LONGTEXT" +
								");");
			
			table = prefix + "_BANKS";
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
								   "id INTEGER PRIMARY KEY NOT NULL," +
								   "username CHAR(40) NOT NULL," +
								   "world CHAR(50) NOT NULL," +
								   "bank LONGTEXT" +
								   ");");
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}