package com.github.tnerevival.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;

public class AccountUtils {
	
	public static Boolean exists(String username) {
		return TNE.instance.manager.accounts.containsKey(username);
	}
	
	public static Account getAccount(String username) {
		if(exists(username)) {
			return TNE.instance.manager.accounts.get(username);
		}
		return null;
	}
	
	public static Double getBalance(String username) {
		Account account = getAccount(username);
		if(MISCUtils.multiWorld()) {
			//return account.getBalance(PlayerUtils.getWorld(username));
		}
		//return account.getBalance(TNE.instance.defaultWorld);
		return 1.0;
	}
	
	public static void setBalance(String username, Double balance) {
		Account account = getAccount(username);
		if(MISCUtils.multiWorld()) {
			//account.setBalance(PlayerUtils.getWorld(username), balance);
			return;
		}
		//account.setBalance(TNE.instance.defaultWorld, balance);
	}
	
	public static void initializeWorldData(String username, String world) {
		Account account = getAccount(username);
		if(!account.getBalances().containsKey(world)) {
			//account.setBalance(world, getInitialBalance(world));
		}
	}
	
	public static Boolean hasFunds(String username, double amount) {
		return getBalance(username) >= amount;
	}
	
	public static void addFunds(String username, double amount) {
		setBalance(username, getBalance(username) + amount);
	}
	
	public static void removeFunds(String username, double amount) {
		setBalance(username, getBalance(username) - amount);
	}
	
	public static Boolean giveMoney(String username, Double amount) {
		if(exists(username)) {
			addFunds(username, amount);
			if(Bukkit.getPlayer(username) != null) Bukkit.getPlayer(username).sendMessage(ChatColor.WHITE + "You were given " + ChatColor.GOLD + MISCUtils.formatBalance(PlayerUtils.getWorld(username), amount) + ChatColor.WHITE + ".");
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean payMoney(String from, String to, Double amount) {
		if(exists(to)) {
			removeFunds(from, amount);
			addFunds(to, amount);
			if(Bukkit.getPlayer(to) != null) Bukkit.getPlayer(to).sendMessage(ChatColor.WHITE + "You were paid " + ChatColor.GOLD + MISCUtils.formatBalance(PlayerUtils.getWorld(from), amount) + ChatColor.WHITE + " by " + from + ".");
			return true;
		} else {
			return false;
		}
	}
	
	//Configuration-related Utils
	
	public static Double getInitialBalance(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Balance")) {
				return TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".Balance");
			}
		}
		return TNE.instance.getConfig().getDouble("Core.Balance");
	}
	
	public static Double getWorldCost(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".balance")) {
				return TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".ChangeFee");
			}
		}
		return TNE.instance.getConfig().getDouble("Core.World.ChangeFee");
	}
}