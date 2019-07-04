package net.tnemc.bounty.configuration;

import net.tnemc.bounty.BountyModule;
import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.common.configurations.Configuration;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class HunterConfiguration extends Configuration {

  @Override
  public CommentedConfiguration getConfiguration() {
    return BountyModule.instance().getHunterFileConfiguration();
  }

  @Override
  public List<String> node() {
    return Collections.singletonList("Hunter");
  }

  @Override
  public File getFile() {
    return BountyModule.instance().getHunter();
  }

  @Override
  public void load(CommentedConfiguration configurationFile) {
    if(BountyModule.instance().getHunter().exists()) BountyModule.instance().saveConfigurations();

    super.load(configurationFile);
  }
}