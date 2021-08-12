package net.tnemc.core.menu.impl.player;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.core.menu.icons.amountselection.AddIcon;
import net.tnemc.core.menu.icons.amountselection.CancelIcon;
import net.tnemc.core.menu.icons.amountselection.ResetIcon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/7/2017.
 */
public class AmountSelectionMenu extends Menu {

  final Icon confirmIcon;

  public AmountSelectionMenu(String name, Icon confirmIcon) {
    super(name, "[TNE]Action", 5);
    this.confirmIcon = confirmIcon;
  }

  @Override
  public Inventory buildInventory(Player player) {
    UUID viewer = IDFinder.getID(player);
    String world = (String) TNE.menuManager().getViewerData(viewer, "action_world");
    UUID playerName = (UUID)TNE.menuManager().getViewerData(viewer, "action_player");
    String currency = (String)TNE.menuManager().getViewerData(viewer, "action_currency");

    if(TNE.menuManager().getViewerData(viewer, "action_amount") == null) {
      TNE.menuManager().setViewerData(viewer, "action_amount", BigDecimal.ZERO);
    }

    BigDecimal amount = (BigDecimal)TNE.menuManager().getViewerData(viewer, "action_amount");

    icons.put(0, new Icon(0, TNE.item().build("PLAYER_HEAD"), ChatColor.stripColor(IDFinder.getUsername(playerName.toString()))));
    icons.put(1, new Icon(1, Material.PAPER, "World: " + world));
    icons.put(4, new Icon(4, Material.PAPER, "Amount: " + amount.toPlainString()));
    icons.put(7, new Icon(7, Material.PAPER, "Currency: " + currency));
    icons.put(8, new Icon(8, TNE.item().build("PLAYER_HEAD"), ChatColor.stripColor(IDFinder.getUsername(playerName.toString()))));

    //Major Icons
    icons.put(18, new AddIcon(18, Material.GOLD_BLOCK, new BigDecimal("100"), getName()));
    icons.put(19, new AddIcon(19, TNE.item().build("LIGHT_WEIGHTED_PRESSURE_PLATE"), new BigDecimal("20"), getName()));
    icons.put(20, new AddIcon(20, Material.GOLD_INGOT, new BigDecimal("5"), getName()));
    icons.put(21, new AddIcon(21, Material.GOLD_NUGGET, BigDecimal.ONE, getName()));

    //Minor Icons
    icons.put(23, new AddIcon(23, TNE.item().build("IRON_NUGGET"), new BigDecimal(".01"), getName()));
    icons.put(24, new AddIcon(24, Material.IRON_INGOT, new BigDecimal(".10"), getName()));
    icons.put(25, new AddIcon(25, TNE.item().build("HEAVY_WEIGHTED_PRESSURE_PLATE"), new BigDecimal(".25"), getName()));
    icons.put(26, new AddIcon(26, Material.IRON_BLOCK, new BigDecimal(".50"), getName()));

    //Control Icons
    icons.put(36, new CancelIcon(36));
    icons.put(40, new ResetIcon(40, getName()));
    icons.put(confirmIcon.getSlot(), confirmIcon);

    return super.buildInventory(player);
  }
}