package net.tnemc.signs.signs.impl.item;

import net.tnemc.core.TNE;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.ItemSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
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
public class TradeSelectionStep implements SignStep {
  @Override
  public int step() {
    return 2;
  }

  @Override
  public boolean onSignInteract(Sign sign, UUID player, boolean rightClick, boolean shifting) {
    final Player playerInstance = Bukkit.getPlayer(player);
    TNESign loaded = null;
    try {
      loaded = SignsData.loadSign(sign.getLocation());
    } catch (SQLException ignore) {
      playerInstance.sendMessage(ChatColor.RED + "Error while changing shop trade.");
      return false;
    }

    if(loaded != null) {
      if(!loaded.getOwner().equals(player)) {
        playerInstance.sendMessage(ChatColor.RED + "This shop is not offering any items currently.");
        return false;
      }
      if(rightClick) {
        final ItemStack holding = playerInstance.getInventory().getItemInMainHand();
        if (holding != null && !holding.getType().equals(Material.AIR)) {

          if(!ItemSign.canOffer(playerInstance, holding.getType())) {
            playerInstance.sendMessage(ChatColor.RED + "Invalid permission.");
            return false;
          }
          try {
            ItemSign.saveItemOffer(loaded.getLocation(), holding, false, BigDecimal.ZERO);
            SignsData.updateStep(sign.getLocation(), 3);

            playerInstance.sendMessage(ChatColor.WHITE + "Changed shop trade to " + holding.getAmount() + " of " + holding.getType().name() + ".");
            playerInstance.sendMessage(ChatColor.WHITE + "Now right click your shop sign, followed by a chest to mark your shop's storage.");
            return false;
          } catch (SQLException ignore) {
            playerInstance.sendMessage(ChatColor.RED + "Error while changing shop trade.");
            return false;
          }
        }
      } else {
        TNE.menuManager().open("shop_currency_selection", playerInstance);
        TNE.menuManager().setViewerData(player, "action_shop", sign.getLocation());
      }
    }
    return false;
  }
}