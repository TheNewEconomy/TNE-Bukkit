package com.github.tnerevival.worker;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.InventoryTimeTracking;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InventoryTimeWorker extends BukkitRunnable {

  private TNE plugin;

  public InventoryTimeWorker(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {

    Iterator<Map.Entry<UUID, InventoryTimeTracking>> iterator = plugin.inventoryManager.inventoryTime.entrySet().iterator();
    while(iterator.hasNext()) {
      Map.Entry<UUID, InventoryTimeTracking> entry = iterator.next();
      InventoryTimeTracking tracking = entry.getValue();
      UUID id = tracking.getPlayer();
      Player player = IDFinder.getPlayer(id.toString());
      Account account = AccountUtils.getAccount(id);
      String type = TNE.configurations.getObjectConfiguration().inventoryType(tracking.getType());
      long timeRemaining = (int) (account.getTimeLeft(IDFinder.getWorld(tracking.getPlayer()), type) - TimeUnit.SECONDS.convert(System.nanoTime() - tracking.getOpened(), TimeUnit.NANOSECONDS));

      if (timeRemaining <= 0) {
        account.setTime(IDFinder.getWorld(id), type, 0);
        iterator.remove();
        IDFinder.getPlayer(id.toString()).closeInventory();
        Message m = new Message("Messages.Inventory.NoTime");
        m.addVariable("$type", type);
        m.translate(IDFinder.getWorld(player), player);
        continue;
      }

      if(tracking.isClosed()) {
        long timeUsed = account.getTimeLeft(IDFinder.getWorld(id), type) - timeRemaining;
        Message m = new Message("Messages.Inventory.TimeRemoved");
        m.addVariable("$type", type);
        m.addVariable("$amount", timeUsed + ((timeUsed == 1) ? " second" : " seconds"));
        m.translate(IDFinder.getWorld(id), player);
        account.setTime(IDFinder.getWorld(id), type, timeRemaining);
        iterator.remove();
      }
      plugin.manager.accounts.put(account.getUid(), account);
    }
  }
}