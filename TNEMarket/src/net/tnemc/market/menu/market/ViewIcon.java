package net.tnemc.market.menu.market;

import net.tnemc.core.menu.icons.Icon;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ViewIcon extends Icon {

  private ItemStack stack;
  private final UUID offer;

  public ViewIcon(Integer slot, ItemStack stack, UUID offer) {
    super(slot, stack.getType(), "");
    this.stack = stack.clone();
    this.offer = offer;
  }

  @Override
  public ItemStack buildStack(Player player) {

    List<String> lore = new ArrayList<>();
    lore.add(ChatColor.RED + "Click to view/buy item.");
    ItemMeta meta = stack.getItemMeta();
    lore.addAll(meta.getLore());
    meta.setLore(lore);
    stack.setItemMeta(meta);

    return stack;
  }
}