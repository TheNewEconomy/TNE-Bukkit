package com.github.tnerevival.core.transaction;

import com.github.tnerevival.TNE;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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
 * Created by creatorfromhell on 10/20/2016.
 */
public class Record {
  private String id = "";
  private String initiator = "";
  private String player = "N/A";
  private String world = TNE.instance().defaultWorld;
  private String type = TransactionType.MONEY_INQUIRY.id;
  private BigDecimal cost = BigDecimal.ZERO;
  private BigDecimal oldBalance = BigDecimal.ZERO;
  private BigDecimal balance = BigDecimal.ZERO;
  private long time = new Date().getTime();

  public Record(String id, String initiator, String player, String world, String type, BigDecimal cost, BigDecimal oldBalance, BigDecimal balance, Long time) {
    this.id = id;
    this.initiator = initiator;
    this.player = player;
    this.world = world;
    this.type = type;
    this.cost = cost;
    if(oldBalance != null) {
      this.oldBalance = oldBalance;
    }

    if(balance != null) {
      this.balance = balance;
    }

    if(time != null) {
      this.time = time;
    }
  }

  public String convert(String world, UUID id, String timeZone) {
    final Date date = new Date(time);
    final SimpleDateFormat dateFormat = new SimpleDateFormat(TNE.instance().api().getString("Core.Transactions.Format", world, id));
    dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

    return dateFormat.format(date);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getInitiator() {
    return initiator;
  }

  public void setInitiator(String initiator) {
    this.initiator = initiator;
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

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public BigDecimal getOldBalance() {
    return oldBalance;
  }

  public void setOldBalance(BigDecimal oldBalance) {
    this.oldBalance = oldBalance;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }
}
