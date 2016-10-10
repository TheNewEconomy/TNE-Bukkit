package com.github.tnerevival.core.inventory.impl;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.inventory.InventoryViewer;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Daniel on 9/28/2016.
 */
public class ShopInventory extends GenericInventory {

  private Shop s;

  public ShopInventory(Shop s) {
    this.s = s;
  }

  @Override
  public void onClose(InventoryViewer viewer) {
    super.onClose(viewer);
    s.removeShopper(viewer.getUUID());
  }

  @Override
  public boolean onClick(InventoryViewer viewer, ClickType type, int slot, ItemStack item) {
    ShopEntry i = s.getItem(slot);
    if(type.equals(ClickType.RIGHT)) {
      if(slot < Shop.getSlots() && i != null && i.getTrade() != null  && !i.getTrade().toItemStack().getType().equals(Material.AIR)) {
        if (i.getStock() >= i.getItem().getAmount()) {
          if (MISCUtils.getItemCount(viewer.getUUID(), i.getTrade().toItemStack()) >= i.getTrade().getAmount()) {
            s.remove(slot, i.getItem().getAmount());
            MISCUtils.getPlayer(viewer.getUUID()).getInventory().removeItem(i.getTrade().toItemStack());
            return false;
          }
          Message invalidStock = new Message("Messages.Shop.NotEnough");
          invalidStock.addVariable("$amount", i.getTrade().getAmount() + "");
          invalidStock.addVariable("$item", i.getTrade().toItemStack().getType().name());
          MISCUtils.getPlayer(viewer.getUUID()).sendMessage(invalidStock.translate());
          return false;
        }
        MISCUtils.getPlayer(viewer.getUUID()).sendMessage(new Message("Messages.Shop.NoStock").translate());
        return false;
      }
      MISCUtils.getPlayer(viewer.getUUID()).sendMessage(new Message("Messages.Shop.NoTrade").translate());
      return false;
    } else if(type.equals(ClickType.LEFT)) {
      if(slot < Shop.getSlots()  && i != null) {
        if (i.isBuy()) {
          if(i.getCost() > 0.0 || i.getCost() <= 0.0 && i.getTrade() == null || i.getCost() <= 0.0 && i.getTrade().toItemStack().getType().equals(Material.AIR)) {
            if (i.getStock() >= i.getItem().getAmount()) {
              if (AccountUtils.transaction(viewer.getUUID().toString(), null, i.getCost(), TransactionType.MONEY_INQUIRY, s.getWorld())) {
                AccountUtils.transaction(viewer.getUUID().toString(), null, i.getCost(), TransactionType.MONEY_REMOVE, s.getWorld());
                s.handlePayment(i.getCost());
                s.remove(slot, i.getItem().getAmount());
                MISCUtils.getPlayer(viewer.getUUID()).getInventory().addItem(i.getItem().toItemStack());
                s.update();
                return false;
              }
              Message insufficient = new Message("Messages.Money.Insufficient");

              insufficient.addVariable("$amount", MISCUtils.formatBalance(
                  MISCUtils.getWorld(viewer.getUUID()),
                  i.getCost()
              ));
              MISCUtils.getPlayer(viewer.getUUID()).sendMessage(insufficient.translate());
              return false;
            }
            MISCUtils.getPlayer(viewer.getUUID()).sendMessage(new Message("Messages.Shop.NoStock").translate());
            return false;
          }
          MISCUtils.getPlayer(viewer.getUUID()).sendMessage(new Message("Messages.Shop.NoBuy").translate());
          return false;
        } else {
          if (i.getMaxstock() - i.getStock() > 0) {
            if (MISCUtils.getItemCount(viewer.getUUID(), i.getItem().toItemStack()) >= i.getItem().getAmount()) {
              if (AccountUtils.transaction(s.getOwner().toString(), null, i.getCost(), TransactionType.MONEY_INQUIRY, s.getWorld())) {
                AccountUtils.transaction(s.getOwner().toString(), viewer.getUUID().toString(), i.getCost(), TransactionType.MONEY_PAY, s.getWorld());
                s.remove(slot, i.getItem().getAmount());
                MISCUtils.getPlayer(viewer.getUUID()).getInventory().removeItem(i.getItem().toItemStack());
                s.update();
                return false;
              }
              MISCUtils.getPlayer(viewer.getUUID()).sendMessage(new Message("Messages.Shop.FundsLack").translate());
              return false;
            }
            Message invalidStock = new Message("Messages.Shop.NotEnough");
            invalidStock.addVariable("$amount", i.getItem().getAmount() + "");
            invalidStock.addVariable("$item", i.getItem().toItemStack().getType().name());
            MISCUtils.getPlayer(viewer.getUUID()).sendMessage(invalidStock.translate());
            return false;
          }
          MISCUtils.getPlayer(viewer.getUUID()).sendMessage(new Message("Messages.Shop.BuyLimit").translate());
          return false;
        }
      }
    } else if(type.equals(ClickType.SHIFT_RIGHT)) {
      if(slot < Shop.getSlots() && Shop.canModify(s.getName(), MISCUtils.getPlayer(viewer.getUUID())) && i != null) {
        s.removeItem(i.getItem().toItemStack(), i.getCost(), i.isBuy(), i.getTrade().toItemStack());
        if(i.getStock() > 0) {
          ItemStack temp = i.getItem().toItemStack();
          temp.setAmount(i.getStock());
          MISCUtils.getPlayer(viewer.getUUID()).getInventory().addItem(temp);
        }
        s.update();
        return false;
      }
      MISCUtils.getPlayer(viewer.getUUID()).sendMessage(new Message("Messages.Shop.Permission").translate());
      return false;
    } /*else if(type.equals(ClickType.SHIFT_LEFT)) {
      if(Shop.canModify(s.getName(), MISCUtils.getPlayer(viewer.getUUID()))) {
        if(slot > 26) {
          ShopItemInventory itemInv = new ShopItemInventory(s);
          TNE.instance.inventoryManager.addInventory(itemInv, viewer);
          MISCUtils.getPlayer(viewer.getUUID()).openInventory(itemInv.getInventory());
          return false;
        } else {
          if(i != null) {
            ShopItemInventory itemInv = new ShopItemInventory(s, s.getItem(slot));
            TNE.instance.inventoryManager.addInventory(itemInv, viewer);
            MISCUtils.getPlayer(viewer.getUUID()).openInventory(itemInv.getInventory());
            return false;
          }
        }
        return false;
      }
      MISCUtils.getPlayer(viewer.getUUID()).sendMessage(new Message("Messages.Shop.Permission").translate());
      return false;
    }*/
    return false;
  }
}