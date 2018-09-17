package net.tnemc.core.worker;

import net.tnemc.core.TNE;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/3/2017.
 */
public class SaveWorker extends BukkitRunnable {

  private TNE plugin;

  public SaveWorker(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    try {
      plugin.getSaveManager().save();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
