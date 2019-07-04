package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.utils.IndependenceUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AdminIndependenceCommand extends TNECommand {

  public AdminIndependenceCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "independence";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.independence";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/tne independence - Happy Independence Day!";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    final Player player = getPlayer(sender);
    Bukkit.broadcastMessage(ChatColor.RED + "Happy " + ChatColor.WHITE + "Independence " + ChatColor.BLUE + "Day!");
    IndependenceUtils.play(player.getLocation());
    return true;
  }
}