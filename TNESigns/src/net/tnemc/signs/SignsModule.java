package net.tnemc.signs;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * All rights reserved.
 **/
@ModuleInfo(
    name = "Signs",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class SignsModule extends Module {


  File signs;
  FileConfiguration fileConfiguration;
  SignConfiguration configuration;

  private SignsManager manager;

  private static SignsModule instance;

  @Override
  public void load(TNE tne, String version) {
    instance = this;
    manager = new SignsManager();
    tne.logger().info("Signs Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Signs Module unloaded!");
  }

  @Override
  public void initializeConfigurations() {
    super.initializeConfigurations();
    signs = new File(TNE.instance().getDataFolder(), "signs.yml");
    fileConfiguration = YamlConfiguration.loadConfiguration(signs);
  }

  @Override
  public void loadConfigurations() {
    super.loadConfigurations();
    fileConfiguration.options().copyDefaults(true);
    configuration = new SignConfiguration();
    configurations.put(configuration, "Signs");
  }

  @Override
  public void saveConfigurations() {
    super.saveConfigurations();
    if(!signs.exists()) {
      Reader mobsStream = null;
      try {
        mobsStream = new InputStreamReader(TNE.instance().getResource("signs.yml"), "UTF8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      if (mobsStream != null) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mobsStream);
        fileConfiguration.setDefaults(config);
      }
    }
    try {
      fileConfiguration.save(signs);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static SignsModule instance() {
    return instance;
  }

  public static SignsManager manager() {
    return instance.manager;
  }
}
