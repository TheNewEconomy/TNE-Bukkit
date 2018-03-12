package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 12/16/2017.
 */
public class ItemCalculations {

  public static void clearItems(TNEAccount account, TNECurrency currency) {
    TNE.debug("===== START Account.clearItems =====");
    Player player = account.getPlayer();

    TNE.debug("UUID: " + account.identifier().toString());
    if(player == null) TNE.debug("Player is null");

    for(TNETier tier : currency.getTNEMajorTiers().values()) {
      MaterialUtils.removeItem(player, tier.getItemInfo());
    }

    for(TNETier tier : currency.getTNEMinorTiers().values()) {
      MaterialUtils.removeItem(player, tier.getItemInfo());
    }
    TNE.debug("===== END Account.clearItems =====");
  }

  public static void setItems(TNEAccount account, TNECurrency currency, BigDecimal amount) {
    TNE.debug("=====START Account.setItems =====");
    TNE.debug("Holdings: " + amount.toPlainString());
    if(currency.isItem()) {
      BigDecimal old = getCurrencyItems(account, currency);
      BigDecimal difference = (amount.compareTo(old) >= 0)? amount.subtract(old) : old.subtract(amount);
      String differenceString = difference.toPlainString();
      String[] split = (differenceString + (differenceString.contains(".")? "" : ".00")).split("\\.");
      boolean consolidate = TNE.instance().api().getBoolean("Core.Server.Consolidate", WorldFinder.getWorld(account.identifier(), WorldVariant.CONFIGURATION), account.identifier());
      boolean add = (consolidate) || amount.compareTo(old) >= 0;

      if(consolidate) split = (amount.toPlainString() + (amount.toPlainString().contains(".")? "" : ".00")).split("\\.");

      if(consolidate) clearItems(account, currency);
      BigInteger majorChange = (consolidate)? setMajorConsolidate(account, currency, new BigInteger(split[0])) :
                                              setMajor(account, currency, new BigInteger(split[0]), add);
      BigInteger minorChange = (consolidate)? setMinorConsolidate(account, currency, new BigInteger(split[1])) :
                                              setMinor(account, currency, new BigInteger(split[1]), add);

      TNE.debug("MajorChange: " + majorChange.toString());
      TNE.debug("MinorChange: " + minorChange.toString());
      if(!consolidate && !add) {
        if(majorChange.compareTo(BigInteger.ZERO) > 0) {
          setMajor(account, currency, majorChange, true);
        }

        if(minorChange.compareTo(BigInteger.ZERO) > 0) {
          setMajor(account, currency, minorChange, true);
        }
      }
    }
    TNE.debug("=====END Account.setItems =====");
  }

  public static BigInteger setMajorConsolidate(TNEAccount account, TNECurrency currency, BigInteger amount) {
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
    giveItems(account, items.values());
    return BigInteger.ZERO;
  }

  public static BigInteger setMinorConsolidate(TNEAccount account, TNECurrency currency, BigInteger amount) {
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
    giveItems(account, items.values());
    return BigInteger.ZERO;
  }

  public static BigInteger setMajor(TNEAccount account, TNECurrency currency, BigInteger amount, boolean add) {
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    BigInteger actualAmount = BigInteger.ZERO;
    NavigableMap<Integer, TNETier> values = (add)? currency.getTNEMajorTiers() :
                                                currency.getTNEMajorTiers().descendingMap();
    String additional = "0";
    for(Map.Entry<Integer, TNETier> entry : values.entrySet()) {
      if(entry.getKey() <= 0) continue;
      BigInteger weight = BigInteger.valueOf(entry.getKey());

      TNE.debug("Weight: " + weight.toString());
      TNE.debug("Addition: " + additional);

      BigInteger itemAmount = workingAmount.divide(weight).add(new BigInteger(additional));
      BigInteger itemActual = new BigInteger(MaterialUtils.getCount(account.getPlayer(), entry.getValue().getItemInfo()) + "");
      additional = "0";
      TNE.debug("ItemAmount: " + itemAmount.toString());
      TNE.debug("itemActual: " + itemActual.toString());

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
    if(add) giveItems(account, items.values());
    else takeItems(account, items.values());

    if(actualAmount.compareTo(amount) > 0) {
      TNE.debug("return actual sub: " + actualAmount.subtract(amount).toString());
      return actualAmount.subtract(amount);
    }
    return BigInteger.ZERO;
  }

  public static BigInteger setMinor(TNEAccount account, TNECurrency currency, BigInteger amount, boolean add) {
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

      if(add || hasItem(account, stack)) {
        actualAmount = actualAmount.add(weight.multiply(itemAmount));
        workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
        items.put(entry.getKey(), stack);
      }
    }
    if(add) giveItems(account, items.values());
    else takeItems(account, items.values());

    if(actualAmount.compareTo(amount) > 0) {
      return actualAmount.subtract(amount);
    }
    return BigInteger.ZERO;
  }

