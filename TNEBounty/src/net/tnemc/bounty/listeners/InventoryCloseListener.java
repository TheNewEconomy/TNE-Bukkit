package net.tnemc.bounty.listeners;

import net.tnemc.bounty.inventory.RewardCenterHolder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.ModuleListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class InventoryCloseListener implements ModuleListener {

  private TNE plugin;

  public InventoryCloseListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEvent(InventoryCloseEvent event) {
    if(event.getInventory().getHolder() instanceof RewardCenterHolder) {
      ((RewardCenterHolder) event.getInventory().getHolder()).saveRewards();
    }
  }
}
