package net.tnemc.signs;

import net.tnemc.core.common.configurations.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/13/2018.
 */
public class SignConfiguration extends Configuration {

  @Override
  public FileConfiguration getConfiguration() {
    return SignsModule.instance().fileConfiguration;
  }

  @Override
  public List<String> node() {
    return Arrays.asList(new String[] { "Signs" });
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    if (!SignsModule.instance().signs.exists()) SignsModule.instance().saveConfigurations();
  }

}
