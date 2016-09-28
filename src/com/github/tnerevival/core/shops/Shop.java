package com.github.tnerevival.core.shops;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Shop implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<UUID> shoppers = new ArrayList<>();
	private List<ShopEntry> items = new ArrayList<>();
	private List<UUID> blacklist = new ArrayList<>();
	private List<UUID> whitelist = new ArrayList<>();
	private List<ShareEntry> shares = new ArrayList<>();
	
	private UUID owner;
	private String name;
	private boolean hidden = false;
	private boolean admin = false;

	public Shop(String name) {
		this.name = name;
	}
	
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
	  this.admin = admin;
  }
	
	public ShopEntry getItem(int id) {
		for(ShopEntry entry : this.items) {
			if(entry.getItem().getSlot() == id) {
				return entry;
			}
		}
		return null;
	}
	
	public boolean addItem(ShopEntry entry) {
		if(items.size() >= 27) { return false; }
		Material mat = Material.getMaterial(entry.getItem().getName());
		if(hasItem(mat)) {
			if(getCost(mat) < 0.0 && entry.getTrade() != null && getTrade(mat) != null && entry.getTrade().getName().equals(getTrade(mat)) || getCost(mat) == entry.getCost()) {
				return false;
			}
		}
		this.items.add(entry);
		return true;
	}
	
	public boolean removeItem(ItemStack item) {
		Iterator<ShopEntry> i = items.iterator();
		while(i.hasNext()) {
			ShopEntry entry = i.next();
			if(entry.getItem().getName().equalsIgnoreCase(item.getType().toString())) {
				i.remove();
				return true;
			}
		}
		return false;
	}
	
	public boolean removeItem(ItemStack item, double cost) {
		Iterator<ShopEntry> i = items.iterator();
		while(i.hasNext()) {
			ShopEntry entry = i.next();
			if(entry.getItem().getName().equalsIgnoreCase(item.getType().toString()) && entry.getCost() == cost) {
				i.remove();
				return true;
			}
		}
		return false;
	}
	
	public boolean removeItem(ItemStack item, ItemStack trade) {
		Iterator<ShopEntry> i = items.iterator();
		while(i.hasNext()) {
			ShopEntry entry = i.next();
			if(entry.getItem().getName().equalsIgnoreCase(item.getType().toString()) &&
			   entry.getTrade().getName().equalsIgnoreCase(trade.getType().toString())) {
				i.remove();
				return true;
			}
		}
		return false;
		
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
	
	public double getCost(Material mat) {
		for(ShopEntry entry : items) {
			if(Material.getMaterial(entry.getItem().getName()).equals(mat)) {
				return entry.getCost();
			}
		}
		return 0.0;
	}
	
	public String getTrade(Material mat) {
		for(ShopEntry entry : items) {
			if(Material.getMaterial(entry.getItem().getName()).equals(mat)) {
				return entry.getTrade().getName();
			}
		}
		return null;
	}
	
	public boolean hasItem(Material mat) {
		for(ShopEntry entry : items) {
			if(Material.getMaterial(entry.getItem().getName()).equals(mat)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasItem(Material mat, double cost) {
		for(ShopEntry entry : items) {
			if(Material.getMaterial(entry.getItem().getName()).equals(mat) && entry.getCost() == cost) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasItem(Material mat, String trade) {
		for(ShopEntry entry : items) {
			if(Material.getMaterial(entry.getItem().getName()).equals(mat) && entry.getTrade().getName().equalsIgnoreCase(trade)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPermission(UUID player) {
		return owner.equals(player);
	}
	
	public double totalSharePercent() {
		double total = 0.00;
		for(ShareEntry entry : shares) {
			total += entry.getPercent();
		}
		return total;
	}
	
	public double canBeShared() {
		return (100.00 - totalSharePercent());
	}
	
	public void removeShares(UUID player) {
		Iterator<ShareEntry> i = shares.iterator();
		while(i.hasNext()) {
			ShareEntry entry = i.next();
			if(entry.getShareOwner().equals(player)) {
				i.remove();
			}
		}
	}
	
	public void addShares(ShareEntry entry) {
		shares.add(entry);
	}

	public String listToString(boolean blacklist) {
	  StringBuilder builder = new StringBuilder();

    List<UUID> list = (blacklist)? this.blacklist : this.whitelist;

    for(UUID id : list) {
      if(builder.length() > 0) builder.append(",");
      builder.append(id.toString());
    }
    return builder.toString();
  }

  public void listFromString(String parse, boolean blacklist) {
    String[] parsed = parse.split(",");

    for(String s : parsed) {
    	if(MISCUtils.isUUID(s)) {
				if (blacklist) {
					this.blacklist.add(UUID.fromString(s));
					continue;
				}
				this.whitelist.add(UUID.fromString(s));
			}
    }
  }

  public String sharesToString() {
    StringBuilder builder = new StringBuilder();

    for(ShareEntry entry : shares) {
      if(builder.length() > 0) builder.append(",");
      builder.append(entry.toString());
    }
    return builder.toString();
  }

  public void sharesFromString(String parse) {
    String[] parsed = parse.split(",");

    for(String s : parsed) {
      ShareEntry entry = ShareEntry.fromString(s);
      if(entry != null) {
        shares.add(ShareEntry.fromString(s));
      }
    }
  }

  public String itemsToString() {
    StringBuilder builder = new StringBuilder();

    for(ShopEntry entry : items) {
      if(builder.length() > 0) builder.append("=");
      builder.append(entry.toString());
    }

    return builder.toString();
  }

  public void itemsFromString(String parse) {
  	MISCUtils.debug(parse);
    String[] parsed = parse.split("=");

    for(String s : parsed) {
      items.add(ShopEntry.fromString(s));
    }
  }
	
	/*
	 * Static methods
	 */
	public static boolean exists(String name) {
		return TNE.instance.manager.shops.containsKey(name);
	}
	
	public static Shop getShop(String name) {
		if(exists(name)) {
			return TNE.instance.manager.shops.get(name);
		}
		return null;
	}
	
	public static boolean shares(String name, UUID player) {
		if(exists(name)) {
			return getShop(name).shares(player);
		}
		return false;
	}
	
	public static boolean canModify(String name, Player p) {
		if(exists(name)) {
			Shop s = getShop(name);
			return s.getOwner() == null && p.hasPermission("tne.shop.admin") ||
				   s.getOwner() != null && s.getOwner().equals(MISCUtils.getID(p)) ||
					 s.isAdmin() && p.hasPermission("tne.shop.admin");
		}
		return false;
	}

	/*
	 * Getters and setters
	 */

	
	public List<UUID> getShoppers() {
		return shoppers;
	}

	public void setShoppers(List<UUID> shoppers) {
		this.shoppers = shoppers;
	}
	
	public void addShopper(UUID shopper) {
		shoppers.add(shopper);
	}
	
	public void removeShopper(UUID shopper) {
		shoppers.remove(shopper);
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
	
	public boolean blacklisted(UUID player) {
		return blacklist.contains(player);
	}
	
	public void addBlacklist(UUID player) {
		blacklist.add(player);
	}
	
	public void removeBlacklist(UUID player) {
		blacklist.remove(player);
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
	
	public boolean whitelisted(UUID player) {
		return whitelist.contains(player);
	}
	
	public void addWhitelist(UUID player) {
		whitelist.add(player);
	}
	
	public void removeWhitelist(UUID player) {
		whitelist.remove(player);
	}
	
	public boolean shares(UUID player) {
		for(ShareEntry entry : shares) {
			if(entry.getShareOwner().equals(player)) {
				return true;
			}
		}
		return false;
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

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}