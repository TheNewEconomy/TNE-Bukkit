package com.github.tnerevival.core.transaction;

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
 * Created by creatorfromhell on 10/20/2016.
 */
public class Record {
  public String id;
  public String player;
  public String world;
  public String type;
  public Double cost;
  public Double oldBalance;
  public Double balance;
  public long time;
  public String zone;

  public Record(String id, String player, String world, String type, Double cost, Double oldBalance, Double balance, Long time, String zone) {
    this.id = id;
    this.player = player;
    this.world = world;
    this.type = type;
    this.cost = cost;
    this.oldBalance = oldBalance;
    this.balance = balance;
    time = time;
    zone = zone;
  }

  public String convert(String timeZone) {
    //TODO: Convert time.
    return "Time coming soon.";
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getPlayer() {
    return player;
  }

  public void setPlayer(String player) {
    this.player = player;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public Double getOldBalance() {
    return oldBalance;
  }

  public void setOldBalance(Double oldBalance) {
    this.oldBalance = oldBalance;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getZone() {
    return zone;
  }

  public void setZone(String zone) {
    this.zone = zone;
  }
}
