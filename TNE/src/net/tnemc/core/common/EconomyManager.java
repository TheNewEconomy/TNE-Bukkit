package net.tnemc.core.common;

import com.github.tnerevival.core.collection.EventMap;
import net.tnemc.commands.core.parameter.parsers.PlayerParser;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.handlers.CoreHoldingsHandler;
import net.tnemc.core.common.account.handlers.EChestHandler;
import net.tnemc.core.common.account.handlers.HoldingsHandler;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.event.account.TNEAccountCreationEvent;
import net.tnemc.core.listeners.collections.AccountListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class EconomyManager {


  /**
   * Used to hold all accounts.
   * Dictionary is a {@link Map} collection that contains {@link UUID Player UUID} as
   * the key and {@link TNEAccount account} as the value.
   */
  private EventMap<UUID, TNEAccount> accounts = new EventMap<>();

  private List<UUID> dead = new ArrayList<>();

  /**
   * The Currency Manager, which manages all loaded currencies, and their respective tiers.
   */
  private CurrencyManager currencyManager;

  /**
   * Used to track active sessions from the REST API located in the web module, may be used by 3rd party APIs/other modules
   * to create a secure session using an account's pin.
   */
  private SessionManager sessionManager;

  /**
   * The {@link TransactionManager transaction manager}, which manages all transactions performed on the server.
   */
  private TransactionManager transactionManager;

  private TreeMap<Integer, List<HoldingsHandler>> holdingsHandlers = new TreeMap<>();

  public static List<UUID> reset = new ArrayList<>();

  private List<UUID> expGain = new ArrayList<>();

  public EconomyManager() {
    this.accounts.setListener(new AccountListener());
    currencyManager = new CurrencyManager();
    sessionManager = new SessionManager();
    transactionManager = new TransactionManager();
    registerHandler(new CoreHoldingsHandler());
    registerHandler(new EChestHandler());
  }

  public void registerHandler(HoldingsHandler handler) {
    List<HoldingsHandler> handlers = holdingsHandlers.getOrDefault(handler.priority(), new ArrayList<>());
    handlers.add(handler);
    holdingsHandlers.put(handler.priority(), handlers);
  }

  public void addDead(UUID id) {
    dead.add(id);
  }

  public void removeDead(UUID id) {
    dead.remove(id);
  }

  public boolean isDead(UUID id) {
    return dead.contains(id);
  }

  public CurrencyManager currencyManager() {
    return currencyManager;
  }

  public SessionManager sessionManager() {
    return sessionManager;
  }

  public TransactionManager transactionManager() {
    return transactionManager;
  }

  public boolean exists(UUID id) {
    return accounts.containsKey(id);
  }

  public void addAccount(TNEAccount account) {
    accounts.put(account.identifier(), account);
    TNE.instance().getUuidManager().addUUID(account.displayName(), account.identifier());
  }

  public TNEAccount getAccount(UUID id) {
    if(!exists(id) && !createAccount(id, IDFinder.getUsername(id.toString()))) {
      return null;
    }
    return accounts.get(id);
  }

  public void removeAccount(UUID id) {
    accounts.remove(id, false);
  }

  public boolean deleteAccount(UUID id) {
    if(exists(id)) {
      accounts.remove(id);
      return true;
    }
    return false;
  }

  public boolean createAccount(UUID id, String displayName) {
    TNE.debug("Creating account for " + displayName + " with ID of " + id.toString());
    TNE.debug("=====START EconomyManager.createAccount =====");
    TNE.debug("UUID: " + id.toString());
    TNEAccount account = new TNEAccount(id, displayName);
    account.setPlayerAccount(!TNE.instance().special.contains(id));

    TNEAccountCreationEvent event = new TNEAccountCreationEvent(id, account, !Bukkit.getServer().isPrimaryThread());
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(event.isCancelled()) {
      return false;
    }
    TNE.instance().getUuidManager().addUUID(displayName, id);
    accounts.put(id, event.getAccount());
    TNE.debug("=====END EconomyManager.createAccount =====");
    return true;
  }

  public boolean isXPGain(UUID id) {
    return expGain.contains(id);
  }

  public void addXPGain(UUID id) {
    expGain.add(id);
  }

  public void removeXPGain(UUID id) {
    expGain.remove(id);
  }

  public EventMap<UUID, TNEAccount> getAccounts() {
    return accounts;
  }

  public TreeMap<Integer, List<HoldingsHandler>> getHoldingsHandlers() {
    return holdingsHandlers;
  }

  public void purge(String world) {
    /*Iterator<TNEAccount> it = accounts.values().iterator();

    while(it.hasNext()) {
      TNEAccount account = it.next();
      boolean remove = true;

      for(Map.Entry<String, BigDecimal> balance : account.getWorldHoldings(world).getHoldings().entrySet()) {
        if(!balance.getValue().equals(TNE.manager().currencyManager().get(world, balance.getKey()).defaultBalance())) {
          remove = false;
        }
      }

      if(remove) {
        TNE.manager().getAccounts().remove(account.identifier(), false);
        TNE.uuidManager().remove(account.displayName());
        try {
          TNE.saveManager().getTNEManager().getTNEProvider().deleteAccount(account.identifier());
        } catch (SQLException e) {
          TNE.debug(e);
        }
        it.remove();
      }
    }*/
  }

  public Collection<TNEAccount> parsePlayerArgument(PlayerProvider provider, String argument) {
    return parsePlayerArgument(provider, argument, false);
  }

  public Collection<TNEAccount> parsePlayerArgument(PlayerProvider provider, String argument, boolean existing) {
    TNE.debug("EconomyManager.parsePlayerArgument: " + argument);
    argument = argument.trim();
    if(argument.equalsIgnoreCase("all") || argument.equalsIgnoreCase("*")) return getAccounts().values();

    if(argument.contains("@a") || argument.contains("@p") || argument.contains("@r")) {
      argument = new PlayerParser().parse(provider, argument);
    }

    List<TNEAccount> accounts = new ArrayList<>();

    if(argument.equalsIgnoreCase("online")) {
      for(Player player : Bukkit.getOnlinePlayers()) {
        accounts.add(getAccount(player.getUniqueId()));
      }
    }

    if(argument.contains(",")) {
      String[] names = argument.split(",");

      for(String name : names) {
        UUID id = IDFinder.getID(name.trim());

        if(exists(id)) accounts.add(getAccount(id));
      }
      return accounts;
    }
    UUID id = IDFinder.getID(argument);
    if(existing && !exists(id)) return null;
    TNEAccount account = TNE.instance().api().getOrCreate(id);
    if(account != null) {
      accounts.add(getAccount(id));
      return accounts;
    }
    return null;
  }
}