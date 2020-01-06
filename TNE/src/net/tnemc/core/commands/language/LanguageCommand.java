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
public class LanguageCommand implements CommandExecution {

  public LanguageCommand(TNE plugin) {
    super(plugin);
    addSub(new LanguageCurrentCommand(plugin));
    addSub(new LanguageListCommand(plugin));
    addSub(new LanguageReloadCommand(plugin));
    addSub(new LanguageSetCommand(plugin));
  }

  @Override
  public String name() {
    return "language";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "lang"
    };
  }

  @Override
  public String node() {
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }
}