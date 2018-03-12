package net.tnemc.core.item.data;

import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class SerialPotionData implements SerialItemData {

  private List<PotionEffect> customEffects = new ArrayList<>();
  private String type;
  private Color color;
  private boolean extended;
  private boolean upgraded;

  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof PotionMeta) {
      valid = true;
      PotionMeta potionMeta = (PotionMeta)meta;

      if(potionMeta.hasColor()) color = potionMeta.getColor();
      customEffects = potionMeta.getCustomEffects();
      type = potionMeta.getBasePotionData().getType().name();
      extended = potionMeta.getBasePotionData().isExtended();
      upgraded = potionMeta.getBasePotionData().isUpgraded();
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      PotionMeta meta = (PotionMeta)stack.getItemMeta();
      if(color != null) meta.setColor(color);
      customEffects.forEach((effect)->meta.addCustomEffect(effect, true));
      PotionData data = new PotionData(PotionType.valueOf(type), extended, upgraded);
      meta.setBasePotionData(data);
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