package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankPriceCommand extends TNECommand {

  public BankPriceCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "price";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.bank.price";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    Message cost = new Message("Messages.Bank.Cost");
    cost.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), BankUtils.cost(player.getWorld().getName(), MISCUtils.getID(player).toString())));
    cost.translate(MISCUtils.getWorld(player), player);
    return true;
  }

  @Override
  public String getHelp() {
    return "/bank price - Displays the price of a bank.";
  }

}