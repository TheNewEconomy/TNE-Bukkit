package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.BankUtils;

public class BankViewCommand extends TNECommand {
	
	public BankViewCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "view";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank.view";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		Player player = getPlayer(sender);
		if(BankUtils.command(player.getWorld().getName())) {
			if(BankUtils.hasBank(player.getUniqueId())) {
				Inventory bankInventory = BankUtils.getBankInventory(player.getUniqueId());
				player.openInventory(bankInventory);
			} else {
				player.sendMessage(new Message("Messages.Bank.None").translate());
			}
		} else {
			player.sendMessage(new Message("Messages.Bank.NoCommand").translate());
		}
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/bank view - view your bank");
	}
	
}