package com.github.tnerevival.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.event.TNECreateAccountEvent;
import com.github.tnerevival.core.event.TNEFundsAddEvent;
import com.github.tnerevival.core.event.TNEFundsGiveEvent;
import com.github.tnerevival.core.event.TNEFundsPayEvent;
import com.github.tnerevival.core.event.TNEFundsRemoveEvent;
import com.github.tnerevival.core.event.TNEFundsTakeEvent;
import com.github.tnerevival.serializable.SerializableItemStack;

public class AccountUtils {
	
	public static Boolean exists(UUID id) {
		return TNE.instance.manager.accounts.containsKey(id);
	}
	
	public static void createAccount(UUID id) {
		TNECreateAccountEvent e = new TNECreateAccountEvent(id);
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			TNE.instance.manager.accounts.put(e.getID(), e.getAccount());
			AccountUtils.addFunds(e.getID(), AccountUtils.getInitialBalance(TNE.instance.defaultWorld));
		}
	}

	public static Account getAccount(UUID id) {
		if(!exists(id)) {
			createAccount(id);
		}
		return TNE.instance.manager.accounts.get(id);
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
			world = MISCUtils.getWorld(id);
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
			return round(account.getBalance(MISCUtils.getWorld(id)));
		}
		if(TNE.configurations.getBoolean("Core.Currency.ItemCurrency")) {
			Material majorItem = Material.getMaterial(TNE.configurations.getString("Core.Currency.ItemMajor"));
			Material minorItem = Material.getMaterial(TNE.configurations.getString("Core.Currency.ItemMinor"));
			Integer major = MISCUtils.getItemCount(id, majorItem);
			Integer minor = MISCUtils.getItemCount(id, minorItem);
			String balance = major + "." + minor;
			return Double.valueOf(balance);
		}
		return round(account.getBalance(TNE.instance.defaultWorld));
	}
	
	private static void setBalance(UUID id, Double balance) {
		setBalance(id, MISCUtils.getWorld(id), balance);
	}

	private static void setBalance(UUID id, String world, Double balance) {
		balance = round(balance);
		Account account = getAccount(id);
		String balanceString = (String.valueOf(balance).contains(".")) ? String.valueOf(balance) : String.valueOf(balance) + ".0";
		String[] split = balanceString.split("\\.");
		if(MISCUtils.multiWorld()) {
			world = MISCUtils.getWorld(id);
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
		if(TNE.configurations.getBoolean("Core.Currency.ItemCurrency")) {
			Material majorItem = Material.getMaterial(TNE.configurations.getString("Core.Currency.ItemMajor"));
			Material minorItem = Material.getMaterial(TNE.configurations.getString("Core.Currency.ItemMinor"));
			MISCUtils.setItemCount(id, majorItem, Integer.valueOf(split[0].trim()));
			MISCUtils.setItemCount(id, minorItem, Integer.valueOf(split[1].trim()));
		} else {
			account.setBalance(TNE.instance.defaultWorld, balance);
		}
	}
	
	public static Double round(double amount) {
		return (double)Math.round(amount * 100) / 100;
	}

	public static Boolean hasFunds(UUID id, double amount) {
		amount = round(amount);
		return getBalance(id) >= amount;
	}
	
	public static Boolean hasFunds(UUID id, String world, double amount) {
		amount = round(amount);
		return getBalance(id, world) >= amount;
	}

	public static void addFunds(UUID id, double amount) {
		addFunds(id, MISCUtils.getWorld(id), amount);
	}
	
	public static void addFunds(UUID id, String world, double amount) {
		amount = round(amount);
		TNEFundsAddEvent e = new TNEFundsAddEvent(id, amount);
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			setBalance(e.getID(), world, e.getNewBalance());
		}
	}

	public static void removeFunds(UUID id, double amount) {
		removeFunds(id, MISCUtils.getWorld(id), amount);
	}
	
	public static void removeFunds(UUID id, String world, double amount) {
		amount = round(amount);
		TNEFundsRemoveEvent e = new TNEFundsRemoveEvent(id, amount);
		Bukkit.getServer().getPluginManager().callEvent(e);
		
		if(!e.isCancelled()) {
			setBalance(e.getID(), world, e.getNewBalance());
		}
	}
	
	public static Double getFunds(UUID id) {
		return getBalance(id);
	}
	
	public static Double getFunds(UUID id, String world) {
		world = MISCUtils.getWorld(id);
		return getBalance(id, world);
	}
	
	public static void setFunds(UUID id, double amount) {
		amount = round(amount);
		setBalance(id, amount);
	}
	
	public static void setFunds(UUID id, String world, double amount) {
		amount = round(amount);
		setBalance(id, world, amount);
	}

	public static Boolean giveMoney(UUID id, UUID from, Double amount) {
		if(exists(id)) {
			String world = MISCUtils.getWorld(id);
			amount = round(amount);
			TNEFundsGiveEvent e = new TNEFundsGiveEvent(MISCUtils.getPlayer(id), MISCUtils.getPlayer(from), amount);
			Bukkit.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				String giver = (from == null) ? "Console" : MISCUtils.getPlayer(from).getDisplayName();
				addFunds(MISCUtils.getID(e.getReceiver()), e.getAmount());
				Message given = new Message("Messages.Money.Given");
				given.addVariable("$amount", MISCUtils.formatBalance(world, amount));
				given.addVariable("$from", giver);
				if(MISCUtils.getPlayer(MISCUtils.getID(e.getReceiver())) != null) MISCUtils.getPlayer(MISCUtils.getID(e.getReceiver())).sendMessage(given.translate());
			}
			return true;
		}
		return false;
	}
	
	public static Boolean takeMoney(UUID id, UUID from, Double amount) {
		if(exists(id)) {
			String world = MISCUtils.getWorld(id);
			amount = round(amount);
			TNEFundsTakeEvent e = new TNEFundsTakeEvent(MISCUtils.getPlayer(id), MISCUtils.getPlayer(from), amount);
			Bukkit.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				String taker = (from == null) ? "Console" : MISCUtils.getPlayer(from).getDisplayName();
				removeFunds(e.getTarget().getUniqueId(), e.getAmount());
				Message taken = new Message("Messages.Money.Taken");
				taken.addVariable("$from", taker);
				taken.addVariable("$amount", MISCUtils.formatBalance(world, amount));
				if(MISCUtils.getPlayer(MISCUtils.getID(e.getTarget())) != null) MISCUtils.getPlayer(MISCUtils.getID(e.getTarget())).sendMessage(taken.translate());
			}
			return true;
		}
		return false;
	}

	public static Boolean payMoney(UUID from, UUID to, Double amount) {
		if(to == null || from == null) { return false; }
		if(exists(to)) {
			String world = MISCUtils.getWorld(to);
			amount = round(amount);
			TNEFundsPayEvent e = new TNEFundsPayEvent(MISCUtils.getPlayer(to), MISCUtils.getPlayer(from), amount);
			Bukkit.getServer().getPluginManager().callEvent(e);
			
			if(!e.isCancelled()) {
				removeFunds(MISCUtils.getID(e.getSender()), e.getAmount());
				addFunds(MISCUtils.getID(e.getReceiver()), e.getAmount());
				Message paid = new Message("Messages.Money.Received");
				paid.addVariable("$from", MISCUtils.getPlayer(from).getDisplayName());
				paid.addVariable("$amount", MISCUtils.formatBalance(world, amount));
				if(Bukkit.getPlayer(to) != null) MISCUtils.getPlayer(to).sendMessage(paid.translate());
			}
			return true;
		}
		return false;
	}
	
	public static void initializeWorldData(UUID id, String world) {
		Account account = getAccount(id);
		world = MISCUtils.getWorld(id);
		if(!account.getBalances().containsKey(world)) {
			account.setBalance(world, 0.0);
			addFunds(id, getInitialBalance(world));
		}
	}

	public static Double getInitialBalance(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Balance")) {
				return round(TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".Balance"));
			}
		}
		return round(TNE.configurations.getDouble("Core.Balance"));
	}

	public static Double getWorldCost(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".balance")) {
				return round(TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".ChangeFee"));
			}
		}
		return round(TNE.configurations.getDouble("Core.World.ChangeFee"));
	}
}