package com.github.tnerevival.commands.vault;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Daniel on 8/10/2016.
 */
public class VaultAddCommand extends TNECommand {

  public VaultAddCommand(TNE plugin) {
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
    return "tne.vault.add";
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

    if(IDFinder.getPlayer(arguments[0]) == null) {
      Message notFound = new Message("Messages.General.NoPlayer");
      notFound.addVariable("$player", arguments[0]);
      notFound.translate(IDFinder.getWorld(player), player);
      return false;
    }

    if(!account.hasVault(world)) {
      Message none = new Message("Messages.Vault.None");
      none.addVariable("$amount",  CurrencyFormatter.format(getWorld(sender), Vault.cost(getWorld(sender), IDFinder.getID(player).toString())));
      none.translate(getWorld(sender), player);
      return false;
    }

    if(!account.getVault(world).getOwner().equals(IDFinder.getID(player)) || !world.equals(getWorld(sender)) && !TNE.instance.api.getBoolean("Core.Vault.MultiManage")) {
      new Message("Messages.General.NoPerm").translate(getWorld(player), player);
      return false;
    }
    account.getVault(world).addMember(IDFinder.getID(IDFinder.getPlayer(arguments[0])));
    Message added = new Message("Messages.Vault.Added");
    added.addVariable("$player", arguments[0]);
    added.translate(getWorld(player), player);
    return true;
  }

  @Override
  public String getHelp() {
    return "/vault add <player> [world] - Add <player> to your vault.";
  }
}