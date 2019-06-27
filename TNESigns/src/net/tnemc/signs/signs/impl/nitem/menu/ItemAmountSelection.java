package net.tnemc.signs.signs.impl.nitem.menu;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.signs.signs.impl.nitem.menu.amountselection.CancelIcon;
import net.tnemc.signs.signs.impl.nitem.menu.itemselection.AddIcon;
import net.tnemc.signs.signs.impl.nitem.menu.itemselection.ResetIcon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/7/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ItemAmountSelection extends Menu {

  final String name;
  final String data;
  Icon confirm;

  public ItemAmountSelection(final String name, final String data, Icon confirm) {
    super(name, "Item Amount Selection", 5);

    this.name = name;
    this.data = data;
    this.confirm = confirm;
  }

  @Override
  public Inventory buildInventory(Player player) {

    final UUID id = player.getUniqueId();
    final int amount = (Integer)TNE.menuManager().getViewerData(id, data);

    icons.put(4, new Icon(4, Material.PAPER, "Amount: " + amount));
    icons.put(19, new AddIcon(19, Material.GOLD_INGOT, name, data, 64));
    icons.put(21, new AddIcon(21, Material.GOLD_NUGGET, name, data, 32));
    icons.put(23, new AddIcon(23, Material.IRON_INGOT, name, data, 5));
    icons.put(25, new AddIcon(25, Material.IRON_NUGGET, name, data, 1));
    icons.put(38, new CancelIcon(38));
    icons.put(40, new ResetIcon(40, name, data));
    icons.put(42, confirm);


    return super.buildInventory(player);
  }
}