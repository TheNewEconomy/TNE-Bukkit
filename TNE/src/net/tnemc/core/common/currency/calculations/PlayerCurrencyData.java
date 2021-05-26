package net.tnemc.core.common.currency.calculations;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 2/2/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerCurrencyData {

  private Map<String, Integer> inventoryMaterials = new HashMap<>();

  private TreeMap<BigDecimal, TNETier> materialValues = new TreeMap<>();

  private Inventory inventory;
  private TNECurrency currency;
  private boolean dropped = false;

  /**
   * A class used to hold information regarding amounts of each {@link TNETier} an inventory contains. This information
   * will be in turn used for a balance calculation.
   * @param inventory The inventory associated with the data to collect.
   * @param currency The currency to use for the data collection.
   */
  public PlayerCurrencyData(Inventory inventory, TNECurrency currency) {
    this.inventory = inventory;
    this.currency = currency;
    materialValues.putAll(currency.getTiers());
    initializeInventoryMaterials(inventory);
  }

  /**
   * Used to calculate the amount of each tier the {@link Inventory} contains.
   * @param inventory The inventory to use for the calculation.
   */
  public void initializeInventoryMaterials(Inventory inventory) {
    for(Map.Entry<BigDecimal, TNETier> entry : materialValues.entrySet()) {

      if(entry.getValue().getItemInfo() != null) {
        inventoryMaterials.put(entry.getValue().getItemInfo().getMaterial(),
            currency.calculation().getCount(entry.getValue().getItemInfo().toStack(), inventory));
      }
    }
  }

  public Map<String, Integer> getInventoryMaterials() {
    return inventoryMaterials;
  }

  public void removeMaterials(TNETier tier, Integer amount) {
    final int contains = inventoryMaterials.get(tier.getItemInfo().getMaterial());

    System.out.println("Item To Remove: " + tier.singular() + " Value: " + tier.getTNEWeight().toPlainString());
    System.out.println("Amount to Remove: " + amount);
    System.out.println("Amount Contained: " + contains);

    if(contains == amount) {
      inventoryMaterials.remove(tier.getItemInfo().getMaterial());
      removeAllItem(tier.getItemInfo().toStack(), inventory);
      return;
    }
    final int left = contains - amount;
    inventoryMaterials.put(tier.getItemInfo().getMaterial(), left);
    removeItemAmount(tier.getItemInfo().toStack(), inventory, amount);
  }

  public void provideMaterials(final TNETier tier, Integer amount) {
    ItemStack stack = tier.getItemInfo().toStack().clone();
    stack.setAmount(amount);
    if(giveItems(Collections.singletonList(stack), inventory)) {
      dropped = true;
    }
  }

  public void setInventoryMaterials(Map<String, Integer> inventoryMaterials) {
    this.inventoryMaterials = inventoryMaterials;
  }

  public TreeMap<BigDecimal, TNETier> getMaterialValues() {
    return materialValues;
  }

  public void setMaterialValues(TreeMap<BigDecimal, TNETier> materialValues) {
    this.materialValues = materialValues;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public TNECurrency getCurrency() {
    return currency;
  }

  public void setCurrency(TNECurrency currency) {
    this.currency = currency;
  }

  public static boolean giveItems(Collection<ItemStack> items, Inventory inventory) {
    boolean dropped = false;
    for(ItemStack item : items) {
      Map<Integer, ItemStack> left = inventory.addItem(item);

      if(left.size() > 0) {
        if(inventory instanceof PlayerInventory) {
          final HumanEntity entity = ((HumanEntity)inventory.getHolder());
          drop(left, entity.getLocation());
          dropped = true;
        }
      }
    }
    return dropped;
  }

  public static void drop(Map<Integer, ItemStack> left, Location location) {

    for (Map.Entry<Integer, ItemStack> entry : left.entrySet()) {
      final ItemStack i = entry.getValue();
      Bukkit.getScheduler().runTask(TNE.instance(), () -> {
        try {
          location.getWorld().dropItemNaturally(location, i);
        } catch (Exception e) {
          //attempted to drop air/some crazy/stupid error.
        }
      });
    }
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

  public static void removeAllItem(ItemStack stack, Inventory inventory) {
    for(int i = 0; i < inventory.getContents().length; i++) {
      final ItemStack item = inventory.getItem(i);

      if(stack != null && MaterialUtils.itemsEqual(stack, item)) inventory.setItem(i, null);
    }
  }

  public static Integer removeItemAmount(ItemStack stack, Inventory inventory, final Integer amount) {
    int left = amount;
    System.out.println("Amount: " + amount);

    for(int i = 0; i < inventory.getStorageContents().length; i++) {
      if(left <= 0) break;
      ItemStack item = inventory.getItem(i);
      System.out.println("Null?: " + (item == null));

      if(item == null) {
        System.out.println("Item is null, skip.");
        continue;
      }

      System.out.println("Item Finding: " + stack.getType().name());
      System.out.println("Item Current: " + item.getType().name());
      System.out.println("Items Amount: " + item.getAmount());
      System.out.println("Amount to remove: " + left);

      if(!MaterialUtils.itemsEqual(stack, item)) {
        System.out.println("Skipping item in removeITemAmount due to not equaling.");
        continue;
      }
      System.out.println("Items Equal, go onwards.");

      if(item.getAmount() <= left) {
        left -= item.getAmount();
        System.out.println("remove stack from inventory");
        inventory.setItem(i, null);
      } else {
        System.out.println("");
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
}