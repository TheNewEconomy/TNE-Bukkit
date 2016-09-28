package com.github.tnerevival.core.signs;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class ShopSign extends TNESign {

	private String title;
	private Shop shop = null;
	
	public ShopSign(UUID owner) {
		super(owner);
		this.title = MISCUtils.getPlayer(owner).getDisplayName() + "'s Shop";
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

	public void setName(String name) {
		if(TNE.instance.manager.shops.containsKey(name)) {
		  this.shop = TNE.instance.manager.shops.get(name);
    }
	}

	@Override
	public boolean onRightClick(Player player) {
	  player.sendMessage("Allo");

    return super.onRightClick(player);
	}

	@Override
	public Inventory getInventory() {
		return shop.getInventory();
	}
	
}