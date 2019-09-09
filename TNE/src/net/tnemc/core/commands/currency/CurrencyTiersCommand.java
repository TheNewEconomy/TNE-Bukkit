package net.tnemc.core.commands.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.TNETier;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class CurrencyTiersCommand extends TNECommand {

  public CurrencyTiersCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "tiers";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "t"
    };
  }

  @Override
  public String node() {
    return "tne.currency.tiers";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Currency.Tiers";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = (arguments.length >= 2)? arguments[1] : TNE.instance().defaultWorld;
    String currencyName = (arguments.length >= 1)? arguments[0] : TNE.manager().currencyManager().get(world).name();
    TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

    if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
      new Message("Messages.General.Disabled").translate(world, sender);
      return false;
    }

    if(!TNE.manager().currencyManager().contains(world, currencyName)) {
      Message m = new Message("Messages.Money.NoCurrency");
      m.addVariable("$currency", currencyName);
      m.addVariable("$world", world);
      m.translate(world, sender);
      return false;
    }

    StringBuilder major = new StringBuilder();
    StringBuilder minor = new StringBuilder();

    for(TNETier tier : currency.getTNEMajorTiers().values()) {
      if(major.length() > 0) major.append(", ");
      major.append(tier.singular());
    }

    for(TNETier tier : currency.getTNEMinorTiers().values()) {
      if(minor.length() > 0) minor.append(", ");
      minor.append(tier.singular());
    }
    Message message = new Message("Messages.Currency.Tiers");
    message.addVariable("$currency", world);
    message.addVariable("$major_tiers", major.toString());
    message.addVariable("$minor_tiers", minor.toString());
    message.translate(world, sender);
    return true;
  }
}