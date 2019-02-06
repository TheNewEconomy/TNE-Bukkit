package net.tnemc.signs.signs.impl.item.menu.itemselection;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
public class AddIcon extends Icon {

  private final String data;
  private final int amount;

  public AddIcon(Integer slot, final ItemStack stack, final String menu,
                 final String data, final int amount) {
    super(slot, stack, "Add " + amount);

    this.data = data;
    this.amount = amount;
    this.switchMenu = menu;
  }

  public AddIcon(Integer slot, final Material iconMat, final String menu,
                 final String data, final int amount) {
    super(slot, iconMat, "Add " + amount);

    this.data = data;
    this.amount = amount;
    this.switchMenu = menu;
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.menuManager().setViewerData(player.getUniqueId(), data, amount + ((Integer)TNE.menuManager().getViewerData(player.getUniqueId(), data)));
    super.onClick(menu, player);
  }
}