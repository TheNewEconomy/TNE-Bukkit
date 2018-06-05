package net.tnemc.tutorial;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.tutorial.command.TutorialCommand;
import net.tnemc.tutorial.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/29/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
@ModuleInfo(
    name = "Tutorial",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class TutorialModule extends Module {

  private static TutorialModule instance;
  private TutorialManager manager;

  @Override
  public void load(TNE tne, String version) {
    instance = this;
    Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(tne), tne);

    manager = new TutorialManager();
    tne.logger().info("Tutorial Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Tutorial Module unloaded!");
  }

  /**
   * Returns a list of command instances this module uses.
   */
  @Override
  public List<TNECommand> getCommands() {
    return Arrays.asList(new TutorialCommand(TNE.instance()));
  }

  public static TutorialManager manager() {
    return instance().manager;
  }

  public static TutorialModule instance() {
    return instance;
  }


  public static Location lookAt(Location loc, Location lookat) {
    //Clone the loc to prevent applied changes to the input loc
    loc = loc.clone();
    // Values of change in distance (make it relative)
    double dx = lookat.getX() - loc.getX();
    double dy = lookat.getY() - loc.getY();
    double dz = lookat.getZ() - loc.getZ();
    // Set yaw
    if (dx != 0) {
      // Set yaw start value based on dx
      if (dx < 0) {
        loc.setYaw((float) (1.5 * Math.PI));
      } else {
        loc.setYaw((float) (0.5 * Math.PI));
      }
      loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
    } else if (dz < 0) {
      loc.setYaw((float) Math.PI);
    }
    // Get the distance from dx/dz
    double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
    // Set pitch
    loc.setPitch((float) -Math.atan(dy / dxz));
    // Set values, convert to degrees (invert the yaw since Bukkit uses a different yaw dimension format)
    loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
    loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);
    return loc;
  }
}