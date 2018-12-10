package net.tnemc.tools.command;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.tools.command.impl.ToolsRemoveAllCommand;
import net.tnemc.tools.command.impl.ToolsResetCommand;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/5/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ToolsCommand extends TNECommand {
  public ToolsCommand(TNE plugin) {
    super(plugin);

    subCommands.add(new ToolsResetCommand(plugin));
    subCommands.add(new ToolsRemoveAllCommand(plugin));
  }

  @Override
  public String getName() {
    return "tnetools";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "tnet"
    };
  }

  @Override
  public String getNode() {
    return "";
  }

  @Override
  public boolean console() {
    return true;
  }
}