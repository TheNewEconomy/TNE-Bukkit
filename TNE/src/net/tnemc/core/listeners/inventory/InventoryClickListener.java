package net.tnemc.core.listeners.inventory;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

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
public class InventoryClickListener implements Listener {

  TNE plugin;

  public InventoryClickListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventoryClick(final org.bukkit.event.inventory.InventoryClickEvent event) {
    if (event.getInventory().getHolder() instanceof MenuHolder) {
      event.setCancelled(true);

      int slot = event.getRawSlot();
      Menu menu = ((MenuHolder)event.getInventory().getHolder()).getMenuInstance();

      menu.click((Player)event.getWhoClicked(), slot, event.getClick());
    } else {
      if(event.getInventory().getType().equals(InventoryType.MERCHANT) &&
          !TNE.configurations().getBoolean("Core.Server.CurrencyTrading")) {
        final String world = WorldFinder.getWorld(event.getWhoClicked(), WorldVariant.BALANCE);
        if(event.getCurrentItem() != null) {
          final ItemStack currencyCheck = event.getCurrentItem().clone();
          if (TNE.manager().currencyManager().currencyFromItem(world, currencyCheck).isPresent()) {
            event.setCancelled(true);
          }
        }
      }
    }
  }
}
