package com.github.tnerevival.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;


public class AdminDeleteCommand extends TNECommand {
  
  public AdminDeleteCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "delete";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.delete";
  }

  @Override
  public boolean console() {
    return true;
  }
  
  @Override
  public boolean execute(CommandSender sender, String[] arguments) {
    if(arguments.length >= 1) {
      //TODO: Messages
      if(AccountUtils.exists(MISCUtils.getID(arguments[0]))) {
        TNE.instance.manager.accounts.remove(MISCUtils.getID(arguments[0]));
        
      }
    }
    return true;
  }

  @Override
  public void help(CommandSender sender) {
    sender.sendMessage(ChatColor.GOLD + "/tne delete <player> - Delete <player>'s account.");
  }
}