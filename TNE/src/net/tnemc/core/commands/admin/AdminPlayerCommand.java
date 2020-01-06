package net.tnemc.core.commands.admin;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.api.IDFinder;
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

  public AdminPlayerCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "player";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.admin.player";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "/tne player <username> - Checks if the specified account is a player or not.";
  }

  /*@Override
  public List<String> onTab(CommandSender sender, Command command, String alias, String[] arguments, boolean shortened) {
    Map<Integer, String> argTypes = new HashMap<>();
    argTypes.put(0, "player");
    argTypes.put(1, "world");
    argTypes.put(2, "currency");
    return buildSuggestions(sender, shortened, arguments, argTypes, 1);
  }*/

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
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