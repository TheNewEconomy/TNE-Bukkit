package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10/8/2016.
 */
public class ShopItemInventory extends GenericInventory {

  private Shop shop;
  private ShopEntry item = null;
  private boolean modifying = false;
  public static ItemStack trade = new ItemStack(Material.PAPER);
  public static ItemStack confirm = new ItemStack(Material.PAPER);

  static {
    List<String> tradeLore = new ArrayList<>();
    tradeLore.add(ChatColor.WHITE + "Place expected trade item here.");
    tradeLore.add(ChatColor.WHITE + "The amount you place is the quantity");
    tradeLore.add(ChatColor.WHITE + "required to make the trade.");
    ItemMeta tradeMeta = trade.getItemMeta();
    tradeMeta.setDisplayName(ChatColor.GREEN + "Trade Item");
    tradeMeta.setLore(tradeLore);
    trade.setItemMeta(tradeMeta);

    List<String> confirmLore = new ArrayList<>();
    confirmLore.add(ChatColor.WHITE + "Click here to finalize this shop item.");
    ItemMeta confirmMeta = confirm.getItemMeta();
    confirmMeta.setDisplayName(ChatColor.GREEN + "Confirm Shop Item");
    confirmMeta.setLore(confirmLore);
    confirm.setItemMeta(confirmMeta);
  }

  public ShopItemInventory(Shop shop) {
    this(shop, null);
  }

  public ShopItemInventory(Shop shop, ShopEntry item) {
    this.shop = shop;
    String title = ((item != null)? "Edit" : "Add") + " Shop Item";
    this.inventory = Bukkit.createInventory(null, InventoryType.ANVIL, title);
    if(item != null) {
      this.item = item;
      this.modifying = true;
      this.inventory.setItem(0, item.getItem().toItemStack());
    }
    this.inventory.setItem(1, trade);
    this.inventory.setItem(2, confirm);
  }

  @Override
  public List<Material> getBlacklisted() {
    List<Material> blocked = new ArrayList<>();
    blocked.add(Material.ENCHANTED_BOOK);
    blocked.add(Material.SPECTRAL_ARROW);
    blocked.add(Material.TIPPED_ARROW);
    blocked.add(Material.POTION);
    blocked.add(Material.LINGERING_POTION);
    blocked.add(Material.SPLASH_POTION);

    return blocked;
  }
}