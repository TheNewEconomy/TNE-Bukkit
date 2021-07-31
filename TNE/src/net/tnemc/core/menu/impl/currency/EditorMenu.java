package net.tnemc.core.menu.impl.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.item.ItemStackBuilder;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.currency.DeleteIcon;
import net.tnemc.core.menu.icons.shared.ActionIcon;
import net.tnemc.core.menu.icons.shared.BackIcon;
import net.tnemc.core.menu.icons.shared.ChatResponseIcon;
import net.tnemc.core.menu.icons.shared.InformIcon;
import net.tnemc.core.menu.icons.shared.SwitchIcon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class EditorMenu extends Menu {

  public EditorMenu() {
    super("currency_editor", "Currency Editor", 6);
  }

  @Override
  public Inventory buildInventory(Player player) {
    UUID viewer = IDFinder.getID(player);

    final String currency = (String) TNE.menuManager().getViewerData(viewer, "action_currency");

    this.setTitle("Currency Editor: " + currency);

    //Row 1
    icons.put(4, new ChatResponseIcon(new ItemStackBuilder(Material.NAME_TAG)
        .setLore(Collections.singletonList(ChatColor.DARK_PURPLE + "Click to edit name.")).getStack(), 4, currency,
        "currency_data_name", ChatColor.YELLOW + "Please enter a new name for this currency:"));

    //Row 2
    icons.put(9, new SwitchIcon(new ItemStack(Material.ANVIL), "Tiers", "currency_tiers", 9));
    icons.put(11, new SwitchIcon(new ItemStack(Material.NETHER_STAR), "Options", "currency_options", 11));
    icons.put(15, new SwitchIcon(new ItemStack(Material.MAP), "Note Settings", "currency_note", 15));
    icons.put(17, new SwitchIcon(new ItemStack(Material.BOOK), "Basic Info", "currency_info", 17));

    //Row 4
    icons.put(27, new InformIcon(new ItemStack(Material.PLAYER_HEAD), 27, "Default?"));
    icons.put(29, new InformIcon(new ItemStack(Material.ENDER_CHEST), 29, "Enderchest Supported"));
    icons.put(31, new SwitchIcon(new ItemStack(Material.CLOCK), "Worlds", "currency_worlds", 31));
    icons.put(33, new InformIcon(new ItemStack(Material.REDSTONE_TORCH), 33, "Enabled?"));
    icons.put(35, new SwitchIcon(new ItemStack(Material.EMERALD), "Currency Type", "currency_type", 35));


    //Row 6
    icons.put(45, new BackIcon("currency_list", 45));
    icons.put(49, new ActionIcon(TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Save Currency", 49, (iconClick)->{
      //TODO: Save currency
    }));
    icons.put(53, new DeleteIcon(53));

    return super.buildInventory(player);
  }
}