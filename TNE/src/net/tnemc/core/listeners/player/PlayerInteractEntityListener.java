package net.tnemc.core.listeners.player;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerInteractEntityListener implements Listener {

  TNE plugin;

  public PlayerInteractEntityListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteractEntityEvent(final PlayerInteractEntityEvent event) {
    TNE.debug("=====START PlayerListener.onInteractEntityEvent =====");
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player.getUniqueId().toString());
    String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);
    TNE.debug("World: " + world);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy && event.getRightClicked() instanceof Player) {

      final Material actionMaterial = MaterialHelper.getMaterial(TNE.instance().api().getString("Core.Server.MenuMaterial"));
      final Material material = MISCUtils.getMainHand(player);

      if(actionMaterial == null && material == null
          || material == null && actionMaterial.equals(Material.AIR)
          || material.equals(actionMaterial)) {
        TNE.menuManager().open("main", player);
        TNE.menuManager().setViewerData(id, "action_player", IDFinder.getID((Player)event.getRightClicked()));
        TNE.menuManager().setViewerData(id, "action_world", world);
      }
    }
    TNE.debug("=====END PlayerListener.onInteractEntityEvent =====");
  }
}
