package net.tnemc.bungee.core;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 12/17/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */

import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.tnemc.bungee.core.messages.BalanceMessageHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all Plugin Channel messages.
 *
 * @author creatorfromhell
 * @since 1.0.0
 */
public class MessageListener implements Listener {

  private Map<String, MessageHandler> handlers = new HashMap<>();

  public MessageListener() {
    handlers.put("balance", new BalanceMessageHandler());
  }


  @EventHandler
  public void onMessage(PluginMessageEvent event) {
    if(!event.getTag().startsWith("tne:")) {
      return;
    }

    if(!(event.getSender() instanceof Server)) {
      event.setCancelled(true);
      return;
    }

    final String tag = event.getTag().replace("tne:", "");

    if(handlers.containsKey(tag)) {
      try {
        ByteArrayInputStream stream = new ByteArrayInputStream(event.getData());
        DataInputStream in = new DataInputStream(stream);

        UUID server = UUID.fromString(in.readUTF());
        UUID player = UUID.fromString(in.readUTF());
        handlers.get(tag).handle(player, server, in);

        in.close();
        stream.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
}
