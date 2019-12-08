package net.tnemc.bounty.command;

import net.tnemc.bounty.BountyData;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BountyListCommand extends TNECommand {
  public BountyListCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "view";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.bounty.view";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String helpLine() {
    return "/bounty view [player] - Views a list of the active bounties.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    if(arguments.length >= 1) {
      final UUID id = IDFinder.getID(arguments[0]);
      if(id == null) {
        sender.sendMessage(ChatColor.RED + "Invalid player argument specified.");
        return false;
      }

      if(!BountyData.hasBounty(id)) {
        sender.sendMessage(ChatColor.RED + "That player does not currently have a bounty on them.");
        return false;
      }


      TNE.menuManager().setViewerData(IDFinder.getID(sender), "hunted_id", id.toString());
      TNE.menuManager().open("bounty_view_player", getPlayer(sender));
      return true;
    }

    TNE.menuManager().open("bounty_view", getPlayer(sender));
    return true;
  }
}