package com.github.tnerevival.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;


public class AdminPurgeCommand extends TNECommand {
  
  public AdminPurgeCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "purge";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.purge";
  }

  @Override
  public boolean console() {
    return true;
  }
  
  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    boolean world = arguments.length >= 1;
    //TODO: Messages
    if(world) {
      TNE.instance.manager.purge(arguments[0]);
      return true;
    }
    TNE.instance.manager.purgeAll();
    return true;
  }

  @Override
  public void help(CommandSender sender) {
    sender.sendMessage(ChatColor.GOLD + "/tne purge [world] - Deletes all accounts with a default balance in [world], otherwise checks every world for activity.");
  }
}