package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.inventory.TNEInventory;
import com.github.tnerevival.core.transaction.TransactionCost;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by creatorfromhell on 3/15/2017.
 * All rights reserved.
 **/
public class AuctionInventory extends TNEInventory {
  private List<Integer> lots = new ArrayList<>();

  public AuctionInventory(UUID inventoryID, Inventory inventory, String world) {
    super(inventoryID, inventory, world);
  }

  public void setLot(Integer lot) {
    this.lots.add(lot);
    initializeInventory();
  }

  public void setLots(List<Integer> lots) {
    this.lots = lots;
    initializeInventory();
  }

  private void initializeInventory() {
    Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Auction View");
    for(int slot = 0; slot < lots.size(); slot++) {
      if(slot >= 9) continue;
      Auction a = TNE.instance().manager.auctionManager.getAuction(lots.get(slot));
      String active = (a.remaining().equals(a.getTime()))? ChatColor.GREEN + "Yes" : ChatColor.RED + "No";
      ItemStack stack = a.getItem().toItemStack();
      List<String> lore = new ArrayList<>();
      if(!a.getSilent()) {
        lore.add(ChatColor.WHITE + "Cost: " + ChatColor.GREEN + a.getNextBid());
      }
      lore.add(ChatColor.WHITE + "Amount: " + ChatColor.GREEN + a.getItem().getAmount());
      lore.add(ChatColor.WHITE + "Increment: " + ChatColor.GREEN + a.getIncrement());
      lore.add(ChatColor.WHITE + "Player: " + ChatColor.GREEN + IDFinder.getPlayer(a.getPlayer().toString()).getDisplayName());
      lore.add(ChatColor.WHITE + "World: " + ChatColor.GREEN + a.getWorld());
      lore.add(ChatColor.WHITE + "Time: " + ChatColor.GREEN + a.remaining());
      lore.add(ChatColor.WHITE + "Active: " + active);
      lore.add(ChatColor.WHITE + "Left Click to place bid.");
      ItemMeta meta = stack.getItemMeta();
      meta.setLore(lore);
      stack.setItemMeta(meta);
      inv.setItem(((lots.size() == 1)? 4 : slot), stack);
    }
    inventory = inv;
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }

  @Override
  public boolean onClick(UUID player, ClickType type, int slot, ItemStack item) {
    if(type.equals(ClickType.LEFT)) {
      if(inventory.getItem(slot) != null && !inventory.getItem(slot).getType().equals(Material.AIR)) {
        int use = (lots.size() == 1)? 0 : slot;
        Auction a = TNE.instance().manager.auctionManager.getAuction(lots.get(use));
        TNE.instance().manager.auctionManager.bid(IDFinder.getWorld(player), lots.get(use), player, new TransactionCost(a.getNextBid()));
        IDFinder.getPlayer(player.toString()).closeInventory();
        onClose(player);
        return false;
      }
    }
    return false;
  }
}