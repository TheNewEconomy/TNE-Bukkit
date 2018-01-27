package net.tnemc.core.common.configurations;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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