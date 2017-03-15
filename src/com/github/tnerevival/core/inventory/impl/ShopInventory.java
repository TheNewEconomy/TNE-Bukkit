package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.inventory.TNEInventory;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by creatorfromhell on 3/15/2017.
 * All rights reserved.
 **/
public class ShopInventory extends TNEInventory {

  private Shop s;

  public ShopInventory(UUID inventoryID, Inventory inventory, String world) {
    super(inventoryID, inventory, world);
    String name = inventory.getTitle().split("]")[1].trim();
    Shop shop = TNE.instance().manager.shops.get(ChatColor.stripColor(name) + ":" + world);
    this.s = shop;
  }

  @Override
  public boolean onOpen(UUID player) {
    super.onOpen(player);
    s.addShopper(player);
    return true;
  }

  @Override
  public void onClose(UUID player) {
    super.onClose(player);
    s.removeShopper(player);
  }

  @Override
  public boolean onClick(UUID player, ClickType type, int slot, ItemStack item) {
    ShopEntry i = s.getItem(slot);
    if (type.equals(ClickType.RIGHT)) {
      if (slot < Shop.getSlots(s.getWorld(), s.getOwner()) && i != null && i.getTrade() != null && !i.getTrade().toItemStack().getType().equals(Material.AIR)) {
        if (i.getStock() >= i.getItem().getAmount()) {
          if (MISCUtils.getItemCount(player, i.getTrade().toItemStack()) >= i.getTrade().getAmount()) {
            s.remove(slot, i.getItem().getAmount());
            IDFinder.getPlayer(player.toString()).getInventory().addItem(i.getItem().toItemStack());
            IDFinder.getPlayer(player.toString()).getInventory().removeItem(i.getTrade().toItemStack());
            s.update();
            return false;
          }
          Message invalidStock = new Message("Messages.Shop.NotEnough");
          invalidStock.addVariable("$amount", i.getTrade().getAmount() + "");
          invalidStock.addVariable("$item", i.getTrade().toItemStack().getType().name());
          invalidStock.translate(world, IDFinder.getPlayer(player.toString()));
          return false;
        }
        new Message("Messages.Shop.NoStock").translate(world, IDFinder.getPlayer(player.toString()));
        return false;
      }
      new Message("Messages.Shop.NoTrade").translate(world, IDFinder.getPlayer(player.toString()));
      return false;
    } else if (type.equals(ClickType.LEFT)) {
      if (slot < Shop.getSlots(s.getWorld(), s.getOwner()) && i != null) {
        if (i.isBuy()) {
          if (i.getCost().doubleValue() > 0.0 || i.getCost().doubleValue() <= 0.0 && i.getTrade() == null
              || i.getCost().doubleValue() <= 0.0 && i.getTrade().toItemStack().getType().equals(Material.AIR)) {
            if (i.getStock() >= i.getItem().getAmount()) {
              if (AccountUtils.transaction(player.toString().toString(), null, i.getCost(), TransactionType.MONEY_INQUIRY, s.getWorld())) {
                AccountUtils.transaction(player.toString().toString(), null, i.getCost(), TransactionType.MONEY_REMOVE, s.getWorld());
                s.handlePayment(i.getCost());
                s.remove(slot, i.getItem().getAmount());
                IDFinder.getPlayer(player.toString()).getInventory().addItem(i.getItem().toItemStack());
                s.update();
                return false;
              }
              Message insufficient = new Message("Messages.Money.Insufficient");

              insufficient.addVariable("$amount", CurrencyFormatter.format(
                  IDFinder.getWorld(player),
                  i.getCost()
              ));
              insufficient.translate(world, IDFinder.getPlayer(player.toString()));
              return false;
            }
            new Message("Messages.Shop.NoStock").translate(world, IDFinder.getPlayer(player.toString()));
            return false;
          }
          new Message("Messages.Shop.NoBuy").translate(world, IDFinder.getPlayer(player.toString()));
          return false;
        } else {
          if (i.getMaxstock() - i.getStock() > 0) {
            if (MISCUtils.getItemCount(player, i.getItem().toItemStack()) >= i.getItem().getAmount()) {
              if (AccountUtils.transaction(s.getOwner().toString(), null, i.getCost(), TransactionType.MONEY_INQUIRY, s.getWorld())) {
                AccountUtils.transaction(s.getOwner().toString(), player.toString().toString(), i.getCost(), TransactionType.MONEY_PAY, s.getWorld());
                s.remove(slot, i.getItem().getAmount());
                IDFinder.getPlayer(player.toString()).getInventory().removeItem(i.getItem().toItemStack());
                s.update();
                return false;
              }
              new Message("Messages.Shop.FundsLack").translate(world, IDFinder.getPlayer(player.toString()));
              return false;
            }
            Message invalidStock = new Message("Messages.Shop.NotEnough");
            invalidStock.addVariable("$amount", i.getItem().getAmount() + "");
            invalidStock.addVariable("$item", i.getItem().toItemStack().getType().name());
            invalidStock.translate(world, IDFinder.getPlayer(player.toString()));
            return false;
          }
          new Message("Messages.Shop.BuyLimit").translate(world, IDFinder.getPlayer(player.toString()));
          return false;
        }
      }
    } else if (type.equals(ClickType.SHIFT_RIGHT)) {
      if (slot < Shop.getSlots(s.getWorld(), s.getOwner()) && Shop.canModify(s.getName(), IDFinder.getPlayer(player.toString())) && i != null) {
        s.removeItem(i.getItem().toItemStack(), i.getCost(), i.isBuy(), i.getTrade().toItemStack());
        if (i.getStock() > 0) {
          ItemStack temp = i.getItem().toItemStack();
          temp.setAmount(i.getStock());
          IDFinder.getPlayer(player.toString()).getInventory().addItem(temp);
        }
        s.update();
        return false;
      }
      new Message("Messages.Shop.Permission").translate(world, IDFinder.getPlayer(player.toString()));
      return false;
    }
    return false;
  }
}