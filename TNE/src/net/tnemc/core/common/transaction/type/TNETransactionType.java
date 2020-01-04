package net.tnemc.core.common.transaction.type;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.Transaction;
import net.tnemc.core.economy.transaction.TransactionAffected;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/8/2017.
 */
public interface TNETransactionType extends TransactionType {

  default boolean voidTransaction(TNETransaction transaction) {
    boolean proceed = false;

    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
      proceed = transaction.getInitiator().canCharge(transaction.initiatorCharge().copy(true)).success();
    }
    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
      if(affected().equals(TransactionAffected.BOTH) && proceed || affected().equals(TransactionAffected.RECIPIENT)) {
        proceed = transaction.getRecipient().canCharge(transaction.recipientCharge().copy(true)).success();
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
   * @param transaction The {@link Transaction} to perform.
   * @return The {@link TransactionResult} of this {@link Transaction}.
   */
  @Override
  default TransactionResult perform(Transaction transaction) {
    final TNETransaction tneTransaction = (TNETransaction)transaction;
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
