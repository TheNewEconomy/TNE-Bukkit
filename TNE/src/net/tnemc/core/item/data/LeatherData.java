package net.tnemc.core.item.data;

import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.json.simple.JSONObject;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class LeatherData implements SerialItemData {

  private Color color;
  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof LeatherArmorMeta) {
      valid = true;
      LeatherArmorMeta leatherMeta = (LeatherArmorMeta)meta;
      color = leatherMeta.getColor();
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      LeatherArmorMeta meta = (LeatherArmorMeta)stack.getItemMeta();
      if(color != null) meta.setColor(color);
      stack.setItemMeta(meta);
    }
    return stack;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("name", "leather");
    json.put("colour", color.asRGB());
    return json;
  }

  @Override
  public void readJSON(JSONHelper json) {
    valid = true;
    if(json.has("color")) color = Color.fromRGB(json.getInteger("colour"));
  }
}
