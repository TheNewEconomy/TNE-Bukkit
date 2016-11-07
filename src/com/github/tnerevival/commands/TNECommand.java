package com.github.tnerevival.commands;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class TNECommand {

  protected TNE plugin;

  public TNECommand(TNE plugin) {
    this.plugin = plugin;
  }

  public List<TNECommand> subCommands = new ArrayList<>();
  public abstract String getName();
  public abstract String[] getAliases();
  public abstract String getNode();
  public abstract boolean console();

  public String getHelp() {
    return "Command help coming soon!";
  }

  public String[] getHelpLines() {
    return new String[] { getHelp() };
  }

  public void help(CommandSender sender) {
    help(sender, 1);
  }

  public void help(CommandSender sender, Integer page) {
    List<String[]> help = new ArrayList<>();
    if(subCommands.size() > 0) {
      for (TNECommand sub : subCommands) {
        if(sender.hasPermission(sub.getNode())) {
          help.add(sub.getHelpLines());
        }
      }
    } else {
      if(sender.hasPermission(getNode())) {
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
        String message = s.replaceFirst("/" , "<green>/").replaceFirst("-", "<white>-");
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

  public boolean execute(CommandSender sender, String command, String[] arguments) {

    String player = (sender instanceof Player)? IDFinder.getID(getPlayer(sender)).toString() : "";
    String world = (!player.equals(""))? MISCUtils.getWorld(getPlayer(sender)) : TNE.instance.defaultWorld;
    if(!activated(world, player)) {
      return false;
    }

    if(locked() && sender instanceof Player) {
      Player p = (Player)sender;
      Account acc = AccountUtils.getAccount(IDFinder.getID(player));

      if(!acc.getStatus().getBalance()) {
        Message locked = new Message("Messages.Account.Locked");
        locked.addVariable("$player", p.getDisplayName());
        locked.translate(MISCUtils.getWorld(p), p);
        return false;
      }
    }

    if(confirm() && sender instanceof Player) {
      Player p = (Player)sender;
      Account acc = AccountUtils.getAccount(IDFinder.getID(p));
      if (TNE.instance.manager.enabled(IDFinder.getID(p), MISCUtils.getWorld(p))) {
        MISCUtils.debug(TNE.instance.manager.enabled(IDFinder.getID(p), MISCUtils.getWorld(p)) + "");
        if (!TNE.instance.manager.confirmed(IDFinder.getID(p), MISCUtils.getWorld(p))) {
          MISCUtils.debug(TNE.instance.manager.confirmed(IDFinder.getID(p), MISCUtils.getWorld(p)) + "");
          if (acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
            MISCUtils.debug(acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE") + "");
            Message set = new Message("Messages.Account.Set");
            set.translate(MISCUtils.getWorld(p), p);
            return false;
          }

          if (!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
            MISCUtils.debug(acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE") + "");
            Message confirm = new Message("Messages.Account.Confirm");
            confirm.translate(MISCUtils.getWorld(p), p);
            return false;
          }
        }
      }
    }

    if(arguments.length == 0) {
      help(sender);
      return false;
    }

    TNECommand sub = FindSub(arguments[0]);
    if(sub == null && !arguments[0].equalsIgnoreCase("help") && !arguments[0].equalsIgnoreCase("?")) {
      Message noCommand = new Message("Messages.Command.None");
      noCommand.addVariable("$command", "/" + getName());
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
      unable.addVariable("$command", "/" + getName());
      unable.translate(world, sender);
      return false;
    }
    return sub.execute(sender, command, removeSub(arguments));
  }

  protected String[] removeSub(String[] oldArguments) {
    String[] arguments = new String[oldArguments.length - 1];
    for(int i = 1; i < oldArguments.length; i++) {
      arguments[i - 1] = oldArguments[i];
    }
    return arguments;
  }

  public TNECommand FindSub(String name) {
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
    Integer page = 1;
    try {
      page = Integer.valueOf(pageValue);
    } catch(Exception e) {
      return 1;
    }
    return page;
  }

  public boolean canExecute(CommandSender sender) {
    if(sender instanceof Player) {
      return sender.hasPermission(getNode());
    }
    return console();
  }

  protected String getWorld(CommandSender sender, String name) {
    if(Bukkit.getWorld(name) != null) return name;
    return getWorld(sender);
  }

  protected String getWorld(CommandSender sender) {
    if(sender instanceof Player) return MISCUtils.getWorld(getPlayer(sender));
    return TNE.instance.defaultWorld;
  }

  protected Currency getCurrency(String world, String name) {
    if(plugin.manager.currencyManager.contains(world, name)) {
      return plugin.manager.currencyManager.get(world, name);
    }
    return plugin.manager.currencyManager.get(world);
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