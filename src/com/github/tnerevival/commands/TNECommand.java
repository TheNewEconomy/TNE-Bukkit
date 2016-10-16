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
	public abstract void help(CommandSender sender);

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

	  if(locked() && !(sender instanceof Player)) return false;
    if(locked()) {
      Player player = (Player)sender;
      Account acc = AccountUtils.getAccount(MISCUtils.getID(player));

      if(!acc.getStatus().getBalance()) {
        Message locked = new Message("Messages.Account.Locked");
        locked.addVariable("$player", player.getDisplayName());
        locked.translate(MISCUtils.getWorld(player), player);
        return false;
      }
    }

	  if(confirm() && !(sender instanceof Player)) return false;
    if(confirm()) {
      Player player = (Player)sender;
      Account acc = AccountUtils.getAccount(MISCUtils.getID(player));
      if (TNE.instance.manager.enabled(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
        if (!TNE.instance.manager.confirmed(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
          if (acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
            Message set = new Message("Messages.Account.Set");
            set.translate(MISCUtils.getWorld(player), player);
            return false;
          }

          if (!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
            Message confirm = new Message("Messages.Account.Confirm");
            confirm.translate(MISCUtils.getWorld(player), player);
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
			help(sender);
			return false;
		}

		String player = (sender instanceof Player)? MISCUtils.getID(getPlayer(sender)).toString() : "";
    if(!sub.activated(MISCUtils.getWorld(getPlayer(sender)), player)) {
      Message noCommand = new Message("Messages.Command.InActive");
      noCommand.addVariable("$command", "/" + getName());
      noCommand.addVariable("$arguments", arguments[0]);
      noCommand.translate(MISCUtils.getWorld(getPlayer(sender)), sender);
      return false;
    }

		if(sub.canExecute(sender) && arguments.length >= 2 && arguments[1].equalsIgnoreCase("?")) {
		  sub.help(sender);
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