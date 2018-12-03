package net.tnemc.core.item.data;

import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.json.simple.JSONObject;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class MapData implements SerialItemData {

  private Integer id = null;
  private String location = null;
  private Color color = null;
  private boolean scaling = false;
  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof MapMeta) {
      valid = true;
      MapMeta mapMeta = (MapMeta)meta;
      if(mapMeta.hasMapId()) id = mapMeta.getMapId();
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
      if(id != null) meta.setMapId(id);
      if(location != null) meta.setLocationName(location);
      if(color != null) meta.setColor(color);
      meta.setScaling(scaling);
      stack.setItemMeta(meta);
    }
    return stack;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("name", "map");
    if(id != null) json.put("id", id);
    if(location != null) json.put("location", location);
    if(color != null) json.put("colour", color.asRGB());
    json.put("scaling", scaling);
    return json;
  }

  @Override
  public void readJSON(JSONHelper json) {
    valid = true;
    if(json.has("id")) id = json.getInteger("id");
    if(json.has("location")) location = json.getString("location");
    if(json.has("colour")) color = Color.fromRGB(json.getInteger("colour"));
    scaling = json.getBoolean("scaling");
  }
}
