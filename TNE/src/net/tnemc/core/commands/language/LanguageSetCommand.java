package net.tnemc.core.commands.language;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 1/27/2018.
 */
public class LanguageSetCommand implements CommandExecution {

  public LanguageSetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "set";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.language.set";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Language.Set";
  }

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.ACTUAL);
      String language = arguments[0];

      if(TNE.instance().messages().getLanguages().containsKey(language) || language.equalsIgnoreCase("default")) {
        TNEAccount account = TNE.manager().getAccount(IDFinder.getID(sender));
        account.setLanguage(language);
        TNE.manager().addAccount(account);

        Message message = new Message("Messages.Language.Set");
        message.addVariable("$language", language);
        message.translate(world, sender);
        return true;
      }
      Message message = new Message("Messages.Language.None");
      message.addVariable("$language", language);
      message.translate(world, sender);
      return false;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}