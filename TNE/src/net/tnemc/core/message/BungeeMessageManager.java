package net.tnemc.core.message;

import net.tnemc.core.TNE;
import net.tnemc.core.message.impl.BungeeBalanceMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class BungeeMessageManager {

  private Map<String, BungeeMessage> messages = new HashMap<>();

  private List<UUID> accountsMessage = new ArrayList<>();

  private BungeeMessageListener listener;

  public BungeeMessageManager() {
    this.listener = new BungeeMessageListener();

    messages.put("tne:balance", new BungeeBalanceMessage());

    register();
  }

  public boolean isAffected(UUID account) {
    return accountsMessage.contains(account);
  }

  public void removeAccount(UUID account) {
    accountsMessage.remove(account);
  }

  public void addAccount(UUID account) {
    accountsMessage.add(account);
  }

  public void register() {
    messages.keySet().forEach(channel->{
      TNE.instance().getServer().getMessenger().registerOutgoingPluginChannel(TNE.instance(), channel);
      TNE.instance().getServer().getMessenger().registerIncomingPluginChannel(TNE.instance(), channel, listener);
    });
  }

  public void handle(String channel, byte[] bytes) {
    if(messages.containsKey(channel)) {
      messages.get(channel).handle(bytes);
    }
  }
}