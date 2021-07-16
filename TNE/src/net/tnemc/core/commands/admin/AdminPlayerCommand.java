package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 2/11/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AdminPlayerCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    TNE.debug("===START AdminBalanceCommand  ===");
    if(arguments.length >= 1 && arguments.length <= 3) {

      UUID id = IDFinder.getID(arguments[0]);
      final boolean player = TNE.manager().getAccount(id).playerAccount();
      final String not = (player)? "" : " not";
      sender.sendMessage(ChatColor.WHITE + arguments[0] + " is" + not + " a player.");
    }
    MISCUtils.help(sender, label, arguments);
    TNE.debug("===END AdminBalanceCommand  ===");
    return false;
  }
}