package net.tnemc.signs;

import com.github.tnerevival.core.collection.MapListener;
import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
import net.tnemc.signs.signs.TNESign;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class SignsListener implements MapListener {
  Map<SerializableLocation, TNESign> changed = new HashMap<>();

  @Override
  public void update() {
    for(TNESign sign : changed.values()) {
      TNE.instance().saveManager.versionInstance.saveSign(sign);
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
    TNE.instance().saveManager.versionInstance.saveSign((TNESign)value);
  }

  @Override
  public Object get(Object key) {
    return TNE.instance().saveManager.versionInstance.loadSign(((SerializableLocation)key).toString());
  }

  @Override
  public Collection<TNESign> values() {
    return TNE.instance().saveManager.versionInstance.loadSigns();
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
    TNE.instance().saveManager.versionInstance.deleteSign((TNESign)value);
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