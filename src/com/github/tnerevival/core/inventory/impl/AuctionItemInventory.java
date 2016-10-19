package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.inventory.InventoryViewer;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by creatorfromhell on 10/18/2016.
 */
public class AuctionItemInventory extends GenericInventory {
  private List<Integer> lots = new ArrayList<>();

  public AuctionItemInventory(Integer lot) {
    this.lots.add(lot);
    initializeInventory();
  }

  public AuctionItemInventory(List<Integer> lots) {
    this.lots = lots;
    initializeInventory();
  }

  private void initializeInventory() {

    Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GREEN + "Auction View");
    for(int slot = 0; slot < lots.size(); slot++) {
      if(slot >= 9) continue;
      Auction a = TNE.instance.manager.auctionManager.getAuction(lots.get(slot));
      String active = (a.remaining().equals(a.getTime()))? ChatColor.GREEN + "Yes" : ChatColor.RED + "No";
      ItemStack stack = a.getItem().toItemStack();
      List<String> lore = new ArrayList<>();
      if(!a.getSilent()) {
        lore.add(ChatColor.WHITE + "Cost: " + ChatColor.GREEN + a.getNextBid());
      }
      lore.add(ChatColor.WHITE + "Amount: " + ChatColor.GREEN + a.getItem().getAmount());
      lore.add(ChatColor.WHITE + "Increment: " + ChatColor.GREEN + a.getIncrement());
      lore.add(ChatColor.WHITE + "Player: " + ChatColor.GREEN + MISCUtils.getPlayer(a.getPlayer()).getDisplayName());
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
  public boolean onClick(InventoryViewer viewer, ClickType type, int slot, ItemStack item) {
    if(type.equals(ClickType.LEFT)) {
      if(inventory.getItem(slot) != null && !inventory.getItem(slot).getType().equals(Material.AIR)) {
        int use = (lots.size() == 1)? 0 : slot;
        Auction a = TNE.instance.manager.auctionManager.getAuction(lots.get(use));
        TNE.instance.manager.auctionManager.bid(viewer.getWorld(), lots.get(use), viewer.getUUID(), new TransactionCost(a.getNextBid()));
        MISCUtils.getPlayer(viewer.getUUID()).closeInventory();
        onClose(viewer);
        return false;
      }
    }
    return false;
  }
}