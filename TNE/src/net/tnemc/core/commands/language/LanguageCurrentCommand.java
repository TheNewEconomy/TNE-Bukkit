package net.tnemc.core.commands.language;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
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
public class LanguageCurrentCommand implements CommandExecution {

  public LanguageCurrentCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "current";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.language.current";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Language.Current";
  }

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
    String language = TNE.manager().getAccount(IDFinder.getID(sender)).getLanguage();

    Message message = new Message("Messages.Language.Current");
    message.addVariable("$language", language);
    message.translate(WorldFinder.getWorld(sender, WorldVariant.ACTUAL), sender);
    return true;
  }
}