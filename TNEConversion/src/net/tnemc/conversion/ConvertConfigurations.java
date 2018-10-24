package net.tnemc.conversion;

import net.tnemc.core.common.configurations.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/5/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ConvertConfigurations extends Configuration {

  @Override
  public FileConfiguration getConfiguration() {
    return ConversionModule.instance().getFileConfiguration();
  }

  @Override
  public List<String> node() {
    return Collections.singletonList("Conversion");
  }

  @Override
  public File getFile() {
    return ConversionModule.instance().getConvert();
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    if(ConversionModule.instance().getConvert().exists()) ConversionModule.instance().saveConfigurations();
    configurations.put("Conversion.Format", "MySQL");
    configurations.put("Conversion.File", "Economy.db");
    configurations.put("Conversion.Options.Host", "localhost");
    configurations.put("Conversion.Options.Port", 3306);
    configurations.put("Conversion.Options.Database", "sql_eco");
    configurations.put("Conversion.Options.User", "root");
    configurations.put("Conversion.Options.Password", "Password");

    super.load(configurationFile);
  }
}