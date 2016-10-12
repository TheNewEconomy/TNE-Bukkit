package com.github.tnerevival.commands.pin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class PinSetCommand extends TNECommand {
	
	public PinSetCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.pin.set";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		if(arguments.length == 2) {
			Player player = (Player)sender;
			Account acc = AccountUtils.getAccount(MISCUtils.getID(player));
			
			if(!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
				help(sender);
				return false;
			}
			
			if(!arguments[0].equals(arguments[1])) {
				help(sender);
				return false;
			}
			
			if(!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE") && !acc.getPin().equals(arguments[2])) {
				help(sender);
				return false;
			}

			acc.setPin(arguments[0]);
			TNE.instance.manager.confirmed.add(MISCUtils.getID(player));
			Message message = new Message("Messages.Pin.Set");
			sender.sendMessage(message.translate());
			return true;
		}
		help(sender);
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/pin set <pin> <confirm pin> [old pin] - Set your pin to <pin>'s value. Old pin is required if you have one set. Pins are case-sensitive.");
	}
}