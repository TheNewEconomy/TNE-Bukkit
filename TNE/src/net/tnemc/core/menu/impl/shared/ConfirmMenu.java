package net.tnemc.core.menu.impl.shared;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.consumables.IconClick;
import net.tnemc.core.menu.icons.shared.ActionIcon;
import net.tnemc.core.menu.icons.shared.InformIcon;
import net.tnemc.core.menu.icons.shared.SwitchIcon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/8/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ConfirmMenu extends Menu {

  private Consumer<IconClick> successAction;
  private String message;

  public ConfirmMenu(String name, String message, Consumer<IconClick> successAction) {
    super(name, "Confirm Action", 1);

    this.message = message;
    this.successAction = successAction;
  }

  @Override
  public Inventory buildInventory(Player player) {
    final String menu = (String)TNE.menuManager().getViewerData(IDFinder.getID(player), "action_menu");

    icons.put(0, new SwitchIcon(TNE.item().build("BARRIER"), "Cancel", menu, 0));

    icons.put(4, new InformIcon(new ItemStack(Material.PAPER), 4, message));

    icons.put(8, new ActionIcon(TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Confirm", 8, successAction));

    return super.buildInventory(player);
  }
}