package com.github.tnerevival.core.auction;

import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.serializable.SerializableItemStack;

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
 * Created by creatorfromhell on 10/18/2016.
 */
public class Claim {
  private UUID player;
  private Integer lot;
  private SerializableItemStack item;
  private TransactionCost cost;
  private boolean paid = true;

  public Claim(UUID player, Integer lot, SerializableItemStack item, TransactionCost cost) {
    this.player = player;
    this.lot = lot;
    this.item = item;
    this.cost = cost;
  }

  public void claim() {
    IDFinder.getPlayer(player.toString()).getInventory().addItem(item.toItemStack());
  }

  public UUID getPlayer() {
    return player;
  }

  public void setPlayer(UUID player) {
    this.player = player;
  }

  public Integer getLot() {
    return lot;
  }

  public void setLot(Integer lot) {
    this.lot = lot;
  }

  public SerializableItemStack getItem() {
    return item;
  }

  public void setItem(SerializableItemStack item) {
    this.item = item;
  }

  public TransactionCost getCost() {
    return cost;
  }

  public void setCost(TransactionCost cost) {
    this.cost = cost;
  }

  public boolean isPaid() {
    return paid;
  }

  public void setPaid(boolean paid) {
    this.paid = paid;
  }
}
