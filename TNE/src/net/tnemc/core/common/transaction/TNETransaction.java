package net.tnemc.core.common.transaction;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.economy.currency.CurrencyEntry;
import net.tnemc.core.economy.transaction.Transaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;

import java.util.Date;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/17/2017.
 */
public class TNETransaction implements Transaction {

  private UUID uuid;
  private TNEAccount initiator;
  private CurrencyEntry initiatorBalance;
  private TransactionCharge initiatorCharge;
  private TNEAccount recipient;
  private CurrencyEntry recipientBalance;
  private TransactionCharge recipientCharge;
  private TransactionType type;
  private String world;
  private long time;

  private boolean voided;

  public TNETransaction(TNEAccount initiator, TNEAccount recipient, String world, TransactionType type) {
    this(TNE.transactionManager().generateTransactionID(), initiator, recipient, world, type, new Date().getTime());
  }

  public TNETransaction(UUID id, TNEAccount initiator, TNEAccount recipient, String world, TransactionType type, long time) {
    this.uuid = id;
    this.initiator = initiator;
    this.recipient = recipient;
    this.type = type;
    this.world = world;
    this.time = time;
  }

  @Override
  public TransactionResult perform() {
    TNE.debug("=====START TNETransaction.perform =====");
    if(initiatorCharge != null) TNE.debug("Initiator Charge: " + initiatorCharge.getAmount());
    if(recipientCharge != null) TNE.debug("Recipient Charge: " + recipientCharge.getAmount());
    CurrencyEntry recipientInitial = null;
    if(recipientCharge != null) {
      TNE.debug("recipientCharge != null");
      if(recipientBalance == null) {
        recipientInitial = this.recipientCharge().getEntry().copy(
            recipient.getHoldings(recipientCharge.getWorld(), TNECurrency.fromReserve(recipientCharge.getCurrency()))
        );
        this.setRecipientBalance(recipientInitial);
        TNE.debug("setRecipientBalance: " + recipientBalance.getAmount() + " in Currency: " + recipientBalance.getCurrency().name());
      }
      if (recipientCharge != null) TNE.debug("Recipient Charge: " + recipientCharge.getAmount());
    }

    CurrencyEntry initiatorInitial = null;
    if(initiatorCharge != null) {
      if(initiatorBalance == null) {
        initiatorInitial = this.initiatorCharge().getEntry().copy(
            initiator.getHoldings(initiatorCharge.getWorld(), TNECurrency.fromReserve(initiatorCharge.getCurrency()))
        );
        this.setInitiatorBalance(initiatorInitial);
      }
      if (initiatorCharge != null) TNE.debug("Initiator Charge: " + initiatorCharge.getAmount());
    }

    if(initiatorInitial != null) TNE.debug("Initiator: " + initiatorInitial.getAmount());
    if(initiatorCharge != null) TNE.debug("Initiator Charge: " + initiatorCharge.getAmount());
    if(recipientInitial != null) TNE.debug("Recipient: " + recipientInitial.getAmount());
    if(recipientCharge != null) TNE.debug("Recipient Charge: " + recipientCharge.getAmount());

    TNE.debug("=====END TNETransaction.perform =====");
    return this.type().perform(this);
  }

  public TNEAccount getInitiator() {
    return initiator;
  }

  public TNEAccount getRecipient() {
    return recipient;
  }

  @Override
  public String initiator() {
    return initiator.identifier().toString();
  }

  @Override
  public String recipient() {
    return recipient.identifier().toString();
  }

  @Override
  public CurrencyEntry initiatorBalance() {
    return initiatorBalance;
  }

  @Override
  public void setInitiatorBalance(CurrencyEntry entry) {
    this.initiatorBalance = entry;
  }

  @Override
  public CurrencyEntry recipientBalance() {
    return recipientBalance;
  }

  @Override
  public void setRecipientBalance(CurrencyEntry entry) {
    this.recipientBalance = entry;
  }

  @Override
  public TransactionCharge initiatorCharge() {
    return initiatorCharge;
  }

  @Override
  public void setInitiatorCharge(TransactionCharge transactionCharge) {
    this.initiatorCharge = transactionCharge;
  }

  @Override
  public TransactionCharge recipientCharge() {
    return recipientCharge;
  }

  @Override
  public void setRecipientCharge(TransactionCharge transactionCharge) {
    TNE.debug("=====START TNETransaction.setRecipientCharge =====");
    this.recipientCharge = transactionCharge;
    TNE.debug("Amount: " + transactionCharge.getAmount());
    TNE.debug("Amount: " + recipientCharge.getAmount());
    TNE.debug("=====END TNETransaction.setRecipientCharge =====");
  }

  @Override
  public boolean voided() {
    return voided;
  }

  @Override
  public void setVoided(boolean voided) {
    this.voided = voided;
  }

  @Override
  public UUID transactionID() {
    return uuid;
  }

  @Override
  public TransactionType type() {
    return type;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  @Override
  public long time() {
    return time;
  }
}