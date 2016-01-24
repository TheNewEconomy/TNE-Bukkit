package com.github.tnerevival.commands.credit;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class CreditCommandsCommand extends TNECommand {
	
	public CreditCommandsCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "commands";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.credit.commands";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(arguments.length == 0) {
			Player player = (Player)sender;
			HashMap<String, Integer> credits = AccountUtils.getAccount(MISCUtils.getID(player)).getCredits();
			sender.sendMessage(ChatColor.WHITE + "Command ~ Credits");
			sender.sendMessage(ChatColor.WHITE + "==============================");
			if(credits.size() > 0) {
				for(String command : credits.keySet()) {
					sender.sendMessage(ChatColor.WHITE + command + " ~ " + credits.get(command));
				}
				return true;
			}
		}
		help(sender);
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/credit commands - View all command credits you have accumulated.");
	}
}