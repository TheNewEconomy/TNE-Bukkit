package net.tnemc.core.menu.icons.shared.toggle;

import net.tnemc.core.menu.icons.shared.ToggleIcon;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/2/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class StringListIcon extends ToggleIcon {

  public StringListIcon(ItemStack stack, String menu, ItemStack toggled, Integer slot, String dataValue) {
    super(slot, stack, menu, toggled);
  }

  @Override
  public void toggleData(UUID uuid) {

  }

  @Override
  public boolean isToggled(UUID uuid) {
    return false;
  }
}