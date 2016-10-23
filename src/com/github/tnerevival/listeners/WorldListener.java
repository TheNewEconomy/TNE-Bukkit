package com.github.tnerevival.listeners;

import com.github.tnerevival.core.transaction.TransactionType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class WorldListener implements Listener {

  TNE plugin;

  public WorldListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onWorldChange(PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    String world = player.getWorld().getName();

    if(TNE.instance.api.getBoolean("Core.World.EnableChangeFee", world, MISCUtils.getID(player).toString())) {
      if(!player.hasPermission("tne.bypass.world")) {
        if(AccountUtils.transaction(MISCUtils.getID(player).toString(), null, AccountUtils.getWorldCost(world), TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(player))) {
          AccountUtils.transaction(MISCUtils.getID(player).toString(), null, AccountUtils.getWorldCost(world), TransactionType.MONEY_REMOVE, MISCUtils.getWorld(player));
          AccountUtils.initializeWorldData(MISCUtils.getID(player), world);
          Message change = new Message("Messages.World.Change");
          change.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.getWorldCost(world)));
          change.translate(world, player);
        } else {
          player.teleport(event.getFrom().getSpawnLocation());
          Message changeFailed = new Message("Messages.World.ChangeFailed");
          changeFailed.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.getWorldCost(world)));
          changeFailed.translate(world, player);
        }
      } else {
        AccountUtils.initializeWorldData(MISCUtils.getID(player), world);
      }
    } else {
      AccountUtils.initializeWorldData(MISCUtils.getID(player), world);
    }
  }
}