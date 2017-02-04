package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Daniel on 8/10/2016.
 */
public class BankRemoveCommand extends TNECommand {

  public BankRemoveCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "remove";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "-" };
  }

  @Override
  public String getNode() {
    return "tne.bank.remove";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    Account account = AccountUtils.getAccount(IDFinder.getID(player));
    String world = (arguments.length >= 2)? arguments[1] : getWorld(sender);

    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    if(IDFinder.getID(arguments[0]) == null) {
      Message notFound = new Message("Messages.General.NoPlayer");
      notFound.addVariable("$player", arguments[0]);
      notFound.translate(IDFinder.getWorld(player), player);
      return false;
    }

    if(!account.hasBank(world)) {
      Message none = new Message("Messages.Bank.None");
      none.addVariable("$amount",  CurrencyFormatter.format(getWorld(sender), Bank.cost(getWorld(sender), IDFinder.getID(player).toString())));
      none.translate(getWorld(sender), player);
      return false;
    }

    if(!account.getBank(world).getOwner().equals(IDFinder.getID(player))|| !world.equals(getWorld(sender)) && !TNE.instance.api.getBoolean("Core.Bank.MultiManage")) {
      new Message("Messages.General.NoPerm").translate(getWorld(player), player);
      return false;
    }
    account.getBank(world).removeMember(IDFinder.getID(IDFinder.getPlayer(arguments[0])));
    Message added = new Message("Messages.Bank.Removed");
    added.addVariable("$player", arguments[0]);
    added.translate(getWorld(player), player);
    return true;
  }

  @Override
  public String getHelp() {
    return "/bank remove <player> [world] - Removes <player> from your bank for world [world].";
  }

}