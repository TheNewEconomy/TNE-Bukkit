package net.tnemc.market;

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
    name = "Market",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class MarketModule extends Module {

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Market Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Market Module unloaded!");
  }

  @Override
  public void registerMainConfigurations(MainConfigurations configuration) {
  }

  @Override
  public void registerCommands(CommandManager manager) {
    super.registerCommands(manager);
  }
}
