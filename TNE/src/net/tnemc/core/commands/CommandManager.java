package net.tnemc.core.commands;

import com.github.tnerevival.TNELib;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommandManager {

  public Map<String[], TNECommand> commands = new HashMap<>();
  private Integer lastRegister = 0;
  private Field commandMap = null;
  private Field knownCommands = null;

  public void registerCommands() {
    if(lastRegister == commands.size()) return;

    lastRegister = commands.size();
    try {
      commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
      commandMap.setAccessible(true);
      knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommands.setAccessible(true);
    } catch (Exception e) {
      /* do nothing */
    }

    if(commandMap != null && knownCommands != null) {

      Iterator<Map.Entry<String[], TNECommand>> i = commands.entrySet().iterator();

      while(i.hasNext()) {
        Map.Entry<String[], TNECommand> entry = i.next();

        for (String s : entry.getKey()) {
          if(entry.getValue().activated("", "")) {
            if(registered(s)) {
              unregister(s);
            }
            register(s);
          }
        }
      }
    }
  }

  public void unregister(String[] accessors) {
    for(String s : accessors) {
      unregister(s);
    }
  }

  private void register(String command) {
    try {
      Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
      c.setAccessible(true);
      PluginCommand pluginCommand = c.newInstance(command, TNELib.instance());
      if(pluginCommand != null) {
        ((SimpleCommandMap) commandMap.get(Bukkit.getServer())).register(command, pluginCommand);
      }
    } catch(Exception e) {
      //nothing to see here;
    }
  }

  private void unregister(String command) {
    try {
      ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).remove(command);
    } catch(Exception e) {
      //nothing to see here;
    }
  }

  private Boolean registered(String command) {
    try {
      return ((Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()))).containsKey(command);
    } catch(Exception e) {
      //nothing to see here;
    }
    return false;
  }

  public TNECommand Find(String name) {
    for(TNECommand c : commands.values()) {
      if(c.getName().equalsIgnoreCase(name)) {
        return c;
      }
    }
    for(TNECommand c : commands.values()) {
      for(String s : c.getAliases()) {
        if(s.equalsIgnoreCase(name)) {
          return c;
        }
      }
    }
    return null;
  }
}