package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/9/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CurrencyNote {

  private List<String> flags = new ArrayList<>();
  private List<String> enchantments = new ArrayList<>();

  private String material;
  private int customModelData;
  private String texture;

  public CurrencyNote(String material) {
    this.material = material;
  }

  public List<String> getFlags() {
    return flags;
  }

  public void setFlags(List<String> flags) {
    this.flags = flags;
  }

  public List<String> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(List<String> enchantments) {
    this.enchantments = enchantments;
  }

  public String getMaterial() {
    return material;
  }

  public void setMaterial(String material) {
    this.material = material;
  }

  public int getCustomModelData() {
    return customModelData;
  }

  public void setCustomModelData(int customModelData) {
    this.customModelData = customModelData;
  }

  public String getTexture() {
    return texture;
  }

  public void setTexture(String texture) {
    this.texture = texture;
  }

  public ItemStack build() {
    ItemStack stack = (material.equalsIgnoreCase("PLAYER_HEAD"))?
        MISCUtils.getCustomTextureHead(texture) :
        new ItemStack(MaterialHelper.getMaterial(material));

    ItemMeta meta = stack.getItemMeta();

    if(MISCUtils.isOneFourteen()) {
      meta.setCustomModelData(customModelData);
    }

    enchantments.forEach((name)->{
      try {
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase()));
        if (enchantment == null) {
        } else {
          stack.addUnsafeEnchantment(enchantment, 1);
        }
      } catch(Exception ignore) {
        TNE.debug("Invalid Enchantment name: " + name.toLowerCase());
      }
    });

    for(String str : flags) {
      try {
        final ItemFlag flag = ItemFlag.valueOf(str.toLowerCase());
        if (flag != null) {
          meta.addItemFlags(flag);
        }
      } catch(Exception ignore) {
        TNE.debug("Invalid ItemFlag name: " + str);
      }
    }
    stack.setItemMeta(meta);
    return stack;
  }
}