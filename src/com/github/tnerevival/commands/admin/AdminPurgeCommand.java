package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
    String world = (sender instanceof Player)? IDFinder.getWorld((Player)sender) : TNE.instance().defaultWorld;
    boolean isWorld = arguments.length >= 1;
    if(isWorld) {
      if(Bukkit.getWorld(arguments[0]) == null)
        new Message("Messages.General.NoWorld").translate(world, sender);
      TNE.instance().manager.purge(arguments[0]);
      Message m = new Message("Messages.Admin.PurgeWorld");
      m.addVariable("$world", arguments[0]);
      return true;
    }
    TNE.instance().manager.purgeAll();
    new Message("Messages.Admin.Purge").translate(world, sender);
    return true;
  }

  @Override
  public String getHelp() {
    return "/tne purge [world] - Deletes all accounts with a default balance in [world], otherwise checks every world for activity.";
  }
}