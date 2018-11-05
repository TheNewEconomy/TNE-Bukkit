package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/4/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MoneySlyCommand extends TNECommand {

  public MoneySlyCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "sly";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.sly";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/money sly - Duplicates your gold, or something like that.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    sender.sendMessage(ChatColor.RED + "No u");
    Player player = getPlayer(sender);
    player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 50, 0);
    Bukkit.getScheduler().runTaskLater(TNE.instance(), ()->player.setHealth(0.0), 40L);
    return true;
  }
}