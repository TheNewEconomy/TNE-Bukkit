package net.tnemc.bungee.core;

import com.google.common.io.ByteArrayDataOutput;

import java.io.DataInputStream;
import java.util.UUID;

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
public abstract class MessageHandler {

  private String tag;

  public MessageHandler(String tag) {
    this.tag = tag;
  }

  public static void sendToAll(final String channel, ByteArrayDataOutput out) {
    TNEConnect.instance().getProxy().getServers().values().forEach(server->{
      if(server.getPlayers().size() > 0) {
        server.sendData(channel, out.toByteArray(), false);
      }
    });
  }

  public abstract void handle(UUID player, UUID server, DataInputStream stream);
}