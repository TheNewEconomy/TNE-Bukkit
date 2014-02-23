package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;

public class MISCUtils {
	
	public static String formatBalance(String world, double balance) {
		return (TNE.instance.getConfig().getBoolean("Core.Shorten")) ? formatMoney(balance) + " " + getName(world, balance) : formatAmount(world, balance);
	}
	
	public static String formatMoney(double balance) {
		Integer dollars = (int) Math.floor(balance);
		if (dollars < 1000) {
			return "" + dollars;
		}
	    int exp = (int) (Math.log(dollars) / Math.log(1000));
	    return String.format("%.1f%c", dollars / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
	}
	
	public static String formatAmount(String world, double amount) {
		return amount + " " + getName(world, amount);
	}
	
	public static String getName(String world, double amount) {
		if(multiWorld()) {
			//if(worldConfigExists()) {
				
			//}
		}
		return (amount > 1.0 || amount == 0.0)? TNE.instance.getConfig().getString("Core.Currency.Name.Plural") : TNE.instance.getConfig().getString("Core.Currency.Name.Singular");
	}
	
	public static void reloadConfigurations(String type) {
		if(type.equalsIgnoreCase("config")) {
			
		} else if(type.equalsIgnoreCase("currency")) {
			
		} else if(type.equalsIgnoreCase("mobs")) {
			
		} else if(type.equalsIgnoreCase("worlds")) {
			
		}
	}
	
	//World Utils
	public static Boolean multiWorld() {
		return TNE.instance.getConfig().getBoolean("Core.Multiworld");
	}
	
	public static Boolean worldConfigExists(String node) {
		return (TNE.instance.worldConfigurations.get(node) != null);
	}
}