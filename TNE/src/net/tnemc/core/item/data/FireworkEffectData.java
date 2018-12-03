package net.tnemc.core.item.data;

import net.tnemc.core.TNE;
import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
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
public class FireworkEffectData implements SerialItemData {

  private List<Color> colors = new ArrayList<>();
  private List<Color> fadeColors = new ArrayList<>();

  private String type;
  private boolean trail;
  private boolean flicker;

  private boolean hasEffect = false;
  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof FireworkEffectMeta) {
      valid = true;
      FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta)meta;
      if(fireworkEffectMeta.hasEffect()) {
        hasEffect = true;
        colors = fireworkEffectMeta.getEffect().getColors();
        fadeColors = fireworkEffectMeta.getEffect().getFadeColors();
        type = fireworkEffectMeta.getEffect().getType().name();
        trail = fireworkEffectMeta.getEffect().hasTrail();
        flicker = fireworkEffectMeta.getEffect().hasFlicker();
      }
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid && hasEffect) {
      FireworkEffectMeta meta = (FireworkEffectMeta)stack.getItemMeta();
      FireworkEffect effect = FireworkEffect.builder().flicker(flicker)
                              .trail(trail).withColor(colors).withFade(fadeColors)
                              .with(FireworkEffect.Type.valueOf(type)).build();
      meta.setEffect(effect);
      stack.setItemMeta(meta);
    }
    return stack;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("name", "fireworkeffect");

    colors = new ArrayList<>();
    if(colors.size() > 0) {
      JSONObject colours = new JSONObject();
      for (int i = 0; i < colors.size(); i++) {
        colours.put(i, colors.get(i).asRGB());
        TNE.debug("Color: " + colors.get(i).asRGB());
      }
      json.put("colours", colours);
    }

    fadeColors = new ArrayList<>();
    if(fadeColors.size() > 0) {
      JSONObject fades = new JSONObject();
      for (int i = 0; i < fadeColors.size(); i++) {
        fades.put(i, fadeColors.get(i).asRGB());
        TNE.debug("Fade: " + fadeColors.get(i).asRGB());
      }
      json.put("fades", fades);
    }

    if(hasEffect) {
      JSONObject effect = new JSONObject();
      effect.put("type", type);
      effect.put("trail", trail);
      effect.put("flicker", flicker);
      json.put("effect", effect);
    }
    return json;
  }

  @Override
  public void readJSON(JSONHelper json) {
    valid = true;

    if(json.has("colours")) {
      JSONObject colours = json.getJSON("colours");
      colours.forEach((ignore, value)->{
        colors.add(Color.fromRGB(Integer.valueOf(String.valueOf(value))));
      });
    }

    if(json.has("fades")) {
      JSONObject fades = json.getJSON("fades");
      fades.forEach((ignore, value)->{
        fadeColors.add(Color.fromRGB(Integer.valueOf(String.valueOf(value))));
      });
    }

    if(json.has("effect")) {
      JSONHelper helper = json.getHelper("effect");
      type = helper.getString("type");
      trail = helper.getBoolean("trail");
      flicker = helper.getBoolean("flicker");
    }
  }
}