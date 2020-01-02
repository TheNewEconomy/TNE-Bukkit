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
    addSub(new AdminAccountCommand(plugin));
    addSub(new AdminBalanceCommand(plugin));
    addSub(new AdminBuildCommand(plugin));
    addSub(new AdminCaveatsCommand(plugin));
    addSub(new AdminCreateCommand(plugin));
    addSub(new AdminDebugCommand(plugin));
    addSub(new AdminDeleteCommand(plugin));
    addSub(new AdminExtractCommand(plugin));
    //addSub(new AdminIDCommand(plugin));
    addSub(new DeveloperIDCommand(plugin));
    addSub(new AdminIDExportCommand(plugin));
    //addSub(new AdminIndependenceCommand(plugin));
    addSub(new AdminMaintenanceMode(plugin));
    addSub(new AdminMenuCommand(plugin));
    addSub(new AdminPlayerCommand(plugin));
    addSub(new AdminPurgeCommand(plugin));
    addSub(new AdminReloadCommand(plugin));
    addSub(new AdminReportCommand(plugin));
    addSub(new AdminResetCommand(plugin));
    addSub(new AdminRestoreCommand(plugin));
    addSub(new AdminSaveCommand(plugin));
    addSub(new AdminStatusCommand(plugin));
    addSub(new AdminTestCommand(plugin));
    addSub(new AdminUploadCommand(plugin));
    addSub(new AdminVersionCommand(plugin));
  }

  @Override
  public String name() {
    return "tne";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "eco", "theneweconomy"
    };
  }

  @Override
  public String node() {
    return "tne.admin";
  }

  @Override
  public boolean console() {
    return true;
  }
}