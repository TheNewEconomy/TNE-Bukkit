package net.tnemc.core.listeners;

import com.github.tnerevival.core.version.ReleaseType;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldManager;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChannelEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/8/2017.
 */
public class ConnectionListener implements Listener {

  TNE plugin;

  public ConnectionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPreJoin(AsyncPlayerPreLoginEvent event) {
    if(TNE.isDuper(event.getUniqueId().toString()) || TNE.isDuper(event.getAddress().getHostAddress())) {
      event.setKickMessage(ChatColor.RED + "The New Economy Global Ban: You've been identified as a known currency duper. Appeal at http://discord.tnemc.net");
      event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChannel(final PlayerChannelEvent event) {
    if(TNE.useMod) {
      if (event.getChannel().equalsIgnoreCase("tnemod")) {
        TNE.instance().addModUser(event.getPlayer().getUniqueId());
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onJoin(final PlayerJoinEvent event) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      TNE.debug("=====START ConnectionListener.onJoin =====");
      TNE.debug("Player null: " + (event.getPlayer() == null));
      final Player player = event.getPlayer();

      final UUID id = IDFinder.getID(player);
      final String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);
      TNE.debug(id + "");
      boolean first = !TNE.manager().exists(id);
      TNEAccount account;

      if(first) {
        if(!TNE.manager().createAccount(id, player.getName())) {
          TNE.debug("Unable to create player account for " + player.getName());
        }
      }
      account = TNE.manager().getAccount(id);

      if (!first) {
        if(!account.displayName().equals(player.getName())) {
          account.setDisplayName(player.getName());
          try {
            TNE.saveManager().getTNEManager().getTNEProvider().removeID(id);
            TNE.saveManager().getTNEManager().getTNEProvider().saveID(player.getName(), id);
          } catch (SQLException e) {
            TNE.debug("Something went wrong while trying to update the player's display name.");
          }
          //TNE.instance().getUuidManager().addUUID(player.getName(), id);
        }
        /*if(player.isDead()) {
          TNE.manager().addDead(player.getUniqueId());
          return;
        }*/
      }

      TNE.manager().addAccount(account);
      if(first) account.initializeHoldings(world);
      if(TNE.instance().api().getBoolean("Core.Update.Notify") && player.hasPermission("tne.admin") && !TNE.instance().updater.getRelease().equals(ReleaseType.LATEST)) {
        String message = ChatColor.RED + "[TNE] Outdated! The current build is " + TNE.instance().updater.getBuild();
        if(TNE.instance().updater.getRelease().equals(ReleaseType.PRERELEASE)) {
          message = ChatColor.GREEN + "[TNE] Prerelease! Thank you for testing TNE Build: " + TNE.instance().updater.getCurrentBuild() + ".";
        }
        player.sendMessage(message);
      }

      boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();
      if(!noEconomy) {
        TNE.instance().getWorldManager(world).getItemCurrencies().forEach(value -> {
          ItemCalculations.setItems(TNE.manager().currencyManager().get(world, value),
              account.getHoldings(world, value, true, true), player.getInventory(), false);
        });
      }

      if(!first) {
        try {
          account.getHistory().populateAway(account.getLastOnline());
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      TNE.manager().addAccount(account);
    });
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onQuit(final PlayerQuitEvent event) {
    final UUID id = event.getPlayer().getUniqueId();
    final Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      if(TNE.manager().exists(id)) {
        TNEAccount account = TNEAccount.getAccount(id.toString());
        if(player == null) TNE.debug("Player is null");
        account.saveItemCurrency(WorldFinder.getWorld(player, WorldVariant.BALANCE), true, player.getInventory());
        account.setLastOnline(new Date().getTime());
        account.getHistory().clearAway();
        TNE.manager().addAccount(account);
        TNE.manager().removeAccount(id);
      }
    });
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTeleport(final PlayerTeleportEvent event) {
    String fromWorld = event.getFrom().getWorld().getName();
    fromWorld = TNE.instance().getWorldManager(fromWorld).getBalanceWorld();
    String toWorld = event.getTo().getWorld().getName();
    toWorld = TNE.instance().getWorldManager(toWorld).getBalanceWorld();

    if(!fromWorld.equals(toWorld) && TNE.instance().api().getBoolean("Core.Multiworld")) {
      TNE.manager().getAccount(IDFinder.getID(event.getPlayer())).saveItemCurrency(fromWorld);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldChange(final PlayerChangedWorldEvent event) {
    final Player player = event.getPlayer();
    final UUID id = IDFinder.getID(player);
    final TNEAccount account = TNE.manager().getAccount(id);
    final String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);

    boolean noEconomy = TNE.instance().getWorldManager(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION)) == null || TNE.instance().getWorldManager(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION)).isEconomyDisabled();
    if(!noEconomy && TNE.instance().api().getBoolean("Core.World.EnableChangeFee", WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), id.toString())) {
      if(!player.hasPermission("tne.bypass.world")) {
        WorldManager manager = TNE.instance().getWorldManager(world);
        TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("worldchange"));
        transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, manager.getChangeFeeCurrency()), manager.getChangeFee()));
        TransactionResult result = TNE.transactionManager().perform(transaction);
        if(!result.proceed()) {
          player.teleport(event.getFrom().getSpawnLocation());
        }
        Message message = new Message(result.recipientMessage());
        final String balanceWorld = WorldFinder.getWorld(player, WorldVariant.BALANCE);
        message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(balanceWorld), balanceWorld, manager.getChangeFee(), player.getUniqueId().toString()));
        message.translate(world, player);
      }
      account.initializeHoldings(world);
    } else if(!noEconomy && !TNE.instance().api().getBoolean("Core.World.EnableChangeFee", WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), id.toString())) {
      account.initializeHoldings(world);
    }

    if(!noEconomy && TNE.instance().api().getBoolean("Core.Multiworld") &&
        !TNE.instance().getWorldManager(event.getFrom().getName()).getBalanceWorld().equalsIgnoreCase(world)) {
      TNE.instance().getWorldManager(world).getItemCurrencies().forEach(value -> {
        ItemCalculations.setItems(TNE.manager().currencyManager().get(world, value),
            account.getHoldings(world, value, true, true), player.getInventory(), false);
      });
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldLoad(final WorldLoadEvent event) {
    String world = event.getWorld().getName();
    TNE.manager().currencyManager().initializeWorld(world);
  }

  /*@EventHandler(priority = EventPriority.HIGHEST)
  public void onRespawn(final PlayerRespawnEvent event) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      final Player player = event.getPlayer();
      final UUID id = event.getPlayer().getUniqueId();
      if(TNE.manager().isDead(id)) {
        TNE.manager().removeDead(id);
        final String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);

        TNEAccount account = TNE.manager().getAccount(id);

        boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();
        if(!noEconomy) {
          TNE.instance().getWorldManager(world).getItemCurrencies().forEach(value -> {
            ItemCalculations.setItems(TNE.manager().currencyManager().get(world, value),
                account.getHoldings(world, value, true, true), player.getInventory(), false);
          });
        }
      }
    });
  }*/
}