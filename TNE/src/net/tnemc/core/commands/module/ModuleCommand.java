package net.tnemc.core.commands.module;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class ModuleCommand extends TNECommand {

  public ModuleCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new ModuleDownloadCommand(plugin));
    subCommands.add(new ModuleInfoCommand(plugin));
    subCommands.add(new ModuleListCommand(plugin));
    subCommands.add(new ModuleLoadCommand(plugin));
    subCommands.add(new ModuleReloadCommand(plugin));
    subCommands.add(new ModuleUnloadCommand(plugin));
  }

  @Override
  public String getName() {
    return "tnemodule";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "tnem"
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