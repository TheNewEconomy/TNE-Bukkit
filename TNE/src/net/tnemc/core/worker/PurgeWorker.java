package net.tnemc.core.worker;

import net.tnemc.core.TNE;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/15/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PurgeWorker implements Runnable {

  private TNE plugin;
  private int days;

  public PurgeWorker(TNE plugin, final int days) {
    this.plugin = plugin;
    this.days = days;
  }

  @Override
  public void run() {
    TNE.logger().info("Running TNE Purge Worker...");
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      try {
        plugin.getSaveManager().save();
      } catch(Exception ignore) {
        TNE.logger().warning("Issue occurred while attempting to purge data");
      }
    });
    try {
      Thread.sleep(TimeUnit.DAYS.toMillis(days));
    } catch (InterruptedException ignore) {
    }
  }
}