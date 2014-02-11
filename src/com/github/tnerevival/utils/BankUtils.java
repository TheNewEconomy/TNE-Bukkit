package com.github.tnerevival.utils;

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
			
			Double gold = entry.getValue().getGold();
			Double interestEarned = gold * TNE.instance.getConfig().getDouble("Core.Bank.Interest.Rate");
			entry.getValue().setGold(gold + interestEarned);
		}
	}
	
	public static void applyInterest(String username, String world) {
		Bank bank = getBank(username, world);

		Double gold = bank.getGold();
		Double interestEarned = gold * TNE.instance.getConfig().getDouble("Core.Bank.Interest.Rate");
		bank.setGold(gold + interestEarned);
	}
	
	public static Boolean hasOldBank(String username) {
		return TNE.instance.manager.banks.containsKey(username);
	}
	
	public static Boolean hasBank(String username) {
		if(TNE.instance.getConfig().getBoolean("Core.Multiworld")) {
			return AccountUtils.getAccount(username).getBanks().containsKey(PlayerUtils.getWorld(username));
		}
		return AccountUtils.getAccount(username).getBanks().containsKey(TNE.instance.defaultWorld);
	}
	
	public static Bank getBank(String username) {
		if(TNE.instance.getConfig().getBoolean("Core.Multiworld")) {
			return AccountUtils.getAccount(username).getBank(PlayerUtils.getWorld(username));			
		}
		return AccountUtils.getAccount(username).getBank(TNE.instance.defaultWorld);
	}
	
	public static Bank getBank(String username, String world) {
		return AccountUtils.getAccount(username).getBank(world);
	}
	
	public static Inventory getBankInventory(String username) {
		if(!hasBank(username)) {
			return null;
		} else {
			Bank bank = getBank(username);
			String gold = "Gold: " + MISCUtils.formatMoney(bank.getGold());
			Inventory bankInventory = Bukkit.createInventory(null, TNE.instance.getConfig().getInt("Core.Bank.Size"), ChatColor.RED + username + " " + ChatColor.GOLD + gold);
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
}