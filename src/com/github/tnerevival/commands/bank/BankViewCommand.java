package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BankViewCommand extends TNECommand {

  public BankViewCommand(TNE plugin) {
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
    return "tne.bank.view";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    Player player = getPlayer(sender);
    if(BankUtils.command(player.getWorld().getName(), IDFinder.getID(player).toString())) {
      if(BankUtils.hasBank(IDFinder.getID(player))) {
        MISCUtils.debug(IDFinder.getID(player).toString());
        Inventory bankInventory = BankUtils.getBankInventory(IDFinder.getID(player));
        player.openInventory(bankInventory);
      } else {
        Message none = new Message("Messages.Bank.None");
        none.addVariable("$amount",  CurrencyFormatter.format(player.getWorld().getName(), BankUtils.cost(player.getWorld().getName(), IDFinder.getID(player).toString())));
        none.translate(MISCUtils.getWorld(player), player);
      }
    } else {
      new Message("Messages.Bank.NoCommand").translate(MISCUtils.getWorld(player), player);
    }
    return false;
  }

  @Override
  public String getHelp() {
    return "/bank view - view your bank";
  }

}