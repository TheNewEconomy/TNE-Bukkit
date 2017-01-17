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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class NewBank {

  private List<UUID> members = new ArrayList<>();
  private UUID owner;
  private Double gold;

  public NewBank(UUID owner) {
    this(owner, 0.0);
  }

  public NewBank(UUID owner, double gold) {
    this.owner = owner;
    this.gold = gold;
  }

  /**
   * @return the gold
   */
  public Double getGold() {
    return gold;
  }

  public UUID getOwner() {
    return owner;
  }

  /**
   * @param gold the gold to set
   */
  public void setGold(Double gold) {
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

  public static NewBank fromString(String parse) {
    String[] parsed = parse.split(":");
    NewBank b = new NewBank(UUID.fromString(parsed[0]));
    b.setGold(Double.valueOf(parsed[1]));
    if(parsed.length >= 3) {
      b.membersFromString(parsed[2]);
    }

    return b;
  }

  public String toString() {
    return owner.toString() + ":" + gold + ":" + membersToString();
  }
}