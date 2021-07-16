package net.tnemc.core.commands.currency;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/16/2017.
 */
public class CurrencyRenameCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 2) {
      String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      String currency = arguments[0];
      String newName = arguments[1];

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        new Message("Messages.General.Disabled").translate(world, sender);
        return false;
      }

      if(!TNE.manager().currencyManager().contains(world, currency)) {
        Message m = new Message("Messages.Money.NoCurrency");
        m.addVariable("$currency", currency);
        m.addVariable("$world", world);
        m.translate(world, sender);
        return false;
      }

      if(TNE.manager().currencyManager().contains(world, newName)) {
        Message m = new Message("Messages.Currency.AlreadyExists");
        m.addVariable("$currency", newName);
        m.addVariable("$world", world);
        m.translate(world, sender);
        return false;
      }

      TNE.manager().currencyManager().rename(world, currency, newName, true);
      Message m = new Message("Messages.Currency.Renamed");
      m.addVariable("$currency", currency);
      m.addVariable("$new_name", newName);
      m.addVariable("$world", world);
      m.translate(world, sender);
      return true;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}