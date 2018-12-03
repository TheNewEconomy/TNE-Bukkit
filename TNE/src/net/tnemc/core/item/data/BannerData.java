package net.tnemc.core.item.data;

import net.tnemc.core.TNE;
import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

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
public class BannerData implements SerialItemData {

  private List<Pattern> patternList = new ArrayList<>();

  private boolean valid = false;
  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof BannerMeta) {
      valid = true;

      BannerMeta bannerMeta = (BannerMeta)meta;
      patternList = bannerMeta.getPatterns();
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      BannerMeta meta = (BannerMeta)stack.getItemMeta();
      meta.setPatterns(patternList);
      stack.setItemMeta(meta);
    }
    return stack;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("name", "banner");

    int i = 0;
    JSONObject patterns = new JSONObject();
    for(Pattern pattern : patternList) {
      JSONObject patternObj = new JSONObject();
      patternObj.put("colour", pattern.getColor().name());
      patternObj.put("pattern", pattern.getPattern().getIdentifier());
      patterns.put(i, patternObj);
      i++;
    }
    json.put("patterns", patterns);
    return json;
  }

  @Override
  public void readJSON(JSONHelper json) {
    valid = true;
    json.getJSON("patterns").forEach((key, value)->{
      JSONHelper helperObj = new JSONHelper((JSONObject)value);
      final Pattern pattern = new Pattern(DyeColor.valueOf(helperObj.getString("colour")),
          PatternType.getByIdentifier(helperObj.getString("pattern")));
      patternList.add(pattern);
    });
    TNE.debug("Banner Data End");
  }
}
