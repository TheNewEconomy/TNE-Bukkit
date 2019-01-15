package net.tnemc.signs.signs.impl.item;

import net.tnemc.core.TNE;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.ItemSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/27/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TradeStep implements SignStep {
  @Override
  public int step() {
    return 4;
  }

  @Override
  public boolean onSignInteract(Sign sign, UUID player, boolean rightClick, boolean shifting) {
    if(!rightClick) return false;

    final Player playerInstance = Bukkit.getPlayer(player);
    TNESign loaded = null;
    try {
      loaded = SignsData.loadSign(sign.getLocation());
    } catch (SQLException ignore) {
      playerInstance.sendMessage(ChatColor.RED + "Error while changing shop trade.");
      return false;
    }

    if(loaded != null) {

      try {
        final Chest chest = SignsData.chest(sign.getLocation());
        final ItemStack item = ItemSign.getItem(sign.getLocation());
        TNE.debug("Trade Enchant Size: " + item.getEnchantments().size());
        TNE.debug("TradeEnchant Size: " + item.getItemMeta().getEnchants().size());
        TNE.debug("Item Null?: " + (item == null));
        TNE.debug("Damage: " + item.getDurability());
        final boolean selling = ItemSign.isSelling(sign.getLocation());
        TNE.menuManager().setViewerData(player, "shop_owner", loaded.getOwner());
        TNE.menuManager().setViewerData(player, "shop_selling", selling);
        TNE.menuManager().setViewerData(player, "shop_chest", chest.getLocation());
        TNE.menuManager().setViewerData(player, "shop_location", loaded.getLocation());
        TNE.menuManager().setViewerData(player, "shop_item", item);

        TNE.debug("Selling: " + selling);

        final boolean currency = ItemSign.isCurrency(sign.getLocation());
        TNE.menuManager().setViewerData(player, "shop_currency", currency);

        final BigDecimal amount = ItemSign.getCost(sign.getLocation());
        ItemStack cost = new ItemStack(Material.PAPER);

        TNE.debug("Currency: " + currency);

        if(currency) {
          TNE.menuManager().setViewerData(player, "shop_currency_cost", amount);
          ItemMeta meta = cost.getItemMeta();

          if(selling) {
            meta.setLore(Collections.singletonList(ChatColor.GOLD + "Cost: " + amount));
          } else {
            meta.setLore(Collections.singletonList(ChatColor.GOLD + "Shop Offer: " + amount));
          }
          cost.setItemMeta(meta);
        } else {
          cost = ItemSign.getTrade(sign.getLocation());
          ItemMeta meta = cost.getItemMeta();
          meta.setLore(Collections.singletonList(meta.getDisplayName()));
          cost.setItemMeta(meta);
        }
        TNE.menuManager().setViewerData(player, "shop_cost", cost);

        TNE.menuManager().open("shop_offer_menu", playerInstance);

      } catch (SQLException e) {
        playerInstance.sendMessage(ChatColor.RED + "Unable to locate this shop's storage chest.");
        playerInstance.playSound(playerInstance.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5f, 5f);
        return false;
      }
    }
    return false;
  }
}