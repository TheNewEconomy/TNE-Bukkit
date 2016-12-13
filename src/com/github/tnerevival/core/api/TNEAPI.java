package com.github.tnerevival.core.api;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.SignType;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.SignUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TNEAPI {

  public TNE plugin;

  public TNEAPI(TNE plugin) {
    this.plugin = plugin;
  }


  /*
   * Account-related Methods
   */
  public UUID getID(String identifier) {
    return IDFinder.getID(identifier);
  }

  public Boolean accountExists(String identifier) {
    return AccountUtils.getAccount(IDFinder.getID(identifier)) != null;
  }

  public void createAccount(String identifier) {
    AccountUtils.createAccount(IDFinder.getID(identifier));
  }

  public Account getAccount(String identifier) {
    return AccountUtils.getAccount(IDFinder.getID(identifier));
  }

  public void fundsAdd(String identifier, Double amount) {
    fundsAdd(identifier, MISCUtils.getWorld(IDFinder.getID(identifier)), amount);
  }

  public void fundsAdd(String identifier, String world, Double amount) {
    AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, plugin.manager.currencyManager.get(world), TransactionType.MONEY_GIVE, world);
  }

  public void fundsAdd(String identifier, String world, Double amount, Currency currency) {
    AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, currency, TransactionType.MONEY_GIVE, world);
  }

  public Boolean fundsHas(String identifier, Double amount) {
    return fundsHas(identifier, MISCUtils.getWorld(IDFinder.getID(identifier)), amount);
  }

  public Boolean fundsHas(String identifier, String world, Double amount) {
    return AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, plugin.manager.currencyManager.get(world), TransactionType.MONEY_INQUIRY, world);
  }

  public Boolean fundsHas(String identifier, String world, Double amount, Currency currency) {
    return AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, currency, TransactionType.MONEY_INQUIRY, world);
  }

  public void fundsRemove(String identifier, Double amount) {
    fundsRemove(identifier, MISCUtils.getWorld(IDFinder.getID(identifier)), amount);
  }

  public void fundsRemove(String identifier, String world, Double amount) {
    AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, plugin.manager.currencyManager.get(world), TransactionType.MONEY_REMOVE, world);
  }

  public void fundsRemove(String identifier, String world, Double amount, Currency currency) {
    AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, currency, TransactionType.MONEY_REMOVE, world);
  }

  public Double getBalance(String identifier) {
    return AccountUtils.getFunds(IDFinder.getID(identifier));
  }

  public Double getBalance(String identifier, String world) {
    return AccountUtils.getFunds(IDFinder.getID(identifier), world);
  }

  public Double getBalance(String identifier, String world, Currency currency) {
    return AccountUtils.getFunds(IDFinder.getID(identifier), world, currency.getName());
  }

  public void setBalance(String identifier, Double amount) {
    AccountUtils.setFunds(IDFinder.getID(identifier), plugin.defaultWorld, amount, getCurrency(plugin.defaultWorld).getName());
  }

  public void setBalance(String identifier, Double amount, String world) {
    AccountUtils.setFunds(IDFinder.getID(identifier), world, amount, getCurrency(plugin.defaultWorld).getName());
  }

  public void setBalance(String identifier, Double amount, String world, Currency currency) {
    AccountUtils.setFunds(IDFinder.getID(identifier), world, amount, currency.getName());
  }

  /*
   * Bank-related Methods.
   */
  public void createBank(String owner, String world) {
    Bank b = new Bank(IDFinder.getID(owner), BankUtils.size(world, IDFinder.getID(owner).toString()));
    Account acc = getAccount(owner);
    acc.setBank(world, b);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  public Boolean hasBank(String owner) {
    return BankUtils.hasBank(IDFinder.getID(owner));
  }
  
  public Boolean hasBank(String owner, String world) {
    return BankUtils.hasBank(IDFinder.getID(owner), world);
  }

  public void addMember(String owner, String identifier, String world) {
    Bank b = BankUtils.getBank(IDFinder.getID(owner), world);
    b.addMember(IDFinder.getID(identifier));
    Account acc = getAccount(owner);
    acc.setBank(world, b);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  public void removeMember(String owner, String identifier, String world) {
    Bank b = BankUtils.getBank(IDFinder.getID(owner), world);
    b.removeMember(IDFinder.getID(identifier));
    Account acc = getAccount(owner);
    acc.setBank(world, b);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  public Boolean bankMember(String owner, String identifier, String world) {
    return BankUtils.bankMember(IDFinder.getID(owner), IDFinder.getID(identifier), world);
  }

  public Double getBankBalance(String owner) {
    return getBankBalance(owner, plugin.defaultWorld);
  }

  public Double getBankBalance(String owner, String world) {
    return BankUtils.getBank(IDFinder.getID(owner), world).getGold();
  }

  public void setBankBalance(String owner, Double amount) {
    setBankBalance(owner, plugin.defaultWorld, amount);
  }

  public void setBankBalance(String owner, String world, Double amount) {
    BankUtils.getBank(IDFinder.getID(owner), world).setGold(amount);
  }

  public Boolean bankHasItem(String owner, Integer slot) {
    return bankHasItem(owner, plugin.defaultWorld, slot);
  }

  public Boolean bankHasItem(String owner, String world, Integer slot) {
    Bank b = BankUtils.getBank(IDFinder.getID(owner), world);

    return b.getItem(slot) != null && !b.getItem(slot).toItemStack().getType().equals(Material.AIR);
  }

  public ItemStack getBankItem(String owner, Integer slot) {
    return getBankItem(owner, plugin.defaultWorld, slot);
  }

  public ItemStack getBankItem(String owner, String world, Integer slot) {
    return BankUtils.getBank(IDFinder.getID(owner), world).getItem(slot).toItemStack();
  }

  public List<ItemStack> getBankItems(String owner) {
    return getBankItems(owner, plugin.defaultWorld);
  }

  public List<ItemStack> getBankItems(String owner, String world) {
    Bank b = BankUtils.getBank(IDFinder.getID(owner), world);
    List<SerializableItemStack> serialized = b.getItems();
    List<ItemStack> items = new ArrayList<>();
    for(SerializableItemStack item : serialized) {
      items.add(item.toItemStack());
    }
    return items;
  }

  public void addBankItem(String owner, Integer slot, ItemStack stack) {
    addBankItem(owner, plugin.defaultWorld, slot, stack);
  }

  public void addBankItem(String owner, String world, Integer slot, ItemStack stack) {
    Bank b = BankUtils.getBank(IDFinder.getID(owner), world);
    b.addItem(slot, stack);
  }

  public void setBankItems(String owner, Collection<ItemStack> items) {
    setBankItems(owner, plugin.defaultWorld, items);
  }

  public void setBankItems(String owner, String world, Collection<ItemStack> items) {
    Bank b = BankUtils.getBank(IDFinder.getID(owner), world);
    List<SerializableItemStack> serialized = b.getItems();
    for(ItemStack item : items) {
      serialized.add(new SerializableItemStack(serialized.size(), item));
    }
    b.setItems(serialized);
  }

  public Inventory getBankInventory(String owner) {
    return BankUtils.getBankInventory(IDFinder.getID(owner));
  }

  public Inventory getBankInventory(String owner, String world) {
    return BankUtils.getBankInventory(IDFinder.getID(owner), world);
  }

  /*
   * Currency-related Methods.
   */
  public String format(Double amount) {
    return CurrencyFormatter.format(plugin.defaultWorld, amount);
  }

  public String format(String world, Double amount) {
    return CurrencyFormatter.format(world, amount);
  }

  public String getCurrencyName(Boolean major, Boolean singular) {
    return getCurrencyName(major, singular, plugin.defaultWorld);
  }

  public String getCurrencyName(Boolean major, Boolean singular, String world) {
    return (major) ? plugin.manager.currencyManager.get(world).getMajor(singular)  : plugin.manager.currencyManager.get(world).getMinor(singular);
  }

  public Boolean getShorten() {
    return getShorten(plugin.defaultWorld);
  }

  public Boolean getShorten(String world) {
    return plugin.manager.currencyManager.get(world).shorten();
  }

  public Boolean currencyExists(String world, String name) {
    return plugin.manager.currencyManager.contains(world, name);
  }

  public Currency getCurrency(String world) {
    return plugin.manager.currencyManager.get(world);
  }

  public Currency getCurrency(String world, String name) {
    return plugin.manager.currencyManager.get(world, name);
  }

  public Map<String, Currency> getCurrencies() {
    return plugin.manager.currencyManager.getCurrencies();
  }

  public List<Currency> getCurrencies(String world) {
    return plugin.manager.currencyManager.getWorldCurrencies(world);
  }

  /*
   * Shop-related Methods.
   */
  public Boolean shopExists(String name) {
    return shopExists(name, plugin.defaultWorld);
  }

  public Boolean shopExists(String name, String world) {
    return Shop.exists(name, world);
  }

  public Shop getShop(String name) {
    return getShop(name, plugin.defaultWorld);
  }

  public Shop getShop(String name, String world) {
    return Shop.getShop(name, world);
  }

  /*
   * Sign-related Methods.
   */
  public Boolean validSign(Location location) {
    return SignUtils.validSign(location);
  }

  public TNESign getSign(Location location) {
    return SignUtils.getSign(new SerializableLocation(location));
  }

  public TNESign createInstance(SignType type, String owner) {
    return createInstance(type, IDFinder.getID(owner));
  }

  public TNESign createInstance(SignType type, UUID owner) {
    return SignUtils.instance(type.getName(), owner);
  }

  public Boolean removeSign(Location location) {
    if(!validSign(location)) return false;
    SignUtils.removeSign(new SerializableLocation(location));
    return true;
  }

  /*
   * Configuration-related Methods.
   */
  public String getString(String configuration) {
    return (String)getConfiguration(configuration, TNE.instance.defaultWorld);
  }

  public String getString(String configuration, String world) {
    return (String)getConfiguration(configuration, world, "");
  }

  public String getString(String configuration, String world, UUID uuid) {
    return (String)getConfiguration(configuration, world, uuid.toString());
  }

  public String getString(String configuration, String world, String player) {
    return (String)getConfiguration(configuration, world, player);
  }

  public Boolean getBoolean(String configuration) {
    return (Boolean)getConfiguration(configuration, TNE.instance.defaultWorld);
  }

  public Boolean getBoolean(String configuration, String world) {
    return (Boolean)getConfiguration(configuration, world, "");
  }

  public Boolean getBoolean(String configuration, String world, UUID uuid) {
    return (Boolean)getConfiguration(configuration, world, uuid.toString());
  }

  public Boolean getBoolean(String configuration, String world, String player) {
    return (Boolean)getConfiguration(configuration, world, player);
  }

  public Double getDouble(String configuration) {
    return getDouble(configuration, TNE.instance.defaultWorld);
  }

  public Double getDouble(String configuration, String world) {
    String value = getConfiguration(configuration, world, "").toString();
    return CurrencyFormatter.translateDouble(value, world);
  }

  public Double getDouble(String configuration, String world, UUID uuid) {
    String value = getConfiguration(configuration, world, uuid).toString();
    return CurrencyFormatter.translateDouble(value, world);
  }

  public Double getDouble(String configuration, String world, String player) {
    String value = getConfiguration(configuration, world, player).toString();
    return CurrencyFormatter.translateDouble(value, world);
  }

  public Integer getInteger(String configuration) {
    return (Integer)getConfiguration(configuration, TNE.instance.defaultWorld);
  }

  public Integer getInteger(String configuration, String world) {
    return (Integer)getConfiguration(configuration, world, "");
  }

  public Integer getInteger(String configuration, String world, UUID uuid) {
    return (Integer)getConfiguration(configuration, world, uuid.toString());
  }

  public Integer getInteger(String configuration, String world, String player) {
    return (Integer)getConfiguration(configuration, world, player);
  }

  public Boolean hasConfiguration(String configuration) {
    if(configuration.toLowerCase().contains("database")) return false;
    return TNE.configurations.hasConfiguration(configuration);
  }

  public Object getConfiguration(String configuration) {
    return getConfiguration(configuration, TNE.instance.defaultWorld);
  }

  public Object getConfiguration(String configuration, String world) {
    return getConfiguration(configuration, world, "");
  }

  public Object getConfiguration(String configuration, String world, UUID uuid) {
    return getConfiguration(configuration, world, uuid.toString());
  }

  public Object getConfiguration(String configuration, String world, String player) {
    if(configuration.toLowerCase().contains("database")) return "";
    return TNE.configurations.getConfiguration(configuration, world, player);
  }

  public void setConfiguration(String configuration, Object value) {
    if(configuration.toLowerCase().contains("database")) return;
    TNE.configurations.setConfiguration(configuration, value);
  }
}