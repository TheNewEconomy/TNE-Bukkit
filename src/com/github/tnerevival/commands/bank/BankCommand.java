package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.BankUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class BankCommand extends TNECommand {

	public BankCommand(TNE plugin) {
		super(plugin);
		subCommands.add(new BankAddCommand(plugin));
		subCommands.add(new BankBalanceCommand(plugin));
		subCommands.add(new BankBuyCommand(plugin));
		subCommands.add(new BankDepositCommand(plugin));
		subCommands.add(new BankPriceCommand(plugin));
		subCommands.add(new BankRemoveCommand(plugin));
		subCommands.add(new BankViewCommand(plugin));
		subCommands.add(new BankWithdrawCommand(plugin));
	}

	@Override
	public String getName() {
		return "bank";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
  public Boolean locked() {
	  return true;
  }

  @Override
  public Boolean confirm() {
    return true;
  }

  @Override
  public Boolean activated(String world, String player) {
    return BankUtils.command(world, player);
  }
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
    return super.execute(sender, command, arguments);
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "~~~~~Bank Commands~~~~~");
		sender.sendMessage(ChatColor.GOLD + "/bank help - general bank help");
		sender.sendMessage(ChatColor.GOLD + "/bank balance - find out how much gold is in your bank");
		sender.sendMessage(ChatColor.GOLD + "/bank buy - buy yourself a bank");
		sender.sendMessage(ChatColor.GOLD + "/bank deposit <amount> - put the specified amount of money in your bank");
		sender.sendMessage(ChatColor.GOLD + "/bank price - see how much a bank cost");
		sender.sendMessage(ChatColor.GOLD + "/bank view - view your bank");
		sender.sendMessage(ChatColor.GOLD + "/bank withdraw <amount> - withdraw the specified amout of money from your bank");
	}
	
}