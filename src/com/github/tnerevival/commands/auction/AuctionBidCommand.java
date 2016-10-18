package com.github.tnerevival.commands.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;

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
public class AuctionBidCommand extends TNECommand {


  public AuctionBidCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "bid";
  }

  @Override
  public String[] getAliases() {
    return new String[]{ "b" };
  }

  @Override
  public String getNode() {
    return "tne.auction.bid";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/auction bid <bid> [lot] - Place a bid on an auction.",
        "<bid> - Amount to bid, [lot] - The auction's lot number.",
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = getWorld(sender);
    if(arguments.length < 1 || !MISCUtils.isDouble(arguments[0], world)) {
      new Message("Messages.Auction.BidRequire").translate(world, sender);
      return false;
    }
    Double bid = Double.valueOf(arguments[0].replace(TNE.instance.api.getString("Core.Currency.Decimal", world), "."));
    Integer lot = (arguments.length >= 2 && MISCUtils.isInteger(arguments[1]))? Integer.valueOf(arguments[1]) : -1;
    if(lot == -1) {
      if(plugin.manager.auctionManager.requireLot(world)) {
        new Message("Messages.Auction.LotRequire").translate(world, sender);
        return false;
      }
      lot = plugin.manager.auctionManager.getLot(world);
    }

    if(!plugin.manager.auctionManager.exists(lot)) {
      Message none = new Message("Messages.Auction.None");
      none.addVariable("$lot", lot + "");
      none.translate(world, sender);
      return false;
    }

    if(!plugin.manager.auctionManager.isActive(lot)) {
      Message none = new Message("Messages.Auction.NotActive");
      none.addVariable("$lot", lot + "");
      none.translate(world, sender);
      return false;
    }

    return plugin.manager.auctionManager.bid(world, lot, getPlayer(sender).getUniqueId(), new TransactionCost(bid));
  }
}
