package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;

public class MISCUtils {
	
	public static String formatBalance(double balance) {
		return (TNE.instance.getConfig().getBoolean("Core.Shorten")) ? formatMoney(balance) + " " + getName(balance) : formatAmount(balance);
	}
	
	public static String formatMoney(double balance) {
		Integer dollars = (int) Math.floor(balance);
		if (dollars < 1000) {
			return "" + dollars;
		}
	    int exp = (int) (Math.log(dollars) / Math.log(1000));
	    return String.format("%.1f%c", dollars / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
	}
	
	public static String formatAmount(double amount) {
		return amount + " " + getName(amount);
	}
	
	public static String getName(double amount) {
		return (amount > 1.0)? TNE.instance.getConfig().getString("Core.Currency.Name.Plural") : TNE.instance.getConfig().getString("Core.Currency.Name.Singular");
	}
	
	public static Boolean multiWorld() {
		return TNE.instance.getConfig().getBoolean("Core.Multiworld");
	}
	
	public static Boolean worldConfigExists(String node) {
		return (TNE.instance.worldConfigurations.get(node) != null);
	}
}