package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/27/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MoneySetAllCommand extends TNECommand {

  public MoneySetAllCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "setall";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "=a"
    };
  }

  @Override
  public String getNode() {
    return "tne.money.setall";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.SetAll";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, ()->{
      if(arguments.length >= 1) {
        final String world = (arguments.length >= 2)? TNE.instance().getWorldManager(arguments[1]).getBalanceWorld() : WorldFinder.getWorld(sender, WorldVariant.BALANCE);

        if(world == null) {
          help(sender);
          return;
        }

        if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
          new Message("Messages.General.Disabled").translate(world, sender);
          return;
        }
        final String parsed = CurrencyFormatter.parseAmount(TNE.manager().currencyManager().get(world), world, arguments[0]);

        BigDecimal value = new BigDecimal(parsed);
        if(TNE.manager().currencyManager().get(world).getTNEMinorTiers().size() <= 0) {
          value = value.setScale(0, BigDecimal.ROUND_FLOOR);
        }

        try {
          TNE.saveManager().getTNEManager().getTNEProvider().setAllBalance(world, value);
        } catch (SQLException ignore) {
          help(sender);
          return;
        }

        Message message = new Message("Messages.Money.SetAll");
        message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world), world, value, arguments[0]));
        message.addVariable("$world", world);
        message.translate(world, IDFinder.getID(sender));
        return;
      }
      help(sender);
    });
    return true;
  }
}