/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.worker;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.collection.EventList;
import com.github.tnerevival.core.collection.EventMap;
import com.github.tnerevival.core.transaction.TransactionHistory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by creatorfromhell on 1/15/2017.
 **/
public class CacheWorker extends BukkitRunnable {

  private TNE plugin;

  public List<EventList> cacheLists = new ArrayList<>();
  public List<EventMap> cacheMaps = new ArrayList<>();

  public CacheWorker(TNE plugin) {
    this.plugin = plugin;

    for(TransactionHistory history : plugin.manager.transactions.transactionHistory.values()) {
      cacheLists.add(history.raw());
    }
    cacheLists.add(plugin.manager.auctionManager.unclaimed);

    cacheMaps.add(plugin.manager.accounts);
    cacheMaps.add(plugin.manager.signs);
    cacheMaps.add(plugin.manager.shops);
    cacheMaps.add(plugin.manager.ecoIDs);
    cacheMaps.add(plugin.manager.auctionManager.auctionQueue);
  }

  @Override
  public void run() {
    for(EventList list : cacheLists) {
      list.update();
    }

    for(EventMap map : cacheMaps) {
      map.update();
    }
  }
}