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

import java.util.*;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class IDSListener implements MapListener {
  Map<String, UUID> changed = new HashMap<>();

  @Override
  public void update() {
    for(Map.Entry<String, UUID> entry : changed.entrySet()) {
      TNE.instance().saveManager.versionInstance.saveID(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public Map changed() {
    return changed;
  }

  @Override
  public void clearChanged() {
    changed.clear();
  }

  @Override
  public void put(Object key, Object value) {
    TNE.instance().saveManager.versionInstance.saveID((String)key, (UUID)value);
  }

  @Override
  public Object get(Object key) {
    return TNE.instance().saveManager.versionInstance.loadID((String)key);
  }

  @Override
  public Collection<UUID> values() {
    return TNE.instance().saveManager.versionInstance.loadIDS().values();
  }

  @Override
  public int size() {
    return TNE.instance().saveManager.versionInstance.loadIDS().size();
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
  }

  @Override
  public Set<String> keySet() {
    return TNE.instance().saveManager.versionInstance.loadIDS().keySet();
  }

  @Override
  public Set<Map.Entry<String, UUID>> entrySet() {
    return TNE.instance().saveManager.versionInstance.loadIDS().entrySet();
  }

  @Override
  public void remove(Object key) {
    TNE.instance().saveManager.versionInstance.removeID((String)key);
  }
}