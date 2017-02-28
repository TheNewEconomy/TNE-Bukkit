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
import com.github.tnerevival.core.collection.MapListener;
import com.github.tnerevival.core.shops.Shop;

import java.util.*;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class ShopsListener implements MapListener {
  Map<String, Shop> changed = new HashMap<>();

  @Override
  public void update() {
    for(Shop s : changed.values()) {
      TNE.instance().saveManager.versionInstance.saveShop(s);
    }
  }

  @Override
  public Map<String, Shop> changed() {
    return changed;
  }

  @Override
  public void clearChanged() {
    changed.clear();
  }

  @Override
  public void put(Object key, Object value) {
    TNE.instance().saveManager.versionInstance.saveShop((Shop)value);
  }

  @Override
  public Object get(Object key) {
    String[] values = ((String)key).split(":");
    return TNE.instance().saveManager.versionInstance.loadShop(values[0], values[1]);
  }

  @Override
  public Collection<Shop> values() {
    return TNE.instance().saveManager.versionInstance.loadShops();
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
    TNE.instance().saveManager.versionInstance.deleteShop((Shop)value);
  }

  @Override
  public Set<String> keySet() {
    Set<String> keys = new HashSet<>();

    for(Shop shop : values()) {
      keys.add(shop.getName() + ":" + shop.getWorld());
    }

    return keys;
  }

  @Override
  public Set<Map.Entry<String, Shop>> entrySet() {
    Map<String, Shop> shops = new HashMap<>();

    for(Shop shop : values()) {
      shops.put(shop.getName() + ":" + shop.getWorld(), shop);
    }

    return shops.entrySet();
  }

  @Override
  public void remove(Object key) {

  }
}