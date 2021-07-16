package net.tnemc.core.commands.currency;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class CurrencyListCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    String world = (arguments.length >= 1)? arguments[0] : TNE.instance().defaultWorld;
    StringBuilder builder = new StringBuilder();

    if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
      new Message("Messages.General.Disabled").translate(world, sender);
      return false;
    }

    for(TNECurrency currency : TNE.manager().currencyManager().getWorldCurrencies(world)) {
      if(builder.length() > 0) builder.append(", ");
      builder.append(currency.name());
    }

    Message message = new Message("Messages.Currency.List");
    message.addVariable("$world", world);
    message.addVariable("$currencies", builder.toString());
    message.translate(world, sender);
    return true;
  }
}