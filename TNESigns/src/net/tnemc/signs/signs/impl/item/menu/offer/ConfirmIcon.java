package net.tnemc.signs.signs.impl.item.menu.offer;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.signs.signs.impl.ItemSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/7/2017.
 */
public class ConfirmIcon extends Icon {
  public ConfirmIcon(Integer slot) {
    super(slot, Material.GREEN_STAINED_GLASS_PANE, "Confirm Transaction");
    this.close = false;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START Confirm.onClick =====");
    final UUID id = IDFinder.getID(player);
    final UUID owner = (UUID) TNE.menuManager().getViewerData(id, "shop_owner");

    Chest chest = (Chest)((Location)TNE.menuManager().getViewerData(id, "shop_chest")).getBlock().getState();
    final ItemStack item = (ItemStack)TNE.menuManager().getViewerData(id, "shop_item");
    final int amount = item.getAmount();
    boolean complete = false;
    final Boolean currency = (Boolean)TNE.menuManager().getViewerData(id, "shop_currency");
    ItemStack trade = null;
    try {
      trade = (currency)? null : ItemSign.getTrade((Location)TNE.menuManager().getViewerData(id, "shop_location"));
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }

    if(ItemCalculations.getCount(item, ItemSign.getChestInventory(chest)) < amount) {
      player.sendMessage(ChatColor.RED + "Shop doesn't have enough items in storage to offer.");
      player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
      return;
    }

    if(!currency) {
      if(ItemSign.getChestInventory(chest).firstEmpty() == -1) {
        player.sendMessage(ChatColor.RED + "Shop doesn't have any free space.");
        player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
        return;
      }
    }

    if(currency) {
      final BigDecimal cost = (BigDecimal)TNE.menuManager().getViewerData(id, "shop_currency_cost");
      if (!TNE.instance().api().hasHoldings(id.toString(), cost)) {
        player.sendMessage(ChatColor.RED + "Insufficient funds.");
        player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
        return;
      }
      TNE.instance().api().addHoldings(owner.toString(), cost);
      complete = TNE.instance().api().removeHoldings(id.toString(), cost);
    } else {

      if(ItemCalculations.getCount(trade, player.getInventory()) < trade.getAmount()) {
        player.sendMessage(ChatColor.RED + "You don't have enough of the trade item to buy from this shop.");
        player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
        return;
      }

      ItemCalculations.removeItem(trade, player.getInventory());
      ItemCalculations.giveItems(Collections.singletonList(trade), ItemSign.getChestInventory(chest));
      complete = true;
    }

    if (complete) {
      ItemCalculations.giveItems(Collections.singletonList(item), player.getInventory());
      ItemCalculations.removeItem(item, ItemSign.getChestInventory(chest));

      player.sendMessage(ChatColor.GREEN + "Successfully bought item.");
      player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 5f);

      final Player ownerInstance = Bukkit.getPlayer(owner);
      if(ownerInstance != null) {
        ownerInstance.sendMessage(player.getDisplayName() + ChatColor.GREEN + " just purchased some " + item.getType().name() + " from your shop.");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 5f);
      }
      return;
    }

    super.onClick(menu, player);
  }
}
