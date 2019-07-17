package net.tnemc.core.common.configurations;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MessageConfigurations extends Configuration {

  private Map<String, Language> languages = new HashMap<>();

  @Override
  public CommentedConfiguration getConfiguration() {
    return TNE.instance().messageConfiguration();
  }

  @Override
  public List<String> node() {
    List<String> nodes = new ArrayList<>();
    nodes.add("Messages");
    return nodes;
  }

  @Override
  public File getFile() {
    return TNE.instance().getMessagesFile();
  }

  @Override
  public void load(CommentedConfiguration configurationFile) {

    loadLanguages();

    super.load(configurationFile);
  }

  public void loadLanguages() {
    File directory = new File(TNE.instance().getDataFolder(), "languages");
    if(!directory.exists()) directory.mkdir();
    File[] langFiles = directory.listFiles((dir, name) -> name.endsWith(".yml"));

    if(langFiles != null) {
      for (File langFile : langFiles) {
        String name = langFile.getName().replace(".yml", "");
        CommentedConfiguration configuration = null;
        try {
          configuration = new CommentedConfiguration(new InputStreamReader(new FileInputStream(langFile), "UTF8"), null);
        } catch (Exception ignore) {
          TNE.debug("Failed to load language: " + name);
        }

        if(configuration != null) {
          Language lang = new Language(name, configuration);
          lang.getConfiguration().load(false);
          TNE.debug("Loaded language: " + lang);
          languages.put(name, lang);
        }
      }
    }
  }

  public Map<String, Language> getLanguages() {
    return languages;
  }

  @Override
  public Object getValue(String node, String world, String player) {
    final String language = (player.trim().equalsIgnoreCase(""))? "Default" : TNE.manager().getAccount(IDFinder.getID(player)).getLanguage();


    if(languages.containsKey(language)) {
      Language lang = languages.get(language);
      if(lang.hasTranslation(node)) {
        TNE.debug("Returning translation for language  \"" + language + "\".");
        return lang.getTranslation(node);
      }
    }
    return super.getValue(node, world, player);
  }
}