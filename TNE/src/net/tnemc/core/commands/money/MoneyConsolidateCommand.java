package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/18/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MoneyConsolidateCommand implements CommandExecution {

  public MoneyConsolidateCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "consolidate";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.money.consolidate";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Money.Consolidate";
  }

  @Override
  public boolean execute(CommandSender sender, Command command, String label, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      if(!(sender instanceof Player) && arguments.length < 1) {
        MISCUtils.help(sender, label, arguments);
        return;
      }

      if(arguments.length >= 1 && !sender.hasPermission("tne.money.consolidate.other")) {
        MISCUtils.help(sender, label, arguments);
        return;
      }

      final UUID id = (arguments.length >= 1)? IDFinder.getID(arguments[0]) : IDFinder.getID(MISCUtils.getPlayer(sender));
      Player player = Bukkit.getPlayer(id);

      if(player == null) {
        MISCUtils.help(sender, label, arguments);
        return;
      }
      final String world = WorldFinder.getWorld(id, WorldVariant.CONFIGURATION);
      final String balWorld = WorldFinder.getWorld(id, WorldVariant.BALANCE);
      TNEAccount account = TNE.manager().getAccount(id);

      for(TNECurrency currency : TNE.manager().currencyManager().getWorldCurrencies(world)) {
        if(currency.isItem()) {
          ItemCalculations.setItems(id, currency, account.getHoldings(balWorld, currency.getIdentifier(), true, false), player.getInventory(), false, true);
        }
      }

      sender.sendMessage(ChatColor.GOLD + "All item currencies have been consolidated.");
      return;

    });
    return true;
  }
}