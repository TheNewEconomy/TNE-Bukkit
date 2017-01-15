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
    if(TNE.instance.saveFormat.equalsIgnoreCase("flatfile") || TNE.instance.cache) {
      return map.get(key);
    }

    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile")) {
      if(!TNE.instance.cache || !map.containsKey(key)) {
        return listener.get(key);
      }
    }
    return map.get(key);
  }

  @Override
  public V put(K key, V value) {
    if(TNE.instance.saveFormat.equalsIgnoreCase("flatfile") || TNE.instance.cache) {
      map.put(key, value);
    }

    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile")) {
      if(!TNE.instance.cache || !map.containsKey(key)) {
        listener.put(key, value);
      }
    }
    return value;
  }

  @Override
  public V remove(Object key) {
    return remove(key, true);
  }

  public V remove(Object key, boolean database) {
    V removed = get(key);
    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile") && database) {
      listener.preRemove(key, removed);
    }

    if(TNE.instance.saveFormat.equalsIgnoreCase("flatfile") || TNE.instance.cache) {
      removed = map.remove(key);
    }

    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile") && !TNE.instance.cache || database) {
      listener.remove(key);
    }
    return removed;
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  public EventMapIterator<Map.Entry<K, V>> getIterator() {
    return new EventMapIterator<>(entrySet().iterator(), listener);
  }

  @Override
  public int size() {
    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile")) {
      return listener.size();
    }
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile")) {
      return listener.isEmpty();
    }
    return map.isEmpty();
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for(Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public boolean containsValue(Object value) {
    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile") && !TNE.instance.cache) {
      return listener.containsValue(value);
    }
    return map.containsValue(value);
  }

  @Override
  public Set<K> keySet() {
    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile") && !TNE.instance.cache) {
      return listener.keySet();
    }
    return map.keySet();
  }

  @Override
  public Collection<V> values() {
    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile") && !TNE.instance.cache) {
      return listener.values();
    }
    return map.values();
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    if(!TNE.instance.saveFormat.equalsIgnoreCase("flatfile") && !TNE.instance.cache) {
      return listener.entrySet();
    }
    return map.entrySet();
  }

  public void setListener(MapListener<K, V> listener) {
    this.listener = listener;
  }
}