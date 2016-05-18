package com.github.tnerevival.core.signs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.utils.MISCUtils;

public class ShopSign extends TNESign {

	private List<ShopEntry> sale = new ArrayList<ShopEntry>();
	private String title;
	private String name;
	
	public ShopSign(UUID owner, String name) {
		super(owner);
		this.title = MISCUtils.getPlayer(owner).getDisplayName() + "'s Shop";
		setType(SignType.SHOP);
	}
	
	public ShopEntry getItem(int id) {
		for(ShopEntry entry : this.sale) {
			if(entry.getItem().getSlot() == id) {
				return entry;
			}
		}
		return null;
	}
	
	public void addItem(ShopEntry entry) {
		if(sale.size() >= 27) { return; }
		this.sale.add(entry);
	}

	public List<ShopEntry> getSale() {
		return sale;
	}

	public void setSale(List<ShopEntry> sale) {
		this.sale = sale;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Inventory getInventory() {
		inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "[SHOP]" + ChatColor.RESET + getTitle());
		for(ShopEntry entry : sale) {
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
	
}