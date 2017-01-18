package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Daniel on 10/14/2016.
 */
public class AdminIDCommand extends TNECommand {

  public AdminIDCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "id";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.id";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = (sender instanceof Player)? IDFinder.getWorld((Player)sender) : TNE.instance.defaultWorld;

      if(AccountUtils.exists(IDFinder.getID(arguments[0]))) {
        Message m = new Message("Messages.Admin.ID");
        m.addVariable("$player", arguments[0]);
        m.addVariable("$id", IDFinder.getID(arguments[0]).toString());

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
    return "/tne id <player> - Get <player>'s id to be used for player configurations.";
  }
}