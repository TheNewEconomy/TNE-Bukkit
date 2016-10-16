package com.github.tnerevival.commands.credit;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

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
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		if(arguments.length == 0) {
			Player player = (Player)sender;
			Map<String, Integer> credits = AccountUtils.getAccount(MISCUtils.getID(player)).getCredits();
			sender.sendMessage(ChatColor.WHITE + "Command ~ Credits");
			sender.sendMessage(ChatColor.WHITE + "==============================");
			if(credits.size() > 0) {
				for(String c : credits.keySet()) {
					sender.sendMessage(ChatColor.WHITE + c + " ~ " + credits.get(c));
				}
				return true;
			}
		}
		help(sender);
		return false;
	}

	@Override
	public String getHelp() {
		return "/credit commands - View all command credits you have accumulated.";
	}
}