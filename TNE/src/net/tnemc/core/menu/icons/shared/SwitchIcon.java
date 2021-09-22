package net.tnemc.core.menu.icons.shared;

import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

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
public class SwitchIcon extends Icon {

  public SwitchIcon(ItemStack stack, String display, String menu, Integer slot) {
    super(slot, stack, display);
    this.switchMenu = menu;
  }

  public SwitchIcon(Material material, String display, LinkedList<String> lore, String menu, Integer slot) {
    super(slot, material, display, (short)0, lore);
    this.switchMenu = menu;
  }
}