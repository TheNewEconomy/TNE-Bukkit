package net.tnemc.module.example;

import net.tnemc.core.TNE;
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
    name = "ExampleModule",
    author = "creatorfromhell",
    version = "1.0.0"
)
public class ExampleModule extends Module {

  @Override
  public void load(TNE tne, String version) {
  }

  @Override
  public void unload(TNE tne) {
  }
}