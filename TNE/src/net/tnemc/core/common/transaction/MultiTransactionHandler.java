package net.tnemc.core.common.transaction;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;

import java.math.BigDecimal;
import java.util.Collection;

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
  private String transactionType;
  private BigDecimal amount;
  private TNECurrency currency;
  private String world;
  private TNEAccount initiator;

  public MultiTransactionHandler(Collection<TNEAccount> affected, String transactionType, BigDecimal amount, TNECurrency currency, String world, TNEAccount initiator) {
    this.data = new MultiTransactionData(affected);
    this.transactionType = transactionType.toLowerCase().trim();
    this.amount = amount;
    this.currency = currency;
    this.world = world;
    this.initiator = initiator;
  }

  public void handle(boolean message) {
    data.handle(this);
    if(message) sendMessages();
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
      if(IDFinder.getPlayer(uuid.toString()) != null) {
        String playerVariable = (uuid.equals(getInitiator().identifier()))? String.join(", ", data.getSucceed())
                                : getInitiator().displayName();
        Message msg = new Message(message);
        msg.addVariable("$player", playerVariable);
        msg.addVariable("$world", world);
        msg.addVariable("$currency", currency.name());
        msg.addVariable("$amount", CurrencyFormatter.format(currency, world, amount));
        msg.translate(world, uuid);
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