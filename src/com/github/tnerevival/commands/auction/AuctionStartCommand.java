package com.github.tnerevival.commands.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by creatorfromhell on 10/17/2016.
 */
public class AuctionStartCommand extends TNECommand {


  public AuctionStartCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "start";
  }

  @Override
  public String[] getAliases() {
    return new String[]{ "s" };
  }

  @Override
  public String getNode() {
    return "tne.auction.start";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
      "/auction start [configurations] - Start a new auction.",
      "[item:[data value]] - The name of the item to auction off, defaults to held item",
      "[amount:#] - The amount of <item> to auction off",
      "[start:#] - The starting bid for this item",
      "[increment:#] - The increment in which bids will be increased.",
      "[time:#] - The length(in seconds) this auction will go on for.",
      "[silent:true/false] - Whether or not this auction is a silent auction.",
      "[global:true/false] - Whether or not this auction is global or world-based.",
      "[permission:node] - The permission needed to partake in this auction."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = getWorld(sender);
    Player player = getPlayer(sender);
    Boolean silent = command.equalsIgnoreCase("sauction");
    Integer slot = player.getInventory().getHeldItemSlot();
    Integer amount = 1;
    BigDecimal start = new BigDecimal(TNE.instance().api().getDouble("Core.Auctions.MinStart", world, IDFinder.getID(player).toString()));
    BigDecimal increment = new BigDecimal(TNE.instance().api().getDouble("Core.Auctions.MinIncrement", world, IDFinder.getID(player).toString()));
    Integer time =  TNE.instance().api().getInteger("Core.Auctions.MinTime", world, IDFinder.getID(player).toString());
    Boolean global = !TNE.instance().api().getBoolean("Core.Auctions.AllowWorld", world, IDFinder.getID(player).toString());
    String permission = "";
    ItemStack stack = null;

    for (int i = 0; i < arguments.length; i++) {
      if(arguments[i].contains(":")) {
        String[] split = arguments[i].toLowerCase().split(":");
        switch(split[0]) {
          case "start":
            if(MISCUtils.isDouble(split[1], world) && CurrencyFormatter.translateBigDecimal(split[1], world).compareTo(start) > 0) {
              start = CurrencyFormatter.translateBigDecimal(split[1], world);
            } else {
              new Message("Messages.Auction.InvalidStart").translate(world, player);
              return false;
            }
            break;
          case "increment":
            if(MISCUtils.isDouble(split[1], world)) {
              BigDecimal value = CurrencyFormatter.translateBigDecimal(split[1], world);
              if(value.compareTo(increment) > 0) {
                increment = value;
              }
            }
            break;
          case "time":
            if(MISCUtils.isInteger(split[1])) {
              Integer value = Integer.parseInt(split[1]);
              if(value > time) {
                time = value;
              }
            }
            break;
          case "global":
            global = (MISCUtils.isBoolean(split[1]) || Boolean.parseBoolean(split[1]));
            break;
          case "permission":
            if(player.hasPermission("tne.bypass.auction")) {
              permission = split[1];
            }
            break;
          case "item":
            Material mat = MaterialHelper.getMaterial(split[1]);
            if(mat.equals(Material.AIR)) {
              Message invalidItem = new Message("Messages.Auction.InvalidItem");
              invalidItem.addVariable("$item", split[1]);
              invalidItem.translate(IDFinder.getWorld(player), player);
              return false;
            }
            stack = new ItemStack(mat);
            try {
              Short damage = (split.length == 3)? Short.parseShort(split[2]) : 1;
              stack.setDurability(damage);
            } catch(NumberFormatException e) {
              new Message("Messages.Item.Invalid").translate(world, player);
              return false;
            }
            break;
          case "amount":
            try {
              amount = Integer.parseInt(split[1]);
            } catch(NumberFormatException e) {
              new Message("Messages.Item.InvalidAmount").translate(IDFinder.getWorld(player), player);
              return false;
            }
            break;
          default:
            break;
        }
      }
    }

    if(amount < 1) {
      amount = 1;
    }

    MISCUtils.debug((player == null) + "");

    if(start.compareTo(new BigDecimal(TNE.instance().api().getDouble("Core.Auctions.MaxStart", world, IDFinder.getID(player)))) > 0) {
      start = new BigDecimal(TNE.instance().api().getDouble("Core.Auctions.MaxStart", world, IDFinder.getID(player)));
    }

    if(increment.compareTo(new BigDecimal(TNE.instance().api().getDouble("Core.Auctions.MaxIncrement", world, IDFinder.getID(player)))) > 0) {
      increment = new BigDecimal(TNE.instance().api().getDouble("Core.Auctions.MaxIncrement", world, IDFinder.getID(player)));
    }

    if(time > TNE.instance().api().getInteger("Core.Auctions.MaxTime", world, IDFinder.getID(player))) {
      time = TNE.instance().api().getInteger("Core.Auctions.MaxTime", world, IDFinder.getID(player));
    }

    if(stack == null) {
      stack = player.getInventory().getItem(slot);
    }

    if(stack == null || stack.getType().equals(Material.AIR)) {
      new Message("Messages.Auction.InvalidItem").translate(world, player);
      return false;
    }
    stack.setAmount(amount);

    if(MISCUtils.getItemCount(IDFinder.getID(player), stack) < amount) {
      Message insufficient = new Message("Messages.Auction.NoItem");
      insufficient.addVariable("$amount", amount + "");
      insufficient.addVariable("$item", stack.getType().name() + "");
      return false;
    }

    MISCUtils.debug(stack.getAmount() + "");
    Auction auction = new Auction(IDFinder.getID(player), world);
    auction.setSilent(silent);
    auction.setItem(new SerializableItemStack(0, stack));
    auction.setCost(new TransactionCost(start));
    auction.setIncrement(increment);
    auction.setTime(time);
    auction.setGlobal(global);
    auction.setNode(permission);

    return plugin.manager.auctionManager.add(auction);
  }
}
