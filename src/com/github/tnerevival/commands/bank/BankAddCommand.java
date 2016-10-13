package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Daniel on 8/10/2016.
 */
public class BankAddCommand extends TNECommand {

  public BankAddCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "add";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "+" };
  }

  @Override
  public String getNode() {
    return "tne.bank.add";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = MISCUtils.getPlayer(sender.getName());

    if(arguments.length >= 1) {
      if(MISCUtils.getPlayer(arguments[0]) != null) {
        if (BankUtils.hasBank(MISCUtils.getID(player))) {
          if (BankUtils.getBank(MISCUtils.getID(player)).getOwner().equals(player.getUniqueId())) {
            BankUtils.getBank(MISCUtils.getID(player)).addMember(MISCUtils.getID(MISCUtils.getPlayer(arguments[0])));
            Message added = new Message("Messages.Bank.Added");
            added.addVariable("$player", arguments[0]);
            added.translate(MISCUtils.getWorld(player), player);
          }
          new Message("Messages.Bank.None").translate(MISCUtils.getWorld(player), player);
          return false;
        }
        new Message("Messages.Bank.None").translate(MISCUtils.getWorld(player), player);
        return false;
      }
      Message notFound = new Message("Messages.General.NoPlayer");
      notFound.addVariable("$player", arguments[0]);
      notFound.translate(MISCUtils.getWorld(player), player);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public void help(CommandSender sender) {
    sender.sendMessage(ChatColor.GOLD + "/bank add <player> - Add <player> to your bank.");
  }

}