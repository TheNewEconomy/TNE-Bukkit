package net.tnemc.core.listeners.collections;

import com.github.tnerevival.core.collection.MapListener;
import net.tnemc.core.TNE;
import net.tnemc.core.common.transaction.TNETransaction;

import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 9/7/2017.
 */
public class TransactionListener implements MapListener {
  private Map<UUID, TNETransaction> changed = new HashMap<>();

  @Override
  public void update() {
    changed.forEach((id, transaction)->TNE.saveManager().getTNEManager().getTNEProvider().saveTransaction(transaction));
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
    TNE.saveManager().getTNEManager().getTNEProvider().saveTransaction((TNETransaction)value);
  }

  @Override
  public Object get(Object key) {
    return TNE.saveManager().getTNEManager().getTNEProvider().loadTransaction((UUID)key);
  }

  @Override
  public Collection<TNETransaction> values() {
    return TNE.saveManager().getTNEManager().getTNEProvider().loadTransactions();
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
    return values().contains((TNETransaction)value);
  }

  @Override
  public void preRemove(Object key, Object value) {

  }

  @Override
  public Set keySet() {
    Set<UUID> keys = new HashSet<>();
    values().forEach((transaction)->keys.add(transaction.transactionID()));
    return keys;
  }

  @Override
  public Set<Map.Entry<UUID, TNETransaction>> entrySet() {
    Map<UUID, TNETransaction> values = new HashMap<>();
    values().forEach((transaction)->values.put(transaction.transactionID(), transaction));
    return values.entrySet();
  }

  @Override
  public void remove(Object key) {
    TNE.saveManager().getTNEManager().getTNEProvider().deleteTransaction((UUID)key);
  }
}