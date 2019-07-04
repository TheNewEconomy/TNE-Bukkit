package net.tnemc.vaults;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.vaults.command.VaultCommand;

import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 **/
@ModuleInfo(
    name = "Vaults",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class VaultsModule extends Module {

  private static VaultsModule instance;
  private VaultManager manager;

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Vaults Module loaded!");
    instance = this;
    manager = new VaultManager();
    commands.add(new VaultCommand(tne));
    listeners.add(new VaultListener(tne));
  }

  @Override
  public Map<String, List<String>> getTables() {
    return super.getTables();
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Vaults Module unloaded!");
  }

  public static VaultsModule instance() {
    return instance;
  }

  public VaultManager manager() {
    return manager;
  }
}