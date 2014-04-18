package com.github.tnerevival.oldutils;

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
	
	public static Double getBalance(String username, String currency) {
		Account account = getAccount(username);
		String world = PlayerUtils.getWorld(username);
		
		if(MISCUtils.advancedCurrency()) {
			
		} else {
			if(MISCUtils.multiWorld()) {
				
			} else {
				
			}
		}
		return account.getBalance("Default", "Default");		
	}
	
	public static void initializeWorldData(String username, String world) {
		TNE.instance.manager.currencyManager.checkWorldData(username, world);
	}
	
	public static Double getInitialBalance(String world, String currency) {
		if(MISCUtils.advancedCurrency()) {
			if(TNE.instance.manager.currencyManager.exists(world) || TNE.instance.manager.currencyManager.exists("Default")) {
				if(TNE.instance.manager.currencyManager.exists(world, currency)) {
					return TNE.instance.manager.currencyManager.getCurrency(world, currency).getBalance();
				}
				return TNE.instance.manager.currencyManager.getDefaultCurrency(world).getBalance();
			}
		}
		
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".Balance")) {
				return TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".Balance");
			}
		}
		return TNE.instance.getConfig().getDouble("Core.Balance");
	}
	
	public static Double getWorldCost(String world) {
		if(MISCUtils.multiWorld()) {
			if(MISCUtils.worldConfigExists("Worlds." + world + ".ChangeFee")) {
				return TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".ChangeFee");
			}
		}
		return TNE.instance.getConfig().getDouble("Core.World.ChangeFee");
	}
}