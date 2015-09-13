package com.github.tnerevival.commands.money;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;

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
	public boolean execute(CommandSender sender, String[] arguments) {
		
		if(arguments.length == 0 && sender instanceof Player) {
			TNECommand sub = FindSub("balance");
			return sub.execute(sender, arguments);
		}
		
		if(arguments.length == 0 || arguments.length == 1 && arguments[0].equalsIgnoreCase("help")) {
			help(sender);
			return false;
		}
		
		TNECommand sub = FindSub(arguments[0]);
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
		return sub.execute(sender, removeSub(arguments));
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