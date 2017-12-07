package net.tnemc.core.item.data;

import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.TNE;
import net.tnemc.core.item.SerialItem;
import net.tnemc.core.item.SerialItemData;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

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
 * Created by Daniel on 11/11/2017.
 */
public class ShulkerData implements SerialItemData {

  private Map<Integer, SerialItem> items = new HashMap<>();

  private boolean valid = false;

  @Override
  public SerialItemData initialize(ItemStack stack) {
    TNE.debug("ShulkerData.initialize.");
    if(SerialItem.isShulker(stack.getType())) {
      TNE.debug("Passed shulker check");
      ItemMeta meta = stack.getItemMeta();
      if(meta instanceof BlockStateMeta) {
        TNE.debug("Is BlockStateMeta");
        BlockStateMeta state = (BlockStateMeta)meta;
        if(state.getBlockState() instanceof ShulkerBox) {
          TNE.debug("Is ShulkerBox BlockState");
          valid = true;
          ShulkerBox shulker = (ShulkerBox)state.getBlockState();
          Inventory inventory = shulker.getInventory();
          TNE.debug("Initializing shulker box..");
          for(int i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)) {
              if(items.containsKey(i)) {
                items.remove(i);
                TNE.debug("Removing item from slot " + i);
              }
            } else {
              TNE.debug("Adding Item: " + inventory.getItem(i).getType().name());
              TNE.debug("Item Slot: " + i);
              items.put(i, new SerialItem(inventory.getItem(i)));
            }
          }
        }
      }
    }
    return this;
  }

  @Override
  public ItemStack build(ItemStack stack) {
    if(valid) {
      BlockStateMeta meta = (BlockStateMeta)stack.getItemMeta();
      ShulkerBox shulkerBox = (ShulkerBox)meta;
      items.forEach((slot, item)->shulkerBox.getInventory().setItem(slot, item.getStack()));
      meta.setBlockState(shulkerBox);
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

  public Map<Integer, SerialItem> getItems() {
    return items;
  }

  public void setItems(Map<Integer, SerialItem> items) {
    this.items = items;
  }
}