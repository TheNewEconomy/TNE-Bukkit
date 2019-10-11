package net.tnemc.core.cmdnew;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/9/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandManager {

  Map<String, CommandExecution> executors = new HashMap<>();

  public CommandManager() {
    executors.put("hello_exe", (sender, command, arguments)->{
      sender.sendMessage(ChatColor.YELLOW + "Hello!");
    });
  }
}