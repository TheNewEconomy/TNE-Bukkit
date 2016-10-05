package com.github.tnerevival.core.signs;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class ShopSign extends TNESign {

	private String title;
	private Shop shop = null;
	
	public ShopSign(UUID owner) {
		super(owner);
		setType(SignType.SHOP);
	}

	public ShopEntry getItem(int id) {
		return shop.getItem(id);
	}
	
	public void addItem(ShopEntry entry) {
		shop.addItem(entry);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return shop.getName();
	}

	public void setName(String name, String world) {
		if(TNE.instance.manager.shops.containsKey(name + ":" + world)) {
		  this.title = ChatColor.GOLD + "[Shop]" + ChatColor.WHITE + name;
      this.shop = TNE.instance.manager.shops.get(name + ":" + world);
    }
	}

	public boolean onRightClick(Player player, String name, String world) {
	  if(super.onRightClick(player)) {
      if (player.hasPermission(SignType.SHOP.getUsePermission())) {
        setName(name, world);
        inventory = shop.getInventory();

        if (Shop.canView(getName(), player.getUniqueId())) {
          if (super.onOpen(player)) {
            player.openInventory(inventory);
            return true;
          }
        }
      }
    }
    return false;
	}

	@Override
	public Inventory getInventory() {
		return shop.getInventory();
	}
	
}