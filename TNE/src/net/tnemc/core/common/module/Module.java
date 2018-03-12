package net.tnemc.core.common.module;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.SaveManager;
import com.github.tnerevival.core.configurations.Configuration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.data.TNEDataProvider;
import net.tnemc.core.common.module.injectors.ModuleInjector;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 07/01/2017.
 */

/**
 * The base class for all TNE Modules.
 */
public abstract class Module {

  protected Map<Configuration, String> configurations = new HashMap<>();
  protected Map<String, Object> mainConfigurations = new HashMap<>();
  protected Map<String, String> messages = new HashMap<>();

  protected List<TNECommand> commands = new ArrayList<>();
  protected List<ModuleListener> listeners = new ArrayList<>();

  public Module() {
  }

  /**
   * @return a list of the classes that contain {@link net.tnemc.core.common.module.injectors.ModuleInjector module injectors} for this module.
   */
  public List<Class<? extends ModuleInjector>> moduleInjectors() {
    return new ArrayList<>();
  }

  /**
   * Called when this @Module is loaded.
   * @param tne An instance of the main TNE class.
   * @param version The last version of this module used on this server.
   */
  public void load(TNE tne, String version) {

  }

  /**
   * Called at the last portion of TNE's onEnable, post initialization.
   * @param tne An instance of the main TNE class.
   */
  public void postLoad(TNE tne) {

  }

  /**
   * Called when this @Module is unloaded.
   * @param tne An instance of the main TNE class.
   */
  public void unload(TNE tne) {

  }

  /**
   * Called when data is being backed up to the "backup/" directory.
   * @param saveManager An instance of TNE's Save Manager.
   */
  public void backup(SaveManager saveManager) {

  }

  /**
   * Used to perform any data loading, manipulation, layout updating, etc.
   * @param saveManager An instance of TNE's Save Manager
   */
  public void enableSave(SaveManager saveManager) {

  }

  /**
   * Used to save any remaining data to the correct database.
   * @param saveManager An instance of TNE's Save Manager
   */
  public void disableSave(SaveManager saveManager) {

  }

  /**
   * Used to initialize any configuration files this module may use.
   */
  public void initializeConfigurations() {

  }

  /**
   * Used to load any configuration files this module may use.
   * This step is for initializing. the File, and YamlConfigurations classes.
   */
  public void loadConfigurations() {

  }

  /**
   * Used to save any configuration files this module may use.
   */
  public void saveConfigurations() {

  }

  /**
   * Returns a map of values that should be added to the MainConfigurations class.
   */
  public Map<String, Object> getMainConfigurations() {
    return mainConfigurations;
  }

  /**
   * Returns a map of values that should be added to the MessageConfigurations class.
   */
  public Map<String, String> getMessages() {
    return messages;
  }

  /**
   * Returns a map configuration files this module may use.
   */
  public Map<Configuration, String> getConfigurations() {
    return configurations;
  }

  /**
   * Returns a list of command instances this module uses.
   */
  public List<TNECommand> getCommands() {
    return commands;
  }

  /**
   * Returns a list of event listener instances this module uses.
   */
  public List<ModuleListener> getListeners(TNE pluginInstance) {
    return listeners;
  }

  /**
   * @return A map containing database providers that this module wishes to add. This allows modules to override current
   * database providers, and provide support for new database types.
   */
  public Map<String, Class<? extends TNEDataProvider>> registerProviders() {
    return new HashMap<>();
  }

  /**
   * @return A map containing transaction results that this module wishes to add. This also allows modules to override
   * existing transaction results.
   */
  public Map<String, TransactionResult> registerResults() {
    return new HashMap<>();
  }

  /**
   * @return A map containing transaction types that this module wishes to add. This also allows modules to override
   * existing transaction types.
   */
  public Map<String, TransactionType> registerTypes() {
    return new HashMap<>();
  }
}