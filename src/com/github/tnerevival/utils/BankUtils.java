package com.github.tnerevival.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.serializable.SerializableItemStack;

public class BankUtils {
	public static void applyInterest(UUID id) {
		Account account = AccountUtils.getAccount(id);
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
		return TNE.configurations.getBoolean("Core.Bank.Interest.Enabled");
	}
	
	public static Double interestRate(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Interest.Rate")) {
				return TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".Bank.Interest.Rate");
			}
		}
		return TNE.configurations.getDouble("Core.Bank.Interest.Rate");
	}
	
	public static Boolean hasOldBank(UUID id) {
		return TNE.instance.manager.banks.containsKey(id);
	}
	
	public static Boolean hasBank(UUID id) {
		if(MISCUtils.multiWorld()) {
			return AccountUtils.getAccount(id).getBanks().containsKey(MISCUtils.getWorld(id));
		}
		String defaultWorld = TNE.instance.defaultWorld;
		if(defaultWorld == null) {
			TNE.instance.getLogger().warning("***WORLD NAME IS NULL***");
		}
		return AccountUtils.getAccount(id).getBanks().containsKey(defaultWorld);
	}
	
	public static Bank getBank(UUID id) {
		if(MISCUtils.multiWorld()) {
			return AccountUtils.getAccount(id).getBank(MISCUtils.getWorld(id));			
		}
		return AccountUtils.getAccount(id).getBank(TNE.instance.defaultWorld);
	}
	
	public static Bank getBank(UUID id, String world) {
		return AccountUtils.getAccount(id).getBank(world);
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
	
	public static Inventory getBankInventory(UUID id) {
		if(!hasBank(id)) {
			return null;
		} else {
			Bank bank = getBank(id);
			String gold = "Gold: " + MISCUtils.getShort(bank.getGold());
			Inventory bankInventory = Bukkit.createInventory(null, size(MISCUtils.getWorld(id)), ChatColor.WHITE + "Bank " + ChatColor.GOLD + gold);
			if(bank.getItems().size() > 0) {
				List<SerializableItemStack> items = bank.getItems();
				
				for(SerializableItemStack stack : items) {
					bankInventory.setItem(stack.getSlot(), stack.toItemStack());
				}
			}
			return bankInventory;
		}
	}
	
	public static Double getBankBalance(UUID id) {
		if(!hasBank(id)) {
			return null;
		} else {
			Bank bank = getBank(id);
			return bank.getGold();
		}
	}
	
	public static Boolean bankHasFunds(UUID id, Double amount) {
		return (getBankBalance(id) != null) ? getBankBalance(id) >= amount : false;
	}
	
	public static Boolean bankDeposit(UUID id, Double amount) {
		if(AccountUtils.hasFunds(id, amount)) {
			Bank bank = getBank(id);
			bank.setGold(bank.getGold() + amount);
			AccountUtils.removeFunds(id, amount);
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean bankWithdraw(UUID id, Double amount) {
		if(bankHasFunds(id, amount)) {
			Bank bank = getBank(id);
			bank.setGold(bank.getGold() - amount);
			AccountUtils.addFunds(id, amount);
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
			rows = TNE.configurations.getInt("Core.Bank.Rows");
		}
		return (rows >= 1 && rows <= 6) ? (rows * 9) : 27;
	}
	
	public static Boolean enabled(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Enabled")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.Enabled");
			}
		}
		return TNE.configurations.getBoolean("Core.Bank.Enabled");
	}
	
	public static Boolean command(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Command")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.Command");
			}
		}
		return TNE.configurations.getBoolean("Core.Bank.Command");
	}
	
	public static Double cost(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Cost")) {
				return TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".Bank.Cost");
			}
		}
		return TNE.configurations.getDouble("Core.Bank.Cost");
	}
	
	public static Boolean sign(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.Sign")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.Sign");
			}
		}
		return TNE.configurations.getBoolean("Core.Bank.Sign");
	}
	
	public static Boolean npc(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Bank.NPC")) {
				return TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Bank.NPC");
			}
		}
		return TNE.configurations.getBoolean("Core.Bank.NPC");
	}
}