package net.tnemc.discord.command.eco;

import github.scarsz.discordsrv.dependencies.jda.core.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.core.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.core.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.core.entities.User;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.currency.CurrencyEntry;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.discord.command.DiscordCommand;
import org.bukkit.Bukkit;

import java.awt.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/7/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class DiscordEcoSetCommand extends DiscordCommand {
  @Override
  public String name() {
    return "set";
  }

  @Override
  public String role() {
    return TNE.instance().api().getString("Discord.Roles.Set");
  }

  @Override
  public EmbedBuilder help(String command) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setTitle(command + " set");
    builder.setDescription("Usage: " + command + " set <discord id> <amount> [world] [currency]");
    final String id = (command.contains("deco")) ? "Discord" : "Minecraft";
    builder.addField("Description", "Sets the balance of the specified " + id + " user to the amount.", false);
    builder.setColor(new Color(190, 57, 0));
    return builder;
  }

  @Override
  public boolean execute(User user, Guild guild, UUID minecraft, boolean fake, MessageChannel channel, String command, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), () -> {
      if (arguments.length >= 2) {
        final String world = (arguments.length >= 3)? TNE.instance().sanitizeWorld(arguments[2]) : TNE.instance().defaultWorld;
        final String currencyName = (arguments.length >= 4)? arguments[3] : TNE.manager().currencyManager().get(world).name();

        if(!validateDiscordID(arguments[0])) {
          channel.sendMessage("Invalid discord id").queue();
          return;
        }

        final UUID id = getID(arguments[0], fake);
        final TNEAccount account = TNE.manager().getAccount(id);

        if (TNE.instance().getWorldManager(world).isEconomyDisabled()) {
          channel.sendMessage(Message.replaceColours(new Message("Messages.General.Disabled").grab(world, Bukkit.getConsoleSender()), true)).queue();
          return;
        }

        if (!TNE.manager().currencyManager().contains(world, currencyName)) {
          Message m = new Message("Messages.Money.NoCurrency");
          m.addVariable("$currency", currencyName);
          m.addVariable("$world", world);
          channel.sendMessage(Message.replaceColours(m.grab(world, Bukkit.getConsoleSender()), true)).queue();
          return;
        }

        if (!arguments[0].contains(",") && !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Faction")) &&
            !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Town")) &&
            !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Nation")) && IDFinder.getOffline(arguments[0], true) == null) {
          channel.sendMessage(Message.replaceColours(new Message(TNE.transactionManager().getResult("failed").initiatorMessage()).grab(world, Bukkit.getConsoleSender()), true)).queue();
          return;
        }

        TNE.debug("MoneySetCommand Currency: " + currencyName);
        final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);
        final String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
        if (parsed.contains("Messages")) {
          Message max = new Message(parsed);
          max.addVariable("$currency", currency.name());
          max.addVariable("$world", world);
          max.addVariable("$player", IDFinder.getUsername(minecraft.toString()));
          channel.sendMessage(Message.replaceColours(max.grab(world, Bukkit.getConsoleSender()), true)).queue();
          return;
        }

        BigDecimal value = new BigDecimal(parsed);
        if (currency.getTNEMinorTiers().size() <= 0) {
          value = value.setScale(0, BigDecimal.ROUND_FLOOR);
        }

        TNE.debug("Value: " + value.toPlainString());
        final BigDecimal balance = account.getHoldings(world, currency);
        TNE.debug("Balance: " + balance.toPlainString());
        final TransactionChargeType type = (balance.compareTo(value) >= 0) ? TransactionChargeType.LOSE
            : TransactionChargeType.GAIN;
        TNE.debug("ChargeType: " + type.name());

        final BigDecimal newBalance = (type.equals(TransactionChargeType.GAIN)) ? value.subtract(balance) : balance.subtract(value);

        TNE.debug("New Balance: " + newBalance.toPlainString());

        TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(minecraft), account, world, TNE.transactionManager().getType("set"));
        transaction.setRecipientCharge(new TransactionCharge(world, currency, newBalance, type));
        transaction.setRecipientBalance(new CurrencyEntry(world, currency, balance));
        final TransactionResult result = TNE.transactionManager().perform(transaction);


        if (result.proceed() && transaction.recipient() != null && MISCUtils.isOnline(id, world)) {
          Message message = new Message(result.recipientMessage());
          message.addVariable("$player", account.displayName());
          message.addVariable("$world", world);
          message.addVariable("$currency", currencyName);
          message.addVariable("$amount", CurrencyFormatter.format(currency, world, value, id.toString()));
          channel.sendMessage(Message.replaceColours(message.grab(world, Bukkit.getConsoleSender()), true)).queue();
        }

        Message message = new Message(result.initiatorMessage());
        message.addVariable("$player", account.displayName());
        message.addVariable("$world", world);
        message.addVariable("$currency", currencyName);
        message.addVariable("$amount", CurrencyFormatter.format(currency, world, value, arguments[0]));
        channel.sendMessage(Message.replaceColours(message.grab(world, Bukkit.getConsoleSender()), true)).queue();
        return;
      }
      channel.sendMessage(help(command).build()).queue();
    });
    return true;
  }
}