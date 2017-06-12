package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.version.ReleaseType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ConnectionListener implements Listener {

  TNE plugin;

  public ConnectionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    Player player = event.getPlayer();
    MISCUtils.debug(IDFinder.getID(player) + "");
    if(!plugin.manager.accounts.containsKey(IDFinder.getID(player))) {
      AccountUtils.createAccount(IDFinder.getID(player));
    }
    if(TNE.instance().api().getBoolean("Core.Update.Notify") && player.hasPermission("tne.admin") && !TNE.updater.getRelease().equals(ReleaseType.LATEST)) {
      String message = ChatColor.RED + "[TNE] Outdated! The current build is " + TNE.updater.getLatestBuild();
      if(TNE.updater.getRelease().equals(ReleaseType.PRERELEASE)) {
        message = ChatColor.GREEN + "[TNE] Prerelease! Thank you for testing TNE Build: " + TNE.updater.getCurrentBuild() + ".";
      }
      player.sendMessage(message);
    }

    Account account = AccountUtils.getAccount(IDFinder.getID(player));

    if(!MISCUtils.ecoDisabled(IDFinder.getWorld(player)) && TNE.instance().manager.enabled(IDFinder.getID(player), IDFinder.getWorld(player))) {
      if(!TNE.instance().manager.confirmed(IDFinder.getID(player), IDFinder.getWorld(player))) {
        String node = "Messages.Account.Confirm";
        if (account.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
          node = "Messages.Account.Set";
        }

        Message message = new Message(node);
        message.translate(IDFinder.getWorld(player), player);
      }
    }
  }

  @EventHandler
  public void onLeave(final PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if(AccountUtils.exists(IDFinder.getID(player))) {
      Account account = AccountUtils.getAccount(IDFinder.getID(player));
      account.setLastOnline(new Date().getTime());
      TNE.instance().manager.accounts.put(account.getUid(), account);
    }
    TNE.instance().manager.confirmed.remove(IDFinder.getID(player));
  }

  @EventHandler
  public void onDeath(final PlayerDeathEvent event) {
    Player killed = event.getEntity();
    String world = IDFinder.getWorld(killed);
    UUID id = IDFinder.getID(killed);
    if(TNE.instance().api().getBoolean("Core.Death.Lose", world, id)) {
      AccountUtils.setFunds(id, world, BigDecimal.ZERO, TNE.instance().manager.currencyManager.get(world).getName());
    }

    if(TNE.instance().api().getInteger("Core.Death.Vault.Drop", world, id) > 0) {
      if(!TNE.instance().api().getBoolean("Core.Death.Vault.PlayerOnly", world, id) || killed.getKiller() != null) {
        if(AccountUtils.getAccount(id).hasVault(world)) {
          Vault vault = AccountUtils.getAccount(id).getVault(world);
          List<Integer> toDrop = vault.generateSlots(world);
          for (Integer i : toDrop) {
            if (vault.getItem(i) != null || !vault.getItem(i).toItemStack().getType().equals(Material.AIR)) {
              ItemStack drop = vault.getItem(i).toItemStack();
              event.getDrops().add(drop);
              vault.removeItem(i);
            }
          }
        }
      }
    }
  }
}