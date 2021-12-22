package net.tnemc.discord.command.eco;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.currency.CurrencyEntry;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.charge.TransactionCharge;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.discord.command.DiscordCommand;
import org.bukkit.Bukkit;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

;
;
;
;

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
public class DiscordEcoBalanceCommand extends DiscordCommand {
  @Override
  public String name() {
    return "balance";
  }

  @Override
  public String role() {
    return TNE.instance().api().getString("Discord.Roles.Balance");
  }

  @Override
  public EmbedBuilder help(String command) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setTitle(command + " balance");
    builder.setDescription("Usage: " + command + " balance [world] [currency]");
    final String id = (command.contains("deco"))? "Discord" : "Minecraft";
    builder.addField("Description", "Checks your " + id + ".", false);
    builder.setColor(new Color(190, 57, 0));
    return builder;
  }

  @Override
  public boolean execute(User user, Guild guild, UUID minecraft, boolean fake, MessageChannel channel, String command, String[] arguments) {
    final String world = (arguments.length >= 1)? TNE.instance().sanitizeWorld(arguments[0]) : TNE.instance().defaultWorld;
    final String currencyName = (arguments.length >= 2)? arguments[1] : TNE.manager().currencyManager().get(world).name();
    final UUID id = getID(user.getId(), fake);

    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{

      final TNEAccount account = TNE.manager().getAccount(id);
      TNE.debug("World: " + world);
      TNE.debug("Args Length: " + arguments.length);

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        channel.sendMessage(Message.replaceColours(new Message("Messages.General.Disabled").grab(world, Bukkit.getConsoleSender()), true)).queue();
        return;
      }

      Map<String, BigDecimal> balances = new HashMap<>();

      List<TNECurrency> currencies = new ArrayList<>();
      if(arguments.length >= 2) {
        currencies.add(TNE.manager().currencyManager().get(world, currencyName));
      } else {
        currencies.addAll(TNE.instance().getWorldManager(world).getCurrencies());
      }
      TNE.debug("Pre transactions of MoneyBalanceCommand");
      TransactionResult result = null;

      for(TNECurrency cur : currencies) {
        TNE.debug("BalanceCommand Currency Loop.. Currency: " + cur.name());
        TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("inquiry"));
        transaction.setRecipientCharge(new TransactionCharge(world, cur, BigDecimal.ZERO));
        transaction.setRecipientBalance(new CurrencyEntry(world, cur, account.getHoldings(world, cur)));
        TNE.debug("BalanceCommand RecipientChargeCurrency: " + transaction.recipientCharge().getCurrency().name());
        TNE.debug("BalanceCommand RecipientCharge: " + transaction.recipientCharge().getAmount().toPlainString());
        result = TNE.transactionManager().perform(transaction);
        balances.put(cur.name(), transaction.recipientBalance().getAmount());
      }

      TNE.debug("Balances Size: " + balances.size());
      TNE.debug("Post transactions of MoneyBalanceCommand");
      if(balances.size() > 1) {
        final String w = world;
        Message message = new Message("Messages.Money.HoldingsMulti");
        message.addVariable("$player", account.displayName());
        message.addVariable("$world", world);
        channel.sendMessage(Message.replaceColours(message.grab(world, Bukkit.getConsoleSender()), true)).queue();

        balances.forEach((curName, balance)->{
          Message m = new Message("Messages.Money.HoldingsMultiSingle");
          m.addVariable("$currency", curName);
          m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(w, curName), w, balances.get(curName), id.toString()));
          if(TNE.instance().api().getBoolean("Core.Currency.Info.FormatMoney")) {
            m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(w, curName), w, balances.get(curName), id.toString()));
          } else {
            m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(w, curName), w, balances.get(curName), id.toString()));
          }
          channel.sendMessage(Message.replaceColours(m.grab(world, Bukkit.getConsoleSender()), true)).queue();
        });
        TNE.debug("===END MoneyBalanceCommand ===");
        return;
      }
      Message message = new Message(result.recipientMessage());
      message.addVariable("$player", account.displayName());
      message.addVariable("$world", world);
      message.addVariable("$currency", currencyName);
      if(TNE.instance().api().getBoolean("Core.Currency.Info.FormatMoney")) {
        message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, currencyName), world, balances.get(currencyName), id.toString()));
      } else {
        message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, currencyName), world, balances.get(currencyName), id.toString()));
      }
      channel.sendMessage(Message.replaceColours(message.grab(world, Bukkit.getConsoleSender()), true)).queue();
      TNE.debug("===END MoneyBalanceCommand ===");
    });
    return true;
  }
}