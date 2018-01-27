package net.tnemc.core.commands.language;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 1/27/2018.
 */
public class LanguageSetCommand extends TNECommand {

  public LanguageSetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "set";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.language.set";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Language.Set";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender, WorldVariant.ACTUAL);
      String language = arguments[0];

      if(TNE.instance().messages().getLanguages().containsKey(language)) {
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
    help(sender);
    return false;
  }
}