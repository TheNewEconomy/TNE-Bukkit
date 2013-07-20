package com.github.tnerevival.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TheNewEconomy;
import com.github.tnerevival.core.accounts.Account;
import com.github.tnerevival.core.accounts.Transaction;

public class MoneyExecutor implements CommandExecutor {
	 
	private TheNewEconomy plugin;

	public MoneyExecutor(TheNewEconomy plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
	           Player player = (Player) sender;
	           Account acc = TheNewEconomy.instance.eco.accounts.get(player.getName());
	           if(cmd.getName().equalsIgnoreCase("money")) {
	        	   if(args.length >= 1) {
	        		   if(args[0].equals("give")) {
	        			   if(args.length == 3) {
	        				   if(Bukkit.getPlayer(args[1]) != null) {
	        					   Transaction.addFunds(player.getName(), Double.valueOf(args[2]));
	        					   Bukkit.getPlayer(args[1]).sendMessage("You have been given " + args[2] + ".");
	        					   player.sendMessage("You have sent " + args[1] + " " + args[2] + ".");
	        				   } else {
        						   player.sendMessage("I'm sorry, but the specified player could not be found.");
        					   }
	        			   } else {
	        				   player.sendMessage("Correct usage is /money give [player name] [amount]");
	        			   }
	        		   } else if(args[0].equals("pay")) {
	        			   if(args.length == 3) {
	        				   if(Transaction.hasEnough(player.getName(), Double.valueOf(args[2]))) {
	        					   if(Bukkit.getPlayer(args[1]) != null) {
	        						   Transaction.addFunds(args[1], Double.valueOf(args[2]));
	        						   Transaction.takeFunds(player.getName(), Double.valueOf(args[2]));
	        						   Bukkit.getPlayer(args[1]).sendMessage("You have received a payment from " + player.getName() + " for the amount of " + args[2] + ".");
	        						   player.sendMessage("You sent " + args[1] + " " + args[2] + ".");
	        					   } else {
	        						   player.sendMessage("I'm sorry, but the specified player could not be found.");
	        					   }
	        				   } else {
	        					   player.sendMessage("I'm sorry, but you don't have enough money to do that.");
	        				   }
	        			   } else {
	        				   player.sendMessage("Correct usage is /money pay [player name] [amount]");
	        			   }
	        		   }
	        	   } else {
	        		   player.sendMessage(ChatColor.GREEN + "Balance: " + acc.getBalance());
	        	   }
	           }
		}
		return false;
	}
}