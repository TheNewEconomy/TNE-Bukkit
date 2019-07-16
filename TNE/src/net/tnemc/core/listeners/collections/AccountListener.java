package net.tnemc.core.listeners.collections;

import com.github.tnerevival.core.collection.MapListener;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;

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
public class AccountListener implements MapListener {
  private Map<UUID, TNEAccount> changed = new HashMap<>();

  @Override
  public void update() {
    Map<UUID, TNEAccount> copy = changed;
    for(TNEAccount account : copy.values()) {
      try {
        TNE.saveManager().getTNEManager().getTNEProvider().saveAccount(account);
      } catch (SQLException e) {
        TNE.debug(e);
      }
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
    try {
      TNE.saveManager().getTNEManager().getTNEProvider().saveAccount((TNEAccount)value);
    } catch (SQLException e) {
      TNE.debug(e);
    }
  }

  @Override
  public Object get(Object key) {
    TNEAccount account = null;
    try {
      account = TNE.saveManager().getTNEManager().getTNEProvider().loadAccount((UUID)key);
    } catch (SQLException e) {
      TNE.debug(e);
    }

    TNE.debug("AccountListener ID? " + ((UUID)key).toString());
    TNE.debug("AccountListener Account null? " + (account == null));

    return account;
  }

  @Override
  public Collection<TNEAccount> values() {
    try {
      return TNE.saveManager().getTNEManager().getTNEProvider().loadAccounts();
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return new ArrayList<>();
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
    return values().contains((TNEAccount)value);
  }

  @Override
  public void preRemove(Object key, Object value) {

  }

  @Override
  public Set<UUID> keySet() {
    Set<UUID> keys = new HashSet<>();
    values().forEach((account)->keys.add(account.identifier()));
    return keys;
  }

  @Override
  public Set<Map.Entry<UUID, TNEAccount>> entrySet() {
    Map<UUID, TNEAccount> accounts = new HashMap<>();
    values().forEach((account)->accounts.put(account.identifier(), account));
    return accounts.entrySet();
  }

  @Override
  public void remove(Object key) {
    try {
      TNE.saveManager().getTNEManager().getTNEProvider().deleteAccount((UUID)key);
    } catch (SQLException e) {
      TNE.debug(e);
    }
  }
}