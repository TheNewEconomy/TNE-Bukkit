package net.tnemc.bounty.command;

import net.tnemc.bounty.BountyData;
import net.tnemc.bounty.BountyModule;
import net.tnemc.bounty.model.Bounty;
import net.tnemc.bounty.model.BountyHunter;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.item.SerialItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BountyPlaceCommand extends TNECommand {
  public BountyPlaceCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "place";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.bounty.place";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/bounty place <player> [item(uses the item in your hand as a reward)/currency(defaults to currency)] - Opens the bounty creation menu to set a bounty on the specified player.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    final UUID id = IDFinder.getID(sender);

    final String type = (arguments.length >= 2)? arguments[1].trim().toLowerCase() : "currency";

    if(!type.equalsIgnoreCase("item") && !type.equals("currency")) {
      sender.sendMessage(ChatColor.RED + "Type must be either item or currency.");
      return false;
    }

    final OfflinePlayer player = Bukkit.getOfflinePlayer(arguments[0]);
    final UUID target = IDFinder.getID(player);

    if(BountyData.hasBounty(target)) {
      sender.sendMessage(ChatColor.RED + "That player already has a bounty on them.");
      return false;
    }

    final long delay = BountyModule.instance().getHunterFileConfiguration().getInt("Bounty.Placing.Limit") * 1000;

    if(delay > 0) {
      BountyHunter hunter = BountyData.getHunter(id);
      if(hunter.getLastBounty() == 0) return true;
      final long difference = new Date().getTime() - hunter.getLastBounty();

      if(difference < delay) {
        sender.sendMessage(ChatColor.RED + "Your /bounty place is still on cooldown.");
        return false;
      }
    }

    if(type.equalsIgnoreCase("item")) {

      if(!BountyModule.instance().getHunterFileConfiguration().getBool("Bounty.Placing.Item.Enabled")) {
        sender.sendMessage(ChatColor.RED + "Item Bounties are disabled in this server.");
        return false;
      }

      final ItemStack stack = BountyData.getItemInHand(getPlayer(sender));
      if(stack == null || stack.getType() == Material.AIR) {
        sender.sendMessage(ChatColor.RED + "Place your bounty reward item in your main hand.");
        return false;
      }

      Bounty bounty = new Bounty(target, id);
      bounty.setCurrencyReward(false);
      bounty.setCurrency("default");
      bounty.setWorld("world");
      bounty.setItemReward(new SerialItem(stack).serialize());

      BountyData.saveBounty(bounty);
      ItemCalculations.removeItem(stack, getPlayer(sender).getInventory());
      Bukkit.broadcastMessage(ChatColor.YELLOW + "An item reward-based bounty has been placed on " + player.getName() + ". Type /bounty view " + player.getName() + " to view it.");
      return true;
    }

    if(!BountyModule.instance().getHunterFileConfiguration().getBool("Bounty.Placing.Currency.Enabled")) {
      sender.sendMessage(ChatColor.RED + "Currency Bounties are disabled in this server.");
      return false;
    }

    TNE.menuManager().open("bounty_currency_selection", getPlayer(sender));
    TNE.menuManager().setViewerData(id, "bounty_target", target.toString());
    TNE.menuManager().setViewerData(id, "bounty_target_name", player.getName());
    TNE.menuManager().setViewerData(id, "bounty_benefactor", id.toString());
    return true;
  }
}