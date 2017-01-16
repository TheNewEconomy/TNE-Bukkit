package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = (sender instanceof Player)? MISCUtils.getWorld((Player)sender) : TNE.instance.defaultWorld;
      if(AccountUtils.exists(IDFinder.getID(arguments[0]))) {
        TNE.instance.saveManager.versionInstance.deleteAccount(IDFinder.getID(arguments[0]));

        Message m = new Message("Messages.Admin.Deleted");
        m.addVariable("$player", arguments[0]);
        m.translate(world, sender);
        return true;
      }
      Message m = new Message("Messages.General.NoPlayer");
      m.addVariable("$player", arguments[0]);
      m.translate(world, sender);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/tne delete <player> - Delete <player>'s account.";
  }
}