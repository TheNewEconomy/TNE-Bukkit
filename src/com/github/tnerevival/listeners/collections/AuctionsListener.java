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
package com.github.tnerevival.listeners.collections;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.collection.MapListener;

import java.util.*;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class AuctionsListener implements MapListener {
  Map<Integer, Auction> changed = new HashMap<>();

  @Override
  public void update() {
    for(Auction auction : changed.values()) {
      TNE.instance().saveManager.versionInstance.saveAuction(auction);
    }
  }

  @Override
  public Map<Integer, Auction> changed() {
    return changed;
  }

  @Override
  public void clearChanged() {
    changed.clear();
  }

  @Override
  public void put(Object key, Object value) {
    TNE.instance().saveManager.versionInstance.saveAuction((Auction)value);
  }

  @Override
  public Object get(Object key) {
    return TNE.instance().saveManager.versionInstance.loadAuction((Integer)key);
  }

  @Override
  public Collection<Auction> values() {
    return TNE.instance().saveManager.versionInstance.loadAuctions();
  }

  @Override
  public int size() {
    return values().size();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  @Override
  public boolean containsValue(Object value) {
    return false;
  }

  @Override
  public void preRemove(Object key, Object value) {
    TNE.instance().saveManager.versionInstance.deleteAuction((Auction)value);
  }

  @Override
  public Set<Integer> keySet() {
    Set<Integer> keys = new HashSet<>();

    for(Auction auction : values()) {
      keys.add(auction.getLotNumber());
    }

    return keys;
  }

  @Override
  public Set<Map.Entry<Integer, Auction>> entrySet() {
    Map<Integer, Auction> auctionMap = new HashMap<>();

    for(Auction auction : values()) {
      auctionMap.put(auction.getLotNumber(), auction);
    }

    return auctionMap.entrySet();
  }

  @Override
  public void remove(Object key) {

  }
}