package com.github.tnerevival.worker;

import java.util.Calendar;
import java.util.Iterator;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.tnerevival.TNE;
import com.github.tnerevival.lottery.Lottery;

public class LotteryWorker extends BukkitRunnable {
	
	private TNE plugin;
	
	public LotteryWorker(TNE plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		Iterator<String> it = plugin.manager.lotteries.keySet().iterator();
		while(it.hasNext()) {
			Lottery l = plugin.manager.lotteries.get(it.next());
			long timeDif = (((long)Calendar.getInstance().get(Calendar.MILLISECOND)) - l.getStart()) / 1000;
			
			if(timeDif >= l.getLength()) {
				l.chooseWinner();
				it.remove();
				return;
			}
			
			if(timeDif % l.getMessage() == 0) {
				l.reminder();
			}
		}
	}
}