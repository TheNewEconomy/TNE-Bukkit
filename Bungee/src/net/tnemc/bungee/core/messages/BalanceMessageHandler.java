package net.tnemc.bungee.core.messages;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.tnemc.bungee.core.MessageHandler;

import java.io.DataInputStream;
import java.io.IOException;
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
public class BalanceMessageHandler extends MessageHandler {
  public BalanceMessageHandler() {
    super("balance");
  }

  @Override
  public void handle(UUID player, UUID server, DataInputStream stream) {

    try {
      final String world = stream.readUTF();
      final String currency = stream.readUTF();
      final String amount = stream.readUTF();
      send(server, player, world, currency, amount);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public static void send(UUID server, UUID player, String world, String currency, String amount) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(server.toString());
    out.writeUTF(player.toString());
    out.writeUTF(world);
    out.writeUTF(currency);
    out.writeUTF(amount);
    sendToAll("tne:balance", out);
  }
}
