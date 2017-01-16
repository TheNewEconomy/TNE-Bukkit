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
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.serializable.SerializableLocation;

import java.util.*;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class SignsListener implements MapListener {
  Map<SerializableLocation, TNESign> changed = new HashMap<>();

  @Override
  public void update() {
    for(TNESign sign : changed.values()) {
      TNE.instance.saveManager.versionInstance.saveSign(sign);
    }
  }

  @Override
  public Map<SerializableLocation, TNESign> changed() {
    return changed;
  }

  @Override
  public void clearChanged() {
    changed.clear();
  }

  @Override
  public void put(Object key, Object value) {
    TNE.instance.saveManager.versionInstance.saveSign((TNESign)value);
  }

  @Override
  public Object get(Object key) {
    return TNE.instance.saveManager.versionInstance.loadSign(((SerializableLocation)key).toString());
  }

  @Override
  public Collection<TNESign> values() {
    return TNE.instance.saveManager.versionInstance.loadSigns();
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
    TNE.instance.saveManager.versionInstance.deleteSign((TNESign)value);
  }

  @Override
  public Set<SerializableLocation> keySet() {
    Set<SerializableLocation> keys = new HashSet<>();

    for(TNESign sign : values()) {
      keys.add(sign.getLocation());
    }

    return keys;
  }

  @Override
  public Set<Map.Entry<SerializableLocation, TNESign>> entrySet() {
    Map<SerializableLocation, TNESign> signMap = new HashMap<>();

    for(TNESign sign : values()) {
      signMap.put(sign.getLocation(), sign);
    }

    return signMap.entrySet();
  }

  @Override
  public void remove(Object key) {
  }
}