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
package com.github.tnerevival.account;

import com.github.tnerevival.TNE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class Bank {

  private List<UUID> members = new ArrayList<>();
  private UUID owner;
  private String world;
  private BigDecimal gold;

  public Bank(UUID owner, String world) {
    this(owner, world, BigDecimal.ZERO);
  }

  public Bank(UUID owner, String world, BigDecimal gold) {
    this.owner = owner;
    this.gold = gold;
    this.world = world;
  }

  /**
   * @return the gold
   */
  public BigDecimal getGold() {
    return gold;
  }

  public UUID getOwner() {
    return owner;
  }

  public List<UUID> getMembers() {
    return members;
  }

  public void addMember(UUID player) {
    members.add(player);
  }

  public void removeMember(UUID player) {
    members.remove(player);
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  /**
   * @param gold the gold to set
   */
  public void setGold(BigDecimal gold) {
    this.gold = gold;
  }

  private String membersToString() {
    StringBuilder builder = new StringBuilder();
    for(UUID id : members) {
      if(builder.length() > 0) builder.append("*");
      builder.append(id.toString());
    }
    return builder.toString();
  }

  public void membersFromString(String parse) {
    String[] parsed = parse.split("\\*");

    for(String s : parsed) {
      members.add(UUID.fromString(s));
    }
  }

  public static Bank fromString(String parse, String world) {
    String[] parsed = parse.split(":");
    Bank b = new Bank(UUID.fromString(parsed[0]), world);
    b.setGold(new BigDecimal(parsed[1]));
    if(parsed.length >= 3) {
      b.membersFromString(parsed[2]);
    }

    return b;
  }

  public String toString() {
    return owner.toString() + ":" + gold.doubleValue() + ":" + membersToString();
  }

  public static Boolean enabled(String world, String player) {
    return TNE.instance.api.getBoolean("Core.Bank.Enabled", world, player);
  }

  public static BigDecimal cost(String world, String player) {
    return new BigDecimal(TNE.instance.api.getDouble("Core.Bank.Cost", world, player));
  }

  public static Boolean interestEnabled(String world, String player) {
    return TNE.instance.api.getBoolean("Core.Bank.Interest.Enabled", world, player);
  }

  public static BigDecimal interestRate(String world, String player) {
    return new BigDecimal(TNE.instance.api.getDouble("Core.Bank.Interest.Rate", world, player));
  }
}