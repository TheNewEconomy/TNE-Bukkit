package net.tnemc.conversion.menu;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.core.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/25/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ConversionMenu extends Menu {
  public ConversionMenu() {
    super("conversion_menu", "Detected Plugins", 3);
  }

  @Override
  public Inventory buildInventory(Player player) {
    List<String> available = ConversionModule.instance().manager().search();

    int i = 0;

    for(String plugin : available) {

      icons.put(i, new ConvertIcon(i, plugin));

      i++;
    }

    return super.buildInventory(player);
  }
}