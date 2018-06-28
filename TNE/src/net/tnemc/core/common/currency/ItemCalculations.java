package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

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
    Integer value = 0;
    for(ItemStack item : inventory.getContents()) {
      if(MaterialUtils.itemsEqual(stack, item)) {
        value += item.getAmount();
      }
    }
    return value;
  }

  public static void setItems(TNECurrency currency, BigDecimal amount, Inventory inventory, boolean remove) {
    TNE.debug("=====START Account.setItems =====");
    TNE.debug("Holdings: " + amount.toPlainString());
    if(currency.isItem()) {
      BigDecimal old = getCurrencyItems(currency, inventory);
      BigDecimal difference = (amount.compareTo(old) >= 0)? amount.subtract(old) : old.subtract(amount);
      if(remove) difference = amount;
      String differenceString = difference.toPlainString();
      String[] split = (differenceString + (differenceString.contains(".")? "" : ".00")).split("\\.");
      boolean consolidate = TNE.instance().api().getBoolean("Core.Server.Consolidate", WorldFinder.getWorld(TNE.instance().defaultWorld, WorldVariant.CONFIGURATION), "");
      boolean add = (consolidate) || amount.compareTo(old) >= 0;
      if(remove) add = false;

      if(consolidate) split = (amount.toPlainString() + (amount.toPlainString().contains(".")? "" : ".00")).split("\\.");

      if(consolidate) clearItems(currency, inventory);
      BigInteger majorChange = (consolidate)? setMajorConsolidate(currency, new BigInteger(split[0]), inventory) :
          setMajor(currency, new BigInteger(split[0]), add, inventory);
      BigInteger minorChange = (consolidate)? setMinorConsolidate(currency, new BigInteger(split[1]), inventory) :
          setMinor(currency, new BigInteger(split[1]), add, inventory);

      TNE.debug("MajorChange: " + majorChange.toString());
      TNE.debug("MinorChange: " + minorChange.toString());
      if(!consolidate && !add) {
        if(majorChange.compareTo(BigInteger.ZERO) > 0) {
          setMajor(currency, majorChange, true, inventory);
        }

        if(minorChange.compareTo(BigInteger.ZERO) > 0) {
          setMinor(currency, minorChange, true, inventory);
        }
      }
    }
    TNE.debug("=====END Account.setItems =====");
  }

  public static BigInteger setMajorConsolidate(TNECurrency currency, BigInteger amount, Inventory inventory) {
    TNE.debug("===== START setMinorItems =====");
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    for(Map.Entry<Integer, TNETier> entry : currency.getTNEMajorTiers().entrySet()) {
      BigInteger weight = BigInteger.valueOf(entry.getKey());

      BigInteger itemAmount = workingAmount.divide(weight);

      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());
      items.put(entry.getKey(), stack);
    }
    giveItems(items.values(), inventory);
    return BigInteger.ZERO;
  }

  public static BigInteger setMinorConsolidate(TNECurrency currency, BigInteger amount, Inventory inventory) {
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    for(Map.Entry<Integer, TNETier> entry : currency.getTNEMinorTiers().entrySet()) {
      BigInteger weight = BigInteger.valueOf(entry.getKey());

      BigInteger itemAmount = workingAmount.divide(weight);

      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());
      items.put(entry.getKey(), stack);
    }
    giveItems(items.values(), inventory);
    return BigInteger.ZERO;
  }

  public static BigInteger setMajor(TNECurrency currency, BigInteger amount, boolean add, Inventory inventory) {
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    BigInteger actualAmount = BigInteger.ZERO;
    NavigableMap<Integer, TNETier> values = (add)? currency.getTNEMajorTiers() :
        currency.getTNEMajorTiers().descendingMap();
    String additional = "0";
    for(Map.Entry<Integer, TNETier> entry : values.entrySet()) {
      if(entry.getKey() <= 0) continue;
      BigInteger weight = BigInteger.valueOf(entry.getKey());

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
    if(add) giveItems(items.values(), inventory);
    else takeItems(items.values(), inventory);

    if(actualAmount.compareTo(amount) > 0) {
      return actualAmount.subtract(amount);
    }
    return BigInteger.ZERO;
  }

  public static BigInteger setMinor(TNECurrency currency, BigInteger amount, boolean add, Inventory inventory) {
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    BigInteger actualAmount = BigInteger.ZERO;
    Set<Map.Entry<Integer, TNETier>> values = (add)? currency.getTNEMinorTiers().entrySet() :
        currency.getTNEMinorTiers().descendingMap().entrySet();
    for(Map.Entry<Integer, TNETier> entry : values) {
      if(entry.getKey() <= 0) continue;
      BigInteger weight = BigInteger.valueOf(entry.getKey());

      BigInteger itemAmount = workingAmount.divide(weight);

      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());

      if(add || hasItem(stack, inventory)) {
        actualAmount = actualAmount.add(weight.multiply(itemAmount));
        workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
        items.put(entry.getKey(), stack);
      }
    }
    if(add) giveItems(items.values(), inventory);
    else takeItems(items.values(), inventory);

    if(actualAmount.compareTo(amount) > 0) {
      return actualAmount.subtract(amount);
    }
    return BigInteger.ZERO;
  }

  public static BigDecimal getCurrencyItems(TNECurrency currency, Inventory inventory) {
    BigDecimal value = BigDecimal.ZERO;

    if(currency.isItem()) {
      for(TNETier tier : currency.getTNEMajorTiers().values()) {
        value = value.add(new BigDecimal(getCount(tier.getItemInfo().toStack(), inventory) * tier.weight()));
      }

      for(TNETier tier : currency.getTNEMinorTiers().values()) {
        Integer parsed = getCount(tier.getItemInfo().toStack(), inventory) * tier.weight();
        String convert = "." + String.format(Locale.US, "%0" + currency.decimalPlaces() + "d", parsed);
        value = value.add(new BigDecimal(convert));
      }
    }

    return value;
  }

  public static boolean hasItem(ItemStack stack, Inventory inventory) {
    return inventory.contains(stack, stack.getAmount());
  }

  public static void takeItems(Collection<ItemStack> items, Inventory inventory) {
    for (ItemStack item : items) {
      removeItem(item, inventory);
    }
  }

  public static void giveItems(Collection<ItemStack> items, Inventory inventory) {
    for(ItemStack item : items) {
      Collection<ItemStack> left = inventory.addItem(item).values();

      if(left.size() > 0) {
        if(inventory instanceof PlayerInventory) {
          final HumanEntity entity = ((HumanEntity)inventory.getHolder());
          for (ItemStack stack : left) {
            Bukkit.getScheduler().runTask(TNE.instance(), () -> {
              entity.getWorld().dropItemNaturally(entity.getLocation(), stack);
            });
          }
        }
      }
    }
  }

  public static Integer removeItem(ItemStack stack, Inventory inventory) {
    int left = stack.getAmount();

    for(int i = 0; i < inventory.getStorageContents().length; i++) {
      if(left <= 0) break;
      ItemStack item = inventory.getItem(i);
      if(item == null || !item.isSimilar(stack)) continue;

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
      ItemStack helmet = ((PlayerInventory) inventory).getHelmet();
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
    }
    return left;
  }
}