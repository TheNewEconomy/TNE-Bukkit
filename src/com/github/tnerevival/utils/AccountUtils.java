package com.github.tnerevival.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	
	public static Boolean exists(UUID id) {
		return TNE.instance.manager.accounts.containsKey(id);
	}
	
	public static void createAccount(UUID id) {
		TNECreateAccountEvent e = new TNECreateAccountEvent(Bukkit.getPlayer(id));
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			TNE.instance.manager.accounts.put(e.getPlayer().getUniqueId(), e.getAccount());
			AccountUtils.addFunds(e.getPlayer().getUniqueId(), AccountUtils.getInitialBalance(TNE.instance.defaultWorld));
		}
	}

	public static Account getAccount(UUID id) {
		if(exists(id)) {
			return TNE.instance.manager.accounts.get(id);
		} else {
			createAccount(id);
			return TNE.instance.manager.accounts.get(id);
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
	
	private static Double getBalance(UUID id) {
		return getBalance(id, MISCUtils.getWorld(id));
	}

	private static Double getBalance(UUID id, String world) {
		Account account = getAccount(id);
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Currency.ItemCurrency")) {
				if(TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Currency.ItemCurrency")) {
					Material majorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMajor"));
					Material minorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMinor"));
					Integer major = MISCUtils.getItemCount(id, majorItem);
					Integer minor = MISCUtils.getItemCount(id, minorItem);
					String balance = major + "." + minor;
					return Double.valueOf(balance);
				}
			}
			if(!account.getBalances().containsKey(world)) {
				initializeWorldData(id, world);
			}
			return account.getBalance(MISCUtils.getWorld(id));
		}
		if(TNE.instance.getConfig().getBoolean("Core.Currency.ItemCurrency")) {
			Material majorItem = Material.getMaterial(TNE.instance.getConfig().getString("Core.Currency.ItemMajor"));
			Material minorItem = Material.getMaterial(TNE.instance.getConfig().getString("Core.Currency.ItemMinor"));
			Integer major = MISCUtils.getItemCount(id, majorItem);
			Integer minor = MISCUtils.getItemCount(id, minorItem);
			String balance = major + "." + minor;
			return Double.valueOf(balance);
		}
		return account.getBalance(TNE.instance.defaultWorld);
	}
	
	private static void setBalance(UUID id, Double balance) {
		setBalance(id, MISCUtils.getWorld(id), balance);
	}

	private static void setBalance(UUID id, String world, Double balance) {
		Account account = getAccount(id);
		String balanceString = (String.valueOf(balance).contains(".")) ? String.valueOf(balance) : String.valueOf(balance) + ".0";
		String[] split = balanceString.split("\\.");
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Currency.ItemCurrency")) {
				if(TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Currency.ItemCurrency")) {
					Material majorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMajor"));
					Material minorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMinor"));
					MISCUtils.setItemCount(id, majorItem, Integer.valueOf(split[0].trim()));
					MISCUtils.setItemCount(id, minorItem, Integer.valueOf(split[1].trim()));
				}
			} else {
				if(!account.getBalances().containsKey(world)) {
					initializeWorldData(id, world);
				}
				account.setBalance(world, balance);
			}
		}
		if(TNE.instance.getConfig().getBoolean("Core.Currency.ItemCurrency")) {
			Material majorItem = Material.getMaterial(TNE.instance.getConfig().getString("Core.Currency.ItemMajor"));
			Material minorItem = Material.getMaterial(TNE.instance.getConfig().getString("Core.Currency.ItemMinor"));
			MISCUtils.setItemCount(id, majorItem, Integer.valueOf(split[0].trim()));
			MISCUtils.setItemCount(id, minorItem, Integer.valueOf(split[1].trim()));
		} else {
			account.setBalance(TNE.instance.defaultWorld, balance);
		}
	}

	public static Boolean hasFunds(UUID id, double amount) {
		return getBalance(id) >= amount;
	}
	
	public static Boolean hasFunds(UUID id, String world, double amount) {
		return getBalance(id, world) >= amount;
	}

	public static void addFunds(UUID id, double amount) {
		addFunds(id, MISCUtils.getWorld(id), amount);
	}
	
	public static void addFunds(UUID id, String world, double amount) {
		TNEFundsAddEvent e = new TNEFundsAddEvent(Bukkit.getPlayer(id), amount);
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			setBalance(e.getPlayer().getUniqueId(), world, e.getNewBalance());
		}
	}

	public static void removeFunds(UUID id, double amount) {
		removeFunds(id, MISCUtils.getWorld(id), amount);
	}
	
	public static void removeFunds(UUID id, String world, double amount) {
		TNEFundsRemoveEvent e = new TNEFundsRemoveEvent(Bukkit.getPlayer(id), amount);
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			setBalance(e.getPlayer().getUniqueId(), world, e.getNewBalance());
		}
	}
	
	public static Double getFunds(UUID id) {
		return getBalance(id);
	}
	
	public static Double getFunds(UUID id, String world) {
		return getBalance(id, world);
	}
	
	public static void setFunds(UUID id, double amount) {
		setBalance(id, amount);
	}
	
	public static void setFunds(UUID id, String world, double amount) {
		setBalance(id, world, amount);
	}

	public static Boolean giveMoney(UUID id, UUID from, Double amount) {
		if(exists(id)) {
			String world = MISCUtils.getWorld(id);
			TNEFundsGiveEvent e = new TNEFundsGiveEvent(Bukkit.getPlayer(id), Bukkit.getPlayer(from), amount);
			Bukkit.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				addFunds(e.getReceiver().getUniqueId(), e.getAmount());
				if(Bukkit.getPlayer(e.getReceiver().getUniqueId()) != null) Bukkit.getPlayer(e.getReceiver().getUniqueId()).sendMessage(ChatColor.WHITE + "You were given " + ChatColor.GOLD + MISCUtils.formatBalance(world, amount) + ChatColor.WHITE + ".");
			}
			return true;
		}
		return false;
	}

	public static Boolean payMoney(UUID from, UUID to, Double amount) {
		if(exists(to)) {
			String world = MISCUtils.getWorld(to);
			TNEFundsPayEvent e = new TNEFundsPayEvent(Bukkit.getPlayer(from), Bukkit.getPlayer(to), amount);
			Bukkit.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				removeFunds(e.getSender().getUniqueId(), e.getAmount());
				addFunds(e.getReceiver().getUniqueId(), e.getAmount());
				if(Bukkit.getPlayer(to) != null) Bukkit.getPlayer(to).sendMessage(ChatColor.WHITE + "You were paid " + ChatColor.GOLD + MISCUtils.formatBalance(world, amount) + ChatColor.WHITE + " by " + from + ".");
			}
			return true;
		}
		return false;
	}
	
	public static void initializeWorldData(UUID id, String world) {
		Account account = getAccount(id);
		if(!account.getBalances().containsKey(world)) {
			account.setBalance(world, 0.0);
			addFunds(id, getInitialBalance(world));
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