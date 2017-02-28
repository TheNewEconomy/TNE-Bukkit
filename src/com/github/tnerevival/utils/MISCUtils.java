package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.event.object.InteractionType;
import com.github.tnerevival.core.event.object.TNEObjectInteractionEvent;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MISCUtils {

  //Minecraft Version Utils
  public static boolean isOneEight() {
    return Bukkit.getVersion().contains("1.8") || isOneNine() || isOneTen() || isOneEleven() || isOneTwelve();
  }

  public static boolean isOneNine() {
    return Bukkit.getVersion().contains("1.9") || isOneTen() || isOneEleven() || isOneTwelve();
  }

  public static boolean isOneTen() {
    return Bukkit.getVersion().contains("1.10") || isOneEleven() || isOneTwelve();
  }

  public static boolean isOneEleven() {
    return Bukkit.getVersion().contains("1.11") || isOneTwelve();
  }

  public static boolean isOneTwelve() {
    return Bukkit.getVersion().contains("1.12");
  }

  //True MISC Utils
  public static void debug(String message) {
    if(TNE.debugMode) {
      TNE.instance().getLogger().info("[DEBUG MODE]" + message);
    }
  }

  public static void debug(StackTraceElement[] stack) {
    for(StackTraceElement element : stack) {
      debug(element.toString());
    }
  }

  public static void debug(Exception e) {
    if(TNE.debugMode) {
      e.printStackTrace();
    }
  }

  public static boolean hasItems(UUID id, List<SerializableItemStack> items) {
    for(SerializableItemStack item : items) {
      ItemStack stack = item.toItemStack();
      if(getItemCount(id, stack.getType()) < stack.getAmount()) {
        return false;
      }
    }
    return true;
  }

  public static void setItems(UUID id, List<SerializableItemStack> items, boolean add) {
    setItems(id, items, add, false);
  }

  public static void setItems(UUID id, List<SerializableItemStack> items, boolean add, boolean set) {
    for(SerializableItemStack item : items) {
      Material type = item.toItemStack().getType();
      if(set) {
        setItemCount(id, type, item.getAmount());
        continue;
      }

      if(add) {
        setItemCount(id, type, getItemCount(id, type) + item.getAmount());
        continue;
      }
      setItemCount(id, type, getItemCount(id, type) - item.getAmount());
    }
  }

  public static Integer getItemCount(UUID id, ItemStack stack) {
    Player p = IDFinder.getPlayer(id.toString());
    int count = 0;
    for(ItemStack i : p.getInventory().getContents()) {
      if(i != null && i.getType() != null && i.getType().equals(stack.getType()) && i.getDurability() == stack.getDurability()) {
        count += i.getAmount();
      }
    }
    return count;
  }

  static Integer getItemCount(UUID id, Material item) {
    Player p = IDFinder.getPlayer(id.toString());
    int count = 0;
    if(item != null) {
      for(ItemStack i : p.getInventory().getContents()) {
        if(i != null && i.getType() != null && i.getType() == item) {
          count += i.getAmount();
        }
      }
    }
    return count;
  }

  static void setItemCount(UUID id, Material item, Integer amount) {
    Player p = IDFinder.getPlayer(id.toString());
    Integer count = getItemCount(id, item);
    if(item != null) {
      if(count > amount) {
        Integer remove = count - amount;
        Integer slot = 0;
        for(ItemStack i : p.getInventory().getContents()) {
          if(i != null && i.getType() != null && i.getType() == item) {
            if(remove > i.getAmount()) {
              remove -= i.getAmount();
              i.setAmount(0);
              p.getInventory().setItem(slot, null);
            } else {
              if(i.getAmount() - remove > 0) {
                i.setAmount(i.getAmount() - remove);
                p.getInventory().setItem(slot, i);
                return;
              }
              p.getInventory().setItem(slot, null);
              return;
            }
          }
          slot++;
        }
      } else if(count < amount) {
        Integer add = amount - count;

        while(add > 0) {
          if(add > item.getMaxStackSize()) {
            if(p.getInventory().firstEmpty() != -1) {
              p.getInventory().addItem(new ItemStack(item, item.getMaxStackSize()));
            } else {
              p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(item, item.getMaxStackSize()));
            }
            add -= item.getMaxStackSize();
          } else {
            if(p.getInventory().firstEmpty() != -1) {
              p.getInventory().addItem(new ItemStack(item, add));
            } else {
              p.getWorld().dropItemNaturally(p.getLocation(), new ItemStack(item, add));
            }
            add = 0;
          }
        }
      }
    }
  }

  public static String sendGetRequest(String URL) {
    StringBuilder builder = new StringBuilder();
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
      connection.setRequestMethod("GET");
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String response;
      while((response = reader.readLine()) != null) {
        builder.append(response);
      }
      reader.close();
    } catch (Exception e) {
      MISCUtils.debug(e);
    }
    MISCUtils.debug("GET VALUE");
    MISCUtils.debug(builder.toString());
    return builder.toString();
  }

  public static void handleObjectEvent(TNEObjectInteractionEvent event) {
    if(event.isCancelled()) return;
    MISCUtils.debug("TNEObjectInteractionEvent called");

    String id = IDFinder.getID(event.getPlayer()).toString();
    String world = IDFinder.getWorld(event.getPlayer());
    BigDecimal cost = event.getType().getCost(event.getIdentifier(), IDFinder.getWorld(event.getPlayer()), IDFinder.getID(event.getPlayer()).toString());
    String message = event.getType().getCharged();

    if(cost.compareTo(BigDecimal.ZERO) != 0 && !event.isCancelled()) {
      if(cost.compareTo(BigDecimal.ZERO) > 0) {
        if(AccountUtils.transaction(id, null, cost, TransactionType.MONEY_INQUIRY, world)) {
          MISCUtils.debug("Removing funds");
          AccountUtils.transaction(id, null, cost, TransactionType.MONEY_REMOVE, world);
        } else {
          MISCUtils.debug("Insufficient funds!");
          event.setCancelled(true);
          Message insufficient = new Message("Messages.Money.Insufficient");
          insufficient.addVariable("$amount", CurrencyFormatter.format(world, cost));
          insufficient.translate(world, event.getPlayer());
          return;
        }
      } else {
        AccountUtils.transaction(id, null, cost, TransactionType.MONEY_GIVE, world);
        message = event.getType().getPaid();
      }

      String newName = event.getIdentifier() + ((event.getAmount() > 1 )? "\'s" : "");

      Message m = new Message(message);
      m.addVariable("$amount", CurrencyFormatter.format(world, cost));
      m.addVariable("$stack_size", event.getAmount() + "");
      m.addVariable("$item", newName);
      m.translate(world, event.getPlayer());
    }

    if(event.getType().equals(InteractionType.ENCHANT) || event.getType().equals(InteractionType.SMELTING)) {
      MISCUtils.debug("Inside work around loop");
      final Player p = event.getPlayer();
      final ItemStack stack = event.getStack();
      final String correctMat = event.getIdentifier();
      final String loreSearch = (event.getType().equals(InteractionType.ENCHANT))? "Enchanting Cost" : "Smelting Cost";
      MISCUtils.debug("LoreSearch: " + loreSearch);
      TNE.instance().getServer().getScheduler().runTaskLater(TNE.instance(), new Runnable() {
        @Override
        public void run() {
          ItemStack[] contents = p.getInventory().getContents().clone();
          MISCUtils.debug("Inventory Item Count: " + contents.length);
          for (int i = 0; i < contents.length; i++) {
            MISCUtils.debug("Looping contents..." + i + "");
            if(contents[i] != null) MISCUtils.debug("Item Type: " + contents[i].getType().name());
            MISCUtils.debug("Correct Material: " + stack.getType().name());
            MISCUtils.debug("Correct Material: " + correctMat);
            if (contents[i] != null && contents[i].getType().name().equalsIgnoreCase(correctMat)) {
              ItemStack cloneStack = contents[i].clone();
              ItemMeta meta = cloneStack.getItemMeta();
              List<String> newLore = new ArrayList<>();
              if (meta.getLore() != null) {
                for (String s : meta.getLore()) {
                  MISCUtils.debug("Contains Search: " + s.contains(loreSearch));
                  if (!s.contains(loreSearch)) {
                    newLore.add(s);
                    MISCUtils.debug("Adding Lore: " + s);
                  }
                }
              }
              meta.setLore(newLore);
              cloneStack.setItemMeta(meta);
              contents[i] = cloneStack;
            }
          }
          p.getInventory().setContents(contents);
        }
      }, 5L);
    }
  }

  public static void printMaterials() {
    FileWriter writer = null;
    BufferedWriter buffWriter = null;
    try {
      writer = new FileWriter(new File(TNE.instance().getDataFolder(), "Material.txt"));
      buffWriter = new BufferedWriter(writer);
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    for(Material mat : Material.values()) {
      if(buffWriter != null) {
        try {
          buffWriter.write("validNames.add(new MaterialNameHelper(Material." + mat.name() + ", new String[0]));" + System.lineSeparator());
        } catch(Exception e) {
          MISCUtils.debug(e);
        }
      }
    }
    if(buffWriter != null) {
      try {
        buffWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if(writer != null) {
      try {
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void materialsYAML() {
    FileWriter writer = null;
    BufferedWriter buffWriter = null;
    try {
      writer = new FileWriter(new File(TNE.instance().getDataFolder(), "item.yml"));
      buffWriter = new BufferedWriter(writer);
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    if(buffWriter != null) {
      try {
        buffWriter.write("Items:" + System.lineSeparator());
      } catch(Exception e) {
        MISCUtils.debug(e);
      }
    }
    for(Material mat : Material.values()) {
      if(buffWriter != null) {
        try {
          buffWriter.write("  " + mat.name() + ":" + System.lineSeparator());
          buffWriter.write("    #The permission node required to create a shop sign with this item" + System.lineSeparator());
          buffWriter.write("    Sign: " + "tne.item." + mat.name().toLowerCase() + ".sign" + System.lineSeparator());
          buffWriter.write("    #The permission node required to buy this item" + System.lineSeparator());
          buffWriter.write("    Buy: " + "tne.item." + mat.name().toLowerCase() + ".buy" + System.lineSeparator());
          buffWriter.write("    #The permission node required to sell this item" + System.lineSeparator());
          buffWriter.write("    Sell: " + "tne.item." + mat.name().toLowerCase() + ".sell" + System.lineSeparator());
          buffWriter.write("    #The names supported by shop signs" + System.lineSeparator());
          buffWriter.write("    Names:" + System.lineSeparator());
          buffWriter.write("      - " + MaterialUtils.formatMaterialName(mat) + System.lineSeparator());
          if(mat.name().contains("_")) {
            buffWriter.write("      - " + MaterialUtils.formatMaterialNameWithSpace(mat) + System.lineSeparator());
          }
        } catch(Exception e) {
          MISCUtils.debug(e);
        }
      }
    }
    if(buffWriter != null) {
      try {
        buffWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if(writer != null) {
      try {
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressWarnings("rawtypes")
  public static Object getKey(Map m, Object value) {
    for(Object obj : m.keySet()) {
      if(m.get(obj).equals(value)) {
        return obj;
      }
    }
    return null;
  }

  public static String dashUUID(String undashed) {
    return undashed.replaceAll(TNE.uuidCreator.pattern(), "$1-$2-$3-$4-$5");
  }

  public static Boolean isBoolean(String value) {
    return value.equalsIgnoreCase(String.valueOf(true)) || value.equalsIgnoreCase(String.valueOf(false));
  }

  public static Boolean isDouble(String value, String world) {
    try {
      Double.valueOf(value.replace(TNE.instance().api().getString("Core.Currency.Decimal", world), "."));
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  public static Boolean isInteger(String value) {
    try {
      Integer.valueOf(value);
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  //World Utils
  public static Boolean ecoDisabled(String world) {
    if(TNE.instance().worldConfigurations.contains("Worlds." + world + ".DisableEconomy")) {
      return TNE.instance().worldConfigurations.getBoolean("Worlds." + world + ".DisableEconomy");
    }
    return false;
  }

  public static Boolean multiWorld() {
    return TNE.instance().api().getBoolean("Core.Multiworld");
  }

  public static Boolean worldConfigExists(String node) {
    return (TNE.instance().worldConfigurations.get(node) != null);
  }
}