package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ShopCreateCommand extends TNECommand {

  public ShopCreateCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "create";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "+" };
  }

  @Override
  public String getNode() {
    return "tne.shop.create";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/shop create <name> [admin] [hidden] - Create a new shop. [admin] true/false, [hidden] true/false";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      Player player = getPlayer(sender);
      if(!Shop.exists(arguments[0], MISCUtils.getWorld(getPlayer(sender)))) {
        if(arguments[0].length() > 16) {
          new Message("Messages.Shop.Long").translate(MISCUtils.getWorld(player), player);
          return false;
        }

        UUID owner = null;
        if(sender instanceof Player) {
          owner = IDFinder.getID((Player)sender);
        }

        if(arguments.length >= 2 && arguments[1].equalsIgnoreCase("true")) {
          owner = null;
        }

        Shop s = new Shop(arguments[0], MISCUtils.getWorld(getPlayer(sender)));
        s.setOwner(owner);
        if(owner == null) {
          s.setAdmin(true);
        }

        if(!s.isAdmin() && Shop.amount(s.getOwner()) >= TNE.instance.api.getInteger("Core.Shops.Max", s.getWorld(), s.getOwner().toString())) {
          new Message("Messages.Shop.Max").translate(MISCUtils.getWorld(player), player);
          return false;
        }

        if(arguments.length >= 3 && arguments[2].equalsIgnoreCase("true")) {
          s.setHidden(true);
        }

        if(!s.isAdmin() && !AccountUtils.transaction(s.getOwner().toString(), null,
            TNE.instance.api.getDouble("Core.Shops.Cost", s.getWorld(), s.getOwner().toString()),
            TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(getPlayer(sender)))) {

          Message insufficient = new Message("Messages.Money.Insufficient");

          insufficient.addVariable("$amount", CurrencyFormatter.format(
              MISCUtils.getWorld(getPlayer(sender)),
              TNE.instance.api.getDouble("Core.Shops.Cost", s.getWorld(), s.getOwner().toString())
          ));
          return false;
        }
        if(!s.isAdmin()) {
          AccountUtils.transaction(s.getOwner().toString(), null,
              TNE.instance.api.getDouble("Core.Shops.Cost", s.getWorld(), s.getOwner().toString()),
              TransactionType.MONEY_REMOVE, MISCUtils.getWorld(getPlayer(sender)));
        }
        TNE.instance.manager.shops.put(s.getName() + ":" + s.getWorld(), s);
        Message created = new Message("Messages.Shop.Created");
        created.addVariable("$shop", s.getName());
        created.translate(MISCUtils.getWorld(player), player);
        return true;
      }
      new Message("Messages.Shop.Already").translate(MISCUtils.getWorld(player), player);
      return false;
    } else {
      help(sender);
    }
    return false;
  }

}