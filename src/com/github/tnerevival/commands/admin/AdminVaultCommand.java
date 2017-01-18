package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminVaultCommand extends TNECommand {

  public AdminVaultCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "vault";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.vault";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length == 1 || arguments.length == 2) {
      Player player = getPlayer(sender);
      String world = (arguments.length == 2) ? arguments[1] : TNE.instance.defaultWorld;
      if(IDFinder.getID(arguments[0]) != null && TNE.instance.manager.accounts.containsKey(IDFinder.getID(arguments[0]))) {
        Account acc = AccountUtils.getAccount(IDFinder.getID(arguments[0]));
        if(acc.getBanks().containsKey(world)) {
          player.openInventory(AccountUtils.getAccount(IDFinder.getID(arguments[0])).getVault(world).getInventory());
          return true;
        }
        Message noBalance = new Message("Messages.Admin.NoBank");
        noBalance.addVariable("$player", arguments[0]);
        noBalance.addVariable("$world", world);
        noBalance.translate(world, player);
        return false;
      }
      Message noPlayer = new Message("Messages.General.NoPlayer");
      noPlayer.addVariable("$player", arguments[0]);
      noPlayer.translate(world, player);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/theneweconomy bank <player> [world] - View the specified player's bank for [world]";
  }
}