package net.tnemc.core.commands.module;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleEntry;
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
 * Created by Daniel on 7/10/2017.
 */
public class ModuleUnloadCommand extends TNECommand {

  public ModuleUnloadCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "unload";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "u"
    };
  }

  @Override
  public String getNode() {
    return "tne.module.unload";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Module.Unload";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String moduleName = arguments[0];
      String world = WorldFinder.getWorld(sender);
      ModuleEntry module = TNE.instance().loader().getModule(moduleName);
      String author = module.getInfo().author();
      String version = module.getInfo().version();

      if(TNE.instance().loader().getModule(moduleName) == null) {
        Message message = new Message("Messages.Module.Invalid");
        message.addVariable("$module", moduleName);
        message.translate(world, sender);
        return false;
      }

      TNE.instance().loader().unload(moduleName);
      Message message = new Message("Messages.Module.Unloaded");
      message.addVariable("$module", moduleName);
      message.addVariable("$author", author);
      message.addVariable("$version", version);
      message.translate(world, sender);
      return true;
    }
    help(sender);
    return false;
  }
}