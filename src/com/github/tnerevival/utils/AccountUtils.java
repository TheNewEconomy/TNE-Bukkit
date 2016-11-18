package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.event.account.TNEAccountCreationEvent;
import com.github.tnerevival.core.event.transaction.TNETransactionEvent;
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.transaction.Transaction;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.core.transaction.TransactionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.UUID;

public class AccountUtils {

  public static Boolean exists(UUID id) {
    return TNE.instance.manager.accounts.containsKey(id);
  }

  public static void createAccount(UUID id) {
    Account a = new Account(id);
    String defaultCurrency = TNE.instance.manager.currencyManager.get(TNE.instance.defaultWorld).getName();
    a.setBalance(TNE.instance.defaultWorld, AccountUtils.getInitialBalance(TNE.instance.defaultWorld, defaultCurrency), defaultCurrency);
    TNEAccountCreationEvent e = new TNEAccountCreationEvent(id, a);
    MISCUtils.debug(e.getId() + "");
    Bukkit.getServer().getPluginManager().callEvent(e);
    TNE.instance.manager.accounts.put(e.getId(), e.getAccount());
  }

  public static Account getAccount(UUID id) {
    if(!exists(id)) {
      createAccount(id);
    }
    return TNE.instance.manager.accounts.get(id);
  }

  private static Double getBalance(UUID id, String world, String currencyName) {
    Account account = getAccount(id);
    Currency currency = TNE.instance.manager.currencyManager.get(world, currencyName);

    if(!account.getStatus().getBalance()) return 0.0;

    if(MISCUtils.multiWorld()) {
      if(!account.getBalances().containsKey(world + ":" + currencyName)) {
        initializeWorldData(id);
      }

      return round(account.getBalance(world, currencyName));
    }
    if(currency.isItem()) {
      Material majorItem = MaterialHelper.getMaterial(currency.getTier("Major").getMaterial());
      Material minorItem = MaterialHelper.getMaterial(currency.getTier("Minor").getMaterial());
      Integer major = MISCUtils.getItemCount(id, majorItem);
      Integer minor = MISCUtils.getItemCount(id, minorItem);
      String balance = major + "." + minor;
      return Double.valueOf(balance);
    }
    MISCUtils.debug("------- NULL Test -------");
    MISCUtils.debug("" + (account == null));
    MISCUtils.debug("" + (currencyName == null));
    MISCUtils.debug("" + (TNE.instance.defaultWorld == null));
    Double balance = account.getBalance(TNE.instance.defaultWorld, currencyName);
    Double rounded = round(balance);
    MISCUtils.debug("" + (balance == null));
    MISCUtils.debug("" + (rounded == null));
    return rounded;
  }

  private static void setBalance(UUID id, String world, String currencyName, Double balance) {
    Currency currency = TNE.instance.manager.currencyManager.get(world, currencyName);

    balance = round(balance);
    Account account = getAccount(id);

    if(!account.getStatus().getBalance()) return;

    String balanceString = (String.valueOf(balance).contains("."))? String.valueOf(balance) : String.valueOf(balance) + ".0";
    String[] split = balanceString.split("\\.");

    if(currency.isItem()) {
      Material majorItem = MaterialHelper.getMaterial(currency.getTier("Major").getMaterial());
      Material minorItem = MaterialHelper.getMaterial(currency.getTier("Minor").getMaterial());
      MISCUtils.setItemCount(id, majorItem, Integer.valueOf(split[0].trim()));
      MISCUtils.setItemCount(id, minorItem, Integer.valueOf(split[1].trim()));
    } else {
      account.setBalance(world, balance, currencyName);
      TNE.instance.manager.accounts.put(id, account);

    }
  }

  public static Double round(double amount) {
    return (double)Math.round(amount * 100) / 100;
  }

  public static boolean transaction(String initiator, String recipient, double amount, TransactionType type, String world) {
    return transaction(initiator, recipient, new TransactionCost(amount), type, world);
  }

  public static boolean transaction(String initiator, String recipient, double amount, Currency currency, TransactionType type, String world) {
    return transaction(initiator, recipient, new TransactionCost(amount, currency), type, world);
  }

  public static boolean transaction(String initiator, String recipient, TransactionCost cost, TransactionType type, String world) {
    Transaction t = new Transaction(initiator, recipient, cost, type, world);

    TNETransactionEvent e = new TNETransactionEvent(t);
    Bukkit.getServer().getPluginManager().callEvent(e);

    return !e.isCancelled() && t.perform();
  }

  public static Double getFunds(UUID id) {
    return getFunds(id, MISCUtils.getWorld(id));
  }

  public static Double getFunds(UUID id, String world) {
    return getFunds(id, world, TNE.instance.manager.currencyManager.get(world).getName());
  }

  public static Double getFunds(UUID id, String world, String currency) {
    double funds = getBalance(id, world, currency);
    if(TNE.instance.api.getBoolean("Core.Bank.Connected", world)) {
      funds += BankUtils.getBankBalance(id, world);
    }
    return funds;
  }

  public static void setFunds(UUID id, String world, double amount, String currency) {
    setBalance(id, world, currency, round(amount));
  }

  public static void removeFunds(UUID id, String world, double amount, String currency) {
    double difference = amount - getBalance(id, world, currency);
    if(difference > 0) {
      BankUtils.setBankBalance(id, world, round(BankUtils.getBankBalance(id, world) - difference));
      setBalance(id, world, currency, 0.0);
      return;
    }
    setBalance(id, world, currency, round(getBalance(id, world, currency) - amount));
  }

  public static void initializeWorldData(UUID id) {
    Account account = getAccount(id);
    String world = MISCUtils.getWorld(id);
    for(Currency c : TNE.instance.manager.currencyManager.getWorldCurrencies(world)) {
      if (!account.getBalances().containsKey(world + ":" + c.getName())) {
        account.setBalance(world, getInitialBalance(world, c.getName()), c.getName());
      }
    }
  }

  public static Double getInitialBalance(String world, String currency) {
    double balance = TNE.instance.manager.currencyManager.get(world, currency).getBalance();
    return round(balance);
  }

  public static Double getWorldCost(String world) {
    if(MISCUtils.multiWorld()) {
      if(MISCUtils.worldConfigExists("Worlds." + world + ".ChangeFee")) {
        return round(TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".ChangeFee"));
      }
    }
    return round(TNE.instance.api.getDouble("Core.World.ChangeFee", world));
  }
}