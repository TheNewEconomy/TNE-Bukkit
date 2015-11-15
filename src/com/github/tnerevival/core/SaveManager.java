package com.github.tnerevival.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.version.Alpha2_2;
import com.github.tnerevival.core.version.Version;

public class SaveManager {

	static HashMap<Double, Version> versions;
	static {
		versions = new HashMap<Double, Version>();
		versions.put(2.2, new Alpha2_2());
	}
	
	Version versionInstance;
	Double currentSaveVersion = 2.2;
	Double saveVersion = 0.0;
	String type = TNE.configurations.getString("Core.Database.Type");
	File file = new File(TNE.instance.getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
	
	public SaveManager() {
		versionInstance = versions.get(currentSaveVersion);
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
			String table = versionInstance.prefix + "_INFO";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + versionInstance.mysqlHost + ":" + versionInstance.mysqlPort + "/" + versionInstance.mysqlDatabase, versionInstance.mysqlUser, versionInstance.mysqlPassword);
				statement = connection.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema = ? AND table_name = ?;");
				statement.setString(1, versionInstance.mysqlDatabase);
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
			File sqliteDB = new File(versionInstance.sqliteFile);
			if(!sqliteDB.exists()) {
				return true;
			}
			Connection connection;
			PreparedStatement statement;
			ResultSet result;
			String table = versionInstance.prefix + "_INFO";
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + versionInstance.sqliteFile);
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
			versionInstance.createTables("mysql");
		} else if(type.equalsIgnoreCase("sqlite")) {
			versionInstance.createTables("sqlite");
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
			String table = versionInstance.prefix + "_INFO";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + versionInstance.mysqlHost + ":" + versionInstance.mysqlPort + "/" + versionInstance.mysqlDatabase, versionInstance.mysqlUser, versionInstance.mysqlPassword);
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
			String table = versionInstance.prefix + "_INFO";
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:" + versionInstance.sqliteFile);
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
	}
	
	public void load() {
		if(saveVersion < versionInstance.versionNumber() && saveVersion != 0) {
			versionInstance.update(saveVersion, type.toLowerCase());
			return;
		}
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
		TNE.instance.getLogger().info("Data saved!");
	}
	
	public Boolean backupDatabase() throws IOException {
		if(type.equalsIgnoreCase("mysql")) return false;
		
		String db = (type.equalsIgnoreCase("flatfile")) ? TNE.configurations.getString("Core.Database.FlatFile.File") : TNE.configurations.getString("Core.Database.SQLite.File");
		FileInputStream fileIn = new FileInputStream(new File(TNE.instance.getDataFolder(), db));
		FileOutputStream fileOut = new FileOutputStream(new File(TNE.instance.getDataFolder(), "Database.zip"));
		ZipOutputStream zipOut = new ZipOutputStream(fileOut);
		ZipEntry entry = new ZipEntry(db);
		zipOut.putNextEntry(entry);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fileIn.read(buffer)) > 0) {
			zipOut.write(buffer, 0, length);
		}
		fileIn.close();
		zipOut.closeEntry();
		zipOut.close();
		return true;
	}
	
	
	//Actual Save/Load Methods
	
	//FlatFile Methods
	public void loadFlatFile() {
		versionInstance.loadFlat(file);
	}
	
	public void saveFlatFile() {
		versionInstance.saveFlat(file);
	}
	
	//MySQL Methods
	public void loadMySQL() {
		versionInstance.loadMySQL();
	}
	
	public void saveMySQL() {
		versionInstance.saveMySQL();
	}
	
	//SQLite Methods
	public void loadSQLite() {
		versionInstance.loadSQLite();
	}
	
	public void saveSQLite() {
		versionInstance.saveSQLite();
	}
}