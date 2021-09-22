package net.tnemc.core.menu.impl.balance;

import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.shared.BackIcon;
import net.tnemc.core.menu.icons.shared.SwitchIcon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
public class BalanceOptionsMenu extends Menu {
  public BalanceOptionsMenu() {
    super("balance_options", ChatColor.GOLD + "Your Money", 1);
  }

  @Override
  public Inventory buildInventory(Player player) {

    LinkedList<String> loreNote = new LinkedList<>();
    loreNote.add(ChatColor.DARK_PURPLE + "Send your money to a physical bank");
    loreNote.add(ChatColor.DARK_PURPLE + "note for trading or storage.");

    icons.put(3, new SwitchIcon(Material.PAPER, ChatColor.GOLD + "Send to Note", loreNote, "balance_note", 3));

    LinkedList<String> anvilNote = new LinkedList<>();
    anvilNote.add(ChatColor.DARK_PURPLE + "Convert this currency");
    anvilNote.add(ChatColor.DARK_PURPLE + "to another currency.");
    icons.put(4, new SwitchIcon(Material.ANVIL, ChatColor.GOLD + "Convert", anvilNote, "balance_note", 4));
    icons.put(1, new BackIcon("balance_menu", 1));

    return super.buildInventory(player);
  }
}
