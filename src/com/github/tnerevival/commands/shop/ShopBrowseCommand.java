package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.shops.Shop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Daniel on 8/10/2016.
 */
public class ShopBrowseCommand extends TNECommand {

  public ShopBrowseCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "browse";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.shop.browse";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/shop browse <name> - Browse the specified shop.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      Player player = getPlayer(sender);
      if(Shop.exists(arguments[0], IDFinder.getWorld(getPlayer(sender)))) {
        Shop s = Shop.getShop(arguments[0], IDFinder.getWorld(player));

        if(s.getShoppers() != null && s.getShoppers().size() >= TNE.instance.api.getInteger("Core.Shops.Shoppers")) {
          new Message("Messages.Shop.Shoppers").translate(IDFinder.getWorld(player), player);
          return false;
        }

        ((Player)sender).openInventory(s.getInventory(Shop.canModify(s.getName(), getPlayer(sender))));
        return true;
      }
      new Message("Messages.Shop.None").translate(IDFinder.getWorld(player), player);
      return false;
    }
    help(sender);
    return false;
  }

}