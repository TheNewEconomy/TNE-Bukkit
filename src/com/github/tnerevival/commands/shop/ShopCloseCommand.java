package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ShopCloseCommand extends TNECommand {

  public ShopCloseCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "close";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "c" };
  }

  @Override
  public String getNode() {
    return "tne.shop.close";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/shop close <name> - Close the specified shop.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(sender instanceof Player && arguments.length >= 1) {
      Player player = getPlayer(sender);
      if(Shop.exists(arguments[0], MISCUtils.getWorld(getPlayer(sender)))) {
        if(Shop.canModify(arguments[0], (Player)sender)) {
          Shop s = Shop.getShop(arguments[0], MISCUtils.getWorld(getPlayer(sender)));

          if(!s.isAdmin()) {
            for (ShopEntry entry : s.getItems()) {
              ItemStack stack = entry.getItem().toItemStack();
              stack.setAmount(entry.getStock());
              ((Player) sender).getInventory().addItem(stack);
            }
          }

          for(UUID shopper : s.getShoppers()) {
            Player p = MISCUtils.getPlayer(shopper);
            p.closeInventory();
            new Message("Messages.Shop.ClosedBrowse").translate(MISCUtils.getWorld(player), player);
          }
          s.getShoppers().clear();

          TNE.instance.saveManager.versionInstance.deleteShop(s);
          Message hidden = new Message("Messages.Shop.Closed");
          hidden.addVariable("$shop", s.getName());
          hidden.translate(MISCUtils.getWorld(player), player);
          return true;
        }
        new Message("Messages.Shop.Permission").translate(MISCUtils.getWorld(player), player);
        return false;
      }
      new Message("Messages.Shop.None").translate(MISCUtils.getWorld(player), player);
      return false;
    } else {
      help(sender);
    }
    return false;
  }
}