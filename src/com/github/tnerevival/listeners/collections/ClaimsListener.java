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
import com.github.tnerevival.core.auction.Claim;
import com.github.tnerevival.core.collection.ListListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by creatorfromhell on 11/15/2016.
 **/
public class ClaimsListener implements ListListener {
  List<Claim> changed = new ArrayList<>();

  @Override
  public void update() {
    for(Claim c : changed) {
      TNE.instance().saveManager.versionInstance.saveClaim(c);
    }
  }

  @Override
  public List changed() {
    return changed;
  }

  @Override
  public void clearChanged() {
    changed.clear();
  }

  @Override
  public Collection<Claim> getAll() {
    return TNE.instance().saveManager.versionInstance.loadClaims();
  }

  @Override
  public Collection<Claim> getAll(Object identifier) {
    return null;
  }

  @Override
  public boolean add(Object item) {
    TNE.instance().saveManager.versionInstance.saveClaim((Claim)item);
    return true;
  }

  @Override
  public int size() {
    return TNE.instance().saveManager.versionInstance.loadClaims().size();
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean contains(Object item) {
    Claim claim = (Claim)item;
    return TNE.instance().saveManager.versionInstance.loadClaim(claim.getPlayer(), claim.getLot()) != null;
  }

  @Override
  public void preRemove(Object item) {

  }

  @Override
  public boolean remove(Object item) {
    TNE.instance().saveManager.versionInstance.deleteClaim((Claim)item);
    return true;
  }
}