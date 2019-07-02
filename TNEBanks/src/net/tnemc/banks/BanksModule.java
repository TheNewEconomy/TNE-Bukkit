package net.tnemc.banks;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/2/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
@ModuleInfo(
    name = "Banks",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class BanksModule extends Module {

  private static BanksModule instance;

  public BanksModule() {
    instance = this;
  }

  @Override
  public void load(TNE tne, String version) {
    TNE.logger().info("Banks Module loaded!");
  }
}