package com.github.tnerevival.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.MISCUtils;

public class CoreExecutor implements CommandExecutor {

	private TNE plugin;
		
	public CoreExecutor(TNE plugin) {
		this.plugin = plugin;
	}
		
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
				Player player = (Player) sender;
				String world = plugin.defaultWorld;
				
				if(MISCUtils.multiWorld()) {
					world = player.getWorld().getName();
				}
				
				if(cmd.getName().equalsIgnoreCase("theneweconomy")) {
					if(args.length >= 1) {
						if(args[0].equalsIgnoreCase("help")) {
							if(player.hasPermission("tne.admin.help")) {
								sendHelp(player);
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that!");
							}
						} else if(args[0].equalsIgnoreCase("reload")) {
							if(player.hasPermission("tne.admin.reload")) {
								if(args.length < 3) {
									if(args.length == 1) {
										MISCUtils.reloadConfigurations("config");
										player.sendMessage(ChatColor.WHITE + "Configurations reloaded!");
									} else if(args.length == 2) {
										if(args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("config") || args[1].equalsIgnoreCase("mobs") || args[1].equalsIgnoreCase("worlds")) {
											MISCUtils.reloadConfigurations(args[1]);
											player.sendMessage(ChatColor.WHITE + args[1] + ".yml reloaded!");
										} else {
											sendHelp(player);
										}
									}
								} else {
									sendHelp(player);
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that!");
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
		player.sendMessage(ChatColor.GOLD + "~~~~~TNE Core Commands~~~~~");
		player.sendMessage(ChatColor.GOLD + "/theneweconomy help - general TNE help");
		player.sendMessage(ChatColor.GOLD + "/theneweconomy reload <all/config/mobs/worlds> - reload the TNE configurations or reload the specified file");
	}
}
