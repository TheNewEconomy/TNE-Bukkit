package com.github.tnerevival.core.io;

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
import com.github.tnerevival.core.Economy;

public class FlatFileIO {
	
	Economy eco;
	
	private String fileName = TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.FlatFile.File");
	private double currentFileVersion = 2.0;
	private double fileVersion = 1.0;
	
	public FlatFileIO(Economy eco) {
		this.eco = eco;
	}
	
	public void check() {
		File file = new File(fileName);
		if(!file.exists()) {
			initiate();
		} else {
			getFileVersion();
			loadData();
		}
	}
	
	public void getFileVersion() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			fileVersion = ois.readDouble();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initiate() {
		TNE.instance.getLogger().info("Initiating economy data file...");
		File file = new File(fileName);
		try {
			TNE.instance.getDataFolder().mkdir();
			file.createNewFile();
			TNE.instance.getLogger().info("Economy data file has been initialized.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveData() {
		if(currentFileVersion == 1.0) {
			fileAlphaOne(false);
		} else if(currentFileVersion == 2.0) {
			fileAlphaTwo(false);
		}
	}
	
	public void loadData() {
		if(fileVersion == 1.0) {
			fileAlphaOne(true);
		} else if(fileVersion == 2.0) {
			fileAlphaTwo(true);
		}
	}
	
	
	//Loading & Saving different eco data file versions
	@SuppressWarnings("unchecked")
	private void fileAlphaOne(boolean load) {
		if(load) {
			//Load an Alpha 1.X file
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
				fileVersion = ois.readDouble();
				eco.accounts = (HashMap<String, Account>)ois.readObject();
				eco.banks = (HashMap<String, Bank>)ois.readObject();
				ois.close();
				TNE.instance.getLogger().info("Economy data has been loaded.");
				TNE.instance.getLogger().info("Save File Version: " + fileVersion);
			} catch (FileNotFoundException e) {
				TNE.instance.getLogger().warning("Economy data file not found...");
			} catch (IOException e) {
				e.printStackTrace();
				TNE.instance.getLogger().warning("Error writing economy data...");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			//Save an Alpha 1.X file
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
				oos.writeDouble(1.0);
				oos.writeObject(eco.accounts);
				oos.writeObject(eco.banks);
				oos.flush();
			    oos.close();
			    TNE.instance.getLogger().info("Economy data has been saved.");
			} catch (FileNotFoundException e) {
				TNE.instance.getLogger().warning("Economy data file not found...");
			} catch (IOException e) {
				e.printStackTrace();
				TNE.instance.getLogger().warning("Error writing economy data...");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void fileAlphaTwo(boolean load) {
		if(load) {
			//Load an Alpha 2.X file
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
				fileVersion = ois.readDouble();
				eco.accounts = (HashMap<String, Account>)ois.readObject();
				ois.close();
				TNE.instance.getLogger().info("Economy data has been loaded.");
				TNE.instance.getLogger().info("Save File Version: " + fileVersion);
			} catch (FileNotFoundException e) {
				TNE.instance.getLogger().warning("Economy data file not found...");
			} catch (IOException e) {
				e.printStackTrace();
				TNE.instance.getLogger().warning("Error writing economy data...");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			//Save an Alpha 2.X file
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
				oos.writeDouble(2.0);
				oos.writeObject(eco.accounts);
				oos.flush();
			    oos.close();
			    TNE.instance.getLogger().info("Economy data has been saved.");
			} catch (FileNotFoundException e) {
				TNE.instance.getLogger().warning("Economy data file not found...");
			} catch (IOException e) {
				e.printStackTrace();
				TNE.instance.getLogger().warning("Error writing economy data...");
			}
		}
	}
}