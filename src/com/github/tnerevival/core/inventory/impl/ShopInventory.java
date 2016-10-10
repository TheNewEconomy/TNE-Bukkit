package com.github.tnerevival.core.inventory.impl;

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
      if(slot < 27 && i != null && i.getTrade() != null  && !i.getTrade().toItemStack().getType().equals(Material.AIR)) {
        if (!s.getOwner().equals(viewer.getUUID())) {

        }
        //TODO: You cannot buy from your own shop.
        return false;
      }
      //TODO: There is no trade option for this item.
      return false;
    } else if(type.equals(ClickType.LEFT)) {
      if(slot < 27  && i != null) {
        if (i.isBuy()) {
          if (i.getStock() > 0) {
            if (AccountUtils.transaction(viewer.getUUID().toString(), null, i.getCost(), TransactionType.MONEY_INQUIRY, s.getWorld())) {
              AccountUtils.transaction(viewer.getUUID().toString(), null, i.getCost(), TransactionType.MONEY_REMOVE, s.getWorld());
              s.handlePayment(i.getCost());
              s.remove(slot, i.getItem().getAmount());
              MISCUtils.getPlayer(viewer.getUUID()).getInventory().addItem(i.getItem().toItemStack());
              s.update();
              //TODO: successful!
              return false;
            }
            //TODO: Insufficient funds.
            return false;
          }
          return false;
        } else {
          if (i.getMaxstock() - i.getStock() > 0) {
            if (MISCUtils.getItemCount(viewer.getUUID(), i.getItem().toItemStack()) >= i.getItem().getAmount()) {
              if (AccountUtils.transaction(s.getOwner().toString(), null, i.getCost(), TransactionType.MONEY_INQUIRY, s.getWorld())) {
                AccountUtils.transaction(s.getOwner().toString(), viewer.getUUID().toString(), i.getCost(), TransactionType.MONEY_PAY, s.getWorld());
                s.remove(slot, i.getItem().getAmount());
                MISCUtils.getPlayer(viewer.getUUID()).getInventory().removeItem(i.getItem().toItemStack());
                s.update();
                //TODO: Successfully sold item!
              }
              //TODO: Shop does not have enough funds.
              return false;
            }
            //TODO: Not enough of $item.
            return false;
          }
          //Shop has reached buy limit.
          return false;
        }
      }
    } else if(type.equals(ClickType.SHIFT_RIGHT)) {
      if(slot < 27 && Shop.canModify(s.getName(), MISCUtils.getPlayer(viewer.getUUID())) && i != null) {
        s.removeItem(i.getItem().toItemStack(), i.getCost(), i.isBuy(), i.getTrade().toItemStack());
        if(i.getStock() > 0) {
          ItemStack temp = i.getItem().toItemStack();
          temp.setAmount(i.getStock());
          MISCUtils.getPlayer(viewer.getUUID()).getInventory().addItem(temp);
        }
        s.update();
        //TODO: Successful!
        return false;
      }
      //TODO: You don't have permission to do that.
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
      }
      //TODO: You don't have permission to do that.
      return false;
    }*/
    return false;
  }
}