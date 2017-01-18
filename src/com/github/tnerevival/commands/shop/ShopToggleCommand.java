package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ShopToggleCommand extends TNECommand {

  public ShopToggleCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "toggle";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "t" };
  }

  @Override
  public String getNode() {
    return "tne.shop.toggle";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/shop toggle <name> - Toggle this shop's visibility. Only whitelisted players can buy from hidden shops.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(sender instanceof Player && arguments.length >= 1) {
      Player player = getPlayer(sender);
      if(Shop.exists(arguments[0], IDFinder.getWorld(player))) {
        if(Shop.canModify(arguments[0], player)) {
          Shop s = Shop.getShop(arguments[0], IDFinder.getWorld(player));
          if(s.isHidden()) {
            s.setHidden(false);
            Message hidden = new Message("Messages.Shop.Visible");
            hidden.addVariable("$shop", s.getName());
            hidden.translate(IDFinder.getWorld(player), player);
          } else {
            s.setHidden(true);

            for(UUID shopper : s.getShoppers()) {
              if(!s.whitelisted(shopper)) {
                Player p = MISCUtils.getPlayer(shopper);
                p.closeInventory();
                Message hidden = new Message("Messages.Shop.MustWhitelist");
                hidden.addVariable("$shop", s.getName());
                hidden.translate(IDFinder.getWorld(player), player);
              }
            }
            Message hidden = new Message("Messages.Shop.Hidden");
            hidden.addVariable("$shop", s.getName());
            hidden.translate(IDFinder.getWorld(player), player);
          }
          return true;
        }
        new Message("Messages.Shop.Permission").translate(IDFinder.getWorld(player), player);
        return false;
      }
      new Message("Messages.Shop.None").translate(IDFinder.getWorld(player), player);
      return false;
    } else {
      help(sender);
    }
    return false;
  }
}