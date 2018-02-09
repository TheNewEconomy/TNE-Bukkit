package net.tnemc.core.listeners.collections;

import com.github.tnerevival.core.collection.MapListener;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;

import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 9/7/2017.
 */
public class AccountListener implements MapListener {
  private Map<UUID, TNEAccount> changed = new HashMap<>();

  @Override
  public void update() {
    changed.values().forEach((account)-> TNE.saveManager().getTNEManager().getTNEProvider().saveAccount(account));
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
    TNE.saveManager().getTNEManager().getTNEProvider().saveAccount((TNEAccount)value);
  }

  @Override
  public Object get(Object key) {
    TNEAccount account = TNE.saveManager().getTNEManager().getTNEProvider().loadAccount((UUID)key);

    TNE.debug("AccountListener ID? " + ((UUID)key).toString());
    TNE.debug("AccountListener Account null? " + (account == null));

    return account;
  }

  @Override
  public Collection<TNEAccount> values() {
    return TNE.saveManager().getTNEManager().getTNEProvider().loadAccounts();
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
    TNE.saveManager().getTNEManager().getTNEProvider().deleteAccount((UUID)key);
  }
}