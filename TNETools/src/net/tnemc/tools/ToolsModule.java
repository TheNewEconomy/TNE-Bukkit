package net.tnemc.tools;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.tools.command.ToolsCommand;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/5/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
@ModuleInfo(
    name = "Tools",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class ToolsModule extends Module {

  public ToolsModule() {
    TNE.instance().registerCommand(new String[] {
        "tnetools", "tnet"
    }, new ToolsCommand(TNE.instance()));
  }

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Tools Module loaded!");
    TNE.instance().registerCommand(new String[] {
        "tnetools", "tnet"
    }, new ToolsCommand(TNE.instance()));
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Tools Module unloaded!");
  }
}