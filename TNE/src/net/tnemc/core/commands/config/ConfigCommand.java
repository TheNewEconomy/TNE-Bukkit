package net.tnemc.core.commands.config;

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
public class ConfigCommand extends TNECommand {

  public ConfigCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new ConfigGetCommand(plugin));
    subCommands.add(new ConfigSaveCommand(plugin));
    subCommands.add(new ConfigSetCommand(plugin));
    subCommands.add(new ConfigTNEGetCommand(plugin));
    subCommands.add(new ConfigUndoCommand(plugin));
  }

  @Override
  public String getName() {
    return "tneconfig";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "tnec"
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