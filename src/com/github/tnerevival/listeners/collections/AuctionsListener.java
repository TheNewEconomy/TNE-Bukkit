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

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class AuctionsListener implements MapListener {

  @Override
  public void add(Object key, Object value) {
    TNE.instance.saveManager.versionInstance.saveAuction((Auction)value);
  }

  @Override
  public Object get(Object key) {
    return null;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean containsKey(Object key) {
    return false;
  }

  @Override
  public void preRemove(Object key, Object value) {
    Integer lot = (Integer)key;

    if(!TNE.instance.manager.auctionManager.exists(lot)) {
      TNE.instance.saveManager.versionInstance.deleteAuction((Auction)value);
    }
  }

  @Override
  public void remove(Object key) {

  }
}