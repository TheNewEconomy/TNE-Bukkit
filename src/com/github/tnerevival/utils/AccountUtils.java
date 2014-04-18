package com.github.tnerevival.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

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
		String world = PlayerUtils.getWorld(username);
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
			return account.getBalance(PlayerUtils.getWorld(username));
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
		Account account = getAccount(username);
		String world = PlayerUtils.getWorld(username);
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

	public static void addFunds(String username, double amount) {
		setBalance(username, getBalance(username) + amount);
	}

	public static void removeFunds(String username, double amount) {
		setBalance(username, getBalance(username) - amount);
	}

	public static Boolean giveMoney(String username, Double amount) {
		if(exists(username)) {
			String world = PlayerUtils.getWorld(username);
			addFunds(username, amount);
			if(Bukkit.getPlayer(username) != null) Bukkit.getPlayer(username).sendMessage(ChatColor.WHITE + "You were given " + ChatColor.GOLD + MISCUtils.formatBalance(world, amount) + ChatColor.WHITE + ".");
			return true;
		}
		return false;
	}

	public static Boolean payMoney(String from, String to, Double amount) {
		if(exists(to)) {
			String world = PlayerUtils.getWorld(to);
			removeFunds(from, amount);
			addFunds(to, amount);
			if(Bukkit.getPlayer(to) != null) Bukkit.getPlayer(to).sendMessage(ChatColor.WHITE + "You were paid " + ChatColor.GOLD + MISCUtils.formatBalance(world, amount) + ChatColor.WHITE + " by " + from + ".");
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