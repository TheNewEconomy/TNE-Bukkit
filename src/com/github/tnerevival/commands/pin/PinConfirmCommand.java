package com.github.tnerevival.commands.pin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PinConfirmCommand extends TNECommand {
	
	public PinConfirmCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "confirm";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.pin.confirm";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		Player player = (Player)sender;
		
		if(TNE.instance.manager.confirmed(MISCUtils.getID(player), MISCUtils.getWorld(player))) {
			Message message = new Message("Messages.Pin.Already");
			message.translate(MISCUtils.getWorld(player), player);
			return false;
		}
		
		if(arguments.length > 0 && arguments.length < 2) {
			Account acc = AccountUtils.getAccount(MISCUtils.getID(player));
			if(acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
				Message message = new Message("Messages.Account.Set");
				message.translate(MISCUtils.getWorld(player), player);
				return false;
			}
			
			if(!acc.getPin().equalsIgnoreCase("TNENOSTRINGVALUE") && !acc.getPin().equals(arguments[0])) {
				help(sender);
				return false;
			}
			
			TNE.instance.manager.confirmed.add(MISCUtils.getID(player));
			Message message = new Message("Messages.Pin.Confirmed");
			message.translate(MISCUtils.getWorld(player), player);
			return true;
		}
		help(sender);
		return false;
	}

	@Override
	public String getHelp() {
		return "/pin confirm <pin> - Cofirm your identity with your account pin. Pins are case-sensitive.";
	}
}