package net.tnemc.signs.listeners;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.signs.ChestHelper;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.SignsManager;
import net.tnemc.signs.SignsModule;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.ItemSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/29/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerListener implements Listener {

  private TNE plugin;

  public PlayerListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteract(final PlayerInteractEvent event) {
    final Action eventAction = event.getAction();
    final UUID id = event.getPlayer().getUniqueId();
    if(eventAction.equals(Action.LEFT_CLICK_AIR) || eventAction.equals(Action.RIGHT_CLICK_AIR)
        || event.getPlayer().getGameMode().equals(GameMode.CREATIVE)|| event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
      return;
    }

    final Block block = event.getClickedBlock();
    if(block.getState() instanceof Sign) {
      final String identifier = ((Sign)block.getState()).getLine(0);
      TNE.debug("Identifier: " + identifier);
      if(SignsManager.validSign(identifier)) {
        TNE.debug("Valid Sign.");
        SignType type = SignsModule.manager().getType(identifier);
        if(type != null) {
          TNE.debug("Type: " + type.name());
          if(type.onSignInteract((Sign) block.getState(), IDFinder.getID(event.getPlayer()), (eventAction.equals(Action.RIGHT_CLICK_BLOCK)), event.getPlayer().isSneaking())) {
            event.setCancelled(true);
          }
        }
      }
    } else {
      if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getPlayer().isSneaking()) {
        if(event.getClickedBlock().getType().equals(Material.CHEST)) {
          if(SignsManager.chestSelection.containsKey(id)) {

            final Location location = event.getClickedBlock().getLocation();
            final Location signLocation = SignsManager.chestSelection.get(id);
            Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
              try {
                if(Bukkit.getPluginManager().getPlugin("Towny") != null) {
                  if(!PlayerCacheUtil.getCachePermission(event.getPlayer(), location, Material.CHEST, TownyPermission.ActionType.SWITCH)) {
                    return;
                  }
                }

                SignsData.updateChest(signLocation, location);
                SignsData.updateStep(signLocation, 4);
                event.getPlayer().sendMessage(ChatColor.WHITE + "Updated your shop's storage to the chest at X: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ());
                SignsManager.chestSelection.remove(id);
              } catch (SQLException e) {
                event.getPlayer().sendMessage(ChatColor.RED + "Error while updating your shop's storage.");
              }
            });
          } else {

            if (event.getClickedBlock().getState() instanceof Chest) {
              Sign sign = new ChestHelper((Chest) event.getClickedBlock().getState()).getSign();
              if (sign != null) {
                TNESign signInstance = null;
                try {
                  signInstance = SignsData.loadSign(sign.getBlock().getLocation());
                } catch (SQLException e) {
                  e.printStackTrace();
                }
                if (signInstance != null) {
                  if (!SignsModule.manager().getType(signInstance.getType()).onChest(signInstance.getOwner(), event.getPlayer().getUniqueId())
                      && !event.getPlayer().hasPermission("tne.shop.override")) {
                    event.setCancelled(true);
                  }
                }
              } else {
                try {
                  UUID owner = ItemSign.chest(event.getClickedBlock().getLocation());

                  if(owner != null) {
                    if(!owner.equals(id)) {
                      event.setCancelled(true);
                    }
                  }
                } catch (SQLException e) {
                  TNE.debug(e);
                }
              }
            }
          }
        }
      }
    }
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onQuit(final PlayerQuitEvent event) {
    if(SignsManager.chestSelection.containsKey(event.getPlayer().getUniqueId())) {
      SignsManager.chestSelection.remove(event.getPlayer().getUniqueId());
    }
  }
}