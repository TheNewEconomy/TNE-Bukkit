package net.tnemc.core.listeners.collections;

import com.github.tnerevival.core.collection.MapListener;
import net.tnemc.core.TNE;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 9/7/2017.
 */
public class IDListener implements MapListener {
  private Map<String, UUID> changed = new HashMap<>();

  @Override
  public void update() {
    changed.forEach((username, id) -> {
      try {
        TNE.saveManager().getTNEManager().getTNEProvider().saveID(username, id);
      } catch (SQLException e) {
        TNE.debug(e);
      }
    });
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
    TNE.debug("IDListener.put");
    try {
      TNE.saveManager().getTNEManager().getTNEProvider().saveID((String)key, (UUID)value);
    } catch (SQLException e) {
      TNE.debug(e);
    }
  }

  @Override
  public Object get(Object key) {
    TNE.debug("IDListener.get");
    try {
      return TNE.saveManager().getTNEManager().getTNEProvider().loadID((String)key);
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return null;
  }

  @Override
  public Collection values() {
    try {
      return TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyIDS().values();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return new ArrayList();
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
    return values().contains(value);
  }

  @Override
  public void preRemove(Object key, Object value) {
  }

  @Override
  public Set<String> keySet() {
    try {
      return TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyIDS().keySet();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return new HashSet<>();
  }

  @Override
  public Set<Map.Entry<String, UUID>> entrySet() {
    try {
      return TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyIDS().entrySet();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return new HashSet<>();
  }

  @Override
  public void remove(Object key) {
    try {
      TNE.saveManager().getTNEManager().getTNEProvider().removeID((String)key);
    } catch (SQLException e) {
      TNE.debug(e);
    }
  }
}