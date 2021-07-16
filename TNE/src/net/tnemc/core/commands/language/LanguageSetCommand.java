package net.tnemc.core.commands.language;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
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

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
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