package net.tnemc.vaults;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.vaults.command.VaultCommand;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * All rights reserved.
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