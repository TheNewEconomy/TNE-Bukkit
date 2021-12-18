package net.tnemc.core.message;

import net.tnemc.core.TNE;

import java.io.IOException;
import java.util.Optional;
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
public abstract class BungeeMessage {

  protected String channel;

  protected UUID server;

  public BungeeMessage(String channel) {
    this.channel = channel;
  }

  public void handle(byte[] bytes) {
    BungeeMessageWrapper wrapper = new BungeeMessageWrapper(bytes);

    Optional<UUID> serverID = Optional.empty();
    try {
      serverID = wrapper.readUUID();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if(serverID.isPresent()) {
      server = serverID.get();

      if(!TNE.serverID.toString().equalsIgnoreCase(server.toString())) {
        handle(wrapper);
      }
    }
  }

  public abstract void handle(BungeeMessageWrapper wrapper);
}