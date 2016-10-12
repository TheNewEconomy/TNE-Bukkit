package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminBankCommand extends TNECommand {
	
	public AdminBankCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "bank";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.admin.bank";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		if(arguments.length == 1 || arguments.length == 2) {
			String world = (arguments.length == 2) ? arguments[1] : TNE.instance.defaultWorld;
			if(MISCUtils.getID(arguments[0]) != null && TNE.instance.manager.accounts.containsKey(MISCUtils.getID(arguments[0]))) {
				Account acc = AccountUtils.getAccount(MISCUtils.getID(arguments[0]));
				if(acc.getBanks().containsKey(world)) {
					Player player = (Player)sender;
					//Access access = new Access(MISCUtils.getID(arguments[0]), world, false);
					//TNE.instance.manager.accessing.put(MISCUtils.getID(player), access);
					player.openInventory(BankUtils.getBankInventory(MISCUtils.getID(arguments[0])));
					return true;
				}
				Message noBalance = new Message("Messages.Admin.NoBank");
				noBalance.addVariable("$player", arguments[0]);
				noBalance.addVariable("$world", world);
				sender.sendMessage(noBalance.translate());
				return false;
			}
			Message noPlayer = new Message("Messages.General.NoPlayer");
			noPlayer.addVariable("$player", arguments[0]);
			sender.sendMessage(noPlayer.translate());
			return false;
		}
		help(sender);
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy bank <player> [world] - View the specified player's bank for [world]");
	}
}