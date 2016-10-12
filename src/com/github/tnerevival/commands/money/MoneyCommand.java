package com.github.tnerevival.commands.money;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class MoneyCommand extends TNECommand {

	public MoneyCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new MoneyBalanceCommand(plugin));
		subCommands.add(new MoneyGiveCommand(plugin));
		subCommands.add(new MoneyPayCommand(plugin));
		subCommands.add(new MoneyTakeCommand(plugin));
	}

	@Override
	public String getName() {
		return "money";
	}

	@Override
	public String[] getAliases() {
		if(TNE.instance.api.getBoolean("Core.Commands.BalanceShort")) {
		  return new String[] { "bal", "balance" };
    }
    return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.money";
	}

	@Override
	public boolean console() {
		return true;
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		
		if(sender instanceof Player) {
			Player player = getPlayer(sender);
			
			Account acc = AccountUtils.getAccount(MISCUtils.getID(player));
			
			if(!acc.getStatus().getBalance()) {
				Message locked = new Message("Messages.Account.Locked");
				locked.addVariable("$player", player.getDisplayName());
				sender.sendMessage(locked.translate());
				return false;
			}
			if(TNE.instance.manager.enabled(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
        if (!TNE.instance.manager.confirmed(MISCUtils.getID(player), MISCUtils.getWorld(player))) {

          boolean set = !acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE");

          String message = (set)? "Messages.Account.Confirm" : "Messages.Account.Set";
          player.sendMessage(new Message(message).translate());
        }
      }
		}
		
		if(arguments.length == 0 && sender instanceof Player && !command.equalsIgnoreCase("pay")) {
			TNECommand sub = FindSub("balance");
			return sub.execute(sender, command, arguments);
		}
		
		if(arguments.length == 0 && !command.equalsIgnoreCase("pay") || arguments.length == 1 && arguments[0].equalsIgnoreCase("help") && !command.equalsIgnoreCase("pay")) {
			help(sender);
			return false;
		}

		String subFind = (command.equalsIgnoreCase("pay"))? "pay" : arguments[0];

		TNECommand sub = FindSub(subFind);
		if(sub == null) {
			Message noCommand = new Message("Messages.Command.None");
			noCommand.addVariable("$command", "/" + getName());
			noCommand.addVariable("$arguments", arguments[0]);
			sender.sendMessage(noCommand.translate());
			return false;
		}
		if(!sub.canExecute(sender)) {
			Message unable = new Message("Messages.Command.Unable");
			unable.addVariable("$command", "/" + getName());
			sender.sendMessage(unable.translate());
			return false;
		}
		if(command.equalsIgnoreCase("pay")) {
		  return sub.execute(sender, command, arguments);
    }
		return sub.execute(sender, command, removeSub(arguments));
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~~Money Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/money help - general money help");
		sender.sendMessage(ChatColor.GOLD + "/money balance - find out how much money you have on you");
		sender.sendMessage(ChatColor.GOLD + "/money give <player> <amount> - summon money from air and give it to a player");
		sender.sendMessage(ChatColor.GOLD + "/money pay <player> <amount> - pay a player money from your balance");
		sender.sendMessage(ChatColor.GOLD + "/money take <player> <amount> - make some of <player>'s money vanish into thin air");
	}
}