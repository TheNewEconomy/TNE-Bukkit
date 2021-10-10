package net.tnemc.core.common.transaction.type;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.TransactionAffected;
import net.tnemc.core.common.transaction.charge.TransactionCharge;
import net.tnemc.core.common.transaction.charge.TransactionChargeType;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.core.common.transaction.tax.TaxEntry;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * Created by creatorfromhell on 8/9/2017.
 * All rights reserved.
 **/
public interface TransactionType {

  /**
   * @return A map containing the account identifier, and matching {@link TaxEntry} for all tax exceptions
   * for this {@link TransactionType}.
   */
  Map<String, TaxEntry> taxExceptions();

  /**
   * @return The name of this transaction type.
   */
  String name();

  /**
   * @return True if the recipient, or initiator contain the console
   */
  boolean console();

  TransactionResult success();

  TransactionResult fail();

  /**
   *
   * @return The {@link TaxEntry} value for the taxes charged to the intiator.
   */
  Optional<TaxEntry> initiatorTax();

  /**
   * Used to calculate a initiator's {@link TaxEntry} after taking into account things such as tax
   * exceptions.
   * @param identifier The identifier of the initiator.
   * @return The final {@link TaxEntry} after lookup is performed.
   */
  default Optional<TaxEntry> calculateInitiatorTax(String identifier) {
    if(taxExceptions().containsKey(identifier)) return Optional.of(taxExceptions().get(identifier));
    return initiatorTax();
  }

  /**
   *
   * @return The {@link TaxEntry} value for the taxes charged to the recipient.
   */
  Optional<TaxEntry> recipientTax();

  /**
   * Used to calculate a recipient's {@link TaxEntry} after taking into account things such as tax
   * exceptions.
   * @param identifier The identifier of the recipient.
   * @return The final {@link TaxEntry} after lookup is performed.
   */
  default Optional<TaxEntry> calculateRecipientTax(String identifier) {
    if(taxExceptions().containsKey(identifier)) return Optional.of(taxExceptions().get(identifier));
    return recipientTax();
  }

  /**
   * @return The {@link TransactionAffected} of this transaction type.
   */
  TransactionAffected affected();

  default boolean voidTransaction(TNETransaction transaction) {
    boolean proceed = false;

    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
      proceed = transaction.getInitiator().canCharge(transaction.initiatorCharge().copy(true));
    }
    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
      if(affected().equals(TransactionAffected.BOTH) && proceed || affected().equals(TransactionAffected.RECIPIENT)) {
        proceed = transaction.getRecipient().canCharge(transaction.recipientCharge().copy(true));
      }
    }


    if(proceed) {
      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
        transaction.getInitiator().handleCharge(transaction.initiatorCharge().copy(true));
      }
      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
        transaction.getRecipient().handleCharge(transaction.recipientCharge().copy(true));
      }
      return true;
    }
    return false;
  }


  /**
   * Performs the actual transaction logic.
   * @param tneTransaction The {@link TNETransaction} to perform.
   * @return The {@link TransactionResult} of this {@link TNETransaction}.
   */
  default TransactionResult perform(TNETransaction tneTransaction) {
    TNE.debug("=====START TNETransactionType.perform =====");
    boolean proceed = false;

    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
      TNE.debug("first if");
      TNE.debug("Account null: " + (tneTransaction.getInitiator() == null));
      TNE.debug("Transaction.initiator null: " + (tneTransaction == null));
      TNE.debug("Transaction.initiatorCharge null: " + (tneTransaction.initiatorCharge() == null));
      proceed = canCharge(tneTransaction.initiatorBalance().getAmount(), tneTransaction.initiatorCharge().getAmount(), tneTransaction.initiatorCharge());
    }
    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
      TNE.debug("second if");
      if(affected().equals(TransactionAffected.BOTH) && proceed || affected().equals(TransactionAffected.RECIPIENT)) {
        proceed = canCharge(tneTransaction.recipientBalance().getAmount(), tneTransaction.recipientCharge().getAmount(), tneTransaction.recipientCharge());
      }
    }

    if(proceed) {
      if(tneTransaction.getInitiator() != null && !tneTransaction.getInitiator().getStatus().getBalance()) proceed = false;
      if(tneTransaction.getRecipient() != null && !tneTransaction.getRecipient().getStatus().getBalance()) proceed = false;
    }


    if(proceed) {
      TNE.debug("yeah, proceed");
      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
        TNE.debug("first if");
        handleCharge(tneTransaction.getInitiator(), tneTransaction.initiatorBalance().getAmount(), tneTransaction.initiatorCharge().getAmount(), tneTransaction.initiatorCharge());
      }

      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
        TNE.debug("second if");
        handleCharge(tneTransaction.getRecipient(), tneTransaction.recipientBalance().getAmount(), tneTransaction.recipientCharge().getAmount(), tneTransaction.recipientCharge());
      }
      TNE.debug("=====ENDSUCCESS TNETransactionType.perform =====");
      return success();
    }
    TNE.debug("=====ENDFAIL TNETransactionType.perform =====");
    return fail();
  }

  default boolean handleCharge(TNEAccount account, BigDecimal balance, BigDecimal amount, TransactionCharge charge) {
    if(amount.compareTo(BigDecimal.ZERO) == 0) return true;
    if(charge.getType().equals(TransactionChargeType.LOSE)) {
      account.removeHoldings(amount, charge.getCurrency(), charge.getWorld());
      return true;
    }
    account.setHoldings(charge.getWorld(), charge.getCurrency().name(), balance.subtract(account.getNonCoreHoldings(charge.getWorld(), charge.getCurrency().name(), false)).add(amount));
    return true;
  }

  default boolean canCharge(BigDecimal balance, BigDecimal amount, TransactionCharge charge) {
    if(charge.getType().equals(TransactionChargeType.LOSE)) {
      return balance.compareTo(amount) >= 0;
    }
    return true;
  }
}