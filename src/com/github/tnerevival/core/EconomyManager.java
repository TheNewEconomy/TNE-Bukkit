package com.github.tnerevival.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.tnerevival.account.Access;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.inventory.View;
import com.github.tnerevival.core.shops.Shop;

public class EconomyManager {
	
	/**
	 * A HashMap holding all accounts for the economy.
	 * Format: Player UUID, Account Class Instance
	 */
	public Map<UUID, Account> accounts = new HashMap<UUID, Account>();
	
	public Map<UUID, View> viewers = new HashMap<UUID, View>();
	
	public Map<UUID, Access> accessing = new HashMap<UUID, Access>();
	
	/**
	 * A Map, which holds the economy UUIDs for each player that are used when UUID support is turned off.
	 */
	public Map<String, UUID> ecoIDs = new HashMap<String, UUID>();
	
	public  Map<String, Shop> shops = new HashMap<String, Shop>();
	
	public List<UUID> confirmed = new ArrayList<UUID>();
}