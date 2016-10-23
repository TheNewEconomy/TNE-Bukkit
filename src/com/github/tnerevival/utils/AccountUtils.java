package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.event.account.TNEAccountCreationEvent;
import com.github.tnerevival.core.event.transaction.TNETransactionEvent;
import com.github.tnerevival.core.transaction.Transaction;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.core.transaction.TransactionType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AccountUtils {

  public static Boolean exists(UUID id) {
    return TNE.instance.manager.accounts.containsKey(id);
  }

  public static void createAccount(UUID id) {
    Account a = new Account(id);
    a.setBalance(TNE.instance.defaultWorld, AccountUtils.getInitialBalance(TNE.instance.defaultWorld));
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

  public static boolean commandLocked(Player player) {
    Account acc = AccountUtils.getAccount(MISCUtils.getID(player));
    if(!acc.getStatus().getBalance()) {
      Message locked = new Message("Messages.Account.Locked");
      locked.addVariable("$player", player.getDisplayName());
      locked.translate(MISCUtils.getWorld(player), player);
      return true;
    }

    if(TNE.instance.manager.enabled(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
      if(!TNE.instance.manager.confirmed(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
        if (acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
          Message set = new Message("Messages.Account.Set");
          set.translate(MISCUtils.getWorld(player), player);
          return false;
        }

        if (!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
          Message confirm = new Message("Messages.Account.Confirm");
          confirm.translate(MISCUtils.getWorld(player), player);
          return false;
        }
      }
    }
    return false;
  }

  private static Double getBalance(UUID id) {
    return getBalance(id, MISCUtils.getWorld(id));
  }

  private static Double getBalance(UUID id, String world) {
    Account account = getAccount(id);

    if(!account.getStatus().getBalance()) return 0.0;

    if(MISCUtils.multiWorld()) {
      world = MISCUtils.getWorld(id);
      if(!account.getBalances().containsKey(world)) {
        initializeWorldData(id, world);
      }

      return round(account.getBalance(MISCUtils.getWorld(id)));
    }
    if(TNE.instance.api.getBoolean("Core.Currency.ItemCurrency", world)) {
      Material majorItem = Material.getMaterial(TNE.instance.api.getString("Core.Currency.ItemMajor", world));
      Material minorItem = Material.getMaterial(TNE.instance.api.getString("Core.Currency.ItemMinor", world));
      Integer major = MISCUtils.getItemCount(id, majorItem);
      Integer minor = MISCUtils.getItemCount(id, minorItem);
      String balance = major + "." + minor;
      return Double.valueOf(balance);
    }
    return round(account.getBalance(TNE.instance.defaultWorld));
  }

  private static void setBalance(UUID id, Double balance) {
    setBalance(id, MISCUtils.getWorld(id), balance);
  }

  private static void setBalance(UUID id, String world, Double balance) {
    balance = round(balance);
    Account account = getAccount(id);

    if(!account.getStatus().getBalance()) return;

    String balanceString = (String.valueOf(balance).contains(".")) ? String.valueOf(balance) : String.valueOf(balance) + ".0";
    String[] split = balanceString.split("\\.");
    if(MISCUtils.multiWorld()) {
      world = MISCUtils.getWorld(id);
      if(MISCUtils.worldConfigExists("Worlds." + world + ".Currency.ItemCurrency")) {
        if(TNE.instance.worldConfigurations.getBoolean("Worlds." + world + ".Currency.ItemCurrency")) {
          Material majorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMajor"));
          Material minorItem = Material.getMaterial(TNE.instance.worldConfigurations.getString("Worlds." + world + ".Currency.ItemMinor"));
          MISCUtils.setItemCount(id, majorItem, Integer.valueOf(split[0].trim()));
          MISCUtils.setItemCount(id, minorItem, Integer.valueOf(split[1].trim()));
        }
      } else {
        if(!account.getBalances().containsKey(world)) {
          initializeWorldData(id, world);
        }
        account.setBalance(world, balance);
      }
    }
    if(TNE.instance.api.getBoolean("Core.Currency.ItemCurrency", world)) {
      Material majorItem = Material.getMaterial(TNE.instance.api.getString("Core.Currency.ItemMajor", world));
      Material minorItem = Material.getMaterial(TNE.instance.api.getString("Core.Currency.ItemMinor", world));
      MISCUtils.setItemCount(id, majorItem, Integer.valueOf(split[0].trim()));
      MISCUtils.setItemCount(id, minorItem, Integer.valueOf(split[1].trim()));
    } else {
      account.setBalance(TNE.instance.defaultWorld, balance);
    }
  }

  public static Double round(double amount) {
    return (double)Math.round(amount * 100) / 100;
  }

  public static boolean transaction(String initiator, String recipient, double amount, TransactionType type, String world) {

    return transaction(initiator, recipient, new TransactionCost(amount), type, world);
  }

  public static boolean transaction(String initiator, String recipient, TransactionCost cost, TransactionType type, String world) {
    Transaction t = new Transaction(initiator, recipient, cost, type, world);

    TNETransactionEvent e = new TNETransactionEvent(t);
    Bukkit.getServer().getPluginManager().callEvent(e);

    if(!e.isCancelled()) {
      return t.perform();
    }
    return false;
  }

  public static Double getFunds(UUID id) {
    return getFunds(id, MISCUtils.getWorld(id));
  }

  public static Double getFunds(UUID id, String world) {
    double funds = getBalance(id, world);
    if(TNE.instance.api.getBoolean("Core.Bank.Connected", world)) {
      funds += BankUtils.getBankBalance(id, world);
    }
    return funds;
  }

  public static void setFunds(UUID id, double amount) {
    setFunds(id, MISCUtils.getWorld(id), amount);
    amount = round(amount);
    setBalance(id, amount);
  }

  public static void setFunds(UUID id, String world, double amount) {
    setBalance(id, world, round(amount));
  }

  public static void removeFunds(UUID id, String world, double amount) {
    double difference = amount - getBalance(id, world);
    if(difference > 0) {
      BankUtils.setBankBalance(id, world, round(BankUtils.getBankBalance(id, world) - difference));
      setBalance(id, world, 0.0);
      return;
    }
    setBalance(id, world, round(getBalance(id, world) - amount));
  }

  public static void initializeWorldData(UUID id, String world) {
    Account account = getAccount(id);
    world = MISCUtils.getWorld(id);
    if(!account.getBalances().containsKey(world)) {
      account.setBalance(world, getInitialBalance(world));
    }
  }

  public static Double getInitialBalance(String world) {
    if(MISCUtils.multiWorld()) {
      if(MISCUtils.worldConfigExists("Worlds." + world + ".Balance")) {
        return round(TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".Balance"));
      }
    }
    return round(TNE.instance.api.getDouble("Core.Balance", world));
  }

  public static Double getWorldCost(String world) {
    if(MISCUtils.multiWorld()) {
      if(MISCUtils.worldConfigExists("Worlds." + world + ".balance")) {
        return round(TNE.instance.worldConfigurations.getDouble("Worlds." + world + ".ChangeFee"));
      }
    }
    return round(TNE.instance.api.getDouble("Core.World.ChangeFee", world));
  }
}