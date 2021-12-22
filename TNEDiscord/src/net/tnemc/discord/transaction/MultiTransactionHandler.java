package net.tnemc.discord.transaction;

import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.charge.TransactionChargeType;
import net.tnemc.discord.command.DiscordCommand;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/8/2017.
 */
public class MultiTransactionHandler {

  private MultiTransactionData data;
  private MessageChannel result;
  private String transactionType;
  private BigDecimal amount;
  private TNECurrency currency;
  private String world;
  private TNEAccount initiator;

  public MultiTransactionHandler(MessageChannel result, Collection<TNEAccount> affected, String transactionType, BigDecimal amount, TNECurrency currency, String world, TNEAccount initiator) {
    this.result = result;
    this.data = new MultiTransactionData(affected);
    this.transactionType = transactionType.toLowerCase().trim();
    this.amount = amount;
    this.currency = currency;
    this.world = world;
    this.initiator = initiator;

    if(currency.getTNEMinorTiers().size() <= 0) {
      this.amount = this.amount.setScale(0, BigDecimal.ROUND_FLOOR);
    }
  }

  public void handle(boolean message) {
    data.handle(this);
    if(message) sendMessages();
  }

  public static Collection<TNEAccount> parsePlayerArgument(String argument, boolean existing, boolean fake) {
    TNE.debug("EconomyManager.parsePlayerArgument: " + argument);
    argument = argument.trim();
    if(argument.equalsIgnoreCase("all")) return TNE.manager().getAccounts().values();

    List<TNEAccount> accounts = new ArrayList<>();
    if(argument.contains(",")) {
      String[] names = argument.split(",");

      for(String name : names) {
        if(DiscordCommand.validateDiscordID(name)) {
          UUID id = DiscordCommand.getID(name, fake);

          if (TNE.manager().exists(id)) accounts.add(TNE.manager().getAccount(id));
        }
      }
      return accounts;
    }

    if(DiscordCommand.validateDiscordID(argument)) {
      UUID id = DiscordCommand.getID(argument, fake);
      if (existing && !TNE.manager().exists(id)) return null;
      TNEAccount account = TNE.instance().api().getOrCreate(id);
      if (account != null) {
        accounts.add(TNE.manager().getAccount(id));
        return accounts;
      }
    }
    return null;
  }

  public TransactionChargeType getChargeType() {
    if(transactionType.equalsIgnoreCase("take")) return TransactionChargeType.LOSE;
    return TransactionChargeType.GAIN;
  }

  public TransactionChargeType getInitiatorType() {
    if(getChargeType().equals(TransactionChargeType.GAIN)) return TransactionChargeType.LOSE;
    return TransactionChargeType.GAIN;
  }

  public BigDecimal initiatorCost() {
    if(transactionType.equalsIgnoreCase("pay")) {
      return amount;
    }
    return BigDecimal.ZERO;
  }

  public void sendMessages() {
    data.getMessages().forEach((uuid, message)->{
      if(uuid.toString().equalsIgnoreCase(getInitiator().identifier().toString()) ||
         IDFinder.getPlayer(uuid.toString()) != null) {
        String playerVariable = (uuid.equals(getInitiator().identifier()))? String.join(", ", data.getSucceed())
                                : getInitiator().displayName();
        Message msg = new Message(message);
        msg.addVariable("$player", playerVariable);
        msg.addVariable("$world", world);
        msg.addVariable("$currency", currency.name());
        msg.addVariable("$amount", CurrencyFormatter.format(currency, world, amount, playerVariable));

        if(uuid.toString().equalsIgnoreCase(getInitiator().identifier().toString())) {
          result.sendMessage(Message.replaceColours(msg.grab(world, Bukkit.getConsoleSender()), true)).queue();
        } else {
          msg.translate(world, uuid);
        }
      }
    });
  }

  public MultiTransactionData getData() {
    return data;
  }

  public void setData(MultiTransactionData data) {
    this.data = data;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public TNECurrency getCurrency() {
    return currency;
  }

  public void setCurrency(TNECurrency currency) {
    this.currency = currency;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public TNEAccount getInitiator() {
    return initiator;
  }

  public void setInitiator(TNEAccount initiator) {
    this.initiator = initiator;
  }
}