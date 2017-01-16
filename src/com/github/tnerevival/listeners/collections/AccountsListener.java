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
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.collection.MapListener;

import java.util.*;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class AccountsListener implements MapListener {
  Map<UUID, Account> changed = new HashMap<>();

  @Override
  public void update() {
    for(Account acc : changed.values()) {
      TNE.instance.saveManager.versionInstance.saveAccount(acc);
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
    TNE.instance.saveManager.versionInstance.saveAccount((Account)value);
  }

  @Override
  public Object get(Object key) {
    return TNE.instance.saveManager.versionInstance.loadAccount((UUID)key);
  }

  @Override
  public Collection values() {
    return TNE.instance.saveManager.versionInstance.loadAccounts();
  }

  @Override
  public int size() {
    return 0;
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
  public Set<UUID> keySet() {
    Set<UUID> keys = new HashSet<>();
    Collection<Account> accounts = TNE.instance.saveManager.versionInstance.loadAccounts();
    for(Account account : accounts) {
      keys.add(account.getUid());
    }
    return keys;
  }

  @Override
  public Set<Map.Entry<UUID, Account>> entrySet() {
    Map<UUID, Account> accountMap = new HashMap<>();
    Collection<Account> accounts = TNE.instance.saveManager.versionInstance.loadAccounts();
    for(Account account : accounts) {
      accountMap.put(account.getUid(), account);
    }
    return accountMap.entrySet();
  }

  @Override
  public void remove(Object key) {
    TNE.instance.saveManager.versionInstance.deleteAccount((UUID)key);
  }
}