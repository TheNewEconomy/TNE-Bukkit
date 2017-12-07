package net.tnemc.signs;

import com.github.tnerevival.commands.CommandManager;
import net.tnemc.core.TNE;
import net.tnemc.core.common.configurations.MainConfigurations;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;

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
    name = "Signs",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class SignsModule extends Module {

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Signs Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Signs Module unloaded!");
  }

  @Override
  public void registerMainConfigurations(MainConfigurations configuration) {
    configuration.configurations.put("Core.Signs.Balance.Enabled", true);
    configuration.configurations.put("Core.Signs.Balance.TNECurrency", "Default");
    configuration.configurations.put("Core.Signs.Balance.Place", 20.0);
    configuration.configurations.put("Core.Signs.Balance.Use", 20.0);
    configuration.configurations.put("Core.Signs.Bank.Enabled", true);
    configuration.configurations.put("Core.Signs.Bank.Place", 20.0);
    configuration.configurations.put("Core.Signs.Bank.Use", 20.0);
    configuration.configurations.put("Core.Signs.Vault.Enabled", true);
    configuration.configurations.put("Core.Signs.Vault.Place", 20.0);
    configuration.configurations.put("Core.Signs.Vault.Use", 20.0);
    configuration.configurations.put("Core.Signs.Shop.Enabled", true);
    configuration.configurations.put("Core.Signs.Shop.Place", 20.0);
    configuration.configurations.put("Core.Signs.Shop.Use", 20.0);
    configuration.configurations.put("Core.Signs.Item.Enabled", true);
    configuration.configurations.put("Core.Signs.Item.Place", 20.0);
    configuration.configurations.put("Core.Signs.Item.Use", 20.0);
    configuration.configurations.put("Core.Signs.Item.Max", 10);
    configuration.configurations.put("Core.Signs.Item.Multiple", true);
    configuration.configurations.put("Core.Signs.Item.MaxOffers", 9);
    configuration.configurations.put("Core.Signs.Item.EnderChest", true);
  }

  @Override
  public void registerCommands(CommandManager manager) {
    super.registerCommands(manager);
  }
}
