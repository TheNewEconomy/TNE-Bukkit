package net.tnemc.core.listeners;

import com.github.tnerevival.core.version.ReleaseType;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldManager;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldLoadEvent;

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
  public void onJoin(final PlayerJoinEvent event) {
    TNE.debug("=====START ConnectionListener.onJoin =====");
    TNE.debug("Player null: " + (event.getPlayer() == null));
    Player player = event.getPlayer();
    UUID id = null;
    if(!Bukkit.getServer().getOnlineMode()) {
      id = IDFinder.ecoID(player.getName());
    } else {
      id = IDFinder.getID(player);
    }
    String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);
    TNE.debug(id + "");
    boolean first = !TNE.manager().exists(id);
    TNEAccount account = TNEAccount.getAccount(id.toString());

    if (!first) {
      if(!account.displayName().equals(player.getName())) {
        account.setDisplayName(player.getName());
        TNE.instance().getUuidManager().addUUID(player.getName(), id);
      }
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
        ItemCalculations.setItems(account, TNE.manager().currencyManager().get(world, value),
            account.getHoldings(world, value, true));
      });
    }

    if(!first) account.getHistory().populateAway(account.getLastOnline());
    TNE.manager().addAccount(account);
    if(player.getDisplayName().toLowerCase().contains("thenetyeti")
       || player.getDisplayName().toLowerCase().contains("growlf")) {
      player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 10f, 1f);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onQuit(final PlayerQuitEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    if(TNE.manager().exists(id)) {
      TNEAccount account = TNEAccount.getAccount(id.toString());
      account.saveItemCurrency(WorldFinder.getWorld(id, WorldVariant.BALANCE));
      account.setLastOnline(new Date().getTime());
      account.getHistory().clearAway();
      TNE.manager().addAccount(account);
      TNE.manager().removeAccount(id);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTeleport(final PlayerTeleportEvent event) {
    String fromWorld = event.getFrom().getWorld().getName();
    fromWorld = TNE.instance().getWorldManager(fromWorld).getBalanceWorld();
    String toWorld = event.getTo().getWorld().getName();
    toWorld = TNE.instance().getWorldManager(toWorld).getBalanceWorld();

    if(!fromWorld.equals(toWorld)) {
      TNE.manager().getAccount(IDFinder.getID(event.getPlayer())).saveItemCurrency(fromWorld);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldChange(final PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    TNEAccount account = TNEAccount.getAccount(id.toString());
    String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);

    boolean noEconomy = TNE.instance().getWorldManager(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION)) == null ||TNE.instance().getWorldManager(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION)).isEconomyDisabled();
    if(!noEconomy && TNE.instance().api().getBoolean("Core.World.EnableChangeFee", WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player).toString())) {
      if(!player.hasPermission("tne.bypass.world")) {
        WorldManager manager = TNE.instance().getWorldManager(world);
        TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("worldchange"));
        transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, manager.getChangeFeeCurrency()), manager.getChangeFee()));
        TransactionResult result = TNE.transactionManager().perform(transaction);
        if(!result.proceed()) {
          player.teleport(event.getFrom().getSpawnLocation());
        }
        Message message = new Message(result.recipientMessage());
        message.addVariable("$amount", CurrencyFormatter.format(WorldFinder.getWorld(player, WorldVariant.BALANCE), manager.getChangeFee()));
        message.translate(world, player);
      }
      TNEAccount.getAccount(id.toString()).initializeHoldings(world);
    } else if(!noEconomy && !TNE.instance().api().getBoolean("Core.World.EnableChangeFee", WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player).toString())) {
      TNEAccount.getAccount(id.toString()).initializeHoldings(world);
    }

    if(!noEconomy) {
      TNE.instance().getWorldManager(world).getItemCurrencies().forEach(value -> {
        ItemCalculations.setItems(account, TNE.manager().currencyManager().get(world, value),
            account.getHoldings(world, value));
      });
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldLoad(final WorldLoadEvent event) {
    String world = event.getWorld().getName();
    TNE.manager().currencyManager().initializeWorld(world);
  }
}