package net.tnemc.signs.signs.impl.item.menu.offer;

import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
public class ItemIcon extends Icon {

  public ItemIcon(Integer slot) {
    super(slot, TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Shop's Offer");
    this.close = false;
  }

  @Override
  public ItemStack buildStack(Player player) {
    final boolean selling = (Boolean)TNE.menuManager().getViewerData(player.getUniqueId(), "shop_selling");
    ItemStack stack = ((ItemStack)TNE.menuManager().getViewerData(player.getUniqueId(), "shop_item")).clone();
    TNE.debug("Icon Enchant Size: " + stack.getEnchantments().size());
    TNE.debug("icon Enchant Size: " + stack.getItemMeta().getEnchants().size());
    TNE.debug("Item Durability: " + stack.getDurability());
    ItemMeta meta = stack.getItemMeta();
    List<String> lore = new ArrayList<>();
    if(meta.hasDisplayName()) {
      lore.add(ChatColor.WHITE + "Name: " + meta.getDisplayName());
    }

    if(!MaterialUtils.isShulker(stack.getType())) {
      if(meta.hasLore()) {
        lore.addAll(meta.getLore());
      }
    } else {
      this.close = true;
      this.switchMenu = "shop_shulker_preview";
      lore.add(ChatColor.RED + "Click to view preview of shulker box.");
    }
    meta.setLore(lore);
    if(selling) {
      meta.setDisplayName(ChatColor.DARK_PURPLE + "Shop's Offer");
    } else {
      meta.setDisplayName(ChatColor.DARK_PURPLE + "Cost");
    }
    stack.setItemMeta(meta);
    return stack;
  }
}