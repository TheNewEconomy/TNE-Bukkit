package net.tnemc.auctions;

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
    name = "Auctions",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class AuctionsModule extends Module {

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Auctions Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Auctions Module unloaded!");
  }

  @Override
  public void registerMainConfigurations(MainConfigurations configuration) {
    configuration.configurations.put("Core.Auctions.Enabled", true);
    configuration.configurations.put("Core.Auctions.Cost", 10.00);
    configuration.configurations.put("Core.Auctions.AllowWorld", false);
    configuration.configurations.put("Core.Auctions.Multiple", false);
    configuration.configurations.put("Core.Auctions.MaxMultiple", 3);
    configuration.configurations.put("Core.Auctions.PersonalQueue", 3);
    configuration.configurations.put("Core.Auctions.MaxQueue", 10);
    configuration.configurations.put("Core.Auctions.MaxStart", 2000);
    configuration.configurations.put("Core.Auctions.MinStart", 1);
    configuration.configurations.put("Core.Auctions.MaxIncrement", 1000);
    configuration.configurations.put("Core.Auctions.MinIncrement", 1);
    configuration.configurations.put("Core.Auctions.MaxTime", 60);
    configuration.configurations.put("Core.Auctions.MinTime", 30);
    configuration.configurations.put("Core.Auctions.AntiSnipe", true);
    configuration.configurations.put("Core.Auctions.SnipePeriod", 30);
    configuration.configurations.put("Core.Auctions.SnipeTime", 20);
    configuration.configurations.put("Core.Auctions.Announce", true);
    configuration.configurations.put("Core.Auctions.Interval", 10);
    configuration.configurations.put("Core.Auctions.Countdown", true);
    configuration.configurations.put("Core.Auctions.CountdownTime", 10);
  }

  @Override
  public void registerCommands(CommandManager manager) {
    super.registerCommands(manager);
  }
}
