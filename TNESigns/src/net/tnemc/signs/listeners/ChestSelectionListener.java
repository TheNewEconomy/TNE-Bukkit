package net.tnemc.signs.listeners;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.signs.SignsModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/10/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ChestSelectionListener implements ModuleListener {

  private TNE plugin;

  public ChestSelectionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerInteract(final PlayerInteractEvent event) {
    final UUID id = event.getPlayer().getUniqueId();
    if(!event.isCancelled()) {
      if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getPlayer().isSneaking()) {
        if (event.getClickedBlock().getType().equals(Material.CHEST)) {
          if (SignsModule.manager().getSelectionManager().isSelecting(id, "chest")) {

            final Location location = event.getClickedBlock().getLocation();
            final Location signLocation = SignsModule.manager().getSelectionManager().getSelectionInstance(id).getSign();
            SignsModule.manager().getSelectionManager().doSelection("chest", id, signLocation, location);
          }
        }
      }
    }
  }
}
