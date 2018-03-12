package net.tnemc.core.worker;

import net.tnemc.core.TNE;
import net.tnemc.core.common.data.TNEDataProvider;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 3/11/2018.
 */
public class MismatchWorker extends BukkitRunnable {

  private TNE plugin;

  public MismatchWorker(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    int ids = ((TNEDataProvider)plugin.getSaveManager().getDataManager().getDb()).idsLength();
    int users = ((TNEDataProvider)plugin.getSaveManager().getDataManager().getDb()).usersLength();
    System.out.println("Eco IDS: " + ids + " Users: " + users);

    if(ids != users) System.out.println("================ECO ID Issue====================");
    if(ids != users) System.out.println("================ECO ID Issue====================");
    if(ids != users) System.out.println("================ECO ID Issue====================");
    if(ids != users) System.out.println("================ECO ID Issue====================");
    if(ids != users) System.out.println("================ECO ID Issue====================");
    if(ids != users) System.out.println("================ECO ID Issue====================");
    if(ids != users) System.out.println("================ECO ID Issue====================");
    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "tps");
  }
}
