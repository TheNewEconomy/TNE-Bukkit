package net.tnemc.core.commands.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/16/2017.
 */
public class CurrencyRenameCommand extends TNECommand {

  public CurrencyRenameCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "rename";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.currency.rename";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Currency.Rename";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
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
    help(sender);
    return false;
  }
}