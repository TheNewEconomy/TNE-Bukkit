package com.github.tnerevival.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;

public class Economy {
	
	public HashMap<String, Account> accounts = new HashMap<String, Account>();
	
	public HashMap<String, Bank> banks = new HashMap<String, Bank>();
	
	public String fileName = TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.FlatFile.File");	

	public double curFileVersion = 1.0;
	public double fileVersion = 1.0;

	public Economy() {
		File file = new File(fileName);
		if(!file.exists()) {
			initiate();
		} else {
			loadData();
		}
	}
	
	public void initiate() {
		TNE.instance.getLogger().info("[TNE] Initiating economy...");
		String db = TNE.instance.getConfig().getString("Core.Database.Type");
		if(db.equalsIgnoreCase("flatfile")) {
			File file = new File(fileName);
			try {
				TNE.instance.getDataFolder().mkdir();
				file.createNewFile();
				TNE.instance.getLogger().info("[TNE] Economy has been initialized.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(db.equalsIgnoreCase("mysql")) {
			//TODO: Add MySQL Support.
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadData() {
		TNE.instance.getLogger().info("[TNE] Loading economy data...");
		String db = TNE.instance.getConfig().getString("Core.Database.Type");
		if(db.equalsIgnoreCase("flatfile")) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
				fileVersion = ois.readDouble();
				accounts = (HashMap<String, Account>)ois.readObject();
				banks = (HashMap<String, Bank>)ois.readObject();
				ois.close();
				TNE.instance.getLogger().info("[TNE] Economy data has been loaded.");
				TNE.instance.getLogger().info("[TNE] Save File Version: " + fileVersion);
			} catch (FileNotFoundException e) {
				TNE.instance.getLogger().warning("[TNE] Economy data file not found...");
			} catch (IOException e) {
				e.printStackTrace();
				TNE.instance.getLogger().warning("[TNE] Error writing economy data file not found...");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if(db.equalsIgnoreCase("mysql")) {
			//TODO: Add MySQL Support.
		}
	}
	
	public void saveData() {
		TNE.instance.getLogger().info("[TNE] Saving economy data...");
		String db = TNE.instance.getConfig().getString("Core.Database.Type");
		if(db.equalsIgnoreCase("flatfile")) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
				oos.writeDouble(curFileVersion);
				oos.writeObject(accounts);
				oos.writeObject(banks);
				oos.flush();
			    oos.close();
			    TNE.instance.getLogger().info("[TNE] Economy data has been saved.");
			} catch (FileNotFoundException e) {
				TNE.instance.getLogger().warning("[TNE] Economy data file not found...");
			} catch (IOException e) {
				e.printStackTrace();
				TNE.instance.getLogger().warning("[TNE] Error writing economy data file not found...");
			}
		} else if(db.equalsIgnoreCase("mysql")) {
			//TODO: Add MySQL Support.
		}
	}
}