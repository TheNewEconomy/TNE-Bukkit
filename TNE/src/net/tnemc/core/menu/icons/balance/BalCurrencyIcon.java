package net.tnemc.core.menu.icons.balance;

import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.LinkedList;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/11/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BalCurrencyIcon extends Icon {

  ItemStack curStack = null;

  public BalCurrencyIcon(TNECurrency currency, String formatBalance, String world, Integer slot) {
    super(slot, Material.PAPER, formatBalance);

    LinkedList<String> lore = new LinkedList<>();
    lore.add(ChatColor.GOLD + "Left" + ChatColor.LIGHT_PURPLE + " click to pay player.");
    lore.add(ChatColor.GOLD + "Right" + ChatColor.LIGHT_PURPLE + " click for other options.");

    if(currency.isItem()) {
      curStack = currency.getTNEMajorTiers().ceilingEntry(new BigInteger("100")).getValue().getItemInfo().toStack();
      ItemMeta meta = curStack.getItemMeta();
      meta.setLore(lore);
      meta.setDisplayName(formatBalance);
      curStack.setItemMeta(meta);
    } else {
      this.lore = lore;
    }

    data.put("action_currency", currency.name());
    data.put("action_world", world);
  }

  @Override
  public ItemStack buildStack(Player player) {
    if(curStack != null) {
      return curStack;
    }
    return super.buildStack(player);
  }

  @Override
  public void onClick(String menu, Player player, ClickType type) {
    if(type.isRightClick()) switchMenu = "balance_options";
    super.onClick(menu, player, type);
  }
}