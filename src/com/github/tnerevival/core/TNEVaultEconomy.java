package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.api.TNEAPI;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TNEVaultEconomy implements Economy {

  public TNE plugin = null;
  public TNEAPI api = null;

  public TNEVaultEconomy(TNE plugin) {
    this.plugin = plugin;
    this.api = plugin.api;
  }

  @Override
  public String getName() {
    return "TheNewEconomy";
  }

  @Override
  public boolean isEnabled() {
    return api != null;
  }

  @Override
  public boolean hasBankSupport() {
    return true;
  }

  @Override
  public EconomyResponse createBank(String name, OfflinePlayer player) {
    String world = IDFinder.getWorld(player.getPlayer());

    if(!Bank.enabled(world, IDFinder.getID(player).toString())) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Banks are not enabled in this world!");
    }

    if(AccountUtils.getAccount(getBankAccount(name)).hasBank(world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, player.getName() + " already has a bank in this world!");
    }

    if(!AccountUtils.transaction(getBankAccount(name).toString(), null, Bank.cost(world, IDFinder.getID(player).toString()), TransactionType.MONEY_INQUIRY, world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, player.getName() + " does not have enough funds for a bank in this world!");
    }

    Bank b = new Bank(getBankAccount(name), world);
    AccountUtils.getAccount(getBankAccount(name)).setBank(world, b);
    AccountUtils.transaction(getBankAccount(name).toString(), null, Bank.cost(world, IDFinder.getID(player).toString()), TransactionType.MONEY_REMOVE, world);
    return new EconomyResponse(0, 0, ResponseType.SUCCESS, player.getName() + " now owns a bank in this world!");
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer player, String world) {
    return createPlayerAccount(player);
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer player) {
    if(hasAccount(player)) {
      return false;
    }
    api.createAccount(player.getUniqueId().toString());
    return true;
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
    if(!hasAccount(player)) {
      AccountUtils.createAccount(IDFinder.getID(player));
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative amounts.");
    }
    api.fundsAdd(player.getUniqueId().toString(), amount);
    return new EconomyResponse(amount, getBalance(player), ResponseType.SUCCESS, "");
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
    if(!hasAccount(player)) {
      AccountUtils.createAccount(IDFinder.getID(player));
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative amounts.");
    }
    api.fundsAdd(player.getUniqueId().toString(), world, amount);
    return new EconomyResponse(amount, getBalance(player, world), ResponseType.SUCCESS, "");
  }

  @Override
  public double getBalance(OfflinePlayer player, String world) {
    return api.getBalance(player.getUniqueId().toString(), world);
  }

  @Override
  public double getBalance(OfflinePlayer player) {
    return api.getBalance(player.getUniqueId().toString());
  }

  @Override
  public boolean has(OfflinePlayer player, double amount) {
    return api.fundsHas(player.getUniqueId().toString(), amount);
  }

  @Override
  public boolean has(OfflinePlayer player, String world, double amount) {
    return api.fundsHas(player.getUniqueId().toString(), world, amount);
  }

  @Override
  public boolean hasAccount(OfflinePlayer player, String world) {
    return hasAccount(player);
  }

  @Override
  public boolean hasAccount(OfflinePlayer player) {
    return api.accountExists(player.getUniqueId().toString());
  }

  @Override
  public EconomyResponse isBankMember(String name, OfflinePlayer player) {
    if(Bank.bankMember(getBankAccount(name), IDFinder.getID(player))) {
      return new EconomyResponse(0, 0, ResponseType.SUCCESS, player.getName() + " is a member of this bank!");
    }
    return new EconomyResponse(0, 0, ResponseType.FAILURE, player.getName() + " is not a member of this bank!");
  }

  @Override
  public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
    if(AccountUtils.getAccount(getBankAccount(name)).getBank(IDFinder.getWorld(getBankAccount(name))).getOwner().equals(IDFinder.getID(player))) {
      return new EconomyResponse(0, 0, ResponseType.SUCCESS, player.getName() + " is the owner of this bank!");
    }
    return new EconomyResponse(0, 0, ResponseType.FAILURE, player.getName() + " is not the owner of this bank!");
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
    if(!hasAccount(player)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
    }

    if(!has(player, amount)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds!");
    }
    api.fundsRemove(player.getUniqueId().toString(), amount);
    return new EconomyResponse(amount, getBalance(player), ResponseType.SUCCESS, "");
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
    if(!hasAccount(player)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
    }

    if(!has(player, world, amount)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds!");
    }
    api.fundsRemove(player.getUniqueId().toString(), world, amount);
    return new EconomyResponse(amount, getBalance(player, world), ResponseType.SUCCESS, "");
  }

  @Override
  public String currencyNamePlural() {
    return api.getCurrencyName(true, false);
  }

  @Override
  public String currencyNameSingular() {
    return api.getCurrencyName(true, true);
  }

  @Override
  public String format(double amount) {
    return api.format(amount);
  }

  @Override
  public int fractionalDigits() {
    if(api.getShorten()) {
      return 0;
    }
    return -1;
  }

  @Override
  public List<String> getBanks() {
    return new ArrayList<String>();
  }

  @Override
  @Deprecated
  public boolean createPlayerAccount(String username) {
    if(hasAccount(username)) {
      return false;
    }
    api.createAccount(username);
    return true;
  }

  @Override
  @Deprecated
  public boolean createPlayerAccount(String username, String world) {
    return createPlayerAccount(username);
  }

  @Override
  @Deprecated
  public EconomyResponse bankBalance(String username) {
    String world = IDFinder.getWorld(getBankAccount(username));
    if(!Bank.enabled(world, getBankAccount(username).toString())) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Banks are not enabled in this world!");
    }

    if(!AccountUtils.getAccount(getBankAccount(username)).hasBank(world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, username + " does not own a bank in this world!");
    }
    return new EconomyResponse(0, 0, ResponseType.SUCCESS, "Bank has " + Bank.getBankBalance(getBankAccount(username), world, plugin.manager.currencyManager.get(world).getName()));
  }

  @Override
  @Deprecated
  public EconomyResponse bankDeposit(String username, double amount) {
    String world = IDFinder.getWorld(getBankAccount(username));
    if(!Bank.enabled(world, getBankAccount(username).toString())) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Banks are not enabled in this world!");
    }

    if(!AccountUtils.getAccount(getBankAccount(username)).hasBank(world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, username + " does not own a bank in this world!");
    }

    if(!AccountUtils.transaction(IDFinder.getID(username).toString(), null, new BigDecimal(amount), TransactionType.MONEY_INQUIRY, world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds in bank account!");
    }
    AccountUtils.transaction(getBankAccount(username).toString(), null, new BigDecimal(amount), TransactionType.BANK_DEPOSIT, world);
    return new EconomyResponse(0, 0, ResponseType.SUCCESS, "Deposited money into bank!");
  }

  @Override
  @Deprecated
  public EconomyResponse bankHas(String username, double amount) {
    String world = IDFinder.getWorld(getBankAccount(username));
    if(!Bank.enabled(world, getBankAccount(username).toString())) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Banks are not enabled in this world!");
    }

    if(!AccountUtils.getAccount(getBankAccount(username)).hasBank(world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, username + " does not own a bank in this world!");
    }

    if(!AccountUtils.transaction(getBankAccount(username).toString(), null, new BigDecimal(amount), TransactionType.BANK_INQUIRY, world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds in bank account!");
    }
    return new EconomyResponse(0, 0, ResponseType.SUCCESS, "Bank has sufficient funds!");
  }

  @Override
  @Deprecated
  public EconomyResponse bankWithdraw(String username, double amount) {
    String world = IDFinder.getWorld(getBankAccount(username));
    if(!Bank.enabled(world, getBankAccount(username).toString())) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Banks are not enabled in this world!");
    }

    if(!AccountUtils.getAccount(getBankAccount(username)).hasBank(world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, username + " does not own a bank in this world!");
    }

    if(!AccountUtils.transaction(getBankAccount(username).toString(), null, new BigDecimal(amount), TransactionType.BANK_INQUIRY, world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds in bank account!");
    }
    AccountUtils.transaction(getBankAccount(username).toString(), null, new BigDecimal(amount), TransactionType.BANK_WITHDRAWAL, world);
    return new EconomyResponse(0, 0, ResponseType.SUCCESS, "Withdrew money from bank!");
  }

  @Override
  @Deprecated
  public EconomyResponse createBank(String username, String world) {
    if(!Bank.enabled(world, getBankAccount(username).toString())) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Banks are not enabled in this world!");
    }

    if(AccountUtils.getAccount(getBankAccount(username)).hasBank(world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, username + " already has a bank in this world!");
    }

    if(!AccountUtils.transaction(getBankAccount(username).toString(), null, Bank.cost(world, getBankAccount(username).toString()), TransactionType.MONEY_INQUIRY, world)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, username + " does not have enough funds for a bank in this world!");
    }

    Bank b = new Bank(getBankAccount(username), world);
    AccountUtils.getAccount(getBankAccount(username)).setBank(world, b);
    AccountUtils.transaction(getBankAccount(username).toString(), null, Bank.cost(world, getBankAccount(username).toString()), TransactionType.MONEY_REMOVE, world);
    return new EconomyResponse(0, 0, ResponseType.SUCCESS, username + " now owns a bank in this world!");
  }

  @Override
  @Deprecated
  public EconomyResponse deleteBank(String username) {
    return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Bank deletion is not supported in The New Economy!");
  }

  @Override
  @Deprecated
  public EconomyResponse depositPlayer(String username, double amount) {
    if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative amounts.");
    }
    api.fundsAdd(username, amount);
    return new EconomyResponse(amount, getBalance(username), ResponseType.SUCCESS, "");
  }

  @Override
  @Deprecated
  public EconomyResponse depositPlayer(String username, String world, double amount) {
    if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot deposit negative amounts.");
    }
    api.fundsAdd(username, world, amount);
    return new EconomyResponse(amount, getBalance(username, world), ResponseType.SUCCESS, "");
  }

  @Override
  @Deprecated
  public double getBalance(String username) {
    return api.getBalance(username);
  }

  @Override
  @Deprecated
  public double getBalance(String username, String world) {
    return api.getBalance(username, world);
  }

  @Override
  @Deprecated
  public boolean has(String username, double amount) {
    return api.fundsHas(username, amount);
  }

  @Override
  @Deprecated
  public boolean has(String username, String world, double amount) {
    return api.fundsHas(username, world, amount);
  }

  @Override
  @Deprecated
  public boolean hasAccount(String username) {
    return api.accountExists(username);
  }

  @Override
  @Deprecated
  public boolean hasAccount(String username, String world) {
    return hasAccount(username);
  }

  @Override
  @Deprecated
  public EconomyResponse isBankMember(String name, String username) {
    if(Bank.bankMember(getBankAccount(name), IDFinder.getID(username))) {
      return new EconomyResponse(0, 0, ResponseType.SUCCESS, username + " is a member of this bank!");
    }
    return new EconomyResponse(0, 0, ResponseType.FAILURE, username + " is not a member of this bank!");
  }

  @Override
  @Deprecated
  public EconomyResponse isBankOwner(String name, String username) {
    if(AccountUtils.getAccount(getBankAccount(name)).getBank(IDFinder.getWorld(getBankAccount(username))).getOwner().equals(IDFinder.getID(username))) {
      return new EconomyResponse(0, 0, ResponseType.SUCCESS, username + " is the owner of this bank!");
    }
    return new EconomyResponse(0, 0, ResponseType.FAILURE, username + " is not the owner of this bank!");
  }

  @Override
  @Deprecated
  public EconomyResponse withdrawPlayer(String username, double amount) {
    if(!hasAccount(username)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
    }

    if(!has(username, amount)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds!");
    }
    api.fundsRemove(username, amount);
    return new EconomyResponse(amount, getBalance(username), ResponseType.SUCCESS, "");
  }

  @Override
  @Deprecated
  public EconomyResponse withdrawPlayer(String username, String world, double amount) {
    if(!hasAccount(username)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "That account does not exist!");
    }

    if(amount < 0) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot withdraw negative amounts.");
    }

    if(!has(username, world, amount)) {
      return new EconomyResponse(0, 0, ResponseType.FAILURE, "Insufficient funds!");
    }
    api.fundsRemove(username, world, amount);
    return new EconomyResponse(amount, getBalance(username, world), ResponseType.SUCCESS, "");
  }

  public UUID getBankAccount(String identifier) {
    return IDFinder.getID(identifier);
  }

}