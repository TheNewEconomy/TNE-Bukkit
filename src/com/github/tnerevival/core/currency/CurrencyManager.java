package com.github.tnerevival.core.currency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.utils.AccountUtils;

public class CurrencyManager {
	
	HashMap<String, HashMap<String, Currency>> currencies = new HashMap<String, HashMap<String, Currency>>();
	HashMap<String, Double> initialBalances = new HashMap<String, Double>();
	
	public CurrencyManager() {
		loadCurrencies();
	}
	
	public void loadCurrencies() {
		Set<String> worlds = TNE.instance.currencyConfigurations.getConfigurationSection("Currencies").getKeys(false);
		
		for(String worldName : worlds) {
			Set<String> currencieNames = TNE.instance.currencyConfigurations.getConfigurationSection("Currencies." + worldName).getKeys(false);
			
			initialBalances.put(worldName, TNE.instance.currencyConfigurations.getDouble("Currencies." + worldName + ".Balance"));
			
			HashMap<String, Currency> loaded = new HashMap<String, Currency>();
			
			for(String currencyName : currencieNames) {
				List<String> skip = Arrays.asList(new String[]{"abbreviation", "balance", "default", "digital", "value"});
				
				Currency currency = new Currency(currencyName);
				currency.setPlural(TNE.instance.currencyConfigurations.getString("Currencies." + worldName + "." + currencyName + ".Plural"));
				currency.setAbbreviation(TNE.instance.currencyConfigurations.getString("Currencies." + worldName + "." + currencyName + ".Abbreviation"));
				currency.setValue(TNE.instance.currencyConfigurations.getDouble("Currencies." + worldName + "." + currencyName + ".Value"));
				currency.setWorldDefault(TNE.instance.currencyConfigurations.getBoolean("Currencies." + worldName + "." + currencyName + ".Default"));
				currency.setDigital(TNE.instance.currencyConfigurations.getBoolean("Currencies." + worldName + "." + currencyName + ".Digital"));
				
				Set<String> tiers = TNE.instance.currencyConfigurations.getConfigurationSection("Currencies." + worldName + "." + currencyName).getKeys(false);
				tiers.removeAll(skip);
				for(String tierName : tiers) {
					Tier tier = new Tier(tierName);
					
					tier.setItem(TNE.instance.currencyConfigurations.getInt("Currencies." + worldName + "." + currencyName + "." + tierName + ".Currency_Item"));
					tier.setValue(TNE.instance.currencyConfigurations.getDouble("Currencies." + worldName + "." + currencyName + "." + tierName + ".Value"));
					currency.getTiers().put(tierName, tier);
				}
				
				loaded.put(currencyName, currency);
			}
			currencies.put(worldName, loaded);
		}
	}
	
	public Currency getCurrency(String world, String name) {
		return currencies.get(world).get(name);
	}
	
	public Currency getDefaultCurrency(String world) {
		Iterator<java.util.Map.Entry<String, Currency>> it = currencies.get(world).entrySet().iterator();
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Currency> entry = it.next();
			
			if(entry.getValue().getWorldDefault()) {
				return entry.getValue();
			}
		}
		
		Currency mainDefault = null;
		Iterator<java.util.Map.Entry<String, Currency>> defaultIT = currencies.get("Default").entrySet().iterator();
		while(defaultIT.hasNext()) {
			java.util.Map.Entry<String, Currency> entry = defaultIT.next();
			
			if(entry.getValue().getWorldDefault()) {
				mainDefault = entry.getValue();
			}
		}
		return mainDefault;
	}
	
	public Boolean exists(String world) {
		return currencies.containsKey(world);
	}
	
	public Boolean exists(String world, String currency) {
		if(currencies.containsKey(world)) {
			return currencies.get(world).containsKey(currency);
		}
		return false;
	}

	public void checkWorldData(String username, String world) {
		Account account = AccountUtils.getAccount(username);
		
		if(!account.getBalances().containsKey(world)) {
			if(initialBalances.containsKey(world)) {
				
			} else if(initialBalances.containsKey("Default")) {
				account.getBalances().put(world, 0.0);
			}
		}
	}
}