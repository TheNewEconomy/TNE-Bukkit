package net.tnemc.signs;

import net.tnemc.core.common.configurations.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/4/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SignsConfiguration extends Configuration {

  @Override
  public FileConfiguration getConfiguration() {
    return SignsModule.instance().getFileConfiguration();
  }

  @Override
  public List<String> node() {
    return Collections.singletonList("Signs");
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    if(SignsModule.instance().getSigns().exists()) SignsModule.instance().saveConfigurations();

    configurations.put("Signs.Item.Enabled", true);
    configurations.put("Signs.Item.Max", 5);

    configurations.put("Signs.Nation.Enabled", true);
    configurations.put("Signs.Nation.Max", 5);

    configurations.put("Signs.Town.Enabled", true);
    configurations.put("Signs.Town.Max", 5);

    configurations.put("Signs.Safe.Enabled", true);
    configurations.put("Signs.Safe.Max", 5);

    super.load(configurationFile);
  }
}