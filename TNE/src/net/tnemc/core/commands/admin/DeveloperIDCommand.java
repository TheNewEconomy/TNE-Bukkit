package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 12/6/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class DeveloperIDCommand extends TNECommand {

  public DeveloperIDCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "id";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean developer() {
    return true;
  }

  @Override
  public String helpLine() {
    return "/tnedev id <username> - Display debug information regarding a username and their UUID.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {

      final String original = arguments[0];
      final boolean exists = TNE.instance().api().hasAccount(original);

      System.out.println("Username: " + original);
      System.out.println("Exists: " + exists);

      if(!exists) {
        TNE.instance().api().createAccount(original);
        System.out.println("Account Created");
      }
      final UUID id = IDFinder.getID(original);
      final String userFromID = IDFinder.getUsername(id.toString());
      final UUID idFromUserFromID = IDFinder.getID(userFromID);

      System.out.println("UUID: " + id.toString());
      System.out.println("UserFromUUID: " + userFromID);
      System.out.println("UUIDFromUserFromUUID: " + idFromUserFromID.toString());

      return true;
    }
    help(sender);
    return false;
  }
}