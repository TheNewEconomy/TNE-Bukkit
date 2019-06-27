package net.tnemc.signs.signs.impl.nitem;

import net.tnemc.core.TNE;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.ItemSign;
import net.tnemc.signs.signs.impl.NationItemSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
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
public class ItemSelectionStep implements SignStep {
  @Override
  public int step() {
    return 1;
  }

  @Override
  public boolean onSignInteract(Sign sign, UUID player, boolean rightClick, boolean shifting) {
    TNE.debug("ItemSelect Interaction");
    TNESign loaded = null;
    try {
      loaded = SignsData.loadSign(sign.getLocation());
    } catch (SQLException ignore) { }

    final Player playerInstance = Bukkit.getPlayer(player);
    if(loaded != null) {
      TNE.debug("ItemSelect Interaction");
      if(!NationItemSign.hasPerms(loaded.getOwner(), player)) {
        playerInstance.sendMessage(ChatColor.RED + "This shop is not offering any items currently.");
        return false;
      }

      final ItemStack holding = playerInstance.getInventory().getItemInMainHand();
      if (holding != null && !holding.getType().equals(Material.AIR)) {

        if(!ItemSign.canOffer(playerInstance, holding.getType())) {
          playerInstance.sendMessage(ChatColor.RED + "Invalid permission.");
          return false;
        }

        TNE.debug("ItemSelect Interaction");
        TNE.menuManager().setViewerData(player, "shop_offer_amount", 0);
        TNE.menuManager().setViewerData(player, "shop_sell", rightClick);
        TNE.menuManager().setViewerData(player, "shop_item", holding);
        TNE.menuManager().setViewerData(player, "action_shop", loaded.getLocation());

        TNE.menuManager().open("shop_offer_amount_selection", playerInstance);
        return false;
      }
    }
    playerInstance.sendMessage(ChatColor.RED + "Error while changing shop offer.");
    return false;
  }
}