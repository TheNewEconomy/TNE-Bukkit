package com.github.tnerevival.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.event.TNECreateAccountEvent;
import com.github.tnerevival.core.event.TNEFundsAddEvent;
import com.github.tnerevival.core.event.TNEFundsGiveEvent;
import com.github.tnerevival.core.event.TNEFundsPayEvent;
import com.github.tnerevival.core.event.TNEFundsRemoveEvent;
import com.github.tnerevival.serializable.SerializableItemStack;

public class AccountUtils {
	
	public static Boolean exists(String username) {
		return TNE.instance.manager.accounts.containsKey(username);
	}
	
	public static void createAccount(String username) {
		TNECreateAccountEvent e = new TNECreateAccountEvent(username);
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			TNE.instance.manager.accounts.put(e.getUsername(), e.getAccount());
			AccountUtils.addFunds(e.getUsername(), AccountUtils.getInitialBalance(TNE.instance.defaultWorld));
		}
	}

	public static Account getAccount(String username) {
		if(exists(username)) {
			return TNE.instance.manager.accounts.get(username);
		} else {
			createAccount(username);
			return TNE.instance.manager.accounts.get(username);
		}
	}
	
	public static List<SerializableItemStack> overflowFromString(String overflowString) {
		List<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
		if(!overflowString.equalsIgnoreCase("TNENOSTRINGVALUE")) {
			String[] itemStrings = overflowString.split("\\*");
			
			for(String s : itemStrings) {
				items.add(MISCUtils.itemstackFromString(s));
			}
		}
		return items;
	}
	
	private static Double getBalance(String username) {
		return getBalance(username, MISCUtils.getWorld(username));
	}

	private static Double getBalance(String username, String world) {
		Account account = getAccount(username);
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Currency.ItemCurrency")) {
				if(TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Currency.ItemCurrency")) {
					Material majorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMajor"));
					Material minorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMinor"));
					Integer major = MISCUtils.getItemCount(username, majorItem);
					Integer minor = MISCUtils.getItemCount(username, minorItem);
					String balance = major + "." + minor;
					return Double.valueOf(balance);
				}
			}
			if(!account.getBalances().containsKey(world)) {
				initializeWorldData(username, world);
			}
			return account.getBalance(MISCUtils.getWorld(username));
		}
		if(TNE.instance.getConfig().getBoolean("Core.Currency.ItemCurrency")) {
			Material majorItem = Material.getMaterial(TNE.instance.getConfig().getString("Core.Currency.ItemMajor"));
			Material minorItem = Material.getMaterial(TNE.instance.getConfig().getString("Core.Currency.ItemMinor"));
			Integer major = MISCUtils.getItemCount(username, majorItem);
			Integer minor = MISCUtils.getItemCount(username, minorItem);
			String balance = major + "." + minor;
			return Double.valueOf(balance);
		}
		return account.getBalance(TNE.instance.defaultWorld);
	}
	
	private static void setBalance(String username, Double balance) {
		setBalance(username, MISCUtils.getWorld(username), balance);
	}

	private static void setBalance(String username, String world, Double balance) {
		Account account = getAccount(username);
		String balanceString = (String.valueOf(balance).contains(".")) ? String.valueOf(balance) : String.valueOf(balance) + ".0";
		String[] split = balanceString.split("\\.");
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Currency.ItemCurrency")) {
				if(TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Currency.ItemCurrency")) {
					Material majorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMajor"));
					Material minorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMinor"));
					MISCUtils.setItemCount(username, majorItem, Integer.valueOf(split[0].trim()));
					MISCUtils.setItemCount(username, minorItem, Integer.valueOf(split[1].trim()));
				}
			} else {
				if(!account.getBalances().containsKey(world)) {
					initializeWorldData(username, world);
				}
				account.setBalance(world, balance);
			}
		}
		if(TNE.instance.getConfig().getBoolean("Core.Currency.ItemCurrency")) {
			Material majorItem = Material.getMaterial(TNE.instance.getConfig().getString("Core.Currency.ItemMajor"));
			Material minorItem = Material.getMaterial(TNE.instance.getConfig().getString("Core.Currency.ItemMinor"));
			MISCUtils.setItemCount(username, majorItem, Integer.valueOf(split[0].trim()));
			MISCUtils.setItemCount(username, minorItem, Integer.valueOf(split[1].trim()));
		} else {
			account.setBalance(TNE.instance.defaultWorld, balance);
		}
	}

	public static Boolean hasFunds(String username, double amount) {
		return getBalance(username) >= amount;
	}
	
	public static Boolean hasFunds(String username, String world, double amount) {
		return getBalance(username, world) >= amount;
	}

	public static void addFunds(String username, double amount) {
		addFunds(username, MISCUtils.getWorld(username), amount);
	}
	
	public static void addFunds(String username, String world, double amount) {
		TNEFundsAddEvent e = new TNEFundsAddEvent(username, amount);
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			setBalance(e.getUsername(), world, e.getNewBalance());
		}
	}

	public static void removeFunds(String username, double amount) {
		removeFunds(username, MISCUtils.getWorld(username), amount);
	}
	
	public static void removeFunds(String username, String world, double amount) {
		TNEFundsRemoveEvent e = new TNEFundsRemoveEvent(username, amount);
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			setBalance(e.getUsername(), world, e.getNewBalance());
		}
	}
	
	public static Double getFunds(String username) {
		return getBalance(username);
	}
	
	public static Double getFunds(String username, String world) {
		return getBalance(username, world);
	}
	
	public static void setFunds(String username, double amount) {
		setBalance(username, amount);
	}
	
	public static void setFunds(String username, String world, double amount) {
		setBalance(username, world, amount);
	}

	public static Boolean giveMoney(String username, String from, Double amount) {
		if(exists(username)) {
			String world = MISCUtils.getWorld(username);
			TNEFundsGiveEvent e = new TNEFundsGiveEvent(username, from, amount);
			Bukkit.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				addFunds(e.getReceiver(), e.getAmount());
				if(Bukkit.getPlayer(e.getReceiver()) != null) Bukkit.getPlayer(e.getReceiver()).sendMessage(ChatColor.WHITE + "You were given " + ChatColor.GOLD + MISCUtils.formatBalance(world, amount) + ChatColor.WHITE + ".");
			}
			return true;
		}
		return false;
	}

	public static Boolean payMoney(String from, String to, Double amount) {
		if(exists(to)) {
			String world = MISCUtils.getWorld(to);
			TNEFundsPayEvent e = new TNEFundsPayEvent(from, to, amount);
			Bukkit.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				removeFunds(e.getSender(), e.getAmount());
				addFunds(e.getReceiver(), e.getAmount());
				if(Bukkit.getPlayer(to) != null) Bukkit.getPlayer(to).sendMessage(ChatColor.WHITE + "You were paid " + ChatColor.GOLD + MISCUtils.formatBalance(world, amount) + ChatColor.WHITE + " by " + from + ".");
			}
			return true;
		}
		return false;
	}
	
	public static void initializeWorldData(String username, String world) {
		Account account = getAccount(username);
		if(!account.getBalances().containsKey(world)) {
			account.setBalance(world, 0.0);
			addFunds(username, getInitialBalance(world));
		}
	}

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