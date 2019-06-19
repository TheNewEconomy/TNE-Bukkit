package net.tnemc.signs.signs.impl.item;

import net.tnemc.core.TNE;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.SignsModule;
import net.tnemc.signs.selection.SelectionPlayer;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

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
public class ChestSelectionStep implements SignStep {
  @Override
  public int step() {
    return 3;
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

      playerInstance.sendMessage(ChatColor.WHITE + "Now click on the chest you wish to use as this shop's storage. This" +
                                 " request will expire in 30 seconds.");
      Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
        if(SignsModule.manager().getSelectionManager().isSelecting(player, "chest")) {
          SignsModule.manager().getSelectionManager().remove(player);
          playerInstance.sendMessage(ChatColor.RED + "Chest selection request has expired.");
          playerInstance.playSound(playerInstance.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 5.0f, 5.0f);
        }
      }, 600);
      SignsModule.manager().getSelectionManager().addPlayer(player, new SelectionPlayer(player, sign.getLocation(), "chest"));
    }
    return false;
  }
}