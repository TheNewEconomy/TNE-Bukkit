package net.tnemc.bounty.menu;

import net.tnemc.bounty.BountyData;
import net.tnemc.bounty.model.Bounty;
import net.tnemc.core.TNE;
import net.tnemc.core.item.SerialItem;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.MenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.parser.ParseException;

import java.util.LinkedList;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BountyViewMenu extends Menu {
  public BountyViewMenu() {
    super("bounty_view_player", "[TNE]Bounty View", 1);
  }

  @Override
  public Inventory buildInventory(Player player) {
    final UUID hunted = UUID.fromString((String)TNE.menuManager().getViewerData(player.getUniqueId(), "hunted_id"));
    final Bounty bounty = BountyData.getBounty(hunted);

    ItemStack stack = new ItemStack(Material.PAPER);
    ItemMeta iMeta = stack.getItemMeta();
    if(bounty.isCurrencyReward()) {
      iMeta.setDisplayName(ChatColor.GOLD + "Currency Reward");
      LinkedList<String> lore = new LinkedList<>();
      lore.add("Currency: " + bounty.getCurrency());
      lore.add("Amount: " + bounty.getAmount().toPlainString());
      iMeta.setLore(lore);
      stack.setItemMeta(iMeta);
    } else {
      try {
        stack = SerialItem.unserialize(bounty.getItemReward()).getStack();
      } catch (ParseException ignore) {
      }
    }


    ItemStack skull = TNE.item().build("PLAYER_HEAD");
    SkullMeta meta = (SkullMeta) skull.getItemMeta();
    meta.setOwningPlayer(Bukkit.getOfflinePlayer(hunted));
    skull.setItemMeta(meta);

    Inventory inventory = Bukkit.createInventory(new MenuHolder(getName()), 9, getTitle());
    inventory.setItem(2, skull);
    inventory.setItem(6, stack);
    return inventory;
  }
}