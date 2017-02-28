package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
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
      String world = (sender instanceof Player)? IDFinder.getWorld((Player)sender) : TNE.instance().defaultWorld;
      UUID id = IDFinder.genUUID(arguments[0]);
      if(!AccountUtils.exists(id)) {
        Account acc = new Account(id);
        BigDecimal balance = AccountUtils.getInitialBalance(TNE.instance().defaultWorld, TNE.instance().manager.currencyManager.get(world).getName());
        if(arguments.length > 1) {
          try {
            balance = CurrencyFormatter.translateBigDecimal(arguments[1], world);
          } catch(Exception e) {
            //Do Nothing
          }
        }
        acc.setBalance(TNE.instance().defaultWorld, balance, TNE.instance().manager.currencyManager.get(world).getName());
        TNE.instance().manager.accounts.put(acc.getUid(), acc);

        Message m = new Message("Messages.Admin.Created");
        m.addVariable("$player", arguments[0]);
        m.translate(world, sender);
        return true;
      }
      new Message("Messages.Admin.Exists").translate(world, sender);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/tne create <player> [balance] - Create an account with <player> as the username. Optional starting balance parameter.([balance])";
  }
}