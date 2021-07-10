package net.tnemc.core.menu.icons.shared;

import net.tnemc.core.menu.consumables.IconClick;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

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
public class ActionIcon extends Icon {


  public ActionIcon(ItemStack stack, String display, Integer slot, Consumer<IconClick> action) {
    super(slot, stack, display);

    this.clickAction = action;
  }
}