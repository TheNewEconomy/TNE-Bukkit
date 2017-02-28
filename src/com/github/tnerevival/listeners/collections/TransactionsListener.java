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
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.collection.ListListener;
import com.github.tnerevival.core.transaction.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public class TransactionsListener implements ListListener {
  List<Record> changed = new ArrayList<>();

  @Override
  public void update() {
    for(Record r : changed) {
      TNE.instance().saveManager.versionInstance.saveTransaction(r);
    }
  }

  @Override
  public List<Record> changed() {
    return changed;
  }

  @Override
  public void clearChanged() {
    changed.clear();
  }

  @Override
  public boolean add(Object item) {
    TNE.instance().saveManager.versionInstance.saveTransaction((Record)item);
    return true;
  }

  @Override
  public Collection<Record> getAll() {
    return new ArrayList<>();
  }

  @Override
  public Collection<Record> getAll(Object identifier) {
    return TNE.instance().saveManager.versionInstance.loadHistory(IDFinder.getID((String)identifier)).getRecords();
  }

  @Override
  public int size() {
    return TNE.instance().saveManager.versionInstance.loadTransactions().size();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean contains(Object item) {
    return false;
  }

  @Override
  public void preRemove(Object item) {

  }

  @Override
  public boolean remove(Object item) {
    TNE.instance().saveManager.versionInstance.deleteTransaction(UUID.fromString(((Record) item).getId()));
    return true;
  }
}