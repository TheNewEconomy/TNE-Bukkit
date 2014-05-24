package com.github.tnerevival.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.companies.Company;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.CompanyUtils;

public class CompanyExecutor implements CommandExecutor {

	private TNE plugin;
		
	public CompanyExecutor(TNE plugin) {
		this.plugin = plugin;
	}
		
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
	        Account account = AccountUtils.getAccount(player.getDisplayName());
				
			if(cmd.getName().equalsIgnoreCase("company")) {
				if(CompanyUtils.enabled(player.getDisplayName())) {
					if(args.length >= 1) {
						if(args[0].equalsIgnoreCase("help")) {
							if(player.hasPermission("tne.company.help")) {
								sendHelp(player);
							} else {
								player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("view")) {
							if(player.hasPermission("tne.company.view")) {
								if(args.length >= 1 && args.length <= 2) {
									if(args.length == 1) {
										if(plugin.manager.companies.containsKey(account.getCompany())) {
											Company company = plugin.manager.companies.get(account.getCompany());
											for(String s : company.details()) {
					        					player.sendMessage(s);
					        				}
										} else {
											player.sendMessage(ChatColor.RED + "I'm sorry, but you must be apart of a company to do that!");
										}
									} else {
										if(plugin.manager.companies.containsKey(args[1])) {
											Company company = plugin.manager.companies.get(args[1]);
											for(String s : company.details()) {
					        					player.sendMessage(s);
					        				}
										} else {
											player.sendMessage(ChatColor.RED + "I'm sorry, but no company by the name " + args[1] + "exists.");
										}
									}
								} else {
									sendHelp(player);
								}
							} else {
								player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("create")) {
							if(player.hasPermission("tne.company.create")) {
								if(args.length == 2) {
			        			    if(plugin.manager.companies.containsKey(args[1])) {
			        				    player.sendMessage("I'm sorry, but that name is already in use.");
			        				    Company company = new Company(args[1], player.getName());
			        				    account.setCompany(company.getName());
			        				    plugin.manager.companies.put(args[1], company);
			        					player.sendMessage(ChatColor.GOLD + "Company has been created successfully!");
			        				} else {
			        					player.sendMessage(ChatColor.RED + "I'm sorry, but that name is already in use.");
			        				}
								} else {
			        				player.sendMessage(ChatColor.RED + "Correct usage is /company create [name].");
			        			}
							} else {
								player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("delete")) {
							if(player.hasPermission("tne.company.delete")) {
								if(args.length == 1) {
									if(plugin.manager.companies.containsKey(account.getCompany())) {
										Company company = plugin.manager.companies.get(account.getCompany());
										if(company.getOwner().equals(player.getName())) {
			        						company.delete();
										} else {
				        					player.sendMessage(ChatColor.RED + "You must be the owner of your company to do that.");
										}
									} else {
										player.sendMessage(ChatColor.RED + "I'm sorry, but you must be apart of a company to do that!");
									}
								} else {
									sendHelp(player);
								}
							} else {
								player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("rename")) {
							if(player.hasPermission("tne.company.rename")) {
								if(plugin.manager.companies.containsKey(account.getCompany())) {
									Company company = plugin.manager.companies.get(account.getCompany());
									if(company.getOwner().equals(player.getName())) {
										if(plugin.manager.companies.containsKey(args[1])) {
											player.sendMessage(ChatColor.RED + "I'm sorry, but that name is already in use.");
				        				} else {
				        					plugin.manager.companies.remove(company.getName());
				        					company.setName(args[1]);
				        					plugin.manager.companies.put(args[1], company);
				        					player.sendMessage(ChatColor.WHITE + "You have successfully renamed your company to " + ChatColor.GREEN + args[1] + ChatColor.WHITE + ".");
				        				}
									} else {
										player.sendMessage(ChatColor.RED + "You must be the owner of your company to do that.");
				        			}
								}
							} else {
								player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("apply")) {
							if(player.hasPermission("tne.company.apply")) {
								sendHelp(player);
							} else {
								player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("hire")) {
							if(player.hasPermission("tne.company.hire")) {
								sendHelp(player);
							} else {
								player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("fire")) {
							if(player.hasPermission("tne.company.fire")) {
								sendHelp(player);
							} else {
								player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
							}
						} else if(args[0].equalsIgnoreCase("jobs")) {
							if(args.length <= 4) {
								if(args.length == 1) {
									if(player.hasPermission("tne.jobs.view")) {
										
									} else {
										player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
									}
								} else {
									if(args[1].equalsIgnoreCase("create")) {
										if(player.hasPermission("tne.jobs.create")) {
											
										} else {
											player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
										}
									} else if(args[1].equalsIgnoreCase("remove")) {
										if(player.hasPermission("tne.jobs.remove")) {
											
										} else {
											player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
										}
									} else if(args[1].equalsIgnoreCase("rename")) {
										if(player.hasPermission("tne.jobs.rename")) {
											
										} else {
											player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
										}
									} else if(args[1].equalsIgnoreCase("view")) {
										if(player.hasPermission("tne.jobs.view")) {
											
										} else {
											player.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to do that.");
										}
									} else {
										sendHelp(player);
									}
								}
							} else {
								sendHelp(player);
							}
						} else {
							sendHelp(player);
						}
					} else {
						sendHelp(player);
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "I'm sorry, but companies are not enabled in your world");
			}
		}
		return false;
	}
	
	void sendHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "~~~~Company Help~~~~");
		player.sendMessage(ChatColor.GOLD + "/company help - View the company help menu.");
		player.sendMessage(ChatColor.GOLD + "/company view - View details about your current company.");
		player.sendMessage(ChatColor.GOLD + "/company create [name] - Create a new company.");
		player.sendMessage(ChatColor.GOLD + "/company delete - Delete your company.");
		player.sendMessage(ChatColor.GOLD + "/company rename [name] - Rename your company.");
		player.sendMessage(ChatColor.GOLD + "/company apply [name] - Express your interest in joining a company.");
		player.sendMessage(ChatColor.GOLD + "/company hire [player] [job] - Hire an employee.");
		player.sendMessage(ChatColor.GOLD + "/company fire [player] <reason> - Fire an employee.");
		player.sendMessage(ChatColor.GOLD + "/company jobs - View your company's jobs.");
		player.sendMessage(ChatColor.GOLD + "/company jobs create [name] <pay> - Create a new job.");
		player.sendMessage(ChatColor.GOLD + "/company jobs remove [name] <reason> - Remove a job from your company.");
	}
}