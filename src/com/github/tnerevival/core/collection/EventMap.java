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

import com.github.tnerevival.utils.MISCUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by creatorfromhell on 11/2/2016.
 **/
public class EventMap<K, V> extends HashMap<K, V> {

  private MapListener<K, V> listener;
  private Map<K, V> map = new HashMap<>();

  public EventMap() {
    super();
  }

  @Override
  public V get(Object key) {
    MISCUtils.debug("Get called using key " + key.toString());
    return map.get(key);
  }

  @Override
  public V put(K key, V value) {
    listener.add(key, value);
    return map.put(key, value);
  }

  @Override
  public V remove(Object key) {
    listener.preRemove(key, get(key));
    V removed = map.remove(key);
    listener.remove(key);
    return removed;
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  public EventMapIterator<Map.Entry<K, V>> getIterator() {
    return new EventMapIterator<>(map.entrySet().iterator(), listener);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    map.putAll(m);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return map.entrySet();
  }

  public void setListener(MapListener<K, V> listener) {
    this.listener = listener;
  }
}