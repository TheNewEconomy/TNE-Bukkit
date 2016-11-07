package com.github.tnerevival.worker;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InventoryTimeWorker extends BukkitRunnable {

  private TNE plugin;

  public InventoryTimeWorker(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {

    Iterator<Entry<UUID, Integer>> it = plugin.inventoryManager.accessing.entrySet().iterator();
    while(it.hasNext()) {
      Entry<UUID, Integer> entry = it.next();

      Player player = MISCUtils.getPlayer(entry.getKey());

      Account acc = AccountUtils.getAccount(entry.getKey());
      InventoryType invType = plugin.inventoryManager.getViewing(entry.getKey()).getInventory().getType();
      String type = TNE.configurations.getObjectConfiguration().inventoryType(invType);

      if(TNE.configurations.getObjectConfiguration().isTimed(invType, MISCUtils.getWorld(player), IDFinder.getID(player).toString())) {
        long timeRemaining = (int) (acc.getTimeLeft(MISCUtils.getWorld(MISCUtils.getPlayer(entry.getKey())), type) - TimeUnit.SECONDS.convert(System.nanoTime() - plugin.inventoryManager.getViewer(entry.getKey()).getOpened(), TimeUnit.NANOSECONDS));

        String message = "Messages.Inventory.NoTime";

        if (timeRemaining <= 0) {
          acc.setTime(MISCUtils.getWorld(MISCUtils.getPlayer(entry.getKey())), type, 0);
          it.remove();
          MISCUtils.getPlayer(entry.getKey()).closeInventory();
          Message m = new Message(message);
          m.addVariable("$type", type);
          m.translate(MISCUtils.getWorld(player), player);
          continue;
        }

        if (plugin.inventoryManager.getViewer(entry.getKey()).willClose()) {
          long timeUsed = acc.getTimeLeft(MISCUtils.getWorld(MISCUtils.getPlayer(entry.getKey())), type) - timeRemaining;
          message = "Messages.Inventory.TimeRemoved";
          Message m = new Message(message);
          m.addVariable("$type", type);
          m.addVariable("$amount", timeUsed + ((timeUsed == 1) ? " second" : " seconds"));
          m.translate(MISCUtils.getWorld(player), player);
          acc.setTime(MISCUtils.getWorld(MISCUtils.getPlayer(entry.getKey())), type, timeRemaining);
          it.remove();
        }
      }
    }
  }
}