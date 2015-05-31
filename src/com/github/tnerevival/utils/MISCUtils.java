package com.github.tnerevival.utils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.tnerevival.TNE;
import com.github.tnerevival.serializable.SerializableEnchantment;
import com.github.tnerevival.serializable.SerializableItemStack;

public class MISCUtils {

	//True MISC Utils
	public static String getWorld(UUID id) {
		if(MISCUtils.multiWorld()) {
			if(Bukkit.getPlayer(id) != null) {
				return Bukkit.getPlayer(id).getWorld().getName();
			}
		}
		return TNE.instance.defaultWorld;
	}
	
	public static Integer getItemCount(UUID id, Material item) {
		Player p = Bukkit.getPlayer(id);
		int count = 0;
		if(item != null) {
			for(ItemStack i : p.getInventory().getContents()) {
				if(i != null && i.getType() != null && i.getType() == item) {
					count += i.getAmount();
				}
			}
		}
		return count;
	}
	
	public static void setItemCount(UUID id, Material item, Integer amount) {
		Player p = Bukkit.getPlayer(id);
		Integer count = getItemCount(id, item);
		if(item != null) {
			if(count > amount) {
				Integer remove = count - amount;
				Integer slot = 0;
				for(ItemStack i : p.getInventory().getContents()) {
					if(i != null && i.getType() != null && i.getType() == item) {
						if(remove > i.getAmount()) {
							remove -= i.getAmount();
							i.setAmount(0);
							p.getInventory().setItem(slot, null);
						} else {
							if(i.getAmount() - remove > 0) {
								i.setAmount(i.getAmount() - remove);
								p.getInventory().setItem(slot, i);
								return;
							}
							p.getInventory().setItem(slot, null);
							return;
						}
					}
					slot++;
				}
			} else if(count < amount) {
				Integer add = amount - count;
				
				while(add > 0) {
					if(add > item.getMaxStackSize()) {
						if(p.getInventory().firstEmpty() != -1) {
							p.getInventory().addItem(new ItemStack(item, item.getMaxStackSize()));
						} else {
							p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(item, item.getMaxStackSize()));
						}
						add -= item.getMaxStackSize();
					} else {
						if(p.getInventory().firstEmpty() != -1) {
							p.getInventory().addItem(new ItemStack(item, add));
						} else {
							p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(item, add));
						}
						add = 0;
					}
				}
			}
		}
	}
	
	public static void reloadConfigurations(String type) {
		if(type.equalsIgnoreCase("all")) {
			TNE.instance.reloadConfig();
			reloadConfigsMobs();
			reloadConfigsWorlds();
		} else if(type.equalsIgnoreCase("config")) {
			TNE.instance.reloadConfig();
		} else if(type.equalsIgnoreCase("mobs")) {
			reloadConfigsMobs();
		} else if(type.equalsIgnoreCase("worlds")) {
			reloadConfigsWorlds();
		}
	}
	
	public static void reloadConfigsMobs() {
		if(TNE.instance.mobs == null) {
			TNE.instance.mobs = new File(TNE.instance.getDataFolder(), "mobs.yml");
		}
		TNE.instance.mobConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.mobs);
	}
	
	public static void reloadConfigsWorlds() {
		if(TNE.instance.worlds == null) {
			TNE.instance.worlds = new File(TNE.instance.getDataFolder(), "worlds.yml");
		}
		TNE.instance.worldConfigurations = YamlConfiguration.loadConfiguration(TNE.instance.worlds);
	}
	
	
	//Format Utils
	public static Boolean shorten(String world) {
		if(multiWorld() && worldConfigExists("Worlds." + world + ".Shorten")) {
			return TNE.instance.getConfig().getBoolean("Worlds." + world + ".Shorten");
		}
		return TNE.instance.getConfig().getBoolean("Worlds." + world + ".Shorten");
	}
	
	public static String formatBalance(String world, double balance) {
		return (shorten(world)) ? formatBalance(world, balance, true) : formatBalance(world, balance, false);
	}
	
	public static String formatBalance(String world, double balance, Boolean shorten) {
		String balanceString = (String.valueOf(balance).contains(".")) ? String.valueOf(balance) : String.valueOf(balance) + ".0";
		String[] split = balanceString.split("\\.");
		if(Integer.valueOf(split[1]) > 0) {
			return (shorten) ? ChatColor.GOLD + getShort(Integer.valueOf(split[0])) + " " + getName(world, balance, "major") + " and " + ChatColor.GOLD + Integer.valueOf(split[1]) + " " + getName(world, balance, "minor") : ChatColor.GOLD + "" + Integer.valueOf(split[0]) + " " + getName(world, balance, "major") + " and " + ChatColor.GOLD + Integer.valueOf(split[1]) + " " + getName(world, balance, "minor");
		} else {
			return (shorten) ? ChatColor.GOLD + getShort(Integer.valueOf(split[0])) + " " + getName(world, balance, "major") : ChatColor.GOLD + "" + Integer.valueOf(split[0]) + " " + getName(world, balance, "major");
		}
	}
	
	public static String getShort(double balance) {
		Integer dollars = (int) Math.floor(balance);
		if (dollars < 1000) {
			return "" + dollars;
		}
	    int exp = (int) (Math.log(dollars) / Math.log(1000));
	    return String.format("%.1f%c", dollars / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
	}
	
	public static String getName(String world, double balance, String type) {
		String balanceString = (String.valueOf(balance).contains(".")) ? String.valueOf(balance) : String.valueOf(balance) + ".0";
		String[] split = balanceString.split("\\.");
		if(type.equalsIgnoreCase("major")) {
			return (Integer.valueOf(split[0]) != 1) ? getMajorCurrencyName(world, false) : getMajorCurrencyName(world, true);
		} else if(type.equalsIgnoreCase("minor")) {
			return (Integer.valueOf(split[0]) != 1) ? getMinorCurrencyName(world, false) : getMinorCurrencyName(world, true);
		}
		return (Integer.valueOf(split[0]) != 1) ? getMajorCurrencyName(world, false) : getMajorCurrencyName(world, true);
	}
	
	public static String getMajorCurrencyName(String world, Boolean singular) {
		if(multiWorld() && worldConfigExists("Worlds." + world + ".Currency.MajorName")) {
			return (singular) ? TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.MajorName.Singular") : TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.MajorName.Plural");
		}
		return (singular) ? TNE.instance.getConfig().getString("Core.Currency.MajorName.Singular") : TNE.instance.getConfig().getString("Core.Currency.MajorName.Plural");
	}
	
	public static String getMinorCurrencyName(String world, Boolean singular) {
		if(multiWorld() && worldConfigExists("Worlds." + world + ".Currency.MinorName")) {
			return (singular) ? TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.MinorName.Singular") : TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.MinorName.Plural");
		}
		return (singular) ? TNE.instance.getConfig().getString("Core.Currency.MinorName.Singular") : TNE.instance.getConfig().getString("Core.Currency.MinorName.Plural");
	}
	
	//ItemStack Utils
	public static String itemstackToString(SerializableItemStack stack) {
		return stack.getName() + ";" + stack.getSlot() + ";" + stack.getAmount() + ";" + stack.getDamage() + ";" + stack.getCustomName() + ";" + stack.loreToString() + ";" + stack.enchantmentsToString();
	}
	
	public static SerializableItemStack itemstackFromString(String itemString) {
		String[] variables = itemString.split("\\;");
		
		SerializableItemStack stack = new SerializableItemStack(Integer.valueOf(variables[1]));
		stack.setName(variables[0]);
		stack.setAmount(Integer.valueOf(variables[2]));
		stack.setDamage(Short.valueOf(variables[3]));
		if(variables[4] != null && !variables[4].equals("TNENOSTRINGVALUE")) {
			stack.setCustomName(variables[4]);
		}
		
		if(variables[5] != null && !variables[5].equals("TNENOSTRINGVALUE")) {
			stack.setLore(Arrays.asList(variables[5].split("\\~")));
		}
		
		if(variables[6] != null && !variables[6].equals("TNENOSTRINGVALUE")) {
			HashMap<SerializableEnchantment, Integer> enchantments = new HashMap<SerializableEnchantment, Integer>();
			String[] enchantmentsArray = variables[6].split("\\~");
			
			for(String s : enchantmentsArray) {
				String[] enchantmentVariables = s.split("\\,");
				
				enchantments.put(new SerializableEnchantment(enchantmentVariables[0]), Integer.valueOf(enchantmentVariables[1]));
			}
			stack.setEnchantments(enchantments);
		}
		return stack;
	}
	
	//World Utils
	public static Boolean multiWorld() {
		return TNE.instance.getConfig().getBoolean("Core.Multiworld");
	}
	
	public static Boolean worldConfigExists(String node) {
		return (TNE.instance.worldConfigurations.get(node) != null);
	}
}