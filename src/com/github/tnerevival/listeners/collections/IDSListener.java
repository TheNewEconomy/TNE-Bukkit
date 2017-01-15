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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class IDSListener implements MapListener {
  @Override
  public void update() {

  }

  @Override
  public Map changed() {
    return null;
  }

  @Override
  public void clearChanged() {

  }

  @Override
  public void put(Object key, Object value) {
    TNE.instance.saveManager.versionInstance.saveID((String)key, (UUID)value);
  }

  @Override
  public Object get(Object key) {
    return null;
  }

  @Override
  public Collection values() {
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
  public boolean containsValue(Object value) {
    return false;
  }

  @Override
  public void preRemove(Object key, Object value) {
  }

  @Override
  public Set keySet() {
    return null;
  }

  @Override
  public Set<Map.Entry> entrySet() {
    return null;
  }

  @Override
  public void remove(Object key) {
    TNE.instance.saveManager.versionInstance.removeID((String)key);
  }
}