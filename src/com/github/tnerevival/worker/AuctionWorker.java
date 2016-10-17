package com.github.tnerevival.worker;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.auction.Auction;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Created by Daniel on 10/17/2016.
 */
public class AuctionWorker extends BukkitRunnable {

  private TNE plugin;

  public AuctionWorker(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    List<World> worlds = TNE.instance.getServer().getWorlds();
    for(int i = 0; i >= worlds.size(); i++) {
      String world = (i == worlds.size())? "Global" : worlds.get(i).getName();
      if(plugin.manager.auctionManager.hasActive(world)) {
        Integer notificationTime = 10;
        Boolean countdown = true;
        Integer countdownStart = 10;

        Auction auction = plugin.manager.auctionManager.getActive(world);
        Integer remaining = auction.remaining();
        if(remaining > 0) {
          if((auction.getTime() - remaining) % notificationTime == 0 || remaining <= countdownStart && countdown) {
            plugin.manager.auctionManager.notifyPlayers(world);
          }
        } else {
          plugin.manager.auctionManager.end(world);
        }
      } else {
        plugin.manager.auctionManager.startNext(world);
      }
    }
  }
}
