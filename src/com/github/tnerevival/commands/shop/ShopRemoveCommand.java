package com.github.tnerevival.commands.shop;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopRemoveCommand extends TNECommand {

	public ShopRemoveCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "-i" };
	}

	@Override
	public String getNode() {
		return "tne.shop.remove";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop remove <shop> [amount:#] [item name[:damage]] [type:(buy/sell)] [trade:name] [gold:amount] - Remove a specific item from your shop. Cost is required if multiple entries exist.");
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
		  Player player = (Player)sender;
			if(Shop.exists(arguments[0], MISCUtils.getWorld(getPlayer(sender)))) {
				if(Shop.canModify(arguments[0], player)) {
					Shop s = Shop.getShop(arguments[0], MISCUtils.getWorld(getPlayer(sender)));
					
					ItemStack item = player.getInventory().getItemInMainHand().clone();
          int amount = 1;
          double cost = 50.00;
          boolean buy = false;
          ItemStack trade = new ItemStack(Material.AIR);

          if(arguments.length >= 2) {
            Material mat;
            for (int i = 1; i < arguments.length; i++) {
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
                  case "trade":
                    mat = MaterialHelper.getMaterial(split[1]);
                    if(mat.equals(Material.AIR)) {
                      Message invalidItem = new Message("Messages.Shop.InvalidTrade");
                      invalidItem.addVariable("$item", split[1]);
                      invalidItem.translate(MISCUtils.getWorld(player), player);
                      return false;
                    }
                    trade = new ItemStack(mat);
                    try {
                      Integer tradeAmount = (split.length == 3)? Integer.parseInt(split[2]) : 1;
                      trade.setAmount(tradeAmount);
                    } catch(NumberFormatException e) {
                      new Message("Messages.Shop.InvalidTradeAmount").translate(MISCUtils.getWorld(player), player);
                      return false;
                    }
                    break;
                  case "type":
                    buy = (!split[1].equals("buy"));
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
            ShopEntry temp = s.getItem(s.getItem(item, cost, buy, trade));
            if(temp.getStock() > 0) {
              //TODO: Move this to a transaction possibly?
              ItemStack tempStack = temp.getItem().toItemStack();
              tempStack.setAmount(temp.getStock());
              player.getInventory().addItem(tempStack);
            }
            s.removeItem(item, cost, buy, trade);
            Message removed = new Message("Messages.Shop.ItemRemoved");
            removed.addVariable("$shop", s.getName());
            removed.addVariable("$item", item.getType().name());
            removed.translate(MISCUtils.getWorld(player), player);
            return true;
          }
          Message wrong = new Message("Messages.Shop.ItemWrong");
          wrong.addVariable("$shop", s.getName());
          wrong.addVariable("$item", item.getType().name());
          wrong.translate(MISCUtils.getWorld(player), player);
          return false;
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