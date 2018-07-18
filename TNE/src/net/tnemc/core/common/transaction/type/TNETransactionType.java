package net.tnemc.core.common.transaction.type;

import net.tnemc.core.TNE;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.Transaction;
import net.tnemc.core.economy.transaction.TransactionAffected;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;

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
      proceed = tneTransaction.getInitiator().canCharge(tneTransaction.initiatorCharge());
    }
    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
      TNE.debug("second if");
      if(affected().equals(TransactionAffected.BOTH) && proceed || affected().equals(TransactionAffected.RECIPIENT)) {
        proceed = tneTransaction.getRecipient().canCharge(tneTransaction.recipientCharge());
      }
    }


    if(proceed) {
      TNE.debug("yeah, proceed");
      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
        TNE.debug("first if");
        tneTransaction.getInitiator().handleCharge(tneTransaction.initiatorCharge());
      }

      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
        TNE.debug("second if");
        tneTransaction.getRecipient().handleCharge(tneTransaction.recipientCharge());
      }
      TNE.debug("=====ENDSUCCESS TNETransactionType.perform =====");
      return success();
    }
    TNE.debug("=====ENDFAIL TNETransactionType.perform =====");
    return fail();
  }
}
