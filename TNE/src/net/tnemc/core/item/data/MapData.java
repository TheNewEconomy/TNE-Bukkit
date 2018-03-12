package net.tnemc.core.item.data;

import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class MapData implements SerialItemData {

  private String location;
  private Color color;
  private boolean scaling;
  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof MapMeta) {
      valid = true;
      MapMeta mapMeta = (MapMeta)meta;
      if(mapMeta.hasLocationName()) location = mapMeta.getLocationName();
      if(mapMeta.hasColor()) color = mapMeta.getColor();
      scaling = mapMeta.isScaling();
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      MapMeta meta = (MapMeta) stack.getItemMeta();
      if(location != null) meta.setLocationName(location);
      if(color != null) meta.setColor(color);
      meta.setScaling(scaling);
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
