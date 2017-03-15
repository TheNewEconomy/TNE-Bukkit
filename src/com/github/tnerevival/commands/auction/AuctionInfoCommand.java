package com.github.tnerevival.commands.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.inventory.impl.AuctionInventory;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
public class AuctionInfoCommand extends TNECommand {


  public AuctionInfoCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "info";
  }

  @Override
  public String[] getAliases() {
    return new String[]{ "i" };
  }

  @Override
  public String getNode() {
    return "tne.auction.info";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/auction info [lot] - View auction information.",
        "[lot] - The auction's lot number."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = getWorld(sender);
    Integer lot = (arguments.length >= 1 && MISCUtils.isInteger(arguments[0]))? Integer.parseInt(arguments[0]) : -1;
    if(lot == -1) {
      if(plugin.manager.auctionManager.requireLot(world)) {
        new Message("Messages.Auction.LotRequire").translate(world, sender);
        return false;
      }
      lot = plugin.manager.auctionManager.getLot(world);
      AuctionInventory inv = (AuctionInventory)TNE.instance().inventoryManager.generateInventory(
                              Bukkit.createInventory(null, 9, ChatColor.GREEN + "Auction View"),
                              getPlayer(sender), world);
      inv.setLot(lot);
      getPlayer(sender).openInventory(inv.getInventory());
    }

    if(!plugin.manager.auctionManager.exists(lot)) {
      Message none = new Message("Messages.Auction.None");
      none.addVariable("$lot", lot + "");
      none.translate(world, sender);
      return false;
    }
    return true;
  }
}
