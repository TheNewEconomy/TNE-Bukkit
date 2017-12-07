package net.tnemc.core.commands.module;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
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
 * Created by Daniel on 7/27/2017.
 */
public class ModuleListCommand extends TNECommand {

  public ModuleListCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "i"
    };
  }

  @Override
  public String getNode() {
    return "tne.module.list";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Module.List";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    StringBuilder modules = new StringBuilder();
    TNE.loader().getModules().forEach((key, value)->{
      if(modules.length() > 0) modules.append(", ");
      modules.append(value.getInfo().name());
    });

    Message message = new Message("Messages.Module.List");
    message.addVariable("$modules", modules.toString());
    message.translate(WorldFinder.getWorld(sender), sender);
    return true;
  }
}