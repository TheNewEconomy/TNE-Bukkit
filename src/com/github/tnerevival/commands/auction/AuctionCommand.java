package com.github.tnerevival.commands.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Created by creatorfromhell on 10/17/2016.
 */
public class AuctionCommand extends TNECommand {

  public AuctionCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new AuctionBidCommand(plugin));
    subCommands.add(new AuctionCancelCommand(plugin));
    subCommands.add(new AuctionClaimCommand(plugin));
    subCommands.add(new AuctionEndCommand(plugin));
    subCommands.add(new AuctionInfoCommand(plugin));
    subCommands.add(new AuctionListCommand(plugin));
    subCommands.add(new AuctionStartCommand(plugin));
  }

  @Override
  public String getName() {
    return "auction";
  }

  @Override
  public String[] getAliases() {
    return new String[] { "sauction" };
  }

  @Override
  public String getNode() {
    return "tne.auction.command";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public Boolean activated(String world, String player) {
    return TNE.instance().api().getBoolean("Core.Auctions.Enabled", world, player);
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(MISCUtils.ecoDisabled(getWorld(sender))) {
      Message disabled = new Message("Messages.General.Disabled");
      disabled.translate(getWorld(sender), sender);
      return false;
    }
    return super.execute(sender, command, arguments);
  }
}