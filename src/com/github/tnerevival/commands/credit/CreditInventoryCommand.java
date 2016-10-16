package com.github.tnerevival.commands.credit;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CreditInventoryCommand extends TNECommand {
	
	public CreditInventoryCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "inventory";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.credit.inventory";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		if(arguments.length == 1) {
			Player player = (Player)sender;
			Map<String, Long> credits = AccountUtils.getAccount(MISCUtils.getID(player)).getTimes(arguments[0]);
			sender.sendMessage(ChatColor.WHITE + "Time Credits for inventory \"" + arguments[0] +"\".");
			sender.sendMessage(ChatColor.WHITE + "World ~ Time Credits(in seconds)");
			sender.sendMessage(ChatColor.WHITE + "==============================");
			if(credits.size() > 0) {
				for(String world : credits.keySet()) {
					sender.sendMessage(ChatColor.WHITE + world + " ~ " + credits.get(world));
				}
				return true;
			} else {
				Message insufficient = new Message("Messages.Credit.Empty");
				insufficient.addVariable("$type",  arguments[0]);
				insufficient.translate(MISCUtils.getWorld(player), player);
				return false;
			}
		}
		help(sender);
		return false;
	}

	@Override
	public String getHelp() {
		return "/credit inventory <inventory> - View time credits for <inventory> in every world.";
	}
}