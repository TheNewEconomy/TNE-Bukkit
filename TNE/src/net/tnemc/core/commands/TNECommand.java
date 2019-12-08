package net.tnemc.core.commands;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/19/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public abstract class TNECommand {

  protected LinkedHashMap<List<String>, TNECommand> subCommands = new LinkedHashMap<>();
  protected JavaPlugin plugin;

  public TNECommand(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  public abstract String name();
  public abstract String[] aliases();
  public abstract String node();
  public abstract boolean console();

  public boolean developer() {
    return false;
  }

  public void addSubs(Collection<TNECommand> subs) {
    for(TNECommand command : subs) {
      addSub(command);
    }
  }

  public void addSub(TNECommand sub) {
    List<String> aliases = new ArrayList<>(Arrays.asList(sub.aliases()));
    aliases.add(sub.name());

    subCommands.put(aliases, sub);
  }

  public String helpLine() {
    return "Command help is currently not implemented for this.";
  }

  public String[] helpLines(final CommandSender sender) {
    return new Message(helpLine()).grabWithNew(TNE.instance().defaultWorld, sender);
  }

  public void help(final CommandSender sender) {
    help(sender, 1);
  }

  public void help(final CommandSender sender, final int page) {

    if(!canExecute(sender)) {
      unable(sender, TNE.instance().defaultWorld);
      return;
    }

    final int linesPerPage = (sender instanceof Player)? 5 : 40;

    final LinkedList<TNECommand> commands = new LinkedList<>(subCommands.values());

    int possible = 0;

    for(TNECommand command : commands) {
      if(command.canExecute(sender)) {
        possible ++;
      }
    }

    int max = possible / linesPerPage;
    if(possible % linesPerPage > 0) max++;

    LinkedList<String[]> helpLines = new LinkedList<>();

    int min = page * (linesPerPage - 1);
    if(min > subCommands.size()) min = 0;
    int remaining = linesPerPage;
    int index = min;

    if (subCommands.size() > 0) {

      while(remaining > 0) {
        if(commands.size() <= index) break;
        try {
          helpLines.add(commands.get(index).helpLines(sender));
        } catch(ArrayIndexOutOfBoundsException ignore) {
          break;
        }

        index++;
        remaining--;
      }

    } else {
      helpLines.add(helpLines(sender));
    }

    if(subCommands.size() > 0) {
      String name = name();
      String formatted = name.substring(0, 1).toUpperCase() + name.substring(1);
      sender.sendMessage(ChatColor.GOLD + "~~~ " + ChatColor.WHITE + formatted + ChatColor.GOLD + " | " + ChatColor.WHITE + page + ChatColor.GOLD + "/" + ChatColor.WHITE + max + ChatColor.GOLD + " ~~~");
    }

    for(String[] help : helpLines) {
      for(String s : help) {
        String message = (s.contains("Messages.")) ? new Message(s).grab("", sender) : s;
        message = message.replaceFirst("/", "<green>/").replaceFirst("-", "<white>-");
        new Message(message).translate("", sender);
      }
    }

  }

  public boolean execute(CommandSender sender, String command, String[] arguments) {

    String world = TNE.instance().defaultWorld;

    if(developer()) {
      if(!(sender instanceof Player) || !TNE.instance().developers.contains(((Player)sender).getUniqueId().toString())) {
        sender.sendMessage(ChatColor.RED + "You must be a TNE developer to use this commands.");
        return false;
      }
    }

    if(arguments.length == 0) {
      help(sender);
      return false;
    }

    TNECommand sub = findSub(arguments[0]);
    if(sub == null && !arguments[0].equalsIgnoreCase("help") && !arguments[0].equalsIgnoreCase("?")) {
      Message noCommand = new Message("Messages.Command.None");
      noCommand.addVariable("$command", "/" + name());
      noCommand.addVariable("$arguments", arguments[0]);
      noCommand.translate(world, sender);
      return false;
    }

    if(arguments[0].equalsIgnoreCase("help") || arguments[0].equalsIgnoreCase("?")) {
      final int page = (arguments.length >= 2)? getPage(arguments[1]) : 1;
      help(sender, page);
      return false;
    }

    if(sub.canExecute(sender) && arguments.length >= 2 && arguments[1].equalsIgnoreCase("?") || sub.canExecute(sender) && arguments.length >= 2 && arguments[1].equalsIgnoreCase("help")) {
      int page = (arguments.length >= 3)? getPage(arguments[2]) : 1;

      sub.help(sender, page);
      return false;
    }

    if(!sub.canExecute(sender)) {
      unable(sender, world);
      return false;
    }
    return sub.execute(sender, command, removeSub(arguments));
  }

  public void unable(final CommandSender sender, final String world) {
    Message unable = new Message("Messages.Command.Unable");
    unable.addVariable("$commands", "/" + name());
    unable.translate(world, sender);
  }

  protected String[] removeSub(String[] oldArguments) {
    String[] arguments = new String[oldArguments.length - 1];
    System.arraycopy(oldArguments, 1, arguments, 0, oldArguments.length - 1);
    return arguments;
  }

  public TNECommand findSub(String name) {
    for(Map.Entry<List<String>, TNECommand> entry : subCommands.entrySet()) {
      if(entry.getKey().contains(name.toLowerCase())) {
        return entry.getValue();
      }
    }
    return null;
  }

  public void unregister() {
    List<String> aliases = new ArrayList<>(Arrays.asList(aliases()));
    aliases.add(name());
    TNE.instance().getCommandManager().unregister(aliases.toArray(new String[aliases.size()]), true);
  }

  public Integer getPage(String pageValue) {
    Integer page;
    try {
      page = Integer.valueOf(pageValue);
    } catch(Exception e) {
      return 1;
    }
    return page;
  }

  public boolean canExecute(CommandSender sender) {
    if(sender instanceof Player) {
      if(TNE.maintenance) return false;
      if(node().equalsIgnoreCase("")) return true;
      return TNE.instance().developers.contains(IDFinder.getID(sender).toString()) || sender.hasPermission(node());
    }
    return console();
  }

  public LinkedHashMap<List<String>, TNECommand> getSubCommands() {
    return subCommands;
  }

  protected Map<String, String> getArguments(String[] arguments) {
    Map<String, String> parsed = new HashMap<>();
    for(int i = 0; i < arguments.length; i++) {
      if(arguments[i].contains(":")) {
        String[] broken = arguments[i].split(":");
        parsed.put(broken[0], broken[1]);
        continue;
      }
      parsed.put(i + "", arguments[i]);
    }
    return parsed;
  }

  protected Player getPlayer(CommandSender sender) {
    if(sender instanceof Player) {
      return (Player)sender;
    }
    return null;
  }

  @SuppressWarnings("deprecation")
  protected Player getPlayer(CommandSender sender, String username) {
    if(username != null) {
      List<Player> matches = sender.getServer().matchPlayer(username);
      if(!matches.isEmpty()) {
        return matches.get(0);
      }
      sender.sendMessage(ChatColor.WHITE + "Player \"" + ChatColor.RED + username + ChatColor.WHITE + "\" could not be found!");
      return null;
    } else {
      if(sender instanceof Player) {
        return (Player)sender;
      }
    }
    return null;
  }
}