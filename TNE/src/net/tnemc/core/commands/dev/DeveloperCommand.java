package net.tnemc.core.commands.dev;

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
public class DeveloperCommand extends TNECommand {

  public DeveloperCommand(TNE plugin) {
    super(plugin);
    addSub(new DeveloperDebugCommand(plugin));
  }

  @Override
  public String name() {
    return "tnedev";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "theneweconomydev"
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

  public boolean developer() {
    return true;
  }
}