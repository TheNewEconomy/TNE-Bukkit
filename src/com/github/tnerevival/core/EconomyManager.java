package com.github.tnerevival.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.github.tnerevival.account.Access;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.companies.Company;
import com.github.tnerevival.core.inventory.View;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.lottery.Lottery;
import com.github.tnerevival.serializable.SerializableLocation;

public class EconomyManager {
	
	/**
	 * A HashMap holding all accounts for the economy.
	 * Format: Player UUID, Account Class Instance
	 */
	public Map<UUID, Account> accounts = new HashMap<UUID, Account>();
	
	/**
	 * A HashMap holding every company created.
	 * Format: Company Name, Company Class Instance
	 */
	public Map<String, Company> companies = new HashMap<String, Company>();
	
	/**
	 * A HashMap holding every Lottery that is currently running.
	 * We have this so we can have multiple lotteries at once.
	 * Format: Lottery Name, Lottery Class Instance.
	 */
	public Map<String, Lottery> lotteries = new HashMap<String, Lottery>();
	
	public Map<UUID, View> viewers = new HashMap<UUID, View>();
	
	public Map<UUID, Access> accessing = new HashMap<UUID, Access>();
	
	/**
	 * A Map, which holds the economy UUIDs for each player that are used when UUID support is turned off.
	 */
	public Map<String, UUID> ecoIDs = new HashMap<String, UUID>();
	
	public Map<SerializableLocation, TNESign> signs = new HashMap<SerializableLocation, TNESign>();
	
	public List<UUID> confirmed = new ArrayList<UUID>();
}