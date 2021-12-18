package net.tnemc.core.listeners.player;

import net.tnemc.core.TNE;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChannelEvent;

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
public class PlayerChannelListener implements Listener {

  TNE plugin;

  public PlayerChannelListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChannel(final PlayerChannelEvent event) {
    //System.out.println("Channel: " + event.getChannel());
    if(TNE.useMod) {
      if (event.getChannel().equalsIgnoreCase("tnemod")) {
        TNE.instance().addModUser(event.getPlayer().getUniqueId());
      }
    }
  }
}
