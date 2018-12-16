package net.tnemc.market.menu;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.Menu;
import net.tnemc.market.menu.preview.BackIcon;
import net.tnemc.market.menu.preview.DisplayIcon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/2/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ShulkerPreviewMenu extends Menu {
  public ShulkerPreviewMenu() {
    super("shop_shulker_preview", ChatColor.GOLD + "[TNE]Shulker Preview", 4);
    icons.put(31, new BackIcon(31, "market_offer_menu"));
  }

  @Override
  public Inventory buildInventory(Player player) {
    final ItemStack stack = ((ItemStack) TNE.menuManager().getViewerData(player.getUniqueId(), "shop_item")).clone();
    final Inventory inv = ((ShulkerBox)((BlockStateMeta)stack.getItemMeta()).getBlockState()).getInventory();
    for(int i = 0; i < inv.getSize(); i++) {
      if(inv.getItem(i) != null && !inv.getItem(i).getType().equals(Material.AIR)) {
        icons.put(i, new DisplayIcon(i, inv.getItem(i)));
      }
    }
    return super.buildInventory(player);
  }
}