package net.tnemc.signs.signs.impl.toitem.menu.offer;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.signs.signs.impl.ItemSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/7/2017.
 */
public class ConfirmBuyIcon extends Icon {
  public ConfirmBuyIcon(Integer slot) {
    super(slot, TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Confirm Transaction");
    this.close = false;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START Confirm.onClick =====");
    final UUID id = IDFinder.getID(player);
    final UUID owner = (UUID) TNE.menuManager().getViewerData(id, "shop_owner");

    final boolean admin = (boolean)TNE.menuManager().getViewerData(id, "shop_admin");

    Chest chest = (admin)? null : (Chest)((Location)TNE.menuManager().getViewerData(id, "shop_chest")).getBlock().getState();
    final ItemStack item = (ItemStack)TNE.menuManager().getViewerData(id, "shop_item");
    final int amount = item.getAmount();
    final Boolean currency = (Boolean)TNE.menuManager().getViewerData(id, "shop_currency");

    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      boolean complete = false;
      ItemStack trade = null;
      try {
        trade = (currency)? null : ItemSign.getTrade((Location)TNE.menuManager().getViewerData(id, "shop_location"));
      } catch (SQLException e) {
        e.printStackTrace();
        return;
      }
      final int tradeAmount = (trade != null)? trade.getAmount() : 0;

      if(!admin && !currency && ItemCalculations.getCount(trade, ItemSign.getChestInventory(chest)) < tradeAmount) {
        player.sendMessage(ChatColor.RED + "Shop doesn't have enough items in storage to offer.");
        player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
        return;
      }

      if(ItemCalculations.getCount(item, player.getInventory()) < amount) {
        player.sendMessage(ChatColor.RED + "You don't have enough items to trade with this shop.");
        player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
        return;
      }

      if(!admin && ItemSign.getChestInventory(chest).firstEmpty() == -1) {
        player.sendMessage(ChatColor.RED + "Shop doesn't have any free space.");
        player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
        return;
      }

      if(!currency && player.getInventory().firstEmpty() == -1) {
        player.sendMessage(ChatColor.RED + "You don't have any free space to hold this item.");
        player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
        return;
      }

      if (currency) {
        final BigDecimal cost = (BigDecimal)TNE.menuManager().getViewerData(id, "shop_currency_cost");

        if (!admin && !TNE.instance().api().hasHoldings(owner.toString(), cost)) {
          player.sendMessage(ChatColor.RED + "Shop has insufficient funds.");
          player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
          return;
        }
        TNE.instance().api().addHoldings(id.toString(), cost);
        complete = (admin)? true : TNE.instance().api().removeHoldings(owner.toString(), cost);
      } else {

        if(!admin) ItemCalculations.removeItemAmount(trade, ItemSign.getChestInventory(chest), tradeAmount);
        ItemCalculations.giveItem(trade, player.getInventory(), tradeAmount);
        complete = true;
      }

      if (complete) {
        ItemCalculations.removeItemAmount(item, player.getInventory(), amount);
        if(!admin) ItemCalculations.giveItem(item, ItemSign.getChestInventory(chest), amount);

        player.sendMessage(ChatColor.GREEN + "Successfully bought item.");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 5f);

        final Player ownerInstance = Bukkit.getPlayer(owner);
        if(!admin && ownerInstance != null) {
          ownerInstance.sendMessage(player.getDisplayName() + ChatColor.GREEN + " just sold some " + item.getType().name() + " to your shop.");
          player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5f, 5f);
        }
        return;
      }

      super.onClick(menu, player);
    });
  }
}
