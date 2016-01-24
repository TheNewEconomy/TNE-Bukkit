package com.github.tnerevival.worker;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.inventory.View;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class InventoryTimeWorker extends BukkitRunnable {
	
	private TNE plugin;
	
	public InventoryTimeWorker(TNE plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		Iterator<Entry<UUID, View>> it = plugin.manager.viewers.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<UUID, View> entry = it.next();
			
			Account acc = AccountUtils.getAccount(entry.getKey());
			String type = TNE.configurations.getObjectConfiguration().inventoryType(entry.getValue().getType());
			long timeRemaining = (int) (acc.getTimeLeft(MISCUtils.getWorld(MISCUtils.getPlayer(entry.getKey())), type) - TimeUnit.SECONDS.convert(System.nanoTime() - entry.getValue().getOpened(), TimeUnit.NANOSECONDS));
			
			String message = "Messages.Inventory.NoTime";
			
			if(timeRemaining <= 0) {
				acc.setTime(MISCUtils.getWorld(MISCUtils.getPlayer(entry.getKey())), type, 0);
				it.remove();
				MISCUtils.getPlayer(entry.getKey()).closeInventory();
				Message m = new Message(message);
				m.addVariable("$type", type);
				MISCUtils.getPlayer(entry.getKey()).sendMessage(m.translate());
				continue;
			}
			
			if(entry.getValue().close()) {
				long timeUsed = acc.getTimeLeft(MISCUtils.getWorld(MISCUtils.getPlayer(entry.getKey())), type) - timeRemaining;
				message = "Messages.Inventory.TimeRemoved";
				Message m = new Message(message);
				m.addVariable("$type", type);
				m.addVariable("$amount", timeUsed + ((timeUsed == 1 )? " second" : " seconds"));
				MISCUtils.getPlayer(entry.getKey()).sendMessage(m.translate());
				acc.setTime(MISCUtils.getWorld(MISCUtils.getPlayer(entry.getKey())), type, timeRemaining);
				it.remove();
			}
		}
	}
}