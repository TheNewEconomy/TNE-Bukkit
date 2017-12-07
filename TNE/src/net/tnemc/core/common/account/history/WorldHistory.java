package net.tnemc.core.common.account.history;

import net.tnemc.core.common.transaction.TNETransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
 * Created by creatorfromhell on 07/05/2017.
 */
public class WorldHistory {

  private List<UUID> history = new ArrayList<>();

  private String world;

  public WorldHistory(String world) {
    this.world = world;
  }

  public void addTransaction(TNETransaction transaction) {
    history.add(transaction.transactionID());
  }

  public List<UUID> getTransactions() {
    return history;
  }

  public String getWorld() {
    return world;
  }
}