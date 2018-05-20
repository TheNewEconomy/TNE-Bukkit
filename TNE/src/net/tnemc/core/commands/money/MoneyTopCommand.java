package net.tnemc.core.commands.money;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyTopCommand extends TNECommand {

  public MoneyTopCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "top";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.top";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Top";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Map<String, String> parsed = getArguments(arguments);

    int page = 1;
    int limit = (parsed.containsKey("limit") && MISCUtils.isInteger(parsed.get("limit")))? Integer.valueOf(parsed.get("limit")) : 10;
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    TNECurrency currency = TNE.manager().currencyManager().get(world);

    if(arguments.length >= 1 && parsed.containsKey(String.valueOf(0)) && MISCUtils.isInteger(parsed.get(String.valueOf(0)))) {
      page = Integer.valueOf(parsed.get(String.valueOf(0)));
    }
    
    int max = TNE.saveManager().getTNEManager().getTNEProvider().balanceCount(world, currency.name(), limit);

    if(page > max) page = max;
    TNE.debug("MoneyTopCommand.java(87): Max Pages - " + max);

    Map<UUID, BigDecimal> values = TNE.saveManager().getTNEManager().getTNEProvider().topBalances(world, currency.name(), limit, page);

    Message top = new Message("Messages.Money.Top");
    top.addVariable("$page", page + "");
    top.addVariable("$page_top", max + "");
    top.translate(world, sender);
    for (Map.Entry<UUID, BigDecimal> entry : values.entrySet()) {
      String username = (Bukkit.getOfflinePlayer(entry.getKey()) != null)? Bukkit.getOfflinePlayer(entry.getKey()).getName() : TNE.manager().getAccount(entry.getKey()).displayName();
      sender.sendMessage(TNE.manager().getAccount(entry.getKey()).displayName() + " has " + CurrencyFormatter.format(currency, world, entry.getValue()));
    }
    return true;
  }
}