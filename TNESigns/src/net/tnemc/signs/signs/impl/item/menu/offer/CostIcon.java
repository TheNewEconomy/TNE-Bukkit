package net.tnemc.signs.signs.impl.item.menu.offer;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
public class CostIcon extends Icon {

  public CostIcon(Integer slot) {
    super(slot, TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Item Cost");
    this.close = false;
  }

  @Override
  public ItemStack buildStack(Player player) {
    final boolean selling = (Boolean)TNE.menuManager().getViewerData(player.getUniqueId(), "shop_selling");
    ItemStack stack = ((ItemStack) TNE.menuManager().getViewerData(player.getUniqueId(), "shop_cost")).clone();
    ItemMeta meta = stack.getItemMeta();
    if(selling) {
      meta.setDisplayName(ChatColor.DARK_PURPLE + "Item Cost");
    } else {
      meta.setDisplayName(ChatColor.DARK_PURPLE + "Shop's Offer");
    }
    stack.setItemMeta(meta);
    return stack;
  }
}
