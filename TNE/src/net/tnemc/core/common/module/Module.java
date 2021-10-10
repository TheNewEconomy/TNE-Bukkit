package net.tnemc.core.common.module;

import com.github.tnerevival.core.SaveManager;
import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.TabCompleter;
import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.configurations.Configuration;
import net.tnemc.core.common.data.TNEDataProvider;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.core.common.transaction.type.TransactionType;
import net.tnemc.core.menu.Menu;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/25/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public interface Module {

  default void load(TNE plugin) {}

  default void postLoad(TNE plugin) {}

  default void unload(TNE plugin) {}

  default void backup(SaveManager manager) {}

  default void enableSave(SaveManager manager) {}

  default void disableSave(SaveManager manager) {}

  default void initializeConfigurations() {}

  default void loadConfigurations() {}

  default void saveConfigurations() {}

  /**
   * @return A TNDBU(https://github.com/TheNewEconomy/TheNewDBUpdater) compliant YAML file containing the SQL tables required
   * for the module.
   */
  default String tablesFile() {
    return "";
  }

  /**
   * @return A map of messages that should be added to message.yml in the format of YamlNode, Value
   */
  default Map<String, String> messages() {
    return new HashMap<>();
  }


  default List<String> events() {
    return new ArrayList<>();
  }

  default Map<Configuration, String> configurations() {
    return new HashMap<>();
  }

  /**
   * @return A list of commands that should be added from this module.
   */
  default List<CommandInformation> commands() {
    return new ArrayList<>();
  }

  /**
   * @return A map of commands that should be added as sub commands to commands found in TNE.jar
   * Format: TNE Command Name, List of sub commands to add.
   */
  default Map<String, List<CommandInformation>> subCommands() {
    return new HashMap<>();
  }

  /**
   * @return A map of command executors that should be added.
   * Format: Executor Name, Executor
   */
  default Map<String, CommandExecution> commandExecutors() {
    return new HashMap<>();
  }

  /**
   * @return A map of command tab completers that should be added.
   * Format: Completer Name, {@link TabCompleter}
   */
  default Map<String, TabCompleter> tabCompleters() {
    return new HashMap<>();
  }

  /**
   * @param plugin The instance of the main TNE plugin class.
   * @return A list of event listeners.
   */
  default List<ModuleListener> listeners(TNE plugin) {
    return new ArrayList<>();
  }

  /**
   * @param plugin The instance of the main TNE plugin class.
   * @return A map containing menus that this module utilize that extend
   * the TNE Menu class.
   */
  default Map<String, Menu> menus(TNE plugin) {
    return new HashMap<>();
  }

  /**
   * @return A map containing database providers that this module wishes to add. This allows modules to override current
   * database providers, and provide support for new database types.
   */
  default Map<String, Class<? extends TNEDataProvider>> providers() {
    return new HashMap<>();
  }

  /**
   * @return A map containing transaction results that this module wishes to add. This also allows modules to override
   * existing transaction results.
   */
  default Map<String, TransactionResult> results() {
    return new HashMap<>();
  }

  /**
   * @return A map containing transaction types that this module wishes to add. This also allows modules to override
   * existing transaction types.
   */
  default Map<String, TransactionType> types() {
    return new HashMap<>();
  }

  default CommentedConfiguration initializeConfiguration(File file, String defaultFile) {
    TNE.debug("Started copying " + file.getName());
    CommentedConfiguration commentedConfiguration = new CommentedConfiguration(file, new InputStreamReader(getResource(defaultFile), StandardCharsets.UTF_8), false);
    TNE.debug("Initializing commented configuration");
    if(commentedConfiguration != null) {
      TNE.debug("Loading commented configuration");
      commentedConfiguration.load();
    }
    TNE.debug("Finished copying " + file.getName());
    return commentedConfiguration;
  }


  /**
   * Load a file from the classpath and return the InputStream.
   * @param filename The name of the file.
   * @return The input stream for the file.
   */
  default InputStream getResource(String filename) {
    try {
      URL url = getClass().getClassLoader().getResource(filename);
      if (url == null) {
        return null;
      } else {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
      }
    } catch (IOException var4) {
      return null;
    }
  }
}