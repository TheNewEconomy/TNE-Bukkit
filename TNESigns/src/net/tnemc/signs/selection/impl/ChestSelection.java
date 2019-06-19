package net.tnemc.signs.selection.impl;

import net.tnemc.signs.selection.Selection;
import org.bukkit.Location;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/19/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ChestSelection implements Selection {
  @Override
  public String name() {
    return "chest";
  }

  @Override
  public void select(UUID identifier, Location location, Object selection) {

  }
}