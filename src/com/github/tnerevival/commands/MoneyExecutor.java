package com.github.tnerevival.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.PlayerUtils;

public class MoneyExecutor implements CommandExecutor {

	 private TNE plugin;
		
		public MoneyExecutor(TNE plugin) {
			this.plugin = plugin;
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				String username = player.getDisplayName();
				Account account = plugin.manager.accounts.get(player.getDisplayName());
				
				if(cmd.getName().equalsIgnoreCase("money")) {
					if(args.length > 0) {
						if(args[0].equalsIgnoreCase("help")) {
							sendHelp(player);
						} else if(args[0].equalsIgnoreCase("balance")) {
							player.sendMessage(ChatColor.WHITE + "You currently have " + ChatColor.GOLD + MISCUtils.formatBalance(account.getBalance()) + ChatColor.WHITE + " on you.");
						} else if(args[0].equalsIgnoreCase("give")) {
							if(args.length == 3) {
								if(PlayerUtils.giveMoney(args[1], Double.valueOf(args[2]))) {
									player.sendMessage(ChatColor.WHITE + "Successfully gave " + args[1] + " " + ChatColor.GOLD + MISCUtils.formatBalance(Double.valueOf(args[2])) + ChatColor.WHITE + ".");		
								} else {
									player.sendMessage(ChatColor.DARK_RED + "The player you specified could not be found!");
								}
							} else {
								sendHelp(player);
							}
						} else if(args[0].equalsIgnoreCase("pay")) {
							if(args.length == 3) {
								if(PlayerUtils.hasFunds(username, Double.valueOf(args[2]))) {
									if(PlayerUtils.payMoney(username, args[1], Double.valueOf(args[2]))) {
										player.sendMessage(ChatColor.WHITE + "Successfully paid " + args[1] + " " + ChatColor.GOLD + MISCUtils.formatBalance(Double.valueOf(args[2])) + ChatColor.WHITE + ".");		
									} else {
										player.sendMessage(ChatColor.DARK_RED + "The player you specified could not be found!");
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have " + ChatColor.GOLD + MISCUtils.formatBalance(Double.valueOf(args[2])) + ChatColor.WHITE + ".");
								}
							} else {
								sendHelp(player);
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