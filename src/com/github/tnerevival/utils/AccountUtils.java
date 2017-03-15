package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.TrackedItems;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.event.account.TNEAccountCreationEvent;
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.transaction.Transaction;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.core.transaction.TransactionType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AccountUtils {

  public static Boolean exists(UUID id) {
    return TNE.instance().manager.accounts.containsKey(id);
  }

  public static void createAccount(UUID id) {
    MISCUtils.debug("MISCUtils.createAccount(" + id.toString() + ")");
    Account a = new Account(id);
    String defaultCurrency = TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld).getName();
    TNEAccountCreationEvent e = new TNEAccountCreationEvent(id, a);
    MISCUtils.debug(e.getId() + "");
    Bukkit.getServer().getPluginManager().callEvent(e);
    if(TNE.instance().manager.special.contains(id)) {
      a.setSpecial(true);
      TNE.instance().manager.special.remove(id);
    }
    MISCUtils.debug("Special Account: " + a.isSpecial());
    TNE.instance().manager.accounts.put(e.getId(), e.getAccount());
    if(!a.isSpecial()) {
      setBalance(id, TNE.instance().defaultWorld, defaultCurrency, AccountUtils.getInitialBalance(TNE.instance().defaultWorld, defaultCurrency));
    }
    String identifier = (IDFinder.ecoToUsername(id) != null)? IDFinder.ecoToUsername(id) : id.toString();
    convertAccount(identifier);
  }

  private static void convertAccount(String username) {
    UUID id = IDFinder.getID(username);
    Account a = getAccount(id);
    if(new File(TNE.instance().getDataFolder(), "conversion.yml").exists()) {
      File conversionFile = new File(TNE.instance().getDataFolder(), "conversion.yml");
      FileConfiguration conversion = YamlConfiguration.loadConfiguration(conversionFile);

      if (conversion.contains("Converted." + username) || conversion.contains("Converted." + id.toString())) {
        String base = "Converted." + ((conversion.contains("Converted." + username)) ? username : id.toString());

        Set<String> worlds = conversion.getConfigurationSection(base + ".Funds").getKeys(false);

        for (String world : worlds) {
          String section = base + ".Funds." + world;

          Set<String> currencies = conversion.getConfigurationSection(section).getKeys(false);

          for(String currency : currencies) {

            BigDecimal amount = (conversion.contains(section + "." + currency + ".Amount")) ? new BigDecimal(conversion.getDouble(section + "." + currency + ".Amount")) : BigDecimal.ZERO;
            a.setBalance(world, amount, currency);
          }
        }
        conversion.set(base, null);
      }
      TNE.instance().manager.accounts.put(id, a);
    }
  }

  public static void convertedAdd(String identifier, String world, String currency, BigDecimal amount) {
    File conversionFile = new File(TNE.instance().getDataFolder(), "conversion.yml");
    FileConfiguration conversion = YamlConfiguration.loadConfiguration(conversionFile);

    BigDecimal starting = BigDecimal.ZERO;

    if(conversion.contains("Converted." + identifier + "." + world + "." + currency + ".Amount")) {
      starting = new BigDecimal(conversion.getDouble("Converted." + identifier + "." + world + "." + currency + ".Amount"));
    }

    conversion.set("Converted." + identifier + "." + world + "." + currency + ".Amount", (starting.add(amount).doubleValue()));
  }

  public static Account getAccount(UUID id) {
    if(!exists(id)) {
      createAccount(id);
    }
    return TNE.instance().manager.accounts.get(id);
  }

  private static BigDecimal getBalance(UUID id, String world, String currencyName) {
    MISCUtils.debug("getBalance(" + id.toString() + ", " + world + ", " + currencyName);
    world = IDFinder.getBalanceShareWorld(world);
    MISCUtils.debug("getBalance(" + id.toString() + ", " + world + ", " + currencyName);
    Account account = getAccount(id);
    Currency currency = TNE.instance().manager.currencyManager.get(world, currencyName);

    if(!account.getBalances().containsKey(world + ":" + currencyName)) {
      initializeWorldData(id);
    }

    if(!account.getStatus().getBalance()) return BigDecimal.ZERO;

    if(!account.isSpecial() && currency.isItem()) {
      MISCUtils.debug("GETTING ITEM CURRENCY");
      Material majorItem = MaterialHelper.getMaterial(currency.getTier("Major").getMaterial());
      Material minorItem = MaterialHelper.getMaterial(currency.getTier("Minor").getMaterial());
      Integer major = MISCUtils.getItemCount(id, majorItem);
      Integer minor = MISCUtils.getItemCount(id, minorItem);
      String balance = major + "." + minor;
      return new BigDecimal(balance);
    }

    if(MISCUtils.multiWorld()) {
      return account.getBalance(world, currencyName);
    }
    return account.getBalance(TNE.instance().defaultWorld, currencyName);
  }

  private static void setBalance(UUID id, String world, String currencyName, BigDecimal balance) {
    world = IDFinder.getBalanceShareWorld(world);
    Currency currency = TNE.instance().manager.currencyManager.get(world, currencyName);
    Account account = getAccount(id);

    if(!account.getStatus().getBalance()) return;

    BigDecimal rounded = round(world, currencyName, balance);
    String balanceString = (rounded.toString().contains("."))? rounded.toString() : rounded.toString() + ".0";
    MISCUtils.debug("AccountUtils.setBalance to " + balanceString);
    String[] split = balanceString.split("\\.");

    if(!account.isSpecial() && currency.isItem()) {
      MISCUtils.debug("SETTING ITEM CURRENCY");
      Material majorItem = MaterialHelper.getMaterial(currency.getTier("Major").getMaterial());
      Material minorItem = MaterialHelper.getMaterial(currency.getTier("Minor").getMaterial());
      MISCUtils.setItemCount(id, majorItem, 0);
      MISCUtils.setItemCount(id, minorItem, 0);
      MISCUtils.setItemCount(id, majorItem, Integer.valueOf(split[0].trim()));
      MISCUtils.setItemCount(id, minorItem, Integer.valueOf(split[1].trim()));
      return;
    }

    if(MISCUtils.multiWorld()) {
      account.setBalance(world, balance, currencyName);
    } else {
      account.setBalance(TNE.instance().defaultWorld, balance, currencyName);
    }
    TNE.instance().manager.accounts.put(id, account);
  }

  private static String pad(int amount) {
    String padding = "";

    for(int i = 0; i < amount; i++) {
      padding = padding + "0";
    }
    return padding;
  }

  public static Material trackedMaterial(Location location, int slot) {
    for(Account acc : TNE.instance().manager.accounts.values()) {
      Material mat = acc.trackedMaterial(location, slot);
      if(mat != null) return mat;
    }
    return null;
  }

  public static boolean trackedMaterial(String world, Material material) {
    if(material == null) return false;
    if(TNE.instance().manager.currencyManager.getTrackedCurrencies(world).size() <= 0) return false;
    for(Currency currency : TNE.instance().manager.currencyManager.getTrackedCurrencies(world)) {
      if(currency.isItem()) {
        return material.equals(MaterialHelper.getMaterial(currency.getTier("Minor").getMaterial()))
               || material.equals(MaterialHelper.getMaterial(currency.getTier("Major").getMaterial()));
      }
    }
    return false;
  }

  public static void track(UUID id, Location location, int slot, Material material) {
    Account account = getAccount(id);
    if(account != null) {
      TrackedItems items = account.getTrackedItems().get(location);
      if(items == null) items = new TrackedItems(location);

      items.getMaterialMap().put(slot, material);
      account.getTrackedItems().put(location, items);
      TNE.instance().manager.accounts.put(account.getUid(), account);
    }
  }

  public static void removeTracked(Location location, int slot) {
    Map<UUID, Account> modified = new HashMap<>();
    for(Account acc : TNE.instance().manager.accounts.values()) {
      if(acc.trackedMaterial(location, slot) != null) {
        TrackedItems items = acc.getTrackedItems().get(location);
        items.getMaterialMap().remove(slot);
        acc.getTrackedItems().put(location, items);
        modified.put(acc.getUid(), acc);
      }
    }
    TNE.instance().manager.accounts.putAll(modified);
  }

  public static BigDecimal round(String world, String currency, BigDecimal amount) {
    if(TNE.instance().manager.currencyManager.contains(world, currency)) {
      return amount.setScale(TNE.instance().manager.currencyManager.get(world, currency).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
    }

    if(TNE.instance().manager.currencyManager.contains(world)) {
      return amount.setScale(TNE.instance().manager.currencyManager.get(world).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
    }
    return amount.setScale(TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
  }

  public static boolean transaction(String initiator, String recipient, BigDecimal amount, TransactionType type, String world) {
    return transaction(initiator, recipient, new TransactionCost(amount), type, world);
  }

  public static boolean transaction(String initiator, String recipient, BigDecimal amount, Currency currency, TransactionType type, String world) {
    return transaction(initiator, recipient, new TransactionCost(amount, currency), type, world);
  }

  public static boolean transaction(String initiator, String recipient, TransactionCost cost, TransactionType type, String world) {
    Transaction t = new Transaction(initiator, recipient, cost, type, world);
    return t.perform();
  }

  public static BigDecimal getFunds(UUID id) {
    return getFunds(id, IDFinder.getWorld(id));
  }

  public static BigDecimal getFunds(UUID id, String world) {
    return getFunds(id, world, TNE.instance().manager.currencyManager.get(world).getName());
  }

  public static BigDecimal getFunds(UUID id, String world, String currency) {
    MISCUtils.debug("AccountUtils.getFunds(" + id.toString() + ", " + world + ", " + currency + ")");
    BigDecimal funds = getBalance(id, world, currency);
    if(TNE.instance().api().getBoolean("Core.Bank.Connected", world)) {
      funds = funds.add(Bank.getBankBalance(id, world, currency));
    }
    return funds;
  }

  public static void setFunds(UUID id, String world, BigDecimal amount, String currency) {
    setBalance(id, world, currency, amount);
  }

  public static void removeFunds(UUID id, String world, BigDecimal amount, String currency) {
    BigDecimal difference = amount.subtract(getBalance(id, world, currency));
    if(difference.doubleValue() > 0) {
      Bank.setBankBalance(id, world, currency, Bank.getBankBalance(id, world, currency).subtract(difference));
      setBalance(id, world, currency, BigDecimal.ZERO);
      return;
    }
    setBalance(id, world, currency, getBalance(id, world, currency).subtract(amount));
  }

  public static void initializeWorldData(UUID id) {
    Account account = getAccount(id);
    String world = IDFinder.getWorld(id);
    for(Currency c : TNE.instance().manager.currencyManager.getWorldCurrencies(world)) {
      if (!account.getBalances().containsKey(world + ":" + c.getName())) {
        account.setBalance(world, getInitialBalance(world, c.getName()), c.getName());
      }
    }
  }

  public static BigDecimal getInitialBalance(String world, String currency) {
    return TNE.instance().manager.currencyManager.get(world, currency).getBalance();
  }

  public static BigDecimal getWorldCost(String world) {
    if(MISCUtils.multiWorld()) {
      if(MISCUtils.worldConfigExists("Worlds." + world + ".ChangeFee")) {
        return new BigDecimal(TNE.instance().worldConfigurations.getDouble("Worlds." + world + ".ChangeFee"));
      }
    }
    return new BigDecimal(TNE.instance().api().getDouble("Core.World.ChangeFee", world));
  }
}