package net.tnemc.signs.listeners;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.signs.ChestHelper;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.SignsManager;
import net.tnemc.signs.SignsModule;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.ItemSign;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
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
          if(SignsModule.manager().getSelectionManager().isSelecting(id, "chest")) {

            final Location location = event.getClickedBlock().getLocation();
            final Location signLocation = SignsModule.manager().getSelectionManager().getSelectionInstance(id).getSign();
            SignsModule.manager().getSelectionManager().doSelection("chest", id, signLocation, location);
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
  public void onPlayerInteractPaper(final PlayerInteractEvent event) {
    final Player player = event.getPlayer();
    /*final ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();

    if(stack.getType().equals(Material.PAPER)) {
      final ItemMeta meta = stack.getItemMeta();

      if(meta.hasDisplayName()) {

        switch(meta.getDisplayName()) {
          case "Experience Note":
            if(meta.hasLore()) {
              if(meta.getLore().get(0).contains("Amount:")) {
                Integer amount = Integer.valueOf(meta.getLore().get(0).split("\\:")[1]);
                ItemCalculations.removeItem(stack, player.getInventory());
                player.giveExp(amount);
              }
            }
            break;
          case "Mob Note":
            if(meta.hasLore()) {
              if(meta.getLore().get(0).contains("Type:")) {

              }
            }
            break;
        }

      }
    }*/
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onQuit(final PlayerQuitEvent event) {
    if(SignsModule.manager().getSelectionManager().isSelectingAny(event.getPlayer().getUniqueId())) {
      SignsModule.manager().getSelectionManager().remove(event.getPlayer().getUniqueId());
    }
  }
}