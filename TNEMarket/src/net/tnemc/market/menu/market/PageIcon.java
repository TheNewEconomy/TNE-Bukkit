package net.tnemc.market.menu.market;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PageIcon extends Icon {
  private final Integer change;
  public PageIcon(Integer slot, Material material, String display, Integer change) {
    super(slot, material, display);
    this.switchMenu = "market_view";
    this.change = change;
  }

  @Override
  public void onClick(String menu, Player player) {
    Integer page = (Integer) TNE.menuManager().getViewerData(player.getUniqueId(), "market_page");
    TNE.menuManager().setViewerData(player.getUniqueId(), "market_page", page + change);
  }
}