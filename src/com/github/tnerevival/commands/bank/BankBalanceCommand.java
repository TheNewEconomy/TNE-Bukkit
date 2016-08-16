package com.github.tnerevival.commands.bank;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;

public class BankBalanceCommand extends TNECommand {
	
	public BankBalanceCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "balance";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank.balance";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
	  String ownerName = (arguments.length >= 1)? arguments[0] : sender.getName();
		Player owner = MISCUtils.getPlayer(ownerName);
    Player player = MISCUtils.getPlayer(sender.getName());

		if(BankUtils.hasBank(MISCUtils.getID(owner))) {
		  if(BankUtils.bankMember(MISCUtils.getID(owner), MISCUtils.getID(sender.getName()))) {
        Message balance = new Message("Messages.Bank.Balance");
        balance.addVariable("$amount",  MISCUtils.formatBalance(owner.getWorld().getName(), BankUtils.getBankBalance(MISCUtils.getID(player))));
        balance.addVariable("$name", ownerName);
        player.sendMessage(balance.translate());
        return true;
      }
      Message noAccess = new Message("Messages.Bank.Invalid");
      noAccess.addVariable("$name", ownerName);
      player.sendMessage(noAccess.translate());
		}
    player.sendMessage(new Message("Messages.Bank.None").translate());
		return false;
	}

	@Override
	public void help(CommandSender sender) {
    sender.sendMessage(ChatColor.GOLD + "/bank balance [owner] - Find out how much gold is in a specific bank. Defaults to your personal bank.");
  }
}