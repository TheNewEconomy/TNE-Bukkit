package net.tnemc.core.listeners.item;

import net.tnemc.core.TNE;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/19/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CraftItemListener implements Listener {

  private TNE plugin;

  public CraftItemListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEvent(CraftItemEvent event) {
    if(!event.isCancelled()) {
      for(ItemStack stack : event.getInventory().getMatrix()) {
        if(stack != null && stack.hasItemMeta()) {
          ItemMeta meta = stack.getItemMeta();

          if(meta.hasDisplayName() && meta.getDisplayName().contains("Currency Note")) event.setCancelled(true);
        }
      }
    }
  }
}