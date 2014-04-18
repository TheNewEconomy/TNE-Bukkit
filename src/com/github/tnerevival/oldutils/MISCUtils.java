package com.github.tnerevival.oldutils;

import com.github.tnerevival.TNE;

public class MISCUtils {
	
	//True MISC Utils
	public static void reloadConfigurations(String type) {
		if(type.equalsIgnoreCase("config")) {
			
		} else if(type.equalsIgnoreCase("currency")) {
			
		} else if(type.equalsIgnoreCase("mobs")) {
			
		} else if(type.equalsIgnoreCase("worlds")) {
			
		}
	}
	
	
	//Format Utils
	public static String formatBalance(String world, String currency, double balance) {
		String amount = (String) ((TNE.instance.getConfig().getBoolean("Core.Shorten")) ? formatAmountShort(balance) : balance);
		return amount + getName(world, currency, balance);
	}
	
	public static String formatAmountShort(double balance) {
		Integer dollars = (int) Math.floor(balance);
		if (dollars < 1000) {
			return "" + dollars;
		}
	    int exp = (int) (Math.log(dollars) / Math.log(1000));
	    return String.format("%.1f%c", dollars / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
	}

	//Currency Utils
	public static String getName(String world, String currency, double amount) {
		if(!currency.toLowerCase().equals("default")) {
			return (amount > 1.0 || amount == 0.0) ? currencyName(world, currency, true) : currencyName(world, currency, false);
		}
		return (amount > 1.0 || amount == 0.0) ? currencyName(world, true) : currencyName(world, false);
	}
	
	public static Boolean advancedCurrency() {
		return TNE.instance.getConfig().getBoolean("Core.Currency.Advanced");
	}
	
	public static String currencyName(String world, Boolean plural) {
		if(advancedCurrency()) {
			if(TNE.instance.manager.currencyManager.exists(world) || TNE.instance.manager.currencyManager.exists("Default")) {
				return (plural) ? TNE.instance.manager.currencyManager.getDefaultCurrency(world).getPlural() : TNE.instance.manager.currencyManager.getDefaultCurrency(world).getName();
			}
		}
		
		if(multiWorld()) {
			if(worldConfigExists("Worlds." + world + ".Currency.Name")) {
				return (plural) ? TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.Name.Plural") : TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.Name.Single");
			}
		}
		return (plural) ? TNE.instance.getConfig().getString("Core.Currency.Name.Plural") : TNE.instance.getConfig().getString("Core.Currency.Name.Single");
	}
	
	public static String currencyName(String world, String currency, Boolean plural) {
		if(TNE.instance.manager.currencyManager.exists(world, currency)) {
			return (plural) ? TNE.instance.manager.currencyManager.getCurrency(world, currency).getPlural() : TNE.instance.manager.currencyManager.getCurrency(world, currency).getName();
		}
		//return our error currency if the currency specified doesn't exists :P
		return (plural) ? "UnitedCurrencyNotFoundDollars" : "UnitedCurrencyNotFoundDollar";
	}
	
	//World Utils
	public static Boolean multiWorld() {
		return TNE.instance.getConfig().getBoolean("Core.Multiworld");
	}
	
	public static Boolean worldConfigExists(String node) {
		return (TNE.instance.worldConfigurations.get(node) != null);
	}
}