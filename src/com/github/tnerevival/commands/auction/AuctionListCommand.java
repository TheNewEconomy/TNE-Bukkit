package com.github.tnerevival.commands.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
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
public class AuctionListCommand extends TNECommand {


  public AuctionListCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String[] getAliases() {
    return new String[]{ "l" };
  }

  @Override
  public String getNode() {
    return "tne.auction.list";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/auction list [global/world] - View a list of auctions.",
        "[global/world] - The scope to list, leave blank to show your auctions"
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = getWorld(sender);


    return false;
  }
}