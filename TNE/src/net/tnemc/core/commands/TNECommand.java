package net.tnemc.core.commands;

import com.github.tnerevival.TNELib;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TNECommand implements TabCompleter {

  protected TNELib plugin;

  public TNECommand(TNELib plugin) {
    this.plugin = plugin;
  }

  protected List<TNECommand> subCommands = new ArrayList<>();
  public abstract String getName();
  public abstract String[] getAliases();
  public abstract String getNode();
  public abstract boolean console();
  public boolean developer() {
    return false;
  }

  public String getHelp() {
    return "Command help coming soon!";
  }

  public String[] getHelpLines() {
    Message message = new Message(getHelp());
    return new Message(getHelp()).grabWithNew(TNE.instance().defaultWorld, null);
  }

  public void help(CommandSender sender) {
    help(sender, 1);
  }

  public void help(CommandSender sender, Integer page) {
    List<String[]> help = new ArrayList<>();
    if(subCommands.size() > 0) {
      for (TNECommand sub : subCommands) {
        if(sub.canExecute(sender)) {
          help.add(sub.getHelpLines());
        }
      }
    } else {
      if(canExecute(sender)) {
        help.add(getHelpLines());
      }
    }


    Integer linesPerPage = 5;
    Integer remaining = linesPerPage;
    Integer maxPage = 1;
    for(int i = 0; i < help.size(); i++) {
      if(remaining <= 0) {
        maxPage++;
        remaining = linesPerPage;
      }
      Integer length = help.get(i).length;
      if(i == help.size() - 1 && remaining - length < 0) maxPage++;
      remaining -= length;
    }

    Integer loopPage = 1;
    remaining = linesPerPage;
    Integer helpPage = (page > maxPage)? maxPage : page;
    List<Integer> send = new ArrayList<>();
    for(int i = 0; i < help.size(); i++) {
      if(remaining <= 0) {
        loopPage++;
        remaining = linesPerPage;
      }
      Integer length = help.get(i).length;
      if(i == help.size() - 1 && remaining - length < 0) loopPage++;
      if(loopPage.equals(helpPage)) send.add(i);
      remaining -= length;
    }

    if(subCommands.size() > 0) {
      String name = getName();
      String formatted = name.substring(0, 1).toUpperCase() + name.substring(1);
      sender.sendMessage(ChatColor.GOLD + "~~~" + ChatColor.WHITE + formatted + " Help " + helpPage + "/" + maxPage + ChatColor.GOLD + "~~~");
    }

    for(Integer i : send) {
      for(String s : help.get(i)) {
        String message = (s.contains("Messages."))? new Message(s).grab("", sender) : s;
        message = message.replaceFirst("/" , "<green>/").replaceFirst("-", "<white>-");
        new Message(message).translate("", sender);
      }
    }
  }

  public Boolean locked() {
    return false;
  }

  public Boolean confirm() {
    return false;
  }

  public Boolean activated(String world, String player) {
    return true;
  }

  public List<String> onTab(CommandSender sender, Command command, String alias, String[] arguments, boolean shortened) {
    List<String> suggestions = new ArrayList<>();
    subCommands.forEach((tneCommand ->{
      if(tneCommand.canExecute(sender) && tneCommand.getName().startsWith(arguments[arguments.length - 1])) {
        suggestions.add(tneCommand.getName());
      }
    }));
    return suggestions;
  }

  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] arguments) {
    TNECommand sub = this;
    boolean useShort = false;
    if(findSub(alias) != null) {
      sub = findSub(alias);
      useShort = true;
    }

    for(int i = 0; i < arguments.length; i++) {
      final TNECommand newSub = sub.findSub(arguments[i]);
      if(newSub == null) {
        return sub.onTab(sender, command, alias, arguments, useShort);
      } else {
        sub = newSub;
      }
    }
    return sub.onTab(sender, command, alias, arguments, useShort);
  }

  public List<String> buildSuggestions(CommandSender sender, boolean shortened, String[] arguments, Map<Integer, String> argType, int world) {
    if(arguments.length > 0 && !shortened) arguments = removeSub(arguments);
    List<String> suggestions = new ArrayList<>();


    final String worldName = (arguments.length > world)? arguments[world] : ((sender instanceof Player)? getPlayer(sender).getWorld().getName() : TNE.instance().defaultWorld);

    final int argCheck = arguments.length - 1;

    if(!argType.containsKey(argCheck)) {
      return suggestions;
    }

    final String check = (arguments.length > 0)? ((arguments.length > 1)? arguments[argCheck] : arguments[0]) : "";
    final String type = argType.getOrDefault(arguments.length - 1, "player");

    switch(type) {
      case "player":
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
          if(player.getName().startsWith(check)) suggestions.add(player.getName());
        }
        break;
      case "amount":
        suggestions.add("1");
        suggestions.add("5");
        suggestions.add("10");
        suggestions.add("20");
        suggestions.add("50");
        suggestions.add("100");
        break;
      case "world":
        TNE.instance().getWorldManagersMap().keySet().forEach((worldValue)->{
          if(worldValue.startsWith(check)) suggestions.add(worldValue);
        });
        break;
      case "currency":
        TNE.manager().currencyManager().getWorldCurrencies(worldName).forEach((currency) -> {
          if (currency.name().startsWith(check)) suggestions.add(currency.name());
        });
        break;
    }
    return suggestions;
  }

  public boolean execute(CommandSender sender, String command, String[] arguments) {

    String world = TNE.instance().defaultWorld;

    if(developer()) {
      if(!TNE.instance().developers.contains(IDFinder.getID(sender).toString())) {
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
      noCommand.addVariable("$commands", "/" + getName());
      noCommand.addVariable("$arguments", arguments[0]);
      noCommand.translate(world, sender);
      return false;
    }

    if(arguments[0].equalsIgnoreCase("help") || arguments[0].equalsIgnoreCase("?")) {
      Integer page = (arguments.length >= 2)? getPage(arguments[1]) : 1;
      help(sender, page);
      return false;
    }

    if(sub.canExecute(sender) && arguments.length >= 2 && arguments[1].equalsIgnoreCase("?") || sub.canExecute(sender) && arguments.length >= 2 && arguments[1].equalsIgnoreCase("help")) {
      int page = (arguments.length >= 3)? getPage(arguments[2]) : 1;

      sub.help(sender, page);
      return false;
    }

    if(!sub.canExecute(sender)) {
      Message unable = new Message("Messages.Command.Unable");
      unable.addVariable("$commands", "/" + getName());
      unable.translate(world, sender);
      return false;
    }
    return sub.execute(sender, command, removeSub(arguments));
  }

  protected String[] removeSub(String[] oldArguments) {
    String[] arguments = new String[oldArguments.length - 1];
    System.arraycopy(oldArguments, 1, arguments, 0, oldArguments.length - 1);
    return arguments;
  }

  public TNECommand findSub(String name) {
    for(TNECommand sub : subCommands) {
      if(sub.getName().equalsIgnoreCase(name)) {
        return sub;
      }
    }
    for(TNECommand sub : subCommands) {
      for(String s : sub.getAliases()) {
        if(s.equalsIgnoreCase(name)) {
          return sub;
        }
      }
    }
    return null;
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
      if(getNode().equalsIgnoreCase("")) return true;
      return TNE.instance().developers.contains(IDFinder.getID(sender).toString()) || sender.hasPermission(getNode());
    }
    return console();
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