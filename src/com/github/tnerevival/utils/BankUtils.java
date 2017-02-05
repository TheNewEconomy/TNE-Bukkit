package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;

import java.math.BigDecimal;
import java.util.UUID;

public class BankUtils {
  public static void applyInterest(UUID id) {
    Account account = AccountUtils.getAccount(id);

    for(Bank b : account.getBanks().values()) {
      if(Bank.interestEnabled(b.getWorld(), id.toString())) {
        BigDecimal gold = b.getGold();
        BigDecimal interestEarned = gold.multiply(Bank.interestRate(b.getWorld(), id.toString()));
        b.setGold(gold.add(interestEarned));
      }
      account.setBank(b.getWorld(), b);
    }
  }

  public static boolean bankMember(UUID owner, UUID id) {
    String world = TNE.instance.defaultWorld;
    if(MISCUtils.multiWorld()) {
      world = IDFinder.getWorld(id);
    }
    if(world == null) {
      TNE.instance.getLogger().warning("***WORLD NAME IS NULL***");
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

  public static BigDecimal getBankBalance(UUID owner, String world) {
    if(AccountUtils.getAccount(owner).hasBank(world)) {
      if(AccountUtils.getAccount(owner).getStatus().getBank()) {
        Bank b = getBank(owner, world);
        return b.getGold();
      }
    }
    return BigDecimal.ZERO;
  }

  public static BigDecimal getBankBalance(UUID owner) {
    return getBankBalance(owner, TNE.instance.defaultWorld);
  }

  public static void setBankBalance(UUID owner, String world, BigDecimal amount) {
    if(AccountUtils.getAccount(owner).getStatus().getBank()) {
      if(AccountUtils.getAccount(owner).hasBank(world)) {
        Bank bank = getBank(owner, world);
        bank.setGold(amount);
      }
    }
  }
}