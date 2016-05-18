package com.github.tnerevival.core.shops;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Shop implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<ShopEntry> items = new ArrayList<ShopEntry>();
	private List<UUID> blacklist = new ArrayList<UUID>();
	private List<UUID> whitelist = new ArrayList<UUID>();
	private List<ShareEntry> shares = new ArrayList<ShareEntry>();
	
	private UUID owner;
	private String name;
	private boolean admin = false;
	
	public Shop(UUID owner) {
		this.owner = owner;
	}
	
	public ShopEntry getItem(int id) {
		for(ShopEntry entry : this.items) {
			if(entry.getItem().getSlot() == id) {
				return entry;
			}
		}
		return null;
	}
	
	public void addItem(ShopEntry entry) {
		if(items.size() >= 27) { return; }
		this.items.add(entry);
	}

	public List<ShopEntry> getSale() {
		return items;
	}

	public void setSale(List<ShopEntry> sale) {
		this.items = sale;
	}

	public Inventory getInventory() {
		Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "[SHOP]" + ChatColor.RESET + name);
		for(ShopEntry entry : items) {
			ItemStack stack = entry.getItem().toItemStack();
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "Left Click to buy. Right Click to trade.");
			if(entry.getCost() > 0.0) {
				lore.add(ChatColor.WHITE + "Cost: " + ChatColor.GOLD + entry.getCost());
			}
			
			if(entry.getTrade() != null) {
				lore.add(ChatColor.WHITE + "Trade: " + entry.getTrade().getAmount() + entry.getTrade().getName());
			}
			
			ItemMeta meta = stack.getItemMeta();
			meta.setLore(lore);
			stack.setItemMeta(meta);
			inventory.setItem(entry.getItem().getSlot(), stack);
		}
		return inventory;
	}

	public List<ShopEntry> getItems() {
		return items;
	}

	public void setItems(List<ShopEntry> items) {
		this.items = items;
	}

	public List<UUID> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(List<UUID> blacklist) {
		this.blacklist = blacklist;
	}

	public List<UUID> getWhitelist() {
		return whitelist;
	}

	public void setWhitelist(List<UUID> whitelist) {
		this.whitelist = whitelist;
	}

	public List<ShareEntry> getShares() {
		return shares;
	}

	public void setShares(List<ShareEntry> shares) {
		this.shares = shares;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}