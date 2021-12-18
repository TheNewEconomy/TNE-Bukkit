package net.tnemc.core.message.impl;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.message.BungeeMessage;
import net.tnemc.core.message.BungeeMessageWrapper;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
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
public class BungeeBalanceMessage extends BungeeMessage {

  public BungeeBalanceMessage() {
    super("tne:balance");
  }

  public static void send(UUID account, String world, String currency, BigDecimal amount) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(TNE.serverID.toString());
    out.writeUTF(account.toString());
    out.writeUTF(world);
    out.writeUTF(currency);
    out.writeUTF(amount.toPlainString());

    if(Bukkit.getOnlinePlayers().size() > 0) {
      Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(TNE.instance(), "tne:balance", out.toByteArray());
    }
  }

  @Override
  public void handle(BungeeMessageWrapper wrapper) {

    try {

      final Optional<UUID> accountOPT = wrapper.readUUID();
      final String world = wrapper.readUTF();
      final String currency = wrapper.readUTF();
      final Optional<BigDecimal> amountOPT = wrapper.readBigDecimal();

      if(accountOPT.isPresent() && amountOPT.isPresent()) {
        TNEAccount account = TNE.manager().getAccount(accountOPT.get());

        if(account != null) {
          TNE.instance().messageManager().addAccount(accountOPT.get());
          account.setHoldings(world, currency, amountOPT.get());
        }
      }

    } catch(Exception e) {

    }
  }
}