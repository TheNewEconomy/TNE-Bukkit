package com.github.tnerevival.commands.credit;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CreditCommand extends TNECommand {

	public CreditCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new CreditCommandsCommand(plugin));
		subCommands.add(new CreditInventoryCommand(plugin));
	}

	@Override
	public String getName() {
		return "credit";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.credit";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
  public Boolean confirm() {
	  return true;
  }
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
    return super.execute(sender, command, arguments);
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~~Credit Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/credit help - View general credit command help.");
		sender.sendMessage(ChatColor.GOLD + "/credit inventory <inventory> - View time credits for <inventory> in every world.");
		sender.sendMessage(ChatColor.GOLD + "/credit commands - View all command credits you have accumulated.");
	}
	
}