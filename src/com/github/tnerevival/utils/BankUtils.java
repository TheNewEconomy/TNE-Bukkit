package com.github.tnerevival.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.serializable.SerializableItemStack;

public class BankUtils {
	public static void applyInterest(String username) {
		Account account = AccountUtils.getAccount(username);
		Iterator<Entry<String, Bank>> it = account.getBanks().entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<String, Bank> entry = it.next();
			
			if(interestEnabled(entry.getKey())) {
				Double gold = entry.getValue().getGold();
				Double interestEarned = gold * interestRate(entry.getKey());
				entry.getValue().setGold(gold + interestEarned);
			}
		}
	}
	
	public static Boolean interestEnabled(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Interest.Enabled")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.Interest.Enabled");
			}
		}
		return TNE.instance.getConfig().getBoolean("Core.Bank.Interest.Enabled");
	}
	
	public static Double interestRate(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Interest.Rate")) {
				return TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".Bank.Interest.Rate");
			}
		}
		return TNE.instance.getConfig().getDouble("Core.Bank.Interest.Rate");
	}
	
	public static Boolean hasOldBank(String username) {
		return TNE.instance.manager.banks.containsKey(username);
	}
	
	public static Boolean hasBank(String username) {
		if(MISCUtils.multiWorld()) {
			return AccountUtils.getAccount(username).getBanks().containsKey(MISCUtils.getWorld(username));
		}
		String defaultWorld = TNE.instance.defaultWorld;
		if(defaultWorld == null) {
			TNE.instance.getLogger().warning("***WORLD NAME IS NULL***");
		}
		return AccountUtils.getAccount(username).getBanks().containsKey(defaultWorld);
	}
	
	public static Bank getBank(String username) {
		if(MISCUtils.multiWorld()) {
			return AccountUtils.getAccount(username).getBank(MISCUtils.getWorld(username));			
		}
		return AccountUtils.getAccount(username).getBank(TNE.instance.defaultWorld);
	}
	
	public static Bank getBank(String username, String world) {
		return AccountUtils.getAccount(username).getBank(world);
	}
	
	public static Bank fromString(String bankString) {
		String[] variables = bankString.split("\\:");
		Bank bank = new Bank(variables[0], Integer.parseInt(variables[2]), Double.parseDouble(variables[3]));
		bank.setPin(variables[1]);
		List<SerializableItemStack> items = new  ArrayList<SerializableItemStack>();
		
		if(variables[4] != "TNENOSTRINGVALUE") {
			String[] itemStrings = variables[4].split("\\*");
			for(String s : itemStrings) {
				items.add(MISCUtils.itemstackFromString(s));
			}
		}
		bank.setItems(items);
		return bank;
	}
	
	public static Inventory getBankInventory(String username) {
		if(!hasBank(username)) {
			return null;
		} else {
			Bank bank = getBank(username);
			String gold = "Gold: " + MISCUtils.getShort(bank.getGold());
			Inventory bankInventory = Bukkit.createInventory(null, size(MISCUtils.getWorld(username)), ChatColor.WHITE + "Bank " + ChatColor.GOLD + gold);
			if(bank.getItems().size() > 0) {
				List<SerializableItemStack> items = bank.getItems();
				
				for(SerializableItemStack stack : items) {
					bankInventory.setItem(stack.getSlot(), stack.toItemStack());
				}
			}
			return bankInventory;
		}
	}
	
	public static Double getBankBalance(String username) {
		if(!hasBank(username)) {
			return null;
		} else {
			Bank bank = getBank(username);
			return bank.getGold();
		}
	}
	
	public static Boolean bankHasFunds(String username, Double amount) {
		return (getBankBalance(username) != null) ? getBankBalance(username) >= amount : false;
	}
	
	public static Boolean bankDeposit(String username, Double amount) {
		if(AccountUtils.hasFunds(username, amount)) {
			Bank bank = getBank(username);
			bank.setGold(bank.getGold() + amount);
			AccountUtils.removeFunds(username, amount);
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean bankWithdraw(String username, Double amount) {
		if(bankHasFunds(username, amount)) {
			Bank bank = getBank(username);
			bank.setGold(bank.getGold() - amount);
			AccountUtils.addFunds(username, amount);
			return true;
		} else {
			return false;
		}
	}
	
	//Configuration-related Utils
	
	public static Integer size(String world) {
		Integer rows = 3;
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Rows")) {
				rows = TNE.instance.worldConfigurations.getInt("Worlds." + world + ".Bank.Rows");
			}
		} else {
			rows = TNE.instance.getConfig().getInt("Core.Bank.Rows");
		}
		return (rows >= 1 && rows <= 6) ? (rows * 9) : 27;
	}
	
	public static Boolean enabled(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Enabled")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.Enabled");
			}
		}
		return TNE.instance.getConfig().getBoolean("Core.Bank.Enabled");
	}
	
	public static Boolean command(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Command")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.Command");
			}
		}
		return TNE.instance.getConfig().getBoolean("Core.Bank.Command");
	}
	
	public static Double cost(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Cost")) {
				return TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".Bank.Cost");
			}
		}
		return TNE.instance.getConfig().getDouble("Core.Bank.Cost");
	}
	
	public static Boolean sign(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Sign")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.Sign");
			}
		}
		return TNE.instance.getConfig().getBoolean("Core.Bank.Sign");
	}
	
	public static Boolean npc(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.NPC")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.NPC");
			}
		}
		return TNE.instance.getConfig().getBoolean("Core.Bank.NPC");
	}
}