package net.tnemc.core.item.data;

import net.tnemc.core.TNE;
import net.tnemc.core.item.JSONHelper;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
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
public class FireworkData implements SerialItemData {

  private List<FireworkEffect> effects = new ArrayList<>();

  private int power;

  private boolean valid = false;
  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof FireworkMeta) {
      valid = true;

      FireworkMeta fireworkMeta = (FireworkMeta)meta;
      power = fireworkMeta.getPower();
      effects = fireworkMeta.getEffects();
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      FireworkMeta meta = (FireworkMeta)stack.getItemMeta();
      meta.setPower(power);
      meta.addEffects(effects);
      stack.setItemMeta(meta);
    }
    return stack;
  }

  @Override
  public JSONObject toJSON() {
    JSONObject firework = new JSONObject();
    firework.put("name", "firework");
    firework.put("power", power);

    if(effects.size() > 0) {
      JSONObject effectsObj = new JSONObject();
      for(int it = 0; it < effects.size(); it++) {
        final FireworkEffect fireworkEffect = effects.get(it);

        JSONObject json = new JSONObject();
        json.put("type", fireworkEffect.getType().name());
        if(fireworkEffect.getColors().size() > 0) {
          JSONObject colours = new JSONObject();
          for (int i = 0; i < fireworkEffect.getColors().size(); i++) {
            colours.put(i, fireworkEffect.getColors().get(i).asRGB());
          }
          json.put("colours", colours);
        }

        if(fireworkEffect.getFadeColors().size() > 0) {
          JSONObject fades = new JSONObject();
          for (int i = 0; i < fireworkEffect.getFadeColors().size(); i++) {
            fades.put(i, fireworkEffect.getFadeColors().get(i).asRGB());
          }
          json.put("fades", fades);
        }
        json.put("flicker", fireworkEffect.hasFlicker());
        json.put("trail", fireworkEffect.hasTrail());

        effectsObj.put(it, json);
      }
      firework.put("effects", effectsObj);
    }
    return firework;
  }

  @Override
  public void readJSON(JSONHelper json) {
    valid = true;
    TNE.debug("FireWork Start");
    power = json.getInteger("power");
    if(json.has("effects")) {
      JSONHelper effectsObj = json.getHelper("effects");

      effects = new ArrayList<>();
      effectsObj.getObject().forEach((key, value)->{
        JSONHelper effect = new JSONHelper((JSONObject)value);

        List<Color> colours = new ArrayList<>();
        TNE.debug("colours");
        if(effect.has("colours")) {
          JSONObject coloursObj = effect.getJSON("colours");
          TNE.debug("Colours?: " + (coloursObj == null));
          TNE.debug("Colours: " + coloursObj.toJSONString());
          coloursObj.forEach((ignore, colour)-> colours.add(Color.fromRGB(Integer.valueOf(String.valueOf(colour)))));
        }

        List<Color> fadeColours = new ArrayList<>();
        TNE.debug("fades");
        if(effect.has("fades")) {
          JSONObject fades = effect.getJSON("fades");
          fades.forEach((ignore, fade)-> fadeColours.add(Color.fromRGB(Integer.valueOf(String.valueOf(fade)))));
        }
        effects.add(FireworkEffect.builder().withFade(fadeColours).withColor(colours).flicker(effect.getBoolean("flicker"))
                    .trail(effect.getBoolean("trail")).with(FireworkEffect.Type.valueOf(effect.getString("type"))).build());
        TNE.debug("Effects End");
      });
      TNE.debug("FireWork End");
    }
  }
}
