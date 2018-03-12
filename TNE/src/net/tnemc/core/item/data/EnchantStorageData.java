package net.tnemc.core.item.data;

import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class EnchantStorageData implements SerialItemData {

  Map<String, Integer> enchantments = new HashMap<>();

  private boolean valid = false;
  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof EnchantmentStorageMeta) {
      valid = true;

      EnchantmentStorageMeta enchantMeta = (EnchantmentStorageMeta)meta;
      enchantMeta.getStoredEnchants().forEach((enchant, level)->enchantments.put(enchant.getName(), level));
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta)stack.getItemMeta();
      enchantments.forEach((name, level)->{
        meta.addStoredEnchant(Enchantment.getByName(name), level, true);
      });
      stack.setItemMeta(meta);
    }
    return stack;
  }

  @Override
  public void save(SaveManager manager) {

  }

  @Override
  public SerialItemData load(SaveManager manager) {
    return null;
  }
}
