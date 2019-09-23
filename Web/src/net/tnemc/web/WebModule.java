package net.tnemc.web;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

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
    fileConfiguration = initializeConfiguration(webFile, "web.yml");
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
      manager.start(fileConfiguration.getInt("Web.Port"));
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

  public CommentedConfiguration initializeConfiguration(File file, String defaultFile) {
    TNE.debug("Started copying " + file.getName());
    CommentedConfiguration commentedConfiguration = new CommentedConfiguration(file, new InputStreamReader(getResource(defaultFile), StandardCharsets.UTF_8), false);
    TNE.debug("Initializing commented configuration");
    if(commentedConfiguration != null) {
      TNE.debug("Loading commented configuration");
      commentedConfiguration.load();
    }
    TNE.debug("Finished copying " + file.getName());
    return commentedConfiguration;
  }


  public InputStream getResource(String filename) {
    try {
      URL url = getClass().getClassLoader().getResource(filename);
      if (url == null) {
        return null;
      } else {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
      }
    } catch (IOException var4) {
      return null;
    }
  }
}
