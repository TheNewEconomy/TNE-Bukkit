package com.github.tnerevival.core.version;

import java.io.File;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.db.Database;
import com.github.tnerevival.core.db.FlatFile;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.SQLite;

public abstract class Version {
	public String mysqlHost = TNE.configurations.getString("Core.Database.MySQL.Host");
	public Integer mysqlPort = TNE.configurations.getInt("Core.Database.MySQL.Port");
	public String mysqlDatabase = TNE.configurations.getString("Core.Database.MySQL.Database");
	public String mysqlUser = TNE.configurations.getString("Core.Database.MySQL.User");
	public String mysqlPassword = TNE.configurations.getString("Core.Database.MySQL.Password");
	public String prefix = TNE.configurations.getString("Core.Database.Prefix");
	public String sqliteFile = TNE.instance.getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.SQLite.File");
	
	Database db;
	
	//Helper methods to automatically cast db to proper database class
	public MySQL mysql() {
		return (MySQL)db;
	}
	
	public SQLite sqlite() {
		return (SQLite)db;
	}
	
	public FlatFile flatfile() {
		return (FlatFile)db;
	}
	
	//abstract methods to be implemented by each child class
	public abstract double versionNumber();
	public abstract void update(double version, String type);
	public abstract void loadFlat(File file);
	public abstract void saveFlat(File file);
	public abstract void loadMySQL();
	public abstract void saveMySQL();
	public abstract void loadSQLite();
	public abstract void saveSQLite();
	public abstract void loadYAML();
	public abstract void saveYAML();
	public abstract void createTables(String type);
}