  public static BigDecimal getCurrencyItems(TNEAccount account, TNECurrency currency) {
    TNE.debug("=====START ItemCalculations.getCurrencyItems =====");
    BigDecimal value = BigDecimal.ZERO;
    if(currency.isItem()) {
      Player player = account.getPlayer();
      for(TNETier tier : currency.getTNEMajorTiers().values()) {
        value = value.add(new BigDecimal(MaterialUtils.getCount(player, tier.getItemInfo()) * tier.weight()));
      }

      for(TNETier tier : currency.getTNEMinorTiers().values()) {
        Integer parsed = MaterialUtils.getCount(player, tier.getItemInfo()) * tier.weight();
        String convert = "." + String.format(Locale.US, "%0" + currency.decimalPlaces() + "d", parsed);
        value = value.add(new BigDecimal(convert));
      }
    }
    TNE.debug("Value: " + value.toPlainString());
    TNE.debug("=====END ItemCalculations.getCurrencyItems =====");
    return value;
  }

  public static boolean hasItem(TNEAccount account, ItemStack stack) {
    Player player = account.getPlayer();
    return player.getInventory().contains(stack, stack.getAmount());
  }

  public static void takeItems(TNEAccount account, Collection<ItemStack> items) {
    TNE.debug("=====START Account.takeItems =====");
    Player player = account.getPlayer();
    for(ItemStack stack : items) {
      removeItem(player, stack);
    }
    TNE.debug("=====END Account.takeItems =====");
  }

  public static void giveItems(TNEAccount account, Collection<ItemStack> items) {
    TNE.debug("=====START Account.giveItems =====");
    Player player = account.getPlayer();
    for(ItemStack stack : items) {
      Map<Integer, ItemStack> left = player.getInventory().addItem(stack);

      if(left.size() > 0) {
        TNE.debug("Some left overs of item: " + stack.getType());
        left.forEach((key, item)->{
          player.getWorld().dropItemNaturally(player.getLocation(), item);
        });
      }
    }
    TNE.debug("=====END Account.giveItems =====");
  }

  public static Integer removeItem(Player player, ItemStack stack) {
    int left = stack.getAmount();

    while(true) {

      int first = first(player, stack);

      if(first == -1) {
        ItemStack helmet = player.getInventory().getHelmet();
        if(helmet != null) {
          int amount = helmet.getAmount();
          if (helmet.isSimilar(stack)) {
            if (amount <= left) {
              left -= amount;
              player.getInventory().setHelmet(null);
            } else {
              helmet.setAmount(amount - left);
              player.getInventory().setHelmet(helmet);
              left = 0;
            }
            return left;
          }
        } else {
          return left;
        }
      } else {
        ItemStack found = player.getInventory().getItem(first);
        int amount = found.getAmount();
        TNE.debug("Found itemstack with amt of " + amount);

        if(amount <= left) {
          left -= amount;
          player.getInventory().setItem(first, null);
        } else {
          found.setAmount(amount - left);
          player.getInventory().setItem(first, found);
          left = 0;
        }
      }

      if(left <= 0) break;
    }
    return left;
  }

  public static int first(Player player, ItemStack stack) {
    if(stack == null) return -1;
    ItemStack[] items = player.getInventory().getStorageContents();

    for(int i = 0; i < items.length; i++) {
      if(items[i] == null) continue;

      if(stack.isSimilar(items[i])) return i;
    }
    return -1;
  }
}