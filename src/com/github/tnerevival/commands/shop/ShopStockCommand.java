package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Daniel on 10/4/2016.
 */
public class ShopStockCommand extends TNECommand {

  public ShopStockCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "stock";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.shop.stock";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/shop stock <shop> <add/remove> [quantity:#] [amount:#] [item name[:damage]] [type:(sell/buy)] [gold:#]  - Add/Remove stock of an item to your shop for [gold] and/or [trade]. Leave out item name to use currently held item.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(sender instanceof Player && arguments.length >= 2) {
      Player player = getPlayer(sender);
      if(Shop.exists(arguments[0], MISCUtils.getWorld(player))) {
        if(Shop.canModify(arguments[0], (Player)sender)) {
          Shop s = Shop.getShop(arguments[0], MISCUtils.getWorld(getPlayer(sender)));
          ItemStack item = player.getInventory().getItemInMainHand().clone();
          boolean add = (arguments[1].equalsIgnoreCase("add"));
          short damage = 0;
          int amount = 1;
          double cost = 50.00;
          int quantity = 1;
          boolean buy = true;
          ItemStack trade = new ItemStack(Material.AIR);

          if(arguments.length >= 3) {
            Material mat;
            for (int i = 2; i < arguments.length; i++) {
              if(arguments[i].contains(":")) {
                String[] split = arguments[i].toLowerCase().split(":");
                switch(split[0]) {
                  case "gold":
                    try {
                      cost = Double.parseDouble(split[1]);
                    } catch(NumberFormatException e) {
                      new Message("Messages.Shop.InvalidCost").translate(MISCUtils.getWorld(player), player);
                      return false;
                    }
                    break;
                  case "quantity":
                    try {
                      quantity = Integer.parseInt(split[1]);
                    } catch(NumberFormatException e) {
                      new Message("Messages.Shop.InvalidStock").translate(MISCUtils.getWorld(player), player);
                      return false;
                    }
                    break;
                  case "type":
                    buy = !split[1].equals("buy");
                    break;
                  case "amount":
                    try {
                      amount = Integer.parseInt(split[1]);
                    } catch(NumberFormatException e) {
                      new Message("Messages.Shop.InvalidAmount").translate(MISCUtils.getWorld(player), player);
                      return false;
                    }
                    break;
                  default:
                    mat = MaterialHelper.getMaterial(split[0]);
                    if(mat == null || mat.equals(Material.AIR)) {
                      Message invalidItem = new Message("Messages.Shop.ItemInvalid");
                      invalidItem.addVariable("$item", arguments[i]);
                      invalidItem.translate(MISCUtils.getWorld(player), player);
                      return false;
                    }
                    item = new ItemStack(mat);
                    try {
                      item.setDurability(Short.valueOf(split[1]));
                    } catch(NumberFormatException e) {
                      help(sender);
                      return false;
                    }
                    break;
                }
                continue;
              }
              mat = MaterialHelper.getMaterial(arguments[i]);
              if(mat == null || mat.equals(Material.AIR)) {
                Message invalidItem = new Message("Messages.Shop.ItemInvalid");
                invalidItem.addVariable("$item", arguments[i]);
                invalidItem.translate(MISCUtils.getWorld(player), player);
                return false;
              }
              item = new ItemStack(mat);
            }
          }
          item.setAmount(amount);
          if(s.hasItem(item, cost, buy, trade)) {
            if(!add || MISCUtils.getItemCount(player.getUniqueId(), item) >= quantity) {
              int slot = s.getItem(item, cost, buy, trade);
              ItemStack tempStack = item.clone();
              tempStack.setAmount(quantity);
              if(add) {
                player.getInventory().removeItem(tempStack);
                s.getItem(slot).setStock(s.getItem(slot).getStock() + quantity);
              } else {
                if(s.getItem(slot).getStock() - quantity <= 0) {
                  s.removeItem(item, cost, buy);
                } else {
                  s.remove(slot, quantity);
                }
                player.getInventory().addItem(tempStack);
              }
              Message stock = new Message("Messages.Shop.StockModified");
              stock.addVariable("$value", ((add)? "added" : "removed"));
              stock.addVariable("$shop", s.getName());
              stock.addVariable("$amount", quantity + "");
              stock.addVariable("$item", item.getType().name());
              stock.translate(MISCUtils.getWorld(player), player);
              return true;
            }
            Message invalidStock = new Message("Messages.Shop.NotEnough");
            invalidStock.addVariable("$amount", quantity + "");
            invalidStock.addVariable("$item", item.getType().name());
            invalidStock.translate(MISCUtils.getWorld(player), player);
            return false;
          }
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