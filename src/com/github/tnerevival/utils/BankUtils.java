package com.github.tnerevival.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.serializable.SerializableEnchantment;
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
		if(MISCUtils.multiWorld()) {
			return AccountUtils.getAccount(username).getBanks().containsKey(PlayerUtils.getWorld(username));
		}
		return AccountUtils.getAccount(username).getBanks().containsKey(TNE.instance.defaultWorld);
	}
	
	public static Bank getBank(String username) {
		if(MISCUtils.multiWorld()) {
			return AccountUtils.getAccount(username).getBank(PlayerUtils.getWorld(username));			
		}
		return AccountUtils.getAccount(username).getBank(TNE.instance.defaultWorld);
	}
	
	public static Bank getBank(String username, String world) {
		return AccountUtils.getAccount(username).getBank(world);
	}
	
	public static Bank fromString(String bank) {
		String[] variables = bank.split(":");
		String[] itemStrings = variables[4].split("*");
		Bank b = new Bank(variables[0], Integer.parseInt(variables[2]), Double.parseDouble(variables[3]));
		b.setPin(variables[1]);
		
		List<SerializableItemStack> items = new  ArrayList<SerializableItemStack>();
		for(int i = 0; i < itemStrings.length; i++) {
			String[] itemVariables = itemStrings[i].split(";");
			String[] loreStrings = itemVariables[5].split("~");
			String[] enchantmentStrings = itemVariables[6].split("~");
			SerializableItemStack item = new SerializableItemStack(Integer.parseInt(itemVariables[1]));
			HashMap<SerializableEnchantment, Integer> enchantments = new HashMap<SerializableEnchantment, Integer>();
			List<String> lore = new ArrayList<String>();
			//ItemVariables
			item.setName(itemVariables[0]);
			item.setAmount(Integer.parseInt(itemVariables[2]));
			item.setDamage(Short.parseShort(itemVariables[3]));
			item.setCustomName(itemVariables[4]);
			
			for(int l = 0; l < loreStrings.length; l++) {
				lore.add(loreStrings[l]);
			}
			item.setLore(lore);
			
			for(int e = 0; e < enchantmentStrings.length; e++) {
				String[] enchantmentVariables = enchantmentStrings[e].split(",");
				enchantments.put(new SerializableEnchantment(Enchantment.getByName(enchantmentVariables[0])), Integer.parseInt(enchantmentVariables[1]));
			}
			item.setEnchantments(enchantments);
			items.add(item);
		}
		b.setItems(items);
		
		return b;
	}
	
	public static Inventory getBankInventory(String username) {
		if(!hasBank(username)) {
			return null;
		} else {
			Bank bank = getBank(username);
			String gold = "Gold: " + MISCUtils.formatMoney(bank.getGold());
			Inventory bankInventory = Bukkit.createInventory(null, size(PlayerUtils.getWorld(username)), ChatColor.RED + username + " " + ChatColor.GOLD + gold);
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