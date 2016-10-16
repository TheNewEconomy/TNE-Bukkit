package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.version.Alpha2_2;
import com.github.tnerevival.core.version.Alpha3_0;
import com.github.tnerevival.core.version.Version;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SaveManager {

	static HashMap<Double, Version> versions;
	static {
		versions = new HashMap<>();
		versions.put(2.2, new Alpha2_2());
		versions.put(3.3, new Alpha3_0());
	}
	
	Version versionInstance;
	Double currentSaveVersion = 3.3;
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

	public void deleteAccount(UUID id) {
	  String table = versionInstance.prefix + "_USERS";
    if(!type.equalsIgnoreCase("flatfile")) {
      versionInstance.mysql().executePreparedUpdate("DELETE FROM " + table + " WHERE uuid = ?",
          new Object[] { id.toString() });
      table = versionInstance.prefix + "_ECOIDS";
      versionInstance.mysql().executePreparedUpdate("DELETE FROM " + table + " WHERE uuid = ?",
          new Object[] { id.toString() });
    }
  }

  public void deleteShop(String name, String world) {
    if(!type.equalsIgnoreCase("flatfile")) {
      String table = versionInstance.prefix + "_SHOPS";
      versionInstance.mysql().executePreparedUpdate("DELETE FROM " + table + " WHERE shop_name = ? AND shop_world = ?",
          new Object[] { name, world });
    }
  }
	
	private Boolean firstRun() {
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


        result = connection.getMetaData().getTables(null, null, table, null);

        return !result.next();
			} catch (Exception e) {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(type.equalsIgnoreCase("h2")) {
      File h2DB = new File(versionInstance.h2File);
      if(!h2DB.exists()) {
        return true;
      }
      Connection connection;
      PreparedStatement statement;
      ResultSet result;
      String table = versionInstance.prefix + "_INFO";
      try {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:" + versionInstance.h2File + ";mode=MySQL", versionInstance.mysqlUser, versionInstance.mysqlPassword);
        result = connection.getMetaData().getTables(null, null, table, null);
        return result.next();
      } catch (Exception e) {
        e.printStackTrace();
      }
		}
		return !file.exists();
	}
	
	private void initiate() {
		if(type.equalsIgnoreCase("flatfile")) {
			try {
				TNE.instance.getDataFolder().mkdir();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(type.equalsIgnoreCase("mysql")) {
			versionInstance.createTables("mysql");
		} else if(type.equalsIgnoreCase("sqlite") || type.equalsIgnoreCase("h2")) {
			versionInstance.createTables("h2");
		}
	}
	
	private void getVersion() {
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
			} catch(Exception e) {
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
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if (type.equalsIgnoreCase("h2")) {
      Connection connection;
      Statement statement;
      ResultSet result;
      String table = versionInstance.prefix + "_INFO";
      try {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:" + versionInstance.h2File + ";mode=MySQL", versionInstance.mysqlUser, versionInstance.mysqlPassword);
        statement = connection.createStatement();
        result = statement.executeQuery("SELECT version FROM " + table + " WHERE id = 1;");
        if(result.next()) {
          saveVersion = Double.valueOf(result.getString("version"));
        }
        connection.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
		}
	}
	
	private void convert() {
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
		} else if(type.equalsIgnoreCase("h2")) {
		  loadH2();
    }
	}
	
	public void save() {
		if(type.equalsIgnoreCase("flatfile")) {
			saveFlatFile();
		} else if(type.equalsIgnoreCase("mysql")) {
			saveMySQL();
		} else if(type.equalsIgnoreCase("h2") || type.equalsIgnoreCase("sqlite")) {
			saveH2();
		}
		TNE.instance.getLogger().info("Data saved!");
	}
	
	public Boolean backupDatabase() throws IOException {
		if(type.equalsIgnoreCase("mysql")) return false;
		
		String db = (type.equalsIgnoreCase("flatfile")) ? TNE.configurations.getString("Core.Database.FlatFile.File") : TNE.configurations.getString("Core.Database.H2.File");
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
  private void loadFlatFile() {
		Version loadVersion = (saveVersion != 0.0) ? versions.get(saveVersion) : versionInstance;
		loadVersion.loadFlat(file);
	}

  private void saveFlatFile() {
		versionInstance.saveFlat(file);
	}
	
	//MySQL Methods
  private void loadMySQL() {
		Version loadVersion = (saveVersion != 0.0) ? versions.get(saveVersion) : versionInstance;
		loadVersion.loadMySQL();
	}

  private void saveMySQL() {
		versionInstance.saveMySQL();
	}
	
	//SQLite Methods
  private void loadSQLite() {
		Version loadVersion = (saveVersion != 0.0) ? versions.get(saveVersion) : versionInstance;
    if(saveVersion != 0.0 && saveVersion < 3.0) {
      loadVersion.loadSQLite();
      return;
    }
    loadVersion.loadH2();
	}

  private void saveSQLite() {
    //We no longer support SQLite starting post Alpha 3.0. For now we just load from SQLite
    if(currentSaveVersion < 3.0) {
      //For testing purposes we need to populate SQLite with some data.
      versionInstance.saveSQLite();
    }
    versionInstance.saveH2();
	}

	private void loadH2() {
    Version loadVersion = (saveVersion != 0.0) ? versions.get(saveVersion) : versionInstance;
    if(saveVersion == 0.0 || saveVersion != 0.0 && saveVersion >= 3.0) {
      loadVersion.loadH2();
    }
  }

  private void saveH2() {
    versionInstance.saveH2();
  }
}