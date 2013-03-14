package com.github.tnerevival;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TheNewEconomyCommands implements CommandExecutor {

	TheNewEconomy plugin;

	public TheNewEconomyCommands(TheNewEconomy plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("area")) {
				if(p.hasPermission("TNE.USER.AREA")) {
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			} else if (cmd.getName().equalsIgnoreCase("auction")) {
				if(p.hasPermission("TNE.USER.AUCTION")) {
					
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			} else if (cmd.getName().equalsIgnoreCase("bank")) {
				if(p.hasPermission("TNE.USER.BANK")) {
				
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			} else if (cmd.getName().equalsIgnoreCase("buy")) {
				if(p.hasPermission("TNE.USER.ECO")) {
					
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			} else if (cmd.getName().equalsIgnoreCase("economy")) {
				if(p.hasPermission("TNE.ADMIN")) {
					
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			} else if (cmd.getName().equalsIgnoreCase("lottery")) {
				if(p.hasPermission("TNE.USER.LOTTO")) {
					
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			} else if (cmd.getName().equalsIgnoreCase("money")) {
				if(p.hasPermission("TNE.USER.ECO")) {
					
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			} else if (cmd.getName().equalsIgnoreCase("offer")) {
				if(p.hasPermission("TNE.USER.TRADE")) {
					
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			}  else if (cmd.getName().equalsIgnoreCase("supply")) {
				if(p.hasPermission("TNE.USER.BUY")) {
					
				} else {
					p.sendMessage("You do not have permission to do that!");
					return false;
				}
			}
		}
		return false;
	}

}
