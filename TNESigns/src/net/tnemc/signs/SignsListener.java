package net.tnemc.signs;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.signs.impl.TNESign;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/13/2018.
 */
public class SignsListener implements ModuleListener {

  private TNE plugin;

  public SignsListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChange(final SignChangeEvent event) {
    String type = SignsManager.getSignType(event.getLine(0));
    if (!type.equalsIgnoreCase("invalid")) {
      Player player = event.getPlayer();
      UUID id = IDFinder.getID(player);
      String world = event.getBlock().getWorld().getName();
      TNESign sign = TNESign.instance(type, id, event.getBlock().getLocation());

      if(sign != null) {

        if(!sign.onCreate(player)) {
          event.setCancelled(true);
        } else {
          switch(type) {
            case "item":
              break;
            case "safe":
              break;
          }
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onClick(final PlayerInteractEvent event) {
    Action action = event.getAction();
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    Block block = event.getClickedBlock();
    Material heldMaterial = event.getItem().getType();

    if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
      if(SignsModule.manager().isAwaitingStep(id)) {

      } else {
        if (block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST)) {
        }
      }
    } else if(action.equals(Action.LEFT_CLICK_BLOCK)) {
      if(SignsModule.manager().isAwaitingStep(id)) {

      } else {
        if (block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST)) {
        }
      }
    }
  }
}