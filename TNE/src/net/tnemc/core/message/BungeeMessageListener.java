package net.tnemc.core.message;

import net.tnemc.core.TNE;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/28/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BungeeMessageListener implements PluginMessageListener {
  @Override
  public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
    if(channel.startsWith("tne:")) {
      TNE.instance().messageManager().handle(channel, bytes);
    }
  }
}