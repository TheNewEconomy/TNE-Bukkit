package net.tnemc.core.commands.language;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
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
public class LanguageReloadCommand extends TNECommand {

  public LanguageReloadCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "reload";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.language.reload";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Language.Reload";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.instance().messages().loadLanguages();
    Message message = new Message("Messages.Language.Reload");
    message.translate(WorldFinder.getWorld(sender, WorldVariant.ACTUAL), sender);
    return true;
  }
}