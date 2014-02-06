package com.github.tnerevival.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.serializable.SerializableItemStack;

public class PlayerUtils {
	
	public static Boolean exists(String username) {
		return TNE.instance.manager.accounts.containsKey(username);
	}
	
	public static Boolean hasFunds(String username, double amount) {
		Account account = TNE.instance.manager.accounts.get(username);
		
		return account.getBalance() >= amount;
	}
	
	public static void addFunds(String username, double amount) {
		Account account = TNE.instance.manager.accounts.get(username);
		account.setBalance(account.getBalance() + amount);
	}
	
	public static void removeFunds(String username, double amount) {
		Account account = TNE.instance.manager.accounts.get(username);
		account.setBalance(account.getBalance() - amount);
	}
	
	public static Boolean giveMoney(String username, Double amount) {
		if(exists(username)) {
			addFunds(username, amount);
			if(Bukkit.getPlayer(username) != null) Bukkit.getPlayer(username).sendMessage(ChatColor.WHITE + "You were given " + ChatColor.GOLD + MISCUtils.formatBalance(amount) + ChatColor.WHITE + ".");
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean payMoney(String from, String to, Double amount) {
		if(exists(to)) {
			removeFunds(from, amount);
			addFunds(to, amount);
			if(Bukkit.getPlayer(to) != null) Bukkit.getPlayer(to).sendMessage(ChatColor.WHITE + "You were paid " + ChatColor.GOLD + MISCUtils.formatBalance(amount) + ChatColor.WHITE + " by " + from + ".");
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean hasBank(String username) {
		return TNE.instance.manager.banks.containsKey(username);
	}
	
	public static Inventory getBank(String username) {
		if(!TNE.instance.manager.banks.containsKey(username)) {
			return null;
		} else {
			Bank bank = TNE.instance.manager.banks.get(username);
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
			Bank bank = TNE.instance.manager.banks.get(username);
			return bank.getGold();
		}
	}
	
	public static Boolean bankHasFunds(String username, Double amount) {
		return (getBankBalance(username) != null) ? getBankBalance(username) >= amount : false;
	}
	
	public static Boolean bankDeposit(String username, Double amount) {
		if(hasFunds(username, amount)) {
			Bank bank = TNE.instance.manager.banks.get(username);
			bank.setGold(bank.getGold() + amount);
			removeFunds(username, amount);
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean bankWithdraw(String username, Double amount) {
		if(bankHasFunds(username, amount)) {
			Bank bank = TNE.instance.manager.banks.get(username);
			bank.setGold(bank.getGold() - amount);
			addFunds(username, amount);
			return true;
		} else {
			return false;
		}
	}
}