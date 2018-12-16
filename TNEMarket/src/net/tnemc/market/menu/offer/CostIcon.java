package net.tnemc.market.menu.offer;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.UUID;

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
    super(slot, Material.GREEN_STAINED_GLASS_PANE, "Item Cost");
    this.close = false;
  }

  @Override
  public ItemStack buildStack(Player player) {
    final UUID id = player.getUniqueId();
    ItemStack cost = new ItemStack(Material.PAPER);
    final String currency = (String)TNE.menuManager().getViewerData(id, "action_currency");
    final BigDecimal amount = (BigDecimal)TNE.menuManager().getViewerData(id, "action_amount");
    final String world = (String)TNE.menuManager().getViewerData(id, "action_world");

    if(TNE.manager().currencyManager().get(world, currency).isItem()) {

    }
    ItemStack stack = ((ItemStack) TNE.menuManager().getViewerData(player.getUniqueId(), "offer_cost")).clone();
    ItemMeta meta = stack.getItemMeta();
    meta.setDisplayName(ChatColor.DARK_PURPLE + "Item Cost");
    stack.setItemMeta(meta);
    return stack;
  }
}
