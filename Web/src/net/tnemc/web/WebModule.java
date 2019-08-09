package net.tnemc.web;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;

import java.io.File;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 9/7/2017.
 */
@ModuleInfo(
    name = "Web",
    author = "creatorfromhell",
    version = "0.1.1",
    updateURL = "https://tnemc.net/files/module-version.xml"
)
public class WebModule implements Module {

  private static WebModule instance;

  private File webFile;
  private CommentedConfiguration fileConfiguration;
  private WebManager manager;

  @Override
  public void load(TNE tne) {
    instance = this;
    webFile = new File(TNE.instance().getDataFolder(), "web.yml");
    fileConfiguration = TNE.instance().initializeConfiguration(webFile, "web.yml");
    manager = new WebManager();
    TNE.logger().info("Web Module loaded!");
  }

  /**
   * Called at the last portion of TNE's onEnable, post initialization.
   *
   * @param tne An instance of the main TNE class.
   */
  @Override
  public void postLoad(TNE tne) {
    try {
      manager.start(8896);
    } catch (Exception ignore) {
      ignore.printStackTrace();
      TNE.logger().warning("Failed to start Web Server on Port: " + fileConfiguration.getInt("Web.Port"));
    }
  }

  @Override
  public void unload(TNE tne) {
    TNE.logger().info("Web Module unloaded!");
  }

  public static WebModule instance() {
    return instance;
  }

  public WebManager getManager() {
    return manager;
  }

  public File getWebFile() {
    return webFile;
  }

  public CommentedConfiguration getFileConfiguration() {
    return fileConfiguration;
  }
}
