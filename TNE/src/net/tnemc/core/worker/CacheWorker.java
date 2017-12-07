package net.tnemc.core.worker;

import com.github.tnerevival.core.collection.EventList;
import com.github.tnerevival.core.collection.EventMap;
import net.tnemc.core.TNE;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 8/3/2017.
 */
public class CacheWorker extends BukkitRunnable {

  private TNE plugin;

  public List<EventList> cacheLists = new ArrayList<>();
  public List<EventMap> cacheMaps = new ArrayList<>();

  public CacheWorker(TNE plugin, List<EventList> cacheLists, List<EventMap> cacheMaps) {
    this.plugin = plugin;
    this.cacheLists = cacheLists;
    this.cacheMaps = cacheMaps;
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