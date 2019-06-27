package net.tnemc.signs.selection.impl;

import net.tnemc.signs.SignsData;
import net.tnemc.signs.selection.Selection;
import net.tnemc.signs.signs.impl.CommandSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.sql.SQLException;
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
public class CommandSelection implements Selection {
  @Override
  public String name() {
    return "command";
  }

  @Override
  public void select(UUID identifier, Location location, Object selection) {
    try {
      SignsData.updateStep(location, 2);
      CommandSign.saveCommand(location, (String)selection);
      Bukkit.getPlayer(identifier).sendMessage(ChatColor.WHITE + "Sign command set to \"" + selection + "\". Now right click with an item to set cost to an item, otherwise left click to select currency cost.");
    } catch (SQLException ignore) {
    }
  }
}