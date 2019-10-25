package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ItemCalculations {

  public static void clearItems(TNECurrency currency, Inventory inventory) {
    for(TNETier tier : currency.getTNETiers()) {
      removeAllItem(tier.getItemInfo().toStack(), inventory);
    }
  }

  public static void removeAllItem(ItemStack stack, Inventory inventory) {
    for(int i = 0; i < inventory.getContents().length; i++) {
      final ItemStack item = inventory.getItem(i);

      if(stack != null && MaterialUtils.itemsEqual(stack, item)) inventory.setItem(i, null);
    }
  }

  public static Integer getCount(ItemStack stack, Inventory inventory) {
    ItemStack compare = stack.clone();
    compare.setAmount(1);

    Integer value = 0;
    for(ItemStack item : inventory.getContents()) {
      TNE.debug("item: " + compare.getItemMeta().getDisplayName());
      if(MaterialUtils.itemsEqual(compare, item)) {
        value += item.getAmount();
      }
    }
    TNE.debug("Count: " + value);
    return value;
  }

  public static BigInteger setMajorConsolidate(UUID account, TNECurrency currency, BigInteger amount, Inventory inventory) {
    TNE.debug("===== START setMinorItems =====");
    Map<BigInteger, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    for(Map.Entry<BigInteger, TNETier> entry : currency.getTNEMajorTiers().entrySet()) {
      BigInteger weight = entry.getKey();

      BigInteger itemAmount = workingAmount.divide(weight);

      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());
      items.put(entry.getKey(), stack);
    }
    final int dropped = giveItemsFeedback(account, items.values(), inventory);
    if(dropped > 0) {
      if(inventory.getHolder() instanceof HumanEntity) {
        ((HumanEntity) inventory.getHolder()).sendMessage(ChatColor.RED + "Your inventory was full, check the ground for excess currency items.");
      }
    }
    return BigInteger.ZERO;
  }

  public static BigInteger setMinorConsolidate(UUID account, TNECurrency currency, BigInteger amount, Inventory inventory) {
    Map<BigInteger, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    for(Map.Entry<BigInteger, TNETier> entry : currency.getTNEMinorTiers().entrySet()) {
      BigInteger weight = entry.getKey();

      BigInteger itemAmount = workingAmount.divide(weight);

      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());
      items.put(entry.getKey(), stack);
    }
    final int dropped = giveItemsFeedback(account, items.values(), inventory);
    if(dropped > 0) {
      if(inventory.getHolder() instanceof HumanEntity) {
        ((HumanEntity) inventory.getHolder()).sendMessage(ChatColor.RED + "Your inventory was full, check the ground for excess currency items.");
      }
    }
    return BigInteger.ZERO;
  }

  public static void setItems(UUID account, TNECurrency currency, BigDecimal amount, Inventory inventory, boolean remove) {
    setItems(account, currency, amount, inventory, remove, TNE.instance().api().getBoolean("Core.Server.Consolidate", WorldFinder.getWorldName(TNE.instance().defaultWorld, WorldVariant.CONFIGURATION), ""));
  }

  public static void setItems(UUID account, TNECurrency currency, BigDecimal amount, Inventory inventory, boolean remove, boolean consolidate) {
    TNE.debug("=====START Account.setItems =====");
    TNE.debug("Holdings: " + amount.toPlainString());
    if(currency.isItem()) {
      BigDecimal old = getCurrencyItems(currency, inventory);
      TNE.debug("Old: " + old.toPlainString());
      BigDecimal difference = (amount.compareTo(old) >= 0)? amount.subtract(old) : old.subtract(amount);
      TNE.debug("difference: " + difference);
      if(remove) difference = amount;
      TNE.debug("difference: " + difference);
      String differenceString = difference.toPlainString();
      TNE.debug("differenceString: " + differenceString);
      String[] split = (differenceString + (differenceString.contains(".")? "" : ".00")).split("\\.");
      boolean add = (consolidate) || amount.compareTo(old) >= 0;
      if(remove) add = false;

      if(consolidate) split = (amount.toPlainString() + (amount.toPlainString().contains(".")? "" : ".00")).split("\\.");

      if(consolidate) clearItems(currency, inventory);
      BigInteger majorChange = (consolidate)? setMajorConsolidate(account, currency, new BigInteger(split[0]), inventory) :
          setMajor(account, currency, new BigInteger(split[0]), add, inventory);
      BigInteger minorChange = (consolidate)? setMinorConsolidate(account, currency, new BigInteger(split[1]), inventory) :
          setMinor(account, currency, new BigInteger(split[1]), add, inventory);

      TNE.debug("MajorChange: " + majorChange.toString());
      TNE.debug("MinorChange: " + minorChange.toString());
      if(!consolidate && !add) {
        if(majorChange.compareTo(BigInteger.ZERO) > 0) {
          setMajor(account, currency, majorChange, true, inventory);
        }

        if(minorChange.compareTo(BigInteger.ZERO) > 0) {
          setMinor(account, currency, minorChange, true, inventory);
        }
      }
    }
    TNE.debug("=====END Account.setItems =====");
  }

  public static BigInteger setMajor(UUID account, TNECurrency currency, BigInteger amount, boolean add, Inventory inventory) {
    Map<BigInteger, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    BigInteger actualAmount = BigInteger.ZERO;
    NavigableMap<BigInteger, TNETier> values = (add)? currency.getTNEMajorTiers() :
        currency.getTNEMajorTiers().descendingMap();
    String additional = "0";
    for(Map.Entry<BigInteger, TNETier> entry : values.entrySet()) {
      if(entry.getKey().compareTo(BigInteger.ZERO) <= 0) continue;
      BigInteger weight = entry.getKey();

      BigInteger itemAmount = workingAmount.divide(weight).add(new BigInteger(additional));
      BigInteger itemActual = new BigInteger(getCount(entry.getValue().getItemInfo().toStack(), inventory) + "");
      additional = "0";

      if(!add && itemActual.compareTo(itemAmount) < 0) {
        additional = itemAmount.subtract(itemActual).toString();
        itemAmount = itemActual;
      }

      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());


      actualAmount = actualAmount.add(weight.multiply(itemAmount));
      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      items.put(entry.getKey(), stack);
    }
    if(add) {
      final int dropped = giveItemsFeedback(account, items.values(), inventory);
      if(dropped > 0) {
        if(inventory.getHolder() instanceof HumanEntity) {
          ((HumanEntity) inventory.getHolder()).sendMessage(ChatColor.RED + "Your inventory was full, check the ground for excess currency items.");
        }
      }
    } else {
      takeItems(items.values(), inventory);

      if(!add && workingAmount.compareTo(BigInteger.ZERO) > 0) {
        setMinor(account, currency, workingAmount.multiply(new BigInteger(currency.getMinorWeight() + "")), add, inventory);
      }
    }

    if(actualAmount.compareTo(amount) > 0) {
      TNE.debug("actualAmount~: " + actualAmount);
      TNE.debug("amount~: " + amount);
      return actualAmount.subtract(amount);
    }
    return BigInteger.ZERO;
  }

  public static BigInteger setMinor(UUID account, TNECurrency currency, BigInteger amount, boolean add, Inventory inventory) {
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    TNE.debug("workingAmount: " + workingAmount);
    BigInteger actualAmount = BigInteger.ZERO;
    NavigableMap<BigInteger, TNETier> values = (add)? currency.getTNEMinorTiers() :
        currency.getTNEMinorTiers().descendingMap();
    String additional = "0";
    for(Map.Entry<BigInteger, TNETier> entry : values.entrySet()) {
      BigInteger weight = entry.getKey();

      BigInteger itemAmount = workingAmount.divide(weight).add(new BigInteger(additional));
      BigInteger itemActual = new BigInteger(getCount(entry.getValue().getItemInfo().toStack(), inventory) + "");
      TNE.debug("itemAmount: " + itemAmount);
      TNE.debug("itemActual: " + itemActual);
      additional = "0";

      if(!add && itemActual.compareTo(itemAmount) < 0) {
        additional = itemAmount.subtract(itemActual).toString();
        itemAmount = itemActual;
      }

      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());


      actualAmount = actualAmount.add(weight.multiply(itemAmount));
      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      items.put(entry.getKey().intValue(), stack);
      TNE.debug("actualAmount: " + actualAmount);
      TNE.debug("workingAmount: " + workingAmount);
    }
    if(add) {

      final int dropped = giveItemsFeedback(account, items.values(), inventory);
      if(dropped > 0) {
        if(inventory.getHolder() instanceof HumanEntity) {
          ((HumanEntity) inventory.getHolder()).sendMessage(ChatColor.RED + "Your inventory was full, check the ground for excess currency items.");
        }
      }
    } else takeItems(items.values(), inventory);

    if(!add && workingAmount.compareTo(BigInteger.ZERO) > 0) {
      TNE.debug("REMOVE 1 MAJOR!!!!!!!!!!!!!!!!!!!!");
      BigInteger minor = new BigInteger(currency.getMinorWeight() - workingAmount.intValue() + "");
      TNE.debug("Minor to add: " + minor);
      setMajor(account, currency, BigInteger.ONE, false, inventory);
      setMinor(account, currency, minor, true, inventory);
    }

    if(add) {
      TNE.debug("ADDDING MAJOR!!!!!!!!!!!!!!!!!!!!");
      final BigInteger minorAmount = getCurrencyItems(currency, inventory, "minor").toBigInteger();
      TNE.debug("Minor Amount: " + minorAmount.toString());
      if (minorAmount.intValue() >= currency.getMinorWeight()) {
        int major = minorAmount.intValue() / currency.getMinorWeight();
        TNE.debug("major to add: " + major);
        setMajor(account, currency, BigInteger.valueOf(major), true, inventory);
        setMinor(account, currency, BigInteger.valueOf(currency.getMinorWeight() * major), false, inventory);
      }
    }

    if(actualAmount.compareTo(amount) > 0) {
      TNE.debug("actualAmount: " + actualAmount);
      return actualAmount.subtract(amount);
    }
    return BigInteger.ZERO;
  }

  public static BigDecimal getCurrencyItems(TNECurrency currency, Inventory inventory) {
    return getCurrencyItems(currency, inventory, "all");
  }

  public static BigDecimal getCurrencyItems(TNECurrency currency, Inventory inventory, String type) {
    BigDecimal value = BigDecimal.ZERO;
    BigInteger minor = BigInteger.ZERO;

    if(currency.isItem()) {
      if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("major")) {
        for (TNETier tier : currency.getTNEMajorTiers().values()) {
          value = value.add(new BigDecimal(getCount(tier.getItemInfo().toStack(), inventory) * tier.weight()));
        }
      }

      if(type.equalsIgnoreCase("all") || type.equalsIgnoreCase("minor")) {
        for (TNETier tier : currency.getTNEMinorTiers().values()) {
          BigInteger parsed = BigInteger.valueOf(getCount(tier.getItemInfo().toStack(), inventory)).multiply(tier.getTNEWeight());
          //String convert = "." + String.format(Locale.US, "%0" + currency.decimalPlaces() + "d", parsed);
          minor = minor.add(new BigInteger(parsed + ""));
        }
        if(type.equalsIgnoreCase("minor")) {
          return new BigDecimal("." + String.format(Locale.US, "%0" + currency.decimalPlaces() + "d", minor.intValue()));
        }
        //TNE.debug("Value: " + value.toPlainString());
        //TNE.debug("Minor: " + minor.toString());
        final BigInteger major = minor.divide(new BigInteger(currency.getMinorWeight() + ""));
        //TNE.debug("Major: " + major.toString());
        minor = minor.subtract(major.multiply(new BigInteger(currency.getMinorWeight() + "")));
        //TNE.debug("Minor: " + minor.toString());
        value = value.add(new BigDecimal("." + String.format(Locale.US, "%0" + currency.decimalPlaces() + "d", minor.intValue())));
        value = value.add(new BigDecimal(major.toString()));
        //TNE.debug("Value: " + value.toPlainString());
      }
    }

    return value;
  }

  public static void takeItems(Collection<ItemStack> items, Inventory inventory) {
    for (ItemStack item : items) {
      removeItem(item, inventory);
    }
  }

  public static int giveItemsFeedback(UUID account, Collection<ItemStack> items, Inventory inventory) {
    int leftAmount = 0;
    for(ItemStack item : items) {

      Map<Integer, ItemStack> left = inventory.addItem(item);

      if(inventory.getType() == InventoryType.ENDER_CHEST) {

        Player player = Bukkit.getPlayer(account);
        if(player != null) {

          left = player.getInventory().addItem(left.values().toArray(new ItemStack[left.size()]));

          if(left.size() > 0) {
            drop(left, player.getLocation());
            player.sendMessage(ChatColor.RED + "Your e chest and inventory was full so some items were dropped on the ground.");
          }
        }
      }

      if(left.size() > 0) {
        if(inventory instanceof PlayerInventory) {
          final HumanEntity entity = ((HumanEntity)inventory.getHolder());

          if(entity != null) {
            left = entity.getEnderChest().addItem(left.values().toArray(new ItemStack[left.size()]));
          }
        }
      }

      if(left.size() > 0) {
        if(inventory instanceof PlayerInventory) {
          final HumanEntity entity = ((HumanEntity)inventory.getHolder());
          drop(left, entity.getLocation());
          entity.sendMessage(ChatColor.RED + "Your inventory was full so some items were dropped on the ground.");
        }
        leftAmount++;
      }
    }
    return 0;
  }

  public static void giveItems(Collection<ItemStack> items, Inventory inventory) {
    for(ItemStack item : items) {
      Map<Integer, ItemStack> left = inventory.addItem(item);

      if(left.size() > 0) {
        if(inventory instanceof PlayerInventory) {
          final HumanEntity entity = ((HumanEntity)inventory.getHolder());
          drop(left, entity.getLocation());
          entity.sendMessage(ChatColor.RED + "Your inventory was full so some items were dropped on the ground.");
        }
      }
    }
  }

  public static List<ItemStack> getItemsForAmount(TNECurrency currency, BigDecimal amount) {
    List<ItemStack> items = new ArrayList<>();


    if(currency.isItem()) {
      final String differenceString = amount.toPlainString();
      String[] split = (differenceString + (differenceString.contains(".")? "" : ".00")).split("\\.");

      final BigInteger majorChange = new BigInteger(split[0]);
      final BigInteger minorChange = new BigInteger(split[1]);

      BigInteger workingAmount = BigInteger.ZERO;
      BigInteger actualAmount = BigInteger.ZERO;
      String additional = "0";

      NavigableMap<BigInteger, TNETier> values;

      if(majorChange.compareTo(BigInteger.ZERO) > 0) {
        values = currency.getTNEMajorTiers();

        workingAmount = majorChange;
        actualAmount = BigInteger.ZERO;

        for(Map.Entry<BigInteger, TNETier> entry : values.entrySet()) {
          BigInteger weight = entry.getKey();

          BigInteger itemAmount = workingAmount.divide(weight).add(new BigInteger(additional));
          additional = "0";

          ItemStack stack = entry.getValue().getItemInfo().toStack();
          stack.setAmount(itemAmount.intValue());


          actualAmount = actualAmount.add(weight.multiply(itemAmount));
          workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
          items.add(stack);
        }
      }

      if(minorChange.compareTo(BigInteger.ZERO) > 0) {
        values = currency.getTNEMinorTiers();

        workingAmount = minorChange;
        actualAmount = BigInteger.ZERO;

        for(Map.Entry<BigInteger, TNETier> entry : values.entrySet()) {
          BigInteger weight = entry.getKey();

          BigInteger itemAmount = workingAmount.divide(weight).add(new BigInteger(additional));
          additional = "0";

          ItemStack stack = entry.getValue().getItemInfo().toStack();
          stack.setAmount(itemAmount.intValue());


          actualAmount = actualAmount.add(weight.multiply(itemAmount));
          workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
          items.add(stack);
        }
      }
    }

    return items;
  }

  public static void giveItem(ItemStack stack, Inventory inventory, final Integer amount) {
    int remaining = amount;
    final int stacks = (amount > stack.getMaxStackSize())? (int)Math.ceil(amount / stack.getMaxStackSize()) : 1;

    Collection<ItemStack> items = new ArrayList<>();
    for(int i = 0; i < stacks; i++) {
      ItemStack clone = stack.clone();
      if(i == stacks - 1) {
        clone.setAmount(remaining);
      } else {
        clone.setAmount(stack.getMaxStackSize());
      }
      items.add(clone);
      remaining = remaining - clone.getAmount();
    }
    giveItems(items, inventory);
  }

  public static Integer removeItemAmount(ItemStack stack, Inventory inventory, final Integer amount) {
    int left = amount;
    TNE.debug("Amount: " + amount);

    for(int i = 0; i < inventory.getStorageContents().length; i++) {
      if(left <= 0) break;
      ItemStack item = inventory.getItem(i);
      TNE.debug("Null?: " + (item == null));
      if(item == null || !MaterialUtils.itemsEqual(stack, item)) {
        TNE.debug("Skipping item in removeITemAmount due to not equaling.");
        continue;
      }
      TNE.debug("Items Equal, go onwards.");

      if(item.getAmount() <= left) {
        left -= item.getAmount();
        TNE.debug("remove stack from inventory");
        inventory.setItem(i, null);
      } else {
        item.setAmount(item.getAmount() - left);
        inventory.setItem(i, item);
        left = 0;
      }
    }

    if(left > 0 && inventory instanceof PlayerInventory) {
      final ItemStack helmet = ((PlayerInventory) inventory).getHelmet();
      if(helmet != null && helmet.isSimilar(stack)) {
        if(helmet.getAmount() <= left) {
          left -= helmet.getAmount();
          ((PlayerInventory) inventory).setHelmet(null);
        } else {
          helmet.setAmount(helmet.getAmount() - left);
          ((PlayerInventory) inventory).setHelmet(helmet);
          left = 0;
        }
      }

      if(left > 0 && MISCUtils.offHand()) {
        final ItemStack hand = ((PlayerInventory) inventory).getItemInOffHand();

        if(hand != null && hand.isSimilar(stack)) {
          if (hand.getAmount() <= left) {
            left -= hand.getAmount();
            ((PlayerInventory) inventory).setItemInOffHand(null);
          } else {
            hand.setAmount(hand.getAmount() - left);
            ((PlayerInventory) inventory).setItemInOffHand(hand);
            left = 0;
          }
        }
      }
    }
    return left;
  }

  public static Integer removeItem(ItemStack stack, Inventory inventory) {

    int left = stack.clone().getAmount();

    for(int i = 0; i < inventory.getStorageContents().length; i++) {
      if(left <= 0) break;
      ItemStack item = inventory.getItem(i);
      if(item == null || !MaterialUtils.itemsEqual(stack, item)) continue;

      if(item.getAmount() <= left) {
        left -= item.getAmount();
        inventory.setItem(i, null);
      } else {
        item.setAmount(item.getAmount() - left);
        inventory.setItem(i, item);
        left = 0;
      }
    }

    if(left > 0 && inventory instanceof PlayerInventory) {
      final ItemStack helmet = ((PlayerInventory) inventory).getHelmet();
      if(helmet != null && helmet.isSimilar(stack)) {
        if(helmet.getAmount() <= left) {
          left -= helmet.getAmount();
          ((PlayerInventory) inventory).setHelmet(null);
        } else {
          helmet.setAmount(helmet.getAmount() - left);
          ((PlayerInventory) inventory).setHelmet(helmet);
          left = 0;
        }
      }

      if(left > 0 && MISCUtils.offHand()) {
        final ItemStack hand = ((PlayerInventory) inventory).getItemInOffHand();

        if(hand != null && hand.isSimilar(stack)) {
          if (hand.getAmount() <= left) {
            left -= hand.getAmount();
            ((PlayerInventory) inventory).setItemInOffHand(null);
          } else {
            hand.setAmount(hand.getAmount() - left);
            ((PlayerInventory) inventory).setItemInOffHand(hand);
            left = 0;
          }
        }
      }
    }
    return left;
  }

  public static void drop(Map<Integer, ItemStack> left, Location location) {

    for (Map.Entry<Integer, ItemStack> entry : left.entrySet()) {
      final ItemStack i = entry.getValue();
      Bukkit.getScheduler().runTask(TNE.instance(), () -> {
        try {
          location.getWorld().dropItemNaturally(location, i);
        } catch(Exception e) {
          //attempted to drop air/some crazy/stupid error.
        }
      });
    }
  }
}