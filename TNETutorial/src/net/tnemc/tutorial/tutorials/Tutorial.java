package net.tnemc.tutorial.tutorials;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
public interface Tutorial {

  String name();

  default Location startingLocation(Player player) {
    return null;
  }

  void run(Player player);

  default void reset(Player player) {
    for(int i = 0; i < 16; i++) {
      player.sendMessage("");
    }
    player.resetTitle();
  }
}