package com.github.tnerevival.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;

public class MoneyExecutor implements CommandExecutor {

	 private TNE plugin;
		
		public MoneyExecutor(TNE plugin) {
			this.plugin = plugin;
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				String world = MISCUtils.getWorld(player.getUniqueId());
				
				if(cmd.getName().equalsIgnoreCase("money")) {
					if(args.length > 0) {
						if(args[0].equalsIgnoreCase("help")) {
							if(player.hasPermission("tne.money.help")) {
								sendHelp(player);
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("balance")) {
							if(player.hasPermission("tne.money.balance")) {
								player.sendMessage(ChatColor.WHITE + "You currently have " + ChatColor.GOLD + MISCUtils.formatBalance(world, plugin.api.getBalance(player)) + ChatColor.WHITE + " on you.");
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("give")) {
							if(player.hasPermission("tne.money.give")) {
								if(args.length == 3) {
									if(AccountUtils.giveMoney(Bukkit.getPlayer(args[1]).getUniqueId(), player.getUniqueId(), Double.valueOf(args[2]))) {
										player.sendMessage(ChatColor.WHITE + "Successfully gave " + args[1] + " " + ChatColor.GOLD + MISCUtils.formatBalance(world, Double.valueOf(args[2])) + ChatColor.WHITE + ".");		
									} else {
										player.sendMessage(ChatColor.DARK_RED + "The player you specified could not be found!");
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Correct usage is /money give <player> <amount>");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("pay")) {
							if(player.hasPermission("tne.money.pay")) {
								if(args.length == 3) {
									if(AccountUtils.hasFunds(player.getUniqueId(), Double.valueOf(args[2]))) {
										if(AccountUtils.payMoney(player.getUniqueId(), Bukkit.getPlayer(args[1]).getUniqueId(), Double.valueOf(args[2]))) {
											player.sendMessage(ChatColor.WHITE + "Successfully paid " + args[1] + " " + ChatColor.GOLD + MISCUtils.formatBalance(world, Double.valueOf(args[2])) + ChatColor.WHITE + ".");		
										} else {
											player.sendMessage(ChatColor.DARK_RED + "The player you specified could not be found!");
										}
									} else {
										player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have " + ChatColor.GOLD + MISCUtils.formatBalance(world, Double.valueOf(args[2])) + ChatColor.WHITE + ".");
									}
									
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Correct usage is /money pay <player> <amount>");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						}
					} else {
						sendHelp(player);
					}
				}
			}
			return false;
		}
		
		private void sendHelp(Player player) {
			player.sendMessage(ChatColor.GOLD + "~~~~~Money Commands~~~~~");
			player.sendMessage(ChatColor.GOLD + "/money help - general money help");
			player.sendMessage(ChatColor.GOLD + "/money balance - find out how much money you have on you");
			player.sendMessage(ChatColor.GOLD + "/money give <player> <amount> - summon money from air and give it to a player");
			player.sendMessage(ChatColor.GOLD + "/money pay <player> <amount> - pay a player money from your balance");		
		}
}