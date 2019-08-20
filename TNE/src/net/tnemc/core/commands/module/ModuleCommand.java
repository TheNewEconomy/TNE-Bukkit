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
    addSub(new ModuleAvailableCommand(plugin));
    addSub(new ModuleDownloadCommand(plugin));
    addSub(new ModuleInfoCommand(plugin));
    addSub(new ModuleListCommand(plugin));
    addSub(new ModuleLoadCommand(plugin));
    addSub(new ModuleReloadCommand(plugin));
    addSub(new ModuleUnloadCommand(plugin));
  }

  @Override
  public String name() {
    return "tnemodule";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "tnem"
    };
  }

  @Override
  public String node() {
    return "";
  }

  @Override
  public boolean console() {
    return true;
  }
}