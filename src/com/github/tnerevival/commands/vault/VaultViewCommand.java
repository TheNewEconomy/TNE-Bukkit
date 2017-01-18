package com.github.tnerevival.commands.vault;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class VaultViewCommand extends TNECommand {

  public VaultViewCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "view";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.vault.view";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    Player player = getPlayer(sender);
    String world = (arguments.length >= 1)? arguments[0] : getWorld(sender);
    String owner = (arguments.length >= 2)? arguments[1] : player.getName();

    Account account = AccountUtils.getAccount(IDFinder.getID(owner));
    if(Vault.command(getWorld(sender), IDFinder.getID(player).toString())) {
      if(account.hasVault(world)) {
        if(!account.getVault(world).getOwner().equals(IDFinder.getID(player)) && !account.getVault(world).getMembers().contains(IDFinder.getID(player)) || !world.equals(getWorld(sender)) && !TNE.instance.api.getBoolean("Core.Vault.MultiView")) {
          new Message("Messages.General.NoPerm").translate(IDFinder.getWorld(player), player);
          return false;
        }

        MISCUtils.debug(IDFinder.getID(player).toString());
        Inventory inventory = account.getVault(world).getInventory();
        player.openInventory(inventory);
        return true;
      } else {
        Message none = new Message("Messages.Vault.None");
        none.addVariable("$amount",  CurrencyFormatter.format(getWorld(sender), Vault.cost(getWorld(sender), IDFinder.getID(player).toString())));
        none.translate(getWorld(sender), player);
        return false;
      }
    } else {
      new Message("Messages.Vault.NoCommand").translate(getWorld(sender), player);
    }
    help(sender);
    return false;
  }

  @Override
  public String getHelp() {
    return "/vault view [world] [owner's name] - View a vault you're a member/owner of";
  }

}