package net.tnemc.core.common.transaction;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.currency.CurrencyEntry;
import net.tnemc.core.common.transaction.charge.TransactionCharge;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.core.common.transaction.type.TransactionType;
import net.tnemc.core.event.transaction.TNETransactionEvent;
import org.bukkit.Bukkit;

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
public class TNETransaction {

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

  public TNETransaction(UUID id, UUID initiator, UUID recipient, String world, TransactionType type, long time) {
    this(id, TNE.manager().getAccount(initiator), TNE.manager().getAccount(recipient), world, type, time);
  }

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
  public boolean voidTransaction() {
    if(!voided()) {
      boolean voided = type().voidTransaction(this);
      setVoided(voided);
      return voided;
    }
    return false;
  }

  public TransactionResult perform() {
    TNE.debug("=====START TNETransaction.perform =====");
    if(initiatorCharge != null) TNE.debug("Initiator Charge: " + initiatorCharge.getAmount());
    if(recipientCharge != null) TNE.debug("Recipient Charge: " + recipientCharge.getAmount());
    CurrencyEntry recipientInitial = null;
    if(recipientCharge != null) {
      TNE.debug("recipientCharge != null");
      if(recipientBalance == null) {
        recipientInitial = this.recipientCharge().getEntry().copy(
            recipient.getHoldings(recipientCharge.getWorld(), recipientCharge.getCurrency())
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
            initiator.getHoldings(initiatorCharge.getWorld(), initiatorCharge.getCurrency())
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
    final TransactionResult result = this.type().perform(this);
    TNETransactionEvent event = new TNETransactionEvent(this, result, !Bukkit.isPrimaryThread());
    Bukkit.getServer().getPluginManager().callEvent(event);
    return result;
  }

  public TNEAccount getInitiator() {
    return initiator;
  }

  public TNEAccount getRecipient() {
    return recipient;
  }

  public String initiator() {
    if(initiator == null) return "None";
    return initiator.identifier().toString();
  }

  public String recipient() {
    if(recipient == null) return "None";
    return recipient.identifier().toString();
  }

  public CurrencyEntry initiatorBalance() {
    return initiatorBalance;
  }

  public void setInitiatorBalance(CurrencyEntry entry) {
    this.initiatorBalance = entry;
  }

  public CurrencyEntry recipientBalance() {
    return recipientBalance;
  }

  public void setRecipientBalance(CurrencyEntry entry) {
    this.recipientBalance = entry;
  }

  public TransactionCharge initiatorCharge() {
    return initiatorCharge;
  }

  public void setInitiatorCharge(TransactionCharge transactionCharge) {
    TNE.debug("=====START TNETransaction.setInitiatorCharge =====");
    this.initiatorCharge = transactionCharge;
    TNE.debug("Amount: " + transactionCharge.getAmount());
    TNE.debug("Amount: " + initiatorCharge.getAmount());
    TNE.debug("=====END TNETransaction.setInitiatorCharge =====");
  }

  public TransactionCharge recipientCharge() {
    return recipientCharge;
  }

  public void setRecipientCharge(TransactionCharge transactionCharge) {
    TNE.debug("=====START TNETransaction.setRecipientCharge =====");
    this.recipientCharge = transactionCharge;
    TNE.debug("Amount: " + transactionCharge.getAmount());
    TNE.debug("Amount: " + recipientCharge.getAmount());
    TNE.debug("=====END TNETransaction.setRecipientCharge =====");
  }

  public boolean voided() {
    return voided;
  }

  public void setVoided(boolean voided) {
    this.voided = voided;
  }

  public UUID transactionID() {
    return uuid;
  }

  public TransactionType type() {
    return type;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public long time() {
    return time;
  }
}