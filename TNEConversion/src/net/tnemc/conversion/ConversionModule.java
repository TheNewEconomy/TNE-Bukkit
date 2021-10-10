package net.tnemc.conversion;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.CommandInformation;
import net.tnemc.commands.core.TabCompleter;
import net.tnemc.commands.core.parameter.CommandParameter;
import net.tnemc.conversion.command.ConvertCommand;
import net.tnemc.conversion.command.ConverterCompleter;
import net.tnemc.conversion.menu.ConversionMenu;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.core.menu.Menu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * All rights reserved.
 **/
@ModuleInfo(
    name = "Conversion",
    author = "creatorfromhell",
    version = "0.1.2",
    updateURL = "https://tnemc.net/files/module-version.xml"
)
public class ConversionModule implements Module {

  private static ConversionModule instance;

  private ConverterManager manager;

  private Map<String, CommandExecution> executors;
  private Map<String, TabCompleter> completers;
  private Map<String, Menu> menus;
  private JoinListener listener;


  @Override
  public void load(TNE tne) {
    tne.getLogger().info("Conversion Module loaded!");
    instance = this;
    manager = new ConverterManager();
  }

  @Override
  public void unload(TNE tne) {
    manager = null;
    listener = null;
    executors.clear();
    executors = null;
    completers.clear();
    completers = null;
    menus.clear();
    menus = null;
    tne.logger().info("Conversion Module unloaded!");
  }

  @Override
  public List<CommandInformation> commands() {
    CommandInformation info = new CommandInformation(new ArrayList<>(), "convert",
        "/convert <from> - Converts all data from plugin <from>.", "tne.command.convert",
        "convert_exe", "creatorfromhell", true, true, false);

    info.addParameter(new CommandParameter(0, "plugin", true, true, "conversion"));

    return Collections.singletonList(info);
  }

  /**
   * @return A map of command executors that should be added.
   * Format: Executor Name, Executor
   */
  @Override
  public Map<String, CommandExecution> commandExecutors() {
    executors = new HashMap<>();
    executors.put("convert_exe", new ConvertCommand());
    return executors;
  }

  /**
   * @return A map of command tab completers that should be added.
   * Format: Completer Name, {@link TabCompleter}
   */
  @Override
  public Map<String, TabCompleter> tabCompleters() {
    completers = new HashMap<>();
    completers.put("conversion", new ConverterCompleter());
    return completers;
  }

  /**
   * @param plugin The instance of the main TNE plugin class.
   * @return A map containing menus that this module utilize that extend
   * the TNE Menu class.
   */
  @Override
  public Map<String, Menu> menus(TNE plugin) {
    menus = new HashMap<>();
    menus.put("conversion_menu", new ConversionMenu());
    return menus;
  }

  public ConverterManager manager() {
    return manager;
  }

  public Optional<Converter> getConverter(String name) {
    return manager.find(name);
  }

  /**
   * @param plugin The instance of the main TNE plugin class.
   * @return A list of event listeners.
   */
  @Override
  public List<ModuleListener> listeners(TNE plugin) {
    listener = new JoinListener(plugin);
    return Collections.singletonList(listener);
  }

  public static void convertedAdd(String identifier, String world, String currency, BigDecimal amount) {
    File conversionFile = new File(TNE.instance().getDataFolder(), "extracted.yml");
    FileConfiguration conversion = YamlConfiguration.loadConfiguration(conversionFile);

    if(!conversion.contains("Accounts")) {
      conversion.createSection("Accounts");
      try {
        conversion.save(conversionFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }


    BigDecimal starting = BigDecimal.ZERO;

    String newID = identifier.replaceAll("\\.", "!").replaceAll("\\-", "@").replaceAll("\\_", "%");

    if(conversion.contains("Accounts." + newID + ".Balances." + world + "." + currency)) {
      starting = new BigDecimal(conversion.getString("Accounts." + newID + ".Balances." + world + "." + currency));
    }

    conversion.set("Accounts." + newID + ".Balances." + world + "." + currency, starting.add(amount).toPlainString());
    try {
      conversion.save(conversionFile);
    } catch (IOException e) {
      TNE.debug(e);
    }
  }

  public static ConversionModule instance() {
    return instance;
  }
}