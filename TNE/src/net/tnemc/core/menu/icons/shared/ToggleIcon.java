package net.tnemc.core.menu.icons.shared;

import net.tnemc.core.menu.icons.Icon;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/31/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public abstract class ToggleIcon extends Icon {

  private final ItemStack toggled;

  public ToggleIcon(Integer slot, ItemStack stack, String menu, ItemStack toggled) {
    super(slot, stack);

    this.toggled = toggled;

    this.switchMenu = menu;
  }

  public abstract void toggleData(UUID uuid);
  public abstract boolean isToggled(UUID uuid);

  @Override
  public ItemStack buildStack(Player player) {
    if(isToggled(player.getUniqueId())) {
      return toggled;
    }
    return super.buildStack(player);
  }

  @Override
  public void onClick(String menu, Player player, ClickType type) {
    toggleData(player.getUniqueId());
    super.onClick(menu, player, type);
  }
}