package com.github.tnerevival.commands.shop;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.serializable.SerializableItemStack;

public class ShopAddCommand extends TNECommand {

	public ShopAddCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "add";
	}

	@Override
	public String[] getAliases() {
		return new String[] { "+i" };
	}

	@Override
	public String getNode() {
		return "tne.shop.add";
	}

	@Override
	public boolean console() {
		return false;
	}

	@Override
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "/shop add <shop> [amount] [item name:stock] [gold:#] [trade:name:amount(default 1)]  - Add a new item to your shop for [cost] and/or [trade]. Leave out item name to use currently held item.");
	}
	
	@Override
	public boolean execute(CommandSender sender, String[] arguments) {
		if(sender instanceof Player && arguments.length >= 1) {
		  if(Shop.exists(arguments[0])) {
		    if(Shop.canModify(arguments[0], (Player)sender)) {
          Player p = (Player)sender;
          Shop s = Shop.getShop(arguments[0]);
          ItemStack item = p.getInventory().getItemInMainHand().clone();
          item.setAmount(1);
          double cost = 50.0;
          int stock = 0;
          ItemStack trade = null;

          if(arguments.length >= 2) {
            for (int i = 1; i < arguments.length; i++) {
              if(arguments[i].contains(":")) {
                String[] split = arguments[i].split(":");
                Material mat;
                switch(split[0]) {
                  case "gold":
                    try {
                      cost = Double.parseDouble(split[1]);
                    } catch(NumberFormatException e) {
                      //TODO: Send invalid cost format message.
                      return false;
                    }
                    break;
                  case "trade":
                    mat = MaterialHelper.getMaterial(split[1]);
                    if(mat.equals(Material.AIR)) {
                      //TODO: Send trade item invalid.
                      return false;
                    }
                    trade = new ItemStack(mat);
                    try {
                      Integer amount = (split.length == 3)? Integer.parseInt(split[2]) : 1;
                      trade.setAmount(amount);
                    } catch(NumberFormatException e) {
                      //TODO: Invalid trade amount.
                      return false;
                    }
                    break;
                  default:
                    mat = MaterialHelper.getMaterial(split[0]);
                    if(mat.equals(Material.AIR)) {
                      //TODO: Send invalid item for shop.
                      return false;
                    }
                    item = new ItemStack(mat);
                    try {
                      stock = Integer.parseInt(split[1]);
                    } catch(NumberFormatException e) {
                      //TODO: Invalid stock amount.
                      return false;
                    }
                }
                continue;
              }
              try {
                Integer.parseInt(arguments[i]);
              } catch(NumberFormatException e) {
                help(sender);
                return false;
              }
            }
          }
          if(MISCUtils.getItemCount(p.getUniqueId(), item) >= stock) {
            if(s.addItem(new ShopEntry(new SerializableItemStack(1, item), cost, stock, new SerializableItemStack(1, trade)))) {
              MISCUtils.setItemCount(p.getUniqueId(), item, (MISCUtils.getItemCount(p.getUniqueId(), item) - stock));
              Message added = new Message("Messages.Shop.ItemAdded");
              added.addVariable("$shop", s.getName());
              added.addVariable("$item", item.getType().name());
              getPlayer(sender).sendMessage(added.translate());
              return true;
            }
            Message wrong = new Message("Messages.Shop.ItemWrong");
            wrong.addVariable("$shop", s.getName());
            wrong.addVariable("$item", item.getType().name());
            getPlayer(sender).sendMessage(wrong.translate());
            return false;
          }
          //TODO: Player doesn't have X amount of item.
          return false;
        }
        getPlayer(sender).sendMessage(new Message("Messages.Shop.Permission").translate());
        return false;
      }
      getPlayer(sender).sendMessage(new Message("Messages.Shop.None").translate());
      return false;
    } else {
      help(sender);
    }
		return false;
	}
}