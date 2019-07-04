package net.tnemc.bounty.command;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

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
public class BountyHunterCommand extends TNECommand {
  public BountyHunterCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "hunter";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.bounty.hunter";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/bounty hunter [player] - Opens up the bounty hunter menu.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    UUID id = IDFinder.getID(sender);

    if(arguments.length > 1) {
      id = IDFinder.getID(Bukkit.getOfflinePlayer(arguments[0]));
    }

    final UUID uuid = id;

    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      TNE.menuManager().setViewerData(uuid, "hunter_id", uuid.toString());

      TNE.menuManager().open("bounty_hunter_menu", getPlayer(sender));
    });
    return true;
  }
}