package com.github.tnerevival.commands.company;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;

public class CompanyCommand  extends TNECommand {

	public CompanyCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "company";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.company";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~Company Help~~~~");
		sender.sendMessage(ChatColor.GOLD + "/company help - View the company help menu.");
		sender.sendMessage(ChatColor.GOLD + "/company view - View details about your current company.");
		sender.sendMessage(ChatColor.GOLD + "/company create [name] - Create a new company.");
		sender.sendMessage(ChatColor.GOLD + "/company delete - Delete your company.");
		sender.sendMessage(ChatColor.GOLD + "/company rename [name] - Rename your company.");
		sender.sendMessage(ChatColor.GOLD + "/company apply [name] - Express your interest in joining a company.");
		sender.sendMessage(ChatColor.GOLD + "/company hire [player] [job] - Hire an employee.");
		sender.sendMessage(ChatColor.GOLD + "/company fire [player] <reason> - Fire an employee.");
		sender.sendMessage(ChatColor.GOLD + "/company jobs - View your company's jobs.");
		sender.sendMessage(ChatColor.GOLD + "/company jobs create [name] <pay> - Create a new job.");
		sender.sendMessage(ChatColor.GOLD + "/company jobs remove [name] <reason> - Remove a job from your company.");
	}
	
}