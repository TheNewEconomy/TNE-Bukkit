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
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class Bank {

  private List<UUID> members = new ArrayList<>();
  private UUID owner;
  private String world;
  private Map<String, BankBalance> balances = new HashMap<>();

  public Bank(UUID owner, String world) {
    this.owner = owner;
    this.world = world;
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

  public BigDecimal getGold(String currency) {
    return (balances.containsKey(currency))? balances.get(currency).getBalance() : BigDecimal.ZERO;
  }

  public void setGold(String currency, BigDecimal gold) {
    balances.put(currency, new BankBalance(currency, gold));
  }

  public Map<String, BigDecimal> getBalances(String currency) {
    Map<String, BigDecimal> sorted = new HashMap<>();
    for(BankBalance balance : balances.values()) {
      if(!currency.equalsIgnoreCase("all") && !balance.getCurrency().equals(currency)) continue;
      sorted.put(balance.getCurrency(), TNE.instance().manager.currencyManager.convert(TNE.instance().manager.currencyManager.get(world, balance.getCurrency()), 1.0, balance.getBalance()));
    }
    return sorted;
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
    //TODO: Balances to String.
    if(parsed.length >= 3) {
      b.membersFromString(parsed[2]);
    }

    return b;
  }

  public String toString() {
    //TODO: Balances to String.
    return owner.toString() + ":" + "<INSERT BALANCES STRING HERE>" + ":" + membersToString();
  }

  public void applyInterest() {
    Iterator<Map.Entry<String, BankBalance>> i = balances.entrySet().iterator();
    while(i.hasNext()) {
      Map.Entry<String, BankBalance> entry = i.next();
      if(TNE.instance().manager.currencyManager.contains(world, entry.getKey())) {
        com.github.tnerevival.core.currency.Currency currency = TNE.instance().manager.currencyManager.get(world, entry.getKey());
        BankBalance balance = entry.getValue();
        if(currency.isInterestEnabled() && (new Date().getTime() - balance.getLastInterest()) >= currency.getInterestInterval()) {
          BigDecimal gold = balance.getBalance();
          BigDecimal interestEarned = gold.multiply(new BigDecimal(currency.getInterestRate()));
          balance.setBalance(gold.add(interestEarned));
          balance.setLastInterest(new Date().getTime());
          balances.put(entry.getKey(), balance);
        }
      }
    }
  }

  public static Boolean enabled(String world, String player) {
    return TNE.instance().api().getBoolean("Core.Bank.Enabled", world, player);
  }

  public static BigDecimal cost(String world, String player) {
    return new BigDecimal(TNE.instance().api().getDouble("Core.Bank.Cost", world, player));
  }

  public static boolean bankMember(UUID owner, UUID id) {
    String world = TNE.instance().defaultWorld;
    if(MISCUtils.multiWorld()) {
      world = IDFinder.getWorld(id);
    }
    if(world == null) {
      TNE.instance().getLogger().warning("***WORLD NAME IS NULL***");
      return false;
    }

    return bankMember(owner, id, world);
  }

  public static Boolean bankMember(UUID owner, UUID id, String world) {
    if(owner.equals(id)) return true;
    Bank b = getBank(owner, world);

    return b.getMembers().contains(id);
  }

  public static Bank getBank(UUID owner, String world) {
    return AccountUtils.getAccount(owner).getBank(world);
  }

  public static BigDecimal getBankBalance(UUID owner, String world, String currency) {
    if(AccountUtils.getAccount(owner).hasBank(world)) {
      if(AccountUtils.getAccount(owner).getStatus().getBank()) {
        Bank b = getBank(owner, world);
        return b.getGold(currency);
      }
    }
    return BigDecimal.ZERO;
  }

  public static BigDecimal getBankBalance(UUID owner) {
    return getBankBalance(owner, TNE.instance().defaultWorld, TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld).getName());
  }

  public static void setBankBalance(UUID owner, String world, String currency, BigDecimal amount) {
    if(AccountUtils.getAccount(owner).getStatus().getBank()) {
      if(AccountUtils.getAccount(owner).hasBank(world)) {
        Bank bank = getBank(owner, world);
        bank.setGold(currency, amount);
      }
    }
  }
}