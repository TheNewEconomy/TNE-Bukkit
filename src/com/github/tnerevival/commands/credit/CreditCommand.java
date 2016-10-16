package com.github.tnerevival.commands.credit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class CreditCommand extends TNECommand {

	public CreditCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new CreditCommandsCommand(plugin));
		subCommands.add(new CreditInventoryCommand(plugin));
	}

	@Override
	public String getName() {
		return "credit";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.credit";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		
		Player player = getPlayer(sender);
		
		Account acc = AccountUtils.getAccount(MISCUtils.getID(player));

		if(TNE.instance.manager.enabled(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
      if(!TNE.instance.manager.confirmed(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
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
		
		if(arguments.length == 0) {
			help(sender);
			return false;
		}
		
		TNECommand sub = FindSub(arguments[0]);
		if(sub == null && !arguments[0].equalsIgnoreCase("help")) {
			Message noCommand = new Message("Messages.Command.None");
			noCommand.addVariable("$command", "/" + getName());
			noCommand.addVariable("$arguments", arguments[0]);
			noCommand.translate(MISCUtils.getWorld(player), player);
			return false;
		}

		if(arguments[0].equalsIgnoreCase("help")) {
			help(sender);
			return false;
		}

		if(sub.canExecute(sender) && arguments.length >= 2 && arguments[1].equalsIgnoreCase("?")) {
			sub.help(sender);
			return false;
		}

		if(!sub.canExecute(sender)) {
			Message unable = new Message("Messages.Command.Unable");
			unable.addVariable("$command", "/" + getName());
			unable.translate(MISCUtils.getWorld(player), player);
			return false;
		}
		return sub.execute(sender, command, removeSub(arguments));
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~~Credit Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/credit help - View general credit command help.");
		sender.sendMessage(ChatColor.GOLD + "/credit inventory <inventory> - View time credits for <inventory> in every world.");
		sender.sendMessage(ChatColor.GOLD + "/credit commands - View all command credits you have accumulated.");
	}
	
}