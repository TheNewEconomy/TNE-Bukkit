package com.github.tnerevival.core;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

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
import com.github.tnerevival.utils.BankUtils;

public class SaveManager {
	
	MySQL sql = new MySQL(TNE.instance.getConfig().getString("Core.Database.MySQL.Host"), TNE.instance.getConfig().getInt("Core.Database.MySQL.Port"), TNE.instance.getConfig().getString("Core.Database.MySQL.Database"), TNE.instance.getConfig().getString("Core.Database.MySQL.User"), TNE.instance.getConfig().getString("Core.Database.MySQL.Password"));
	FlatFile ff = new FlatFile(TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.FlatFile.File"));
	SQLite sqlite = new SQLite(TNE.instance.getDataFolder() + File.separator + TNE.instance.getConfig().getString("Core.Database.SQLite.File"));
	
	String type = TNE.instance.getConfig().getString("Core.Database.Type");
	Double currentSaveVersion = 2.0;
	Double saveVersion = 0.0;
	
	public SaveManager() {
		
	}
	
	public void getVersion() {
		if(type.equalsIgnoreCase("flatfile")) {
			ff.connect();
			ObjectInputStream ois = ((FlatFileConnection)ff.connection()).getOIS();
			
			try {
				saveVersion = ois.readDouble();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ff.close();
		} else if(type.equalsIgnoreCase("mysql")) {
			//TODO: MySQL Support
		} else if(type.equalsIgnoreCase("sqlite")) {
			//TODO: SQLite Support
		}
	}
	
	public void load() {
		if(type.equalsIgnoreCase("flatfile")) {
			loadFlatFile();
		} else if(type.equalsIgnoreCase("mysql")) {
			//TODO: MySQL Support
		} else if(type.equalsIgnoreCase("sqlite")) {
			//TODO: SQLite Support
		}
	}
	
	public void save() {
		if(type.equalsIgnoreCase("flatfile")) {
			saveFlatFile();
		} else if(type.equalsIgnoreCase("mysql")) {
			//TODO: MySQL Support
		} else if(type.equalsIgnoreCase("sqlite")) {
			//TODO: SQLite Support
		}
	}
	
	
	//Actual Save/Load Methods
	
	//FlatFile Methods
	@SuppressWarnings("unchecked")
	public void loadFlatFile() {
		if(saveVersion == 1.0) {
			ff.connect();
			ObjectInputStream ois = ((FlatFileConnection)ff.connection()).getOIS();
			
			try {
				saveVersion = ois.readDouble();
				TNE.instance.manager.accounts = (HashMap<String, Account>) ois.readObject();
				TNE.instance.manager.banks = (HashMap<String, Bank>) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ff.close();
		} else if(saveVersion == 2.0) {
			Section accounts = null;
			ff.connect();
			ObjectInputStream ois = ((FlatFileConnection)ff.connection()).getOIS();
			
			try {
				saveVersion = ois.readDouble();
				accounts = (Section) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ff.close();
			
			Iterator<java.util.Map.Entry<String, Article>> it = accounts.getArticle().entrySet().iterator();
			
			while(it.hasNext()) {
				java.util.Map.Entry<String, Article> entry = it.next();
				Account account = new Account(entry.getKey());
				HashMap<String, HashMap<String, Double>> balanceMap = new HashMap<String, HashMap<String, Double>>();
				HashMap<String, Bank> bankMap = new HashMap<String, Bank>();
				
				Entry info = entry.getValue().getEntry("info");
				Entry balances = entry.getValue().getEntry("balances");
				Entry banks = entry.getValue().getEntry("banks");
				
				account.setAccountNumber((Integer) info.getData("accountnumber"));
				account.setCompany((String) info.getData("company"));
				account.setStatus((String) info.getData("status"));
				
				Iterator<java.util.Map.Entry<String, Object>> balanceIterator = balances.getData().entrySet().iterator();
				
				while(balanceIterator.hasNext()) {
					java.util.Map.Entry<String, Object> balanceEntry = balanceIterator.next();
					
					balanceMap.put(balanceEntry.getKey(), (HashMap<String, Double>)balanceEntry.getValue());
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
				account.addEntry(info);
				//Balances
				Entry balances = new Entry("balances");
				Iterator<java.util.Map.Entry<String, HashMap<String, Double>>> balanceIterator = acc.getBalances().entrySet().iterator();
				
				while(balanceIterator.hasNext()) {
					java.util.Map.Entry<String, HashMap<String, Double>> balanceEntry = balanceIterator.next();
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
			ff.connect();
			ObjectOutputStream oos = ((FlatFileConnection)ff.connection()).getOOS();
			try {
				oos.writeDouble(currentSaveVersion);
				oos.writeObject(accounts);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ff.close();
		}
	}
	
	//MySQL Methods
	public void loadMySQL() {
		if(saveVersion == 2.0) {
			
		}
	}
	
	public void saveMySQL() {
		if(currentSaveVersion == 2.0) {
			
		}
	}
	
	//SQLite Methods
	public void loadSQLite() {
		if(saveVersion == 2.0) {
			
		}
	}
	
	public void saveSQLite() {
		if(currentSaveVersion == 2.0) {
			
		}
	}
}