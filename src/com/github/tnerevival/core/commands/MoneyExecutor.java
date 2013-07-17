package com.github.tnerevival.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TheNewEconomy;

public class MoneyExecutor implements CommandExecutor {
	 
	private TheNewEconomy plugin;

	public MoneyExecutor(TheNewEconomy plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
	           Player player = (Player) sender;
	           if(cmd.getName().equalsIgnoreCase("money")) {
	        	   
	           }
		}
		return false;
	}
}