package net.tnemc.signs;

import com.github.tnerevival.core.collection.MapListener;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class SignsListener implements MapListener {
  Map<Location, TNESign> changed = new HashMap<>();

  @Override
  public void update() {
    for(TNESign sign : changed.values()) {
      SignsModule.manager().saveSign(sign);
    }
  }

  @Override
  public Map<Location, TNESign> changed() {
    return changed;
  }

  @Override
  public void clearChanged() {
    changed.clear();
  }

  @Override
  public void put(Object key, Object value) {
    SignsModule.manager().saveSign((TNESign)value);
  }

  @Override
  public Object get(Object key) {
    return SignsModule.manager().loadSign(((Location)key));
  }

  @Override
  public Collection<TNESign> values() {
    return SignsModule.manager().loadSigns();
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
    SignsModule.manager().deleteSign((Location)key);
  }

  @Override
  public Set<Location> keySet() {
    Set<Location> keys = new HashSet<>();

    for(TNESign sign : values()) {
      keys.add(sign.getLocation());
    }

    return keys;
  }

  @Override
  public Set<Map.Entry<Location, TNESign>> entrySet() {
    Map<Location, TNESign> signMap = new HashMap<>();

    for(TNESign sign : values()) {
      signMap.put(sign.getLocation(), sign);
    }

    return signMap.entrySet();
  }

  @Override
  public void remove(Object key) {
  }
}