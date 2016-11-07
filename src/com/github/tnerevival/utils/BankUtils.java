package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.serializable.SerializableItemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class BankUtils {
  public static void applyInterest(UUID id) {
    Account account = AccountUtils.getAccount(id);
    Iterator<Entry<String, Bank>> it = account.getBanks().entrySet().iterator();

    while(it.hasNext()) {
      Entry<String, Bank> entry = it.next();

      if(interestEnabled(entry.getKey(), id.toString())) {
        Double gold = entry.getValue().getGold();
        Double interestEarned = gold * interestRate(entry.getKey(), id.toString());
        entry.getValue().setGold(gold + AccountUtils.round(interestEarned));
      }
    }
  }

  private static Boolean interestEnabled(String world, String player) {
    return TNE.instance.api.getBoolean("Core.Bank.Interest.Enabled", world, player);
  }

  private static Double interestRate(String world, String player) {
    return TNE.instance.api.getDouble("Core.Bank.Interest.Rate", world, player);
  }

  public static boolean bankMember(UUID owner, UUID id) {
    String world = TNE.instance.defaultWorld;
    if(MISCUtils.multiWorld()) {
      world = MISCUtils.getWorld(id);
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

  public static Boolean hasBank(UUID id, String world) {
    return AccountUtils.getAccount(id).getBanks().containsKey(world);
  }

  public static Boolean hasBank(UUID id) {
    String world = TNE.instance.defaultWorld;
    if(MISCUtils.multiWorld()) {
      world = MISCUtils.getWorld(id);
    }
    if(world == null) {
      TNE.instance.getLogger().warning("***WORLD NAME IS NULL***");
      return false;
    }
    return hasBank(id, world);
  }

  public static Bank getBank(UUID owner) {
    String world = TNE.instance.defaultWorld;
    if(MISCUtils.multiWorld()) {
      world = MISCUtils.getWorld(owner);
    }
    return getBank(owner, world);
  }

  public static Bank getBank(UUID owner, String world) {
    return AccountUtils.getAccount(owner).getBank(world);
  }

  public static Bank fromString(String bankString) {
    String[] variables = bankString.split("\\:");
    Bank bank;
    try {
      UUID id = UUID.fromString(variables[0]);
      bank = new Bank(id, Integer.parseInt(variables[2]), Double.parseDouble(variables[3]));
    } catch(IllegalArgumentException e) {
      bank = new Bank(IDFinder.getID(variables[0]), Integer.parseInt(variables[2]), Double.parseDouble(variables[3]));
    }

    List<SerializableItemStack> items = new  ArrayList<SerializableItemStack>();

    if(!variables[4].equalsIgnoreCase("TNENOSTRINGVALUE")) {
      String[] itemStrings = variables[4].split("\\*");
      for(String s : itemStrings) {
        items.add(SerializableItemStack.fromString(s));
      }
    }
    bank.setItems(items);
    return bank;
  }

  private static Inventory getBankInventory(UUID owner, String world) {

    if(!hasBank(owner, world)) {
      return null;
    }

    if(!AccountUtils.getAccount(owner).getStatus().getBank()) {
      return null;
    }

    Bank bank = getBank(owner, world);
    MISCUtils.debug("OWNER UUID: " + owner.toString());
    MISCUtils.debug((IDFinder.getPlayer(owner.toString()) == null) + "");
    String title = ChatColor.GOLD + "[" + ChatColor.WHITE + "Bank" + ChatColor.GOLD + "]" + ChatColor.WHITE + IDFinder.getPlayer(owner.toString()).getDisplayName();
    Inventory bankInventory = Bukkit.createInventory(null, size(world, owner.toString()), title);
    if(bank.getItems().size() > 0) {
      List<SerializableItemStack> items = bank.getItems();

      for(SerializableItemStack stack : items) {
        bankInventory.setItem(stack.getSlot(), stack.toItemStack());
      }
    }
    return bankInventory;
  }

  public static Inventory getBankInventory(UUID owner) {
    String world = TNE.instance.defaultWorld;
    if(MISCUtils.multiWorld() && MISCUtils.getWorld(owner) != null) {
      world = MISCUtils.getWorld(owner);
    }
    return getBankInventory(owner, world);
  }

  public static Double getBankBalance(UUID owner, String world) {
    if(hasBank(owner, world)) {
      if(AccountUtils.getAccount(owner).getStatus().getBank()) {
        Bank b = getBank(owner, world);
        return AccountUtils.round(b.getGold());
      }
    }
    return 0.0;
  }

  public static Double getBankBalance(UUID owner) {
    return getBankBalance(owner, TNE.instance.defaultWorld);
  }

  public static void setBankBalance(UUID owner, String world, Double amount) {
    if(AccountUtils.getAccount(owner).getStatus().getBank()) {
      if(hasBank(owner, world)) {
        Bank bank = getBank(owner, world);
        bank.setGold(AccountUtils.round(amount));
      }
    }
  }

  //Configuration-related Utils

  public static Integer size(String world, String player) {
    Integer rows = TNE.instance.api.getInteger("Core.Bank.Rows", world, player);
    return (rows >= 1 && rows <= 6) ? (rows * 9) : 27;
  }

  public static Boolean enabled(String world, String player) {
    return TNE.instance.api.getBoolean("Core.Bank.Enabled", world, player);
  }

  public static Boolean command(String world, String player) {
    return TNE.instance.api.getBoolean("Core.Bank.Command", world, player);
  }

  public static Double cost(String world, String player) {
    return AccountUtils.round(TNE.instance.api.getDouble("Core.Bank.Cost", world, player));
  }

  public static Boolean sign(String world, String player) {
    return TNE.instance.api.getBoolean("Core.Signs.Bank.Enabled", world, player);
  }

  public static Boolean npc(String world) {
    return TNE.instance.api.getBoolean("Core.Bank.NPC", world);
  }
}