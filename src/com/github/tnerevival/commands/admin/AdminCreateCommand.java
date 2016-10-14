package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class AdminCreateCommand extends TNECommand {
  
  public AdminCreateCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "create";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.create";
  }

  @Override
  public boolean console() {
    return true;
  }
  
  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = (sender instanceof Player)? MISCUtils.getWorld((Player)sender) : TNE.instance.defaultWorld;
      UUID id = MISCUtils.genUUID(arguments[0]);
      if(!AccountUtils.exists(id)) {
        Account acc = new Account(id);
        if(arguments.length > 1) {
          acc.setBalance(TNE.instance.defaultWorld, Double.parseDouble(arguments[1]));
        }
        TNE.instance.manager.accounts.put(acc.getUid(), acc);

        Message m = new Message("Messages.Admin.Created");
        m.addVariable("$player", arguments[0]);
      }
      new Message("Messages.Admin.Exists").translate(world, sender);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public void help(CommandSender sender) {
    sender.sendMessage(ChatColor.GOLD + "/tne create <player> [balance] - Create an account with <player> as the username. Optional starting balance parameter.([balance])");
  }
}