package com.github.tnerevival.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.MISCUtils;


public class AdminCreateCommand extends TNECommand {
  
  public AdminCreateCommand(TNE plugin) {
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
    return true;
  }
  
  @Override
  public boolean execute(CommandSender sender, String[] arguments) {
    if(arguments.length >= 1) {
      //TODO: Messages
      Account acc = new Account(MISCUtils.getID(arguments[0]));
      if(arguments.length > 1) {
        acc.setBalance(TNE.instance.defaultWorld, Double.parseDouble(arguments[1]));
      }
      TNE.instance.manager.accounts.put(acc.getUid(), acc);
    }
    return true;
  }

  @Override
  public void help(CommandSender sender) {
    sender.sendMessage(ChatColor.GOLD + "/ecoaccount create <player> [balance] - Create an account with <player> as the username. Optional starting balance parameter.([balance])");
  }
}