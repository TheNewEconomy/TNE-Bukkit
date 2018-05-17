package net.tnemc.core.commands.admin;

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
public class AdminCommand extends TNECommand {

  public AdminCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new AdminBackupCommand(plugin));
    subCommands.add(new AdminBalanceCommand(plugin));
    subCommands.add(new AdminBuildCommand(plugin));
    subCommands.add(new AdminCaveatsCommand(plugin));
    subCommands.add(new AdminCreateCommand(plugin));
    subCommands.add(new AdminDeleteCommand(plugin));
    subCommands.add(new AdminExtractCommand(plugin));
    subCommands.add(new AdminIDCommand(plugin));
    subCommands.add(new AdminIDExportCommand(plugin));
    subCommands.add(new AdminMenuCommand(plugin));
    subCommands.add(new AdminPurgeCommand(plugin));
    subCommands.add(new AdminReloadCommand(plugin));
    subCommands.add(new AdminResetCommand(plugin));
    subCommands.add(new AdminRestoreCommand(plugin));
    subCommands.add(new AdminSaveCommand(plugin));
    subCommands.add(new AdminStatusCommand(plugin));
    subCommands.add(new AdminTestCommand(plugin));
    subCommands.add(new AdminUploadCommand(plugin));
    subCommands.add(new AdminVersionCommand(plugin));
  }

  @Override
  public String getName() {
    return "tne";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin";
  }

  @Override
  public boolean console() {
    return true;
  }
}