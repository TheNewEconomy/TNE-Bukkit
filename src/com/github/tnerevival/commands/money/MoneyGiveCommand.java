package com.github.tnerevival.commands.money;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class MoneyGiveCommand extends TNECommand {

	public MoneyGiveCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "give";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.money.give";
	}

	@Override
	public boolean console() {
		return true;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		UUID id = (sender instanceof Player) ? getPlayer(sender).getUniqueId() : null;
		if(arguments.length == 2) {
			if(Double.valueOf(arguments[1]) < 0) {
				sender.sendMessage(ChatColor.RED + "Amount cannot be a negative value!");
				return false;
			}
			
			if(getPlayer(sender, arguments[0]) != null && AccountUtils.giveMoney(getPlayer(sender, arguments[0]).getUniqueId(), id, Double.valueOf(arguments[1]))) {
				sender.sendMessage(ChatColor.WHITE + "Successfully gave " + arguments[0] + " " + ChatColor.GOLD + MISCUtils.formatBalance(getPlayer(sender, arguments[0]).getWorld().getName(), Double.valueOf(arguments[1])) + ChatColor.WHITE + ".");
				return true;
			}
		} else {
			help(sender);
		}
		return false;
	}
	
	

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/money give <player> <amount> - summon money from air and give it to a player");
	}
	
}