package net.tnemc.db2;

import net.tnemc.core.TNE;
import net.tnemc.core.common.data.TNEDataProvider;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 10/25/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
@ModuleInfo(
    name = "DB2",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class DB2Module extends Module {

  @Override
  public void load(TNE tne, String version) {
    TNE.logger().info("DB2 Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    TNE.logger().info("DB2 Module unloaded!");
  }

  @Override
  public Map<String, Class<? extends TNEDataProvider>> registerProviders() {
    Map<String, Class<? extends TNEDataProvider>> providers = new HashMap<>();
    providers.put("db2", DB2Provider.class);
    return providers;
  }
}

