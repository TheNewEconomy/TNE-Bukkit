package com.github.tnerevival.commands.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

/**
 * Created by Daniel on 10/17/2016.
 */
public class AuctionCommand extends TNECommand {

  public AuctionCommand(TNE plugin) {
    super(plugin);
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
    return TNE.instance.api.getBoolean("Core.Auctions.Enabled", world, player);
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    return super.execute(sender, command, arguments);
  }
}