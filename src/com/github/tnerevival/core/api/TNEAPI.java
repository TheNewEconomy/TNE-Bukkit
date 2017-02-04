package com.github.tnerevival.core.api;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
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

  /**
   * Used to get a player's UUID from their username or string version of UUID.
   * @param identifier The player's username of stringified version of their UUID.
   * @return The UUID for the identifier, or null if it doesn't exist.
   */
  public UUID getID(String identifier) {
    return IDFinder.getID(identifier);
  }

  /**
   * Used to determine if a player has an economy account.
   * @param identifier The player's username of stringified version of their UUID.
   * @return True if the account exists, otherwise false.
   */
  public Boolean accountExists(String identifier) {
    return AccountUtils.getAccount(IDFinder.getID(identifier)) != null;
  }

  /**
   * Create an economy account for the player using the specific identifier.
   * @param identifier The player's username of stringified version of their UUID.
   */
  public void createAccount(String identifier) {
    AccountUtils.createAccount(IDFinder.getID(identifier));
  }

  /**
   * Used to get a player's economy account.
   * @param identifier The player's username of stringified version of their UUID.
   * @return The player's account if it exists, otherwise null.
   */
  public Account getAccount(String identifier) {
    return AccountUtils.getAccount(IDFinder.getID(identifier));
  }

  /**
   * Add funds to a player's economy account.
   * @param identifier The player's username of stringified version of their UUID.
   * @param amount The amount of funds to add to the player's account.
   */
  @Deprecated
  public void fundsAdd(String identifier, Double amount) {
    fundsAdd(identifier, IDFinder.getWorld(IDFinder.getID(identifier)), amount);
  }

  /**
   * Add funds to a player's economy account.
   * @param identifier The player's username of stringified version of their UUID.
   * @param world The world balance to perform this action on.
   * @param amount The amount of funds to add to the player's account.
   */
  @Deprecated
  public void fundsAdd(String identifier, String world, Double amount) {
    AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, plugin.manager.currencyManager.get(world), TransactionType.MONEY_GIVE, world);
  }

  /**
   * Add funds to a player's economy account.
   * @param identifier The player's username of stringified version of their UUID.
   * @param world The world balance to perform this action on.
   * @param amount The amount of funds to add to the player's account.
   * @param currency The currency of the funds.
   */
  @Deprecated
  public void fundsAdd(String identifier, String world, Double amount, Currency currency) {
    AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, currency, TransactionType.MONEY_GIVE, world);
  }

  /**
   * Determines if the specified player has the specified amount of funds.
   * @param identifier The player's username of stringified version of their UUID.
   * @param amount The amount of funds to check for.
   * @return Whether or not this player has the specified funds.
   */
  @Deprecated
  public Boolean fundsHas(String identifier, Double amount) {
    return fundsHas(identifier, IDFinder.getWorld(IDFinder.getID(identifier)), amount);
  }

  /**
   * Determines if the specified player has the specified amount of funds.
   * @param identifier The player's username of stringified version of their UUID.
   * @param world The world balance to perform this action on.
   * @param amount The amount of funds to check for.
   * @return Whether or not this player has the specified funds.
   */
  @Deprecated
  public Boolean fundsHas(String identifier, String world, Double amount) {
    return AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, plugin.manager.currencyManager.get(world), TransactionType.MONEY_INQUIRY, world);
  }

  /**
   * Determines if the specified player has the specified amount of funds.
   * @param identifier The player's username of stringified version of their UUID.
   * @param world The world balance to perform this action on.
   * @param amount The amount of funds to check for.
   * @param currency The currency of the funds.
   * @return Whether or not this player has the specified funds.
   */
  @Deprecated
  public Boolean fundsHas(String identifier, String world, Double amount, Currency currency) {
    return AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, currency, TransactionType.MONEY_INQUIRY, world);
  }


  /**
   * Remove funds from a player's economy account.
   * @param identifier The player's username of stringified version of their UUID.
   * @param amount The amount of funds to remove to the player's account.
   */
  @Deprecated
  public void fundsRemove(String identifier, Double amount) {
    fundsRemove(identifier, IDFinder.getWorld(IDFinder.getID(identifier)), amount);
  }


  /**
   * Remove funds from a player's economy account.
   * @param identifier The player's username of stringified version of their UUID.
   * @param world The world balance to perform this action on.
   * @param amount The amount of funds to remove to the player's account.
   */
  @Deprecated
  public void fundsRemove(String identifier, String world, Double amount) {
    AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, plugin.manager.currencyManager.get(world), TransactionType.MONEY_REMOVE, world);
  }

  /**
   * Remove funds from a player's economy account.
   * @param identifier The player's username of stringified version of their UUID.
   * @param world The world balance to perform this action on.
   * @param amount The amount of funds to remove to the player's account.
   * @param currency The currency of the funds.
   */
  @Deprecated
  public void fundsRemove(String identifier, String world, Double amount, Currency currency) {
    AccountUtils.transaction(IDFinder.getID(identifier).toString(), null, amount, currency, TransactionType.MONEY_REMOVE, world);
  }

  /**
   * Get the specified player's balance.
   * @param identifier The player's username of stringified version of their UUID.
   * @return The balance for the specified player.
   */
  @Deprecated
  public Double getBalance(String identifier) {
    return AccountUtils.getFunds(IDFinder.getID(identifier));
  }

  /**
   * Get the specified player's balance.
   * @param identifier The player's username of stringified version of their UUID.
   * @param world The world balance to perform this action on.
   * @return The balance for the specified player.
   */
  @Deprecated
  public Double getBalance(String identifier, String world) {
    return AccountUtils.getFunds(IDFinder.getID(identifier), world);
  }

  /**
   * Get the  specified player's balance.
   * @param identifier The player's username of stringified version of their UUID.
   * @param world The world balance to perform this action on.
   * @param currency The currency of the funds.
   * @return The balance for the specified player.
   */
  @Deprecated
  public Double getBalance(String identifier, String world, Currency currency) {
    return AccountUtils.getFunds(IDFinder.getID(identifier), world, currency.getName());
  }

  /**
   * Set the specified player's balance.
   * @param identifier The player's username of stringified version of their UUID.
   * @param amount The new balance amount for this player.
   */
  @Deprecated
  public void setBalance(String identifier, Double amount) {
    AccountUtils.setFunds(IDFinder.getID(identifier), plugin.defaultWorld, amount, getCurrency(plugin.defaultWorld).getName());
  }

  /**
   * Set the specified player's balance.
   * @param identifier The player's username of stringified version of their UUID.
   * @param amount The new balance amount for this player.
   * @param world The world balance to perform this action on.
   */
  @Deprecated
  public void setBalance(String identifier, Double amount, String world) {
    AccountUtils.setFunds(IDFinder.getID(identifier), world, amount, getCurrency(plugin.defaultWorld).getName());
  }

  /**
   * Set the specified player's balance.
   * @param identifier The player's username of stringified version of their UUID.
   * @param amount The new balance amount for this player.
   * @param world The world balance to perform this action on.
   * @param currency The currency of the funds.
   */
  @Deprecated
  public void setBalance(String identifier, Double amount, String world, Currency currency) {
    AccountUtils.setFunds(IDFinder.getID(identifier), world, amount, currency.getName());
  }

  /*
   * Bank-related Methods.
   */

  /**
   * Create a bank for the specified owner in a specific world.
   * @param owner The identifier of the bank owner.
   * @param world The name of the world to use.
   */
  public void createBank(String owner, String world) {
    Bank b = new Bank(IDFinder.getID(owner), world);
    Account acc = getAccount(owner);
    acc.setBank(world, b);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  /**
   * Determiner whether or not the specified owner has a bank.
   * @param owner The identifier of the bank owner.
   * @return True if the owner has a bank, otherwise false.
   */
  public Boolean hasBank(String owner) {
    return AccountUtils.getAccount(IDFinder.getID(owner)).hasBank(TNE.instance.defaultWorld);
  }

  /**
   * Determiner whether or not the specified owner has a bank in a world.
   * @param owner The identifier of the bank owner.
   * @param world The name of the world to use.
   * @return True if the owner has a bank, otherwise false.
   */
  public Boolean hasBank(String owner, String world) {
    return AccountUtils.getAccount(IDFinder.getID(owner)).hasBank(world);
  }

  /**
   * Add a member to the specific owner's bank.
   * @param owner The identifier of the bank owner.
   * @param identifier The identifier of the player to add as a member.
   * @param world The name of the world to use.
   */
  public void addBankMember(String owner, String identifier, String world) {
    Bank b = BankUtils.getBank(IDFinder.getID(owner), world);
    b.addMember(IDFinder.getID(identifier));
    Account acc = getAccount(owner);
    acc.setBank(world, b);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  /**
   * Remove a member from the specific owner's bank.
   * @param owner The identifier of the bank owner.
   * @param identifier The identifier of the player to remove from the bank.
   * @param world The name of the world to use.
   */
  public void removeBankMember(String owner, String identifier, String world) {
    Bank b = BankUtils.getBank(IDFinder.getID(owner), world);
    b.removeMember(IDFinder.getID(identifier));
    Account acc = getAccount(owner);
    acc.setBank(world, b);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  /**
   * Determine whether or not a player is a member of a bank.
   * @param owner The identifier of the bank owner.
   * @param identifier The identifier of the player.
   * @param world The name of the world to use.
   * @return True if the player is a member, otherwise false.
   */
  public Boolean bankMember(String owner, String identifier, String world) {
    return BankUtils.bankMember(IDFinder.getID(owner), IDFinder.getID(identifier), world);
  }

  /**
   * Get the balance of a bank.
   * @param owner The identifier of the bank owner.
   * @return The balance of the bank.
   */
  @Deprecated
  public Double getBankBalance(String owner) {
    return getBankBalance(owner, plugin.defaultWorld);
  }

  /**
   * Get the balance of a bank.
   * @param owner The identifier of the bank owner.
   * @param world The name of the world to use.
   * @return The balance of the bank.
   */
  @Deprecated
  public Double getBankBalance(String owner, String world) {
    return BankUtils.getBank(IDFinder.getID(owner), world).getGold();
  }

  /**
   * Set the balance of a bank to a new amount.
   * @param owner The identifier of the bank owner.
   * @param amount The new amount for the bank balance.
   */
  @Deprecated
  public void setBankBalance(String owner, Double amount) {
    setBankBalance(owner, plugin.defaultWorld, amount);
  }

  /**
   * Set the balance of a bank to a new amount.
   * @param owner The identifier of the bank owner.
   * @param world The name of the world to use.
   * @param amount The new amount for the bank balance.
   */
  @Deprecated
  public void setBankBalance(String owner, String world, Double amount) {
    BankUtils.getBank(IDFinder.getID(owner), world).setGold(amount);
  }
  
  /*
   * Vault-related methods
   */
  /**
   * Create a vault for the specified owner in a specific world.
   * @param owner The identifier of the vault owner.
   * @param world The name of the world to use.
   */
  public void createVault(String owner, String world) {
    Vault vault = new Vault(IDFinder.getID(owner), world, Vault.size(world, owner));
    Account acc = getAccount(owner);
    acc.setVault(world, vault);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  /**
   * Determiner whether or not the specified owner has a vault.
   * @param owner The identifier of the vault owner.
   * @return True if the owner has a vault, otherwise false.
   */
  public Boolean hasVault(String owner) {
    return AccountUtils.getAccount(IDFinder.getID(owner)).hasVault(TNE.instance.defaultWorld);
  }

  /**
   * Determiner whether or not the specified owner has a vault in a world.
   * @param owner The identifier of the vault owner.
   * @param world The name of the world to use.
   * @return True if the owner has a vault, otherwise false.
   */
  public Boolean hasVault(String owner, String world) {
    return AccountUtils.getAccount(IDFinder.getID(owner)).hasVault(world);
  }

  /**
   * Add a member to the specific owner's vault.
   * @param owner The identifier of the vault owner.
   * @param identifier The identifier of the player to add as a member.
   * @param world The name of the world to use.
   */
  public void addVaultMember(String owner, String identifier, String world) {
    Vault vault = AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world);
    vault.addMember(IDFinder.getID(identifier));
    Account acc = getAccount(owner);
    acc.setVault(world, vault);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  /**
   * Remove a member from the specific owner's vault.
   * @param owner The identifier of the vault owner.
   * @param identifier The identifier of the player to remove from the vault.
   * @param world The name of the world to use.
   */
  public void removeVaultMember(String owner, String identifier, String world) {
    Vault vault = AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world);
    vault.removeMember(IDFinder.getID(identifier));
    Account acc = getAccount(owner);
    acc.setVault(world, vault);
    TNE.instance.manager.accounts.put(acc.getUid(), acc);
  }

  /**
   * Determine whether or not a player is a member of a vault.
   * @param owner The identifier of the vault owner.
   * @param identifier The identifier of the player.
   * @param world The name of the world to use.
   * @return True if the player is a member, otherwise false.
   */
  public Boolean vaultMember(String owner, String identifier, String world) {
    return AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world).getMembers().contains(IDFinder.getID(identifier));
  }

  /**
   * Determine whether or not a vault slot is occupied.
   * @param owner The identifier of the vault owner.
   * @param slot The slot to check.
   * @return True if the slot is occupied, otherwise false.
   */
  public Boolean vaultHasItem(String owner, Integer slot) {
    return vaultHasItem(owner, plugin.defaultWorld, slot);
  }

  /**
   * Determine whether or not a vault slot is occupied.
   * @param owner The identifier of the vault owner.
   * @param world The name of the world to use.
   * @param slot The slot to check.
   * @return True if the slot is occupied, otherwise false.
   */
  public Boolean vaultHasItem(String owner, String world, Integer slot) {
    Vault vault = AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world);
    return vault.getItem(slot) != null && !vault.getItem(slot).toItemStack().getType().equals(Material.AIR);
  }

  /**
   * Get the itemstack in a slot of a bank.
   * @param owner The identifier of the bank owner.
   * @param slot The slot to use.
   * @return An ItemStack instance if the slot is occupied, otherwise null.
   */
  public ItemStack getVaultItem(String owner, Integer slot) {
    return getVaultItem(owner, plugin.defaultWorld, slot);
  }

  /**
   * Get the itemstack in a slot of a vault.
   * @param owner The identifier of the vault owner.
   * @param world The name of the world to use.
   * @param slot The slot to use.
   * @return An ItemStack instance if the slot is occupied, otherwise null.
   */
  public ItemStack getVaultItem(String owner, String world, Integer slot) {
    return AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world).getItem(slot).toItemStack();
  }

  /**
   * Get a list of items in a vault.
   * @param owner The identifier of the vault owner.
   * @return A list of ItemStack instances.
   */
  public List<ItemStack> getVaultItems(String owner) {
    return getVaultItems(owner, plugin.defaultWorld);
  }

  /**
   * Get a list of items in a vault.
   * @param owner The identifier of the vault owner.
   * @param world The name of the world to use.
   * @return A list of ItemStack instances.
   */
  public List<ItemStack> getVaultItems(String owner, String world) {
    Vault vault = AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world);
    List<SerializableItemStack> serialized = vault.getItems();
    List<ItemStack> items = new ArrayList<>();
    for(SerializableItemStack item : serialized) {
      items.add(item.toItemStack());
    }
    return items;
  }

  /**
   * Add an item to a vault.
   * @param owner The identifier of the vault owner.
   * @param slot The slot to use.
   * @param stack The item to add.
   */
  public void addVaultItem(String owner, Integer slot, ItemStack stack) {
    addVaultItem(owner, plugin.defaultWorld, slot, stack);
  }

  /**
   * Add an item to a vault.
   * @param owner The identifier of the vault owner.
   * @param world The name of the world to use.
   * @param slot The slot to use.
   * @param stack The item to add.
   */
  public void addVaultItem(String owner, String world, Integer slot, ItemStack stack) {
    Vault vault = AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world);
    vault.addItem(slot, stack);
    AccountUtils.getAccount(IDFinder.getID(owner)).setVault(world, vault);
  }

  /**
   * Set a vault's items.
   * @param owner The identifier of the vault owner.
   * @param items The new list of items.
   */
  public void setVaultItems(String owner, Collection<ItemStack> items) {
    setVaultItems(owner, plugin.defaultWorld, items);
  }

  /**
   * Set a vault's items.
   * @param owner The identifier of the vault owner.
   * @param world The name of the world to use.
   * @param items The new list of items.
   */
  public void setVaultItems(String owner, String world, Collection<ItemStack> items) {
    Vault vault = AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world);
    List<SerializableItemStack> serialized = vault.getItems();
    for(ItemStack item : items) {
      serialized.add(new SerializableItemStack(serialized.size(), item));
    }
    vault.setItems(serialized);
    AccountUtils.getAccount(IDFinder.getID(owner)).setVault(world, vault);
  }

  /**
   * Get the inventory instance of a player's vault.
   * @param owner The identifier of the vault's owner.
   * @return The inventory instance of the vault.
   */
  public Inventory getVaultInventory(String owner) {
    return getVaultInventory(owner, TNE.instance.defaultWorld);
  }

  /**
   * Get the inventory instance of a player's vault.
   * @param owner The identifier of the vault's owner.
   * @param world The name of the world to use.
   * @return The inventory instance of the vault.
   */
  public Inventory getVaultInventory(String owner, String world) {
    return AccountUtils.getAccount(IDFinder.getID(owner)).getVault(world).getInventory();
  }

  /*
   * Currency-related Methods.
   */
  /**
   * Format the specified amount based on this server's configurations.
   * @param amount The amount to format.
   * @return The formatted balance.
   */
  @Deprecated
  public String format(Double amount) {
    return CurrencyFormatter.format(plugin.defaultWorld, amount);
  }

  /**
   * Format the specified amount based on this server's configurations.
   * @param world The name of the world, for world-specific configurations.
   * @param amount The amount to format.
   * @return The formatted balance.
   */
  @Deprecated
  public String format(String world, Double amount) {
    return CurrencyFormatter.format(world, amount);
  }


  /**
   * Format the specified amount based on this server's configurations.
   * @param name The name of the currency for formatting purposes.
   * @param world The name of the world, for world-specific configurations.
   * @param amount The amount to format.
   * @return The formatted balance.
   */
  @Deprecated
  public String format(String name, String world, Double amount) {
    return CurrencyFormatter.format(world, name, amount);
  }

  /**
   * Retrieve the default currency's shortened format.
   * @return The shortened format of the default currency.
   */
  public Boolean getShorten() {
    return getShorten(plugin.defaultWorld);
  }

  /**
   * Retrieve the world default currency's shortened format.
   * @param world The name of the world to use.
   * @return The shortened format of the default currency.
   */
  public Boolean getShorten(String world) {
    return plugin.manager.currencyManager.get(world).shorten();
  }

  /**
   * Retrieve the currency's shortened format.
   * @param world The name of the world to use.
   * @param currencyName The name of the currency to use.
   * @return The shortened format of the default currency if the specified currency exists,
   * otherwise null.
   */
  public Boolean getShorten(String world, String currencyName) {
    if(!currencyExists(world, currencyName)) return null;
    return plugin.manager.currencyManager.get(world, currencyName).shorten();
  }

  /**
   * Get the name of the default currency. This will return the major/minor singular/plural name based on the parameters given.
   * @param major Whether or not to retrieve the currency's major name.
   * @param singular Whether or not to retrieve the plural name.
   * @return The currency's name based on the given parameters.
   */
  public String getCurrencyName(Boolean major, Boolean singular) {
    return getCurrencyName(major, singular, plugin.defaultWorld);
  }


  /**
   * Get the name of the default currency for the specified world. This will return the major/minor singular/plural name based on the parameters given.
   * @param major Whether or not to retrieve the currency's major name.
   * @param singular Whether or not to retrieve the plural name.
   * @param world The name of the world to use.
   * @return The currency's name based on the given parameters.
   */
  public String getCurrencyName(Boolean major, Boolean singular, String world) {
    return (major) ? plugin.manager.currencyManager.get(world).getMajor(singular)  : plugin.manager.currencyManager.get(world).getMinor(singular);
  }

  /**
   * Determine whether or not a specific currency exists.
   * @param name The name of the currency to use.
   * @return True if the currency exists, otherwise false.
   */
  public Boolean currencyExists(String name) {
    return plugin.manager.currencyManager.contains(TNE.instance.defaultWorld, name);
  }

  /**
   * Determine whether or not a specific currency exists.
   * @param world The name of the world to use.
   * @param name The name of the currency to use.
   * @return True if the currency exists in the specific world, otherwise false.
   */
  public Boolean currencyExists(String world, String name) {
    return plugin.manager.currencyManager.contains(world, name);
  }

  /**
   * Get the instance of a currency based on its world and name.
   * @param world The name of the world to use.
   * @return The instance of the currency if it exists, otherwise null.
   */
  public Currency getCurrency(String world) {
    return plugin.manager.currencyManager.get(world);
  }

  /**
   * Get the instance of a currency based on its world and name.
   * @param world The name of the world to use.
   * @param name The name of the currency.
   * @return The instance of the currency if it exists, otherwise null.
   */
  public Currency getCurrency(String world, String name) {
    return plugin.manager.currencyManager.get(world, name);
  }

  /**
   * Get a map of all the currencies.
   * @return A map containing every currency with the world as the key, and instance as value.
   */
  public Map<String, Currency> getCurrencies() {
    return plugin.manager.currencyManager.getCurrencies();
  }

  /**
   * Get a list of all the currencies for a specific world.
   * @param world The name of the world to use.
   * @return A list containing every currency for the specified world.
   */
  public List<Currency> getCurrencies(String world) {
    return plugin.manager.currencyManager.getWorldCurrencies(world);
  }

  /*
   * Shop-related Methods.
   */

  /**
   * Determine whether or not there is a shop with the specified name.
   * @param name The name of the shop.
   * @return True if the shop exists, otherwise false.
   */
  public Boolean shopExists(String name) {
    return shopExists(name, plugin.defaultWorld);
  }

  /**
   * Determine whether or not there is a shop with the specified name in a specific world.
   * @param name The name of the shop.
   * @param world The name of the world to check.
   * @return True if the shop exists, otherwise false.
   */
  public Boolean shopExists(String name, String world) {
    return Shop.exists(name, world);
  }

  /**
   * Get the instance of a shop based on its name.
   * @param name The name of the shop.
   * @return The instance of the shop if it exists, otherwise null.
   */
  public Shop getShop(String name) {
    return getShop(name, plugin.defaultWorld);
  }

  /**
   * Get the instance of a shop based on its name and world.
   * @param name The name of the shop.
   * @param world The name of the world to check.
   * @return The instance of the shop if it exists, otherwise null.
   */
  public Shop getShop(String name, String world) {
    return Shop.getShop(name, world);
  }

  /*
   * Sign-related Methods.
   */

  /**
   * Determine whether or not there is a TNESign at the specified location
   * @param location The location to check for a sign.
   * @return True if there is a TNESign, otherwise false.
   */
  public Boolean validSign(Location location) {
    return SignUtils.validSign(location);
  }

  /**
   * Get the instance of a TNESign based on its location.
   * @param location The location of the sign.
   * @return The instance of the sign at the location if it exists, otherwise null.
   */
  public TNESign getSign(Location location) {
    return SignUtils.getSign(new SerializableLocation(location));
  }

  /**
   * Create an empty instance of a TNESign with the specified type, and owner.
   * @param type The sign type for this sign.
   * @param owner The string identifier of the owner for this sign.
   * @return The sign instance based on the type, and owner.
   */
  public TNESign createInstance(SignType type, String owner) {
    return createInstance(type, IDFinder.getID(owner));
  }


  /**
   * Create an empty instance of a TNESign with the specified type, and owner.
   * @param type The sign type for this sign.
   * @param owner The UUID of the owner for this sign.
   * @return The sign instance based on the type, and owner.
   */
  public TNESign createInstance(SignType type, UUID owner) {
    return SignUtils.instance(type.getName(), owner);
  }

  /**
   * Remove the TNESign located at the specified location.
   * @param location The location of the sign.
   * @return True if the sign was removed, otherwise false.
   */
  public Boolean removeSign(Location location) {
    if(!validSign(location)) return false;
    SignUtils.removeSign(new SerializableLocation(location));
    return true;
  }

  /*
   * Configuration-related Methods.
   */
  /**
   * Get the value of a String configuration.
   * @param configuration The configuration node.
   * @return The value of the configuration.
   */
  public String getString(String configuration) {
    return (String)getConfiguration(configuration, TNE.instance.defaultWorld);
  }

  /**
   * Get the value of a String configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @return The value of the configuration.
   */
  public String getString(String configuration, String world) {
    return (String)getConfiguration(configuration, world, "");
  }

  /**
   * Get the value of a String configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param uuid The uuid of the player to use.
   * @return The value of the configuration.
   */
  public String getString(String configuration, String world, UUID uuid) {
    return (String)getConfiguration(configuration, world, uuid.toString());
  }

  /**
   * Get the value of a String configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param player The identifier of the player to use.
   * @return The value of the configuration.
   */
  public String getString(String configuration, String world, String player) {
    return (String)getConfiguration(configuration, world, player);
  }

  /**
   * Get the value of a Boolean configuration.
   * @param configuration The configuration node.
   * @return The value of the configuration.
   */
  public Boolean getBoolean(String configuration) {
    return (Boolean)getConfiguration(configuration, TNE.instance.defaultWorld);
  }

  /**
   * Get the value of a Boolean configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @return The value of the configuration.
   */
  public Boolean getBoolean(String configuration, String world) {
    return (Boolean)getConfiguration(configuration, world, "");
  }

  /**
   * Get the value of a Boolean configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param uuid The uuid of the player to use.
   * @return The value of the configuration.
   */
  public Boolean getBoolean(String configuration, String world, UUID uuid) {
    return (Boolean)getConfiguration(configuration, world, uuid.toString());
  }

  /**
   * Get the value of a Boolean configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param player The identifier of the player to use.
   * @return The value of the configuration.
   */
  public Boolean getBoolean(String configuration, String world, String player) {
    return (Boolean)getConfiguration(configuration, world, player);
  }

  /**
   * Get the value of a Double configuration.
   * @param configuration The configuration node.
   * @return The value of the configuration.
   */
  public Double getDouble(String configuration) {
    return getDouble(configuration, TNE.instance.defaultWorld);
  }

  /**
   * Get the value of a Double configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @return The value of the configuration.
   */
  public Double getDouble(String configuration, String world) {
    String value = getConfiguration(configuration, world, "").toString();
    return CurrencyFormatter.translateDouble(value, world);
  }

  /**
   * Get the value of a Double configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param uuid The uuid of the player to use.
   * @return The value of the configuration.
   */
  public Double getDouble(String configuration, String world, UUID uuid) {
    String value = getConfiguration(configuration, world, uuid).toString();
    return CurrencyFormatter.translateDouble(value, world);
  }

  /**
   * Get the value of a Double configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param player The identifier of the player to use.
   * @return The value of the configuration.
   */
  public Double getDouble(String configuration, String world, String player) {
    String value = getConfiguration(configuration, world, player).toString();
    return CurrencyFormatter.translateDouble(value, world);
  }

  /**
   * Get the value of a Integer configuration.
   * @param configuration The configuration node.
   * @return The value of the configuration.
   */
  public Integer getInteger(String configuration) {
    return (Integer)getConfiguration(configuration, TNE.instance.defaultWorld);
  }

  /**
   * Get the value of a Integer configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @return The value of the configuration.
   */
  public Integer getInteger(String configuration, String world) {
    return (Integer)getConfiguration(configuration, world, "");
  }

  /**
   * Get the value of a Integer configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param uuid The uuid of the player to use.
   * @return The value of the configuration.
   */
  public Integer getInteger(String configuration, String world, UUID uuid) {
    return (Integer)getConfiguration(configuration, world, uuid.toString());
  }

  /**
   * Get the value of a Integer configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param player The identifier of the player to use.
   * @return The value of the configuration.
   */
  public Integer getInteger(String configuration, String world, String player) {
    return (Integer)getConfiguration(configuration, world, player);
  }

  /**
   * Determine if the specified configuration exists.
   * @param configuration The configuration node.
   * @return True if it exists, otherwise false.
   */
  public Boolean hasConfiguration(String configuration) {
    if(configuration.toLowerCase().contains("database")) return false;
    return TNE.configurations.hasConfiguration(configuration);
  }

  /**
   * Get the value of a configuration.
   * @param configuration The configuration node.
   * @return The value of the configuration.
   */
  public Object getConfiguration(String configuration) {
    return getConfiguration(configuration, TNE.instance.defaultWorld);
  }

  /**
   * Get the value of a configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @return The value of the configuration.
   */
  public Object getConfiguration(String configuration, String world) {
    return getConfiguration(configuration, world, "");
  }

  /**
   * Get the value of a configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param uuid The uuid of the player to use.
   * @return The value of the configuration.
   */
  public Object getConfiguration(String configuration, String world, UUID uuid) {
    return getConfiguration(configuration, world, uuid.toString());
  }

  /**
   * Get the value of a configuration.
   * @param configuration The configuration node.
   * @param world The name of the world to use.
   * @param player The identifier of the player to use.
   * @return The value of the configuration.
   */
  public Object getConfiguration(String configuration, String world, String player) {
    if(configuration.toLowerCase().contains("database")) return "";
    return TNE.configurations.getConfiguration(configuration, world, player);
  }

  /**
   * Set a configuration value.
   * @param configuration The configuration node.
   * @param value The new value for the configuration.
   */
  public void setConfiguration(String configuration, Object value) {
    if(configuration.toLowerCase().contains("database")) return;
    TNE.configurations.setConfiguration(configuration, value);
  }
}