package net.tnemc.signs.signs.impl.toitem.menu.itemselection;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.menu.icons.Icon;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.impl.ItemSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
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
public class ConfirmIcon extends Icon {

  final String data;

  public ConfirmIcon(Integer slot, final String data) {
    super(slot, TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Confirm Amount");

    this.data = data;
    this.close = true;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START Confirm.onClick =====");
    final UUID id = IDFinder.getID(player);
    final int amount = (Integer)TNE.menuManager().getViewerData(id, data);
    final boolean selling = (Boolean)TNE.menuManager().getViewerData(id, "shop_sell");
    ItemStack stack = ((ItemStack)TNE.menuManager().getViewerData(id, "shop_item")).clone();
    stack.setAmount(amount);
    final Location location = (Location) TNE.menuManager().getViewerData(id, "action_shop");

    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{

      try {
        ItemSign.saveItemSelection(location, stack, selling);
        SignsData.updateStep(location, 2);

        this.message = ChatColor.WHITE + "Changed shop offer to " + amount + " of " + stack.getType().name() + "."
            + " Right click with item to trade, or left click to enter currency amount.";

      } catch (SQLException e) {
        this.message = ChatColor.RED + "Unable to update your shop's offer amount.";
      }
      player.sendMessage(message);
      this.message = "";

      super.onClick(menu, player);
    });
  }
}
