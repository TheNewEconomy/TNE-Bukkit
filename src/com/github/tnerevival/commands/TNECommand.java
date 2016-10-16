package com.github.tnerevival.commands;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
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

  public void help(CommandSender sender) {
    help(sender, 1);
  }

  public void help(CommandSender sender, Integer page) {
    List<String> help = new ArrayList<>();
    if(subCommands.size() > 0) {
      for (TNECommand sub : subCommands) {
        help.add(sub.getHelp());
      }
    } else {
      help.add(getHelp());
    }

    Integer perPage = 4;
    MISCUtils.debug(help.size() + "");
    MISCUtils.debug((help.size() / perPage) + "");
    Integer maxPage = help.size() / perPage;
    if(help.size() % perPage > 0) maxPage++;
    Integer currentPage = (page > maxPage || page <= 0)? 1 : page;
    Integer start = (currentPage - 1) * perPage;
    Integer stop = (currentPage * perPage > help.size())? help.size() : currentPage * perPage;

    if(subCommands.size() > 0 ) {
      String name = getName();
      String formatted = name.substring(0, 1).toUpperCase() + name.substring(1);
      sender.sendMessage(ChatColor.GOLD + "~~~" + ChatColor.WHITE + formatted + " Help " + currentPage + "/" + maxPage + ChatColor.GOLD + "~~~");
    }
    for(int i = start; i < stop; i++) {
      sender.sendMessage(ChatColor.GOLD + help.get(i));
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

    String player = (sender instanceof Player)? MISCUtils.getID(getPlayer(sender)).toString() : "";
    String world = (!player.equals(""))? MISCUtils.getWorld(getPlayer(sender)) : TNE.instance.defaultWorld;
    if(!activated(world, player)) {
      return false;
    }

	  if(locked() && !(sender instanceof Player)) return false;
    if(locked()) {
      Player p = (Player)sender;
      Account acc = AccountUtils.getAccount(MISCUtils.getID(player));

      if(!acc.getStatus().getBalance()) {
        Message locked = new Message("Messages.Account.Locked");
        locked.addVariable("$player", p.getDisplayName());
        locked.translate(MISCUtils.getWorld(p), p);
        return false;
      }
    }

	  if(confirm() && !(sender instanceof Player)) return false;
    if(confirm()) {
      Player p = (Player)sender;
      Account acc = AccountUtils.getAccount(MISCUtils.getID(player));
      if (TNE.instance.manager.enabled(MISCUtils.getID(player), MISCUtils.getWorld(p))) {
        if (!TNE.instance.manager.confirmed(MISCUtils.getID(player), MISCUtils.getWorld(p))) {
          if (acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
            Message set = new Message("Messages.Account.Set");
            set.translate(MISCUtils.getWorld(p), p);
            return false;
          }

          if (!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
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
			noCommand.translate(MISCUtils.getWorld(getPlayer(sender)), getPlayer(sender));
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
			unable.translate(MISCUtils.getWorld(getPlayer(sender)), getPlayer(sender));
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