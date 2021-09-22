package net.tnemc.core.common.menu.consumable.menu.icon;

import net.tnemc.core.common.menu.icon.IconType;
import org.bukkit.inventory.ItemStack;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/3/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class IconBuild {

  private IconType type;
  private ItemStack stack;
  private String menu;

  public IconBuild(IconType type, ItemStack stack, String menu) {
    this.type = type;
    this.stack = stack;
    this.menu = menu;
  }

  public IconType getType() {
    return type;
  }

  public ItemStack getStack() {
    return stack;
  }

  public String getMenu() {
    return menu;
  }
}