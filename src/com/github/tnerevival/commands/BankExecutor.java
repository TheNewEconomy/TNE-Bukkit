package com.github.tnerevival.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.PlayerUtils;

public class BankExecutor implements CommandExecutor {

 private TNE plugin;
	
	public BankExecutor(TNE plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String username = player.getDisplayName();
			String world = PlayerUtils.getWorld(username);
			
			if(cmd.getName().equalsIgnoreCase("bank")) {
				if(BankUtils.enabled(world)) {
					if(args.length > 0) {
						if(args[0].equalsIgnoreCase("help")) {
							if(player.hasPermission("tne.bank.help") || player.hasPermission("tne.bank.*")) {
								sendHelp(player);
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
							}
						} else if(args[0].equalsIgnoreCase("balance")) {
							if(player.hasPermission("tne.bank.balance") || player.hasPermission("tne.bank.*")) {
								if(BankUtils.hasBank(username)) {
									player.sendMessage(ChatColor.WHITE + "You currently have " + ChatColor.GOLD + MISCUtils.formatBalance(BankUtils.getBankBalance(username)) + " in your bank.");
								} else {
									player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("buy")) { 
							if(player.hasPermission("tne.bank.buy") || player.hasPermission("tne.bank.*")) {
								if(!BankUtils.hasBank(username)) {
									if(!player.hasPermission("tne.bank.bypass") && !player.hasPermission("tne.bank.*")) {
										if(AccountUtils.hasFunds(username, BankUtils.cost(world))) {
											AccountUtils.removeFunds(username, BankUtils.cost(world));
											Bank bank = new Bank(username, BankUtils.size(world));
											plugin.manager.banks.put(username, bank);
											player.sendMessage(ChatColor.WHITE + "Congratulations! You have successfully purchased a bank!");
										} else {
											player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you need at least " + ChatColor.GOLD + MISCUtils.formatBalance(BankUtils.cost(world)) + ChatColor.DARK_RED + " to create a bank.");
										}
									} else {
										Bank bank = new Bank(username, BankUtils.size(world));
										plugin.manager.banks.put(username, bank);
										player.sendMessage(ChatColor.WHITE + "Congratulations! You have successfully purchased a bank!");
									}
								} else {
									player.sendMessage(ChatColor.RED + "You already have a bank!");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("deposit")) {
							if(player.hasPermission("tne.bank.deposit") || player.hasPermission("tne.bank.*")) {
								if(BankUtils.hasBank(username)) {
									if(BankUtils.bankDeposit(username, Double.valueOf(args[1]))) {
										player.sendMessage(ChatColor.WHITE + "You have deposited " + ChatColor.GOLD + MISCUtils.formatBalance(Double.valueOf(args[1])) + ChatColor.WHITE + " into your bank.");
									} else {
										player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have " + ChatColor.GOLD + MISCUtils.formatBalance(Double.valueOf(args[1])) + ChatColor.DARK_RED + ".");
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("price")) { 
							if(player.hasPermission("tne.bank.price") || player.hasPermission("tne.bank.*")) {
								player.sendMessage(ChatColor.WHITE + "A bank is currently " + ChatColor.GOLD + MISCUtils.formatBalance(BankUtils.cost(world)) + ChatColor.WHITE + ".");
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("view")) {
							if(player.hasPermission("tne.bank.use") || player.hasPermission("tne.bank.*")) {
								if(BankUtils.command(world)) {
									if(BankUtils.hasBank(username)) {
										Inventory bankInventory = BankUtils.getBankInventory(username);
										player.openInventory(bankInventory);
									} else {
										player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but accessing banks via /bank has been disabled in this world!");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("withdraw")) {
							if(player.hasPermission("tne.bank.withdraw") || player.hasPermission("tne.bank.*")) {
								if(BankUtils.hasBank(username)) {
									if(BankUtils.bankWithdraw(username, Double.valueOf(args[1]))) {
										player.sendMessage(ChatColor.WHITE + "You have withdrawn " + ChatColor.GOLD + MISCUtils.formatBalance(Double.valueOf(args[1])) + ChatColor.WHITE + " from your bank.");
									} else {
										player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but your bank does not have " + ChatColor.GOLD + MISCUtils.formatBalance(Double.valueOf(args[1])) + ChatColor.WHITE + ".");
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
							}
						}
					} else {
						sendHelp(player);
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but banks are not enabled in this world!");
				}
			}
		}
		return false;
	}
	
	private void sendHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "~~~~~Bank Commands~~~~~");
		player.sendMessage(ChatColor.GOLD + "/bank help - general bank help");
		player.sendMessage(ChatColor.GOLD + "/bank balance - find out how much gold is in your bank");
		player.sendMessage(ChatColor.GOLD + "/bank buy - buy yourself a bank");
		player.sendMessage(ChatColor.GOLD + "/bank deposit <amount> - put the specified amount of money in your bank");
		player.sendMessage(ChatColor.GOLD + "/bank price - see how much a bank cost");
		player.sendMessage(ChatColor.GOLD + "/bank view - view your bank");
		player.sendMessage(ChatColor.GOLD + "/bank withdraw <amount> - withdraw the specified amout of money from your bank");		
	}
}