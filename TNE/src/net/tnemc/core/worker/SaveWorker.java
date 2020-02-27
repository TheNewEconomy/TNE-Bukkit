package net.tnemc.core.worker;

import net.tnemc.core.TNE;

import java.sql.SQLException;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/3/2017.
 */
public class SaveWorker implements Runnable {

  private TNE plugin;
  private long time;

  public SaveWorker(TNE plugin, final long seconds) {
    this.plugin = plugin;
    this.time = seconds;
  }

  @Override
  public void run() {
    System.out.println("Running TNE AutoSaver...");
    try {
      plugin.getSaveManager().save();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    try {
      Thread.sleep(time * 1000);
    } catch (InterruptedException ignore) {
    }
  }
}
