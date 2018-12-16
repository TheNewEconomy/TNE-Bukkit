package net.tnemc.market.command;

import com.github.tnerevival.TNELib;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MarketSellCommand extends TNECommand {
  public MarketSellCommand(TNELib plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "sell";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.command.market.sell";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/market sell - Brings up the selling UI for the market, using the item in your hand.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    final Player player = getPlayer(sender);
    if(player.getInventory().getItemInMainHand() != null && !player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
      sender.sendMessage(ChatColor.RED + "Invalid item in main hand.");
      return false;
    }
    TNE.menuManager().setViewerData(player.getUniqueId(), "market_item", player.getInventory().getItemInMainHand());
    TNE.menuManager().open("market_offer_amount", player);
    return true;
  }
}
