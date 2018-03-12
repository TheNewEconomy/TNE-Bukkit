package net.tnemc.auctions;

import com.github.tnerevival.commands.CommandManager;
import net.tnemc.core.TNE;
import net.tnemc.core.common.configurations.MainConfigurations;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
