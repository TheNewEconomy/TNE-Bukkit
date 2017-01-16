package com.github.tnerevival.commands.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class AuctionClaimCommand extends TNECommand {


  public AuctionClaimCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "claim";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.auction.claim";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/auction claim <lot> - Claim an auction reward.",
        "<lot> - The auction's lot number."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = getWorld(sender);
    Player player = getPlayer(sender);
    if(arguments.length < 1 || !MISCUtils.isInteger(arguments[0])) {
      new Message("Messages.Auction.LotRequire").translate(world, sender);
      return false;
    }
    Integer lot = Integer.valueOf(arguments[0]);

    if(!plugin.manager.auctionManager.unclaimed(IDFinder.getID(player)).contains(lot)) {
      Message noClaim = new Message("Messages.Auction.NoClaim");
      noClaim.addVariable("$lot", lot + "");
      noClaim.translate(world, sender);
      return false;
    }

    plugin.manager.auctionManager.claim(lot, IDFinder.getID(player));
    Message processed = new Message("Messages.Auction.Claimed");
    processed.addVariable("$lot", lot + "");
    processed.translate(world, sender);
    return true;
  }
}