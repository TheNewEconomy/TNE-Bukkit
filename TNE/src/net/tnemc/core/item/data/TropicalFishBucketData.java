package net.tnemc.core.item.data;

import net.tnemc.core.TNE;
import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.json.simple.JSONObject;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/30/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TropicalFishBucketData implements SerialItemData {

  private boolean variant;
  private DyeColor bodyColour;
  private DyeColor patternColour;
  private TropicalFish.Pattern pattern;

  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof TropicalFishBucketMeta) {
      valid = true;
      TropicalFishBucketMeta fishMeta = (TropicalFishBucketMeta)meta;

      if(fishMeta.hasVariant()) {
        variant = true;
        bodyColour = fishMeta.getBodyColor();
        patternColour = fishMeta.getPatternColor();
        pattern = fishMeta.getPattern();
      }
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      TropicalFishBucketMeta meta = (TropicalFishBucketMeta)stack.getItemMeta();
      if(variant) {
        meta.setBodyColor(bodyColour);
        meta.setPatternColor(patternColour);
        meta.setPattern(pattern);
      }
      stack.setItemMeta(meta);
    }
    return stack;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("name", "tropicalfish");
    json.put("variant", variant);

    if(variant) {
      json.put("bodyColour", bodyColour.getColor().asRGB());
      json.put("patternColour", patternColour.getColor().asRGB());
      json.put("pattern", pattern.name());
    }
    return json;
  }

  @Override
  public void readJSON(JSONHelper json) {
    valid = true;
    TNE.debug("READING FISH BUCKET DATA");
    TNE.debug("JSON: " + json.getObject().toJSONString());
    if(json.has("variant")) {
      this.variant = json.getBoolean("variant");

      if(variant) {
        this.bodyColour = DyeColor.getByColor(Color.fromRGB(json.getInteger("bodyColour")));
        this.patternColour = DyeColor.getByColor(Color.fromRGB(json.getInteger("patternColour")));
        this.pattern = TropicalFish.Pattern.valueOf(json.getString("pattern"));
      }
    }
  }
}