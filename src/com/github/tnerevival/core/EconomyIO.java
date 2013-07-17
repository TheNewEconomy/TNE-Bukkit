package com.github.tnerevival.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.github.tnerevival.TheNewEconomy;
import com.github.tnerevival.core.accounts.Account;
import com.github.tnerevival.core.areas.Area;
import com.github.tnerevival.core.companies.Company;

public class EconomyIO {
	static File accountsFile = new File(TheNewEconomy.instance.getDataFolder(), "accounts.tne");
	static File areasFile = new File(TheNewEconomy.instance.getDataFolder(), "areas.tne");
	static File companiesFile = new File(TheNewEconomy.instance.getDataFolder(), "companies.tne");
	
	public static HashMap<String, Account> loadAccounts() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(accountsFile));
		Object result = ois.readObject();
		ois.close();
		return (HashMap<String, Account>)result;
	}
	
	public static void saveAccounts(HashMap<String, Account> accounts) {
		System.out.println("[TNE] Saving player accounts...");
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(accountsFile));
		  	oos.writeObject(accounts);
		  	oos.flush();
		  	oos.close();
		  	System.out.println("[TNE] Player accounts successfully saved.");
		} catch (FileNotFoundException e) {
			System.out.println("[TNE] Player accounts file not found. Generating file...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, Area> loadAreas() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(areasFile));
		Object result = ois.readObject();
		ois.close();
		return (HashMap<String, Area>)result;
	}
	
	public static void saveAreas(HashMap<String, Area> areas) {
		System.out.println("[TNE] Saving areas...");
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(areasFile));
		  	oos.writeObject(areas);
		  	oos.flush();
		  	oos.close();
		  	System.out.println("[TNE] Areas successfully saved.");
		} catch (FileNotFoundException e) {
			System.out.println("[TNE] Areas file not found. Generating file...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, Company> loadCompanies() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(companiesFile));
		Object result = ois.readObject();
		ois.close();
		return (HashMap<String, Company>)result;
	}
	
	public static void saveCompanies(HashMap<String, Company> companies) {
		System.out.println("[TNE] Saving companies...");
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(companiesFile));
		  	oos.writeObject(companies);
		  	oos.flush();
		  	oos.close();
		  	System.out.println("[TNE] Companies successfully saved.");
		} catch (FileNotFoundException e) {
			System.out.println("[TNE] Companies file not found. Generating file...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}