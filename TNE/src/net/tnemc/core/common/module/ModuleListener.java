package net.tnemc.core.common.module;

import net.tnemc.core.TNE;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/26/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public interface ModuleListener extends Listener {

  default void register(TNE plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  default void unregister() {
    HandlerList.unregisterAll(this);
  }
}