package net.tnemc.core.commands.language;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 1/27/2018.
 */
public class LanguageCommand extends TNECommand {

  public LanguageCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new LanguageCurrentCommand(plugin));
    subCommands.add(new LanguageListCommand(plugin));
    subCommands.add(new LanguageReloadCommand(plugin));
    subCommands.add(new LanguageSetCommand(plugin));
  }

  @Override
  public String getName() {
    return "language";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "lang"
    };
  }

  @Override
  public String getNode() {
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }
}