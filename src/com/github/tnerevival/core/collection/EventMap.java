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
package com.github.tnerevival.core.collection;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.MISCUtils;

import java.util.*;

/**
 * Created by creatorfromhell on 11/2/2016.
 **/
public class EventMap<K, V> extends HashMap<K, V> {

  private MapListener<K, V> listener;
  private Map<K, V> map = new HashMap<>();
  private long lastRefresh = new Date().getTime();

  public EventMap() {
    super();
  }

  public EventMap(MapListener listener) {
    super();
    this.listener = listener;
  }

  public void update() {
    listener.update();
    listener.clearChanged();
    lastRefresh = new Date().getTime();
  }

  @Override
  public V get(Object key) {
    MISCUtils.debug("EventMap.get");
    if(TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") || TNE.instance.saveManager.cache) {
      return map.get(key);
    }

    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile")) {
      if(!TNE.instance.saveManager.cache || !map.containsKey(key)) {
        return listener.get(key);
      }
    }
    return map.get(key);
  }

  @Override
  public V put(K key, V value) {
    MISCUtils.debug("EventMap.put");
    return put(key, value, false);
  }

  public V put(K key, V value, boolean skip) {
    MISCUtils.debug("EventMap.put(skip)");
    if(TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") || TNE.instance.saveManager.cache) {
      map.put(key, value);
    }

    if(!skip) {
      if (!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile")) {
        if (!TNE.instance.saveManager.cache || !map.containsKey(key)) {
          listener.put(key, value);
        }

        if(TNE.instance.saveManager.cache && map.containsKey(key)) {
          listener.changed().put(key, value);
        }
      }
    }
    return value;
  }

  @Override
  public V remove(Object key) {
    MISCUtils.debug("EventMap.remove");
    return remove(key, true);
  }

  public V remove(Object key, boolean database) {
    MISCUtils.debug("EventMap.remove(database)");
    V removed = get(key);
    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") && database) {
      listener.preRemove(key, removed);
    }

    if(TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") || TNE.instance.saveManager.cache) {
      removed = map.remove(key);
    }

    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") && !TNE.instance.saveManager.cache || database) {
      listener.remove(key);
    }
    return removed;
  }

  @Override
  public boolean containsKey(Object key) {
    MISCUtils.debug("EventMap.containsKey");
    return get(key) != null;
  }

  public EventMapIterator<Map.Entry<K, V>> getIterator() {
    return new EventMapIterator<>(entrySet().iterator(), listener);
  }

  @Override
  public int size() {
    MISCUtils.debug("EventMap.size");
    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile")) {
      return listener.size();
    }
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    MISCUtils.debug("EventMap.isEmpty");
    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile")) {
      return listener.isEmpty();
    }
    return map.isEmpty();
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    putAll(m, false);
  }

  public void putAll(Map<? extends K, ? extends V> m, boolean skip) {
    for(Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue(), skip);
    }
  }

  @Override
  public boolean containsValue(Object value) {
    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") && !TNE.instance.saveManager.cache) {
      return listener.containsValue(value);
    }
    return map.containsValue(value);
  }

  @Override
  public Set<K> keySet() {
    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") && !TNE.instance.saveManager.cache) {
      return listener.keySet();
    }
    return map.keySet();
  }

  @Override
  public Collection<V> values() {
    return values(true);
  }

  public Collection<V> values(boolean useCache) {
    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") && !TNE.instance.saveManager.cache ||
       !TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") && TNE.instance.saveManager.cache && !useCache) {
      return listener.values();
    }
    return map.values();
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    if(!TNE.instance.saveManager.type.equalsIgnoreCase("flatfile") && !TNE.instance.saveManager.cache) {
      return listener.entrySet();
    }
    return map.entrySet();
  }

  public void setListener(MapListener<K, V> listener) {
    this.listener = listener;
  }
}