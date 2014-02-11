package com.github.tnerevival.core;

import java.util.HashMap;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.io.FlatFileIO;
import com.github.tnerevival.core.io.MySQLIO;

public class Economy {
	
	public HashMap<String, Account> accounts = new HashMap<String, Account>();
	
	public HashMap<String, Bank> banks = new HashMap<String, Bank>();
	
	FlatFileIO flatfile;
	MySQLIO mysql;

	public Economy() {
		flatfile = new FlatFileIO(this);
		mysql = new MySQLIO(this);
		initiate();
	}
	
	public void initiate() {
		String db = TNE.instance.getConfig().getString("Core.Database.Type");
		if(db.equalsIgnoreCase("flatfile")) {
			flatfile.check();
		} else if(db.equalsIgnoreCase("mysql")) {
			//TODO: Add MySQL Support.
		}
	}
	
	public void loadData() {
		TNE.instance.getLogger().info("Loading economy data...");
		String db = TNE.instance.getConfig().getString("Core.Database.Type");
		if(db.equalsIgnoreCase("flatfile")) {
			flatfile.loadData();
		} else if(db.equalsIgnoreCase("mysql")) {
			//TODO: Add MySQL Support.
		}
	}
	
	public void saveData() {
		TNE.instance.getLogger().info("Saving economy data...");
		String db = TNE.instance.getConfig().getString("Core.Database.Type");
		if(db.equalsIgnoreCase("flatfile")) {
			flatfile.saveData();
		} else if(db.equalsIgnoreCase("mysql")) {
			//TODO: Add MySQL Support.
		}
	}
}