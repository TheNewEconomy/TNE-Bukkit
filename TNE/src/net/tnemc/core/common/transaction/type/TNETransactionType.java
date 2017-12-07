package net.tnemc.core.common.transaction.type;

import net.tnemc.core.TNE;
import net.tnemc.core.economy.transaction.Transaction;
import net.tnemc.core.economy.transaction.TransactionAffected;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 11/8/2017.
 */
public interface TNETransactionType extends TransactionType {

  default boolean voidTransaction(Transaction transaction) {
    boolean proceed = false;

    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
      proceed = TNE.instance().reserve().getAccount(transaction.initiator()).canCharge(transaction.initiatorCharge().copy(true));
    }
    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
      if(affected().equals(TransactionAffected.BOTH) && proceed || affected().equals(TransactionAffected.RECIPIENT)) {
        proceed = TNE.instance().reserve().getAccount(transaction.recipient()).canCharge(transaction.recipientCharge().copy(true));
      }
    }


    if(proceed) {
      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
        TNE.instance().reserve().getAccount(transaction.initiator()).handleCharge(transaction.initiatorCharge().copy(true));
      }
      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
        TNE.instance().reserve().getAccount(transaction.recipient()).handleCharge(transaction.recipientCharge().copy(true));
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
  default TransactionResult perform(Transaction transaction) {
    boolean proceed = false;

    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
      proceed = TNE.instance().api().getAccount(transaction.initiator()).canCharge(transaction.initiatorCharge());
    }
    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
      if(affected().equals(TransactionAffected.BOTH) && proceed || affected().equals(TransactionAffected.RECIPIENT)) {
        proceed = TNE.instance().api().getAccount(transaction.recipient()).canCharge(transaction.recipientCharge());
      }
    }


    if(proceed) {
      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
        TNE.instance().api().getAccount(transaction.initiator()).handleCharge(transaction.initiatorCharge());
      }

      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
        TNE.instance().api().getAccount(transaction.recipient()).handleCharge(transaction.recipientCharge());
      }
      return success();
    }
    return fail();
  }
}
