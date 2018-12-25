package net.tnemc.web;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.common.configurations.Configuration;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/24/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class WebConfiguration extends Configuration {

  @Override
  public CommentedConfiguration getConfiguration() {
    return WebModule.instance().getFileConfiguration();
  }

  @Override
  public List<String> node() {
    return Collections.singletonList("Web");
  }

  @Override
  public File getFile() {
    return WebModule.instance().getWebFile();
  }

  @Override
  public void load(CommentedConfiguration configurationFile) {
    super.load(configurationFile);
  }
}