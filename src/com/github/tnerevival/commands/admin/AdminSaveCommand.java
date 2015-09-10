package com.github.tnerevival.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.MISCUtils;

public class AdminSaveCommand extends TNECommand {
	
	public AdminSaveCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.admin.save";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		Player player = getPlayer(sender);
		this.plugin.saveManager.save();
		player.sendMessage("Successfully saved all TNE Data!");
		return true;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/theneweconomy save - force saves all TNE data");
	}
}