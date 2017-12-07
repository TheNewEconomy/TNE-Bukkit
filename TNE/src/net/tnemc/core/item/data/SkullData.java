package net.tnemc.core.item.data;

import com.github.tnerevival.core.SaveManager;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

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
public class SkullData implements SerialItemData {

  private UUID owner;
  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    ItemMeta meta = stack.getItemMeta();
    if(meta instanceof SkullMeta) {
      valid = true;
      if(((SkullMeta) meta).hasOwner()) {
        owner = IDFinder.getID(((SkullMeta) meta).getOwner());
      }
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      SkullMeta meta = (SkullMeta)stack.getItemMeta();
      if(owner != null) meta.setOwner(owner.toString());
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
