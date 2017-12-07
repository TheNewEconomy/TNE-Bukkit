package net.tnemc.core.item.data;

import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
  public void save(SaveManager manager) {

  }

  @Override
  public SerialItemData load(SaveManager manager) {
    return null;
  }
}