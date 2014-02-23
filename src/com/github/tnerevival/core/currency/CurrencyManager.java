package com.github.tnerevival.core.currency;

import java.util.HashMap;

public class CurrencyManager {
	
	HashMap<String, HashMap<String, Currency>> currencies = new HashMap<String, HashMap<String, Currency>>();
	
	public CurrencyManager() {
		loadCurrencies();
	}
	
	public void loadCurrencies() {
		
	}
	
	public Currency getCurrency(String world, String name) {
		return currencies.get(world).get(name);
	}
	
	public Currency getDefaultCurrency(String world) {
		return null;
	}
}