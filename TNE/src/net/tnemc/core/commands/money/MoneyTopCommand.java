package net.tnemc.core.commands.money;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.common.utils.TopBalance;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyTopCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      CommandSender sender = MISCUtils.getSender(provider);
      final Map<String, String> parsed = MISCUtils.getArguments(arguments);

      int page = 1;
      int limit = (parsed.containsKey("limit") && MISCUtils.isInteger(parsed.get("limit")))? Integer.valueOf(parsed.get("limit")) : 10;
      final String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
      final TNECurrency currency = (sender instanceof Player)?
          TNE.manager().currencyManager().get(world, ((Player) sender).getLocation())
          : TNE.manager().currencyManager().get(world);

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        new Message("Messages.General.Disabled").translate(world, sender);
        return;
      }

      if(arguments.length >= 1 && parsed.containsKey(String.valueOf(0)) && MISCUtils.isInteger(parsed.get(String.valueOf(0)))) {
        page = Integer.valueOf(parsed.get(String.valueOf(0)));
      }

      int max = 0;
      try {
        max = TNE.saveManager().getTNEManager().getTNEProvider().balanceCount(world, currency.name(), limit);
      } catch (SQLException e) {
        TNE.debug(e);
      }
      if(max == 0) max = 1;

      if(page > max) page = max;
      TNE.debug("MoneyTopCommand.java(87): Max Pages - " + max);

      LinkedList<TopBalance> values = new LinkedList<>();
      try {
        values = TNE.saveManager().getTNEManager().getTNEProvider().topBalances(world, currency.name(), limit, page);
      } catch (SQLException e) {
        TNE.debug(e);
      }

      LinkedList<String[]> message = new LinkedList<>();

      Message top = new Message("Messages.Money.Top");
      top.addVariable("$page", page + "");
      top.addVariable("$page_top", max + "");
      message.add(top.grabWithNew(world, sender));
      Message topEntry = new Message("Messages.Money.TopEntry");
      Iterator<TopBalance> it = values.iterator();
      while(it.hasNext()) {
        TopBalance entry = it.next();
        topEntry.addVariable("$player", entry.getUsername());
        if(TNE.instance().api().getBoolean("Core.Currency.Info.FormatTop")) {
          topEntry.addVariable("$amount", CurrencyFormatter.format(currency, world, entry.getBalance(), ""));
        } else {
          topEntry.addVariable("$amount", entry.getBalance().toPlainString());
        }
        message.add(topEntry.grabWithNew(world, sender));
      }

      for(String[] s : message) {
        sender.sendMessage(s);
      }
    });
    return true;
  }
}