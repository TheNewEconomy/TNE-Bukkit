package net.tnemc.core.listeners.entity;

import net.tnemc.core.TNE;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

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
public class EntityPortalListener implements Listener {

  TNE plugin;

  public EntityPortalListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onNetherPortal(EntityPortalEvent event) {
    if(event.getEntityType().equals(EntityType.DROPPED_ITEM)) {
      final String world = TNE.instance().getWorldManager(event.getEntity().getWorld().getName()).getBalanceWorld();
      if(TNE.manager().currencyManager().currencyFromItem(world, ((Item)event.getEntity()).getItemStack()).isPresent()) event.setCancelled(true);
    }
  }
}