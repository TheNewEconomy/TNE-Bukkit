package net.tnemc.core.listeners.collections;

import com.github.tnerevival.core.collection.MapListener;
import net.tnemc.core.TNE;

import java.util.*;

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
    changed.forEach((username, id)->TNE.saveManager().getTNEManager().getTNEProvider().saveID(username, id));
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
    TNE.saveManager().getTNEManager().getTNEProvider().saveID((String)key, (UUID)value);
  }

  @Override
  public Object get(Object key) {
    TNE.debug("IDListener.get");
    return TNE.saveManager().getTNEManager().getTNEProvider().loadID((String)key);
  }

  @Override
  public Collection values() {
    return TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyIDS().values();
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
    return TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyIDS().keySet();
  }

  @Override
  public Set<Map.Entry<String, UUID>> entrySet() {
    return TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyIDS().entrySet();
  }

  @Override
  public void remove(Object key) {
    TNE.saveManager().getTNEManager().getTNEProvider().removeID((String)key);
  }
}