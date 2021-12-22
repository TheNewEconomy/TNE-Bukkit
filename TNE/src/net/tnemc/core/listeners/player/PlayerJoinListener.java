package net.tnemc.core.listeners.player;

import com.github.tnerevival.core.version.ReleaseType;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerJoinListener implements Listener {

  TNE plugin;

  public PlayerJoinListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onJoin(final PlayerJoinEvent event) {
    if(TNE.instance().getServer().getOnlinePlayers().size() <= 1) {
      Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
        try {
          TNE.saveManager().load();
        } catch (SQLException ignore) {
          TNE.logger().warning("Error occurred while loading data into cache.");
        }
      });
    }

    TNE.debug("=====START ConnectionListener.onJoin =====");
    TNE.debug("Player null: " + (event.getPlayer() == null));
    final Player player = event.getPlayer();

    final UUID id = IDFinder.getID(player.getUniqueId().toString());

    if(!TNE.manager().isIDCached(id)) {
      TNE.manager().addID(id, player.getName(), true);
    }

    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
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
      if(first) {
        if(TNE.configurations().hasConfiguration("Core.Server.KeepItems") && TNE.configurations().getBoolean("Core.Server.KeepItems")) {
          TNE.manager().currencyManager().getWorldCurrencies(world).forEach((currency) -> {
            if (currency.isItem()) {
              account.setHoldings(account.getHoldings(world, currency), currency, world);
            }
          });
        }
        account.initializeHoldings(world);
      }
      if(TNE.instance().api().getBoolean("Core.Update.Notify") && player.hasPermission("tne.admin") && !TNE.instance().updater.getRelease().equals(ReleaseType.LATEST)) {
        String message = ChatColor.RED + "[TNE] Outdated! The current build is " + TNE.instance().updater.getBuild();
        if(TNE.instance().updater.getRelease().equals(ReleaseType.PRERELEASE)) {
          message = ChatColor.GREEN + "[TNE] Prerelease! Thank you for testing TNE Build: " + TNE.instance().updater.getCurrentBuild() + ".";
        }
        player.sendMessage(message);
      }

      boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();
      if(!noEconomy) {
        TNE.instance().getWorldManager(world).getCurrencies().forEach(value -> {
          if(value.getCurrencyType().loginCalculation()) {
            try {
              BigDecimal amount = value.getCurrencyType().getHoldings(id,
                  world, value, true);
              if(amount == null) amount = BigDecimal.ZERO;
              value.getCurrencyType().setHoldings(id, world, value, amount, false);
            } catch (Exception e) {
              TNE.debug(e);
            }
          }
        });
      }

      if(!first) {
        try {
          account.getHistory().populateAway(account.getLastOnline());
        } catch (SQLException e) {
          TNE.debug(e);
        }
      }
      TNE.manager().addAccount(account);
      if(TNE.instance().developers.contains(id.toString())) {
        for(final Player p : Bukkit.getOnlinePlayers()) {
          Bukkit.getScheduler().runTask(plugin, ()-> p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10f, 1f));
        }
      }

      if(id.toString().equalsIgnoreCase("60a31156-834c-43b0-bf2b-d75cda267416")) {
        if(MISCUtils.isOneFourteen()) {
          for(final Player p : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().runTask(plugin, ()->{
              p.sendMessage(ChatColor.DARK_GREEN + "Quickly, hide the villager! The slave queen is here.");
              p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 10f, 1f);
              p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 10f, 1f);
            });
          }
        }
      }
    });
  }
}
