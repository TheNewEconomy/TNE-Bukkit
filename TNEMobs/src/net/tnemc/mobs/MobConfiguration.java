package net.tnemc.mobs;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.common.configurations.Configuration;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 10/21/2017.
 */
public class MobConfiguration extends Configuration {

  @Override
  public CommentedConfiguration getConfiguration() {
    return MobsModule.instance().fileConfiguration;
  }

  @Override
  public List<String> node() {
    return Collections.singletonList("Mobs");
  }

  @Override
  public File getFile() {
    return MobsModule.instance().mobs;
  }

  @Override
  public void load(CommentedConfiguration configurationFile) {
    if(MobsModule.instance().mobs.exists()) MobsModule.instance().saveConfigurations();

    super.load(configurationFile);
  }
}