package net.tnemc.core.common.configurations;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 1/27/2018.
 */
public class Language {

  private Map<String, String> translations = new HashMap<>();
  private FileConfiguration configuration;

  private String name;

  public Language(String name, FileConfiguration configuration) {
    this.name = name;
    this.configuration = configuration;
  }

  public Map<String, String> getTranslations() {
    return translations;
  }

  public FileConfiguration getConfiguration() {
    return configuration;
  }

  public String getName() {
    return name;
  }

  public boolean hasTranslation(String node) {
    return translations.containsKey(node);
  }

  public String getTranslation(String node) {
    return translations.get(node);
  }

  public void addTranslation(String node, String value) {
    translations.put(node, value);
  }
}