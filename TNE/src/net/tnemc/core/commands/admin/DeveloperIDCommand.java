package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
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
public class DeveloperIDCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 1) {

      final String original = arguments[0];
      final boolean exists = TNE.instance().api().hasAccount(original);

      TNE.logger().info("Username: " + original);
      TNE.logger().info("Exists: " + exists);

      if(!exists) {
        TNE.instance().api().createAccount(original);
        TNE.logger().info("Account Created");
      }
      final UUID id = IDFinder.getID(original);
      final String userFromID = IDFinder.getUsername(id.toString());
      final UUID idFromUserFromID = IDFinder.getID(userFromID);

      TNE.logger().info("UUID: " + id.toString());
      TNE.logger().info("UserFromUUID: " + userFromID);
      TNE.logger().info("UUIDFromUserFromUUID: " + idFromUserFromID.toString());

      return true;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}