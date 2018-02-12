package net.tnemc.core.common.transaction.type;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.economy.transaction.Transaction;
import net.tnemc.core.economy.transaction.TransactionAffected;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.economy.transaction.type.TransactionType;
import org.bukkit.Bukkit;

import java.util.UUID;

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
    TNE.debug("=====START TNETransactionType.perform =====");
    boolean proceed = false;

    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
      TNE.debug("first if");
      TNE.debug("Account null: " + (TNE.instance().api().getAccount(transaction.initiator()) == null));
      TNE.debug("Transaction.initiator null: " + (transaction == null));
      TNE.debug("Transaction.initiatorCharge null: " + (transaction.initiatorCharge() == null));
      proceed = TNE.instance().api().getAccount(transaction.initiator()).canCharge(transaction.initiatorCharge());
    }
    if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
      TNE.debug("second if");
      if(affected().equals(TransactionAffected.BOTH) && proceed || affected().equals(TransactionAffected.RECIPIENT)) {
        proceed = TNE.instance().api().getAccount(transaction.recipient()).canCharge(transaction.recipientCharge());
      }
    }


    if(proceed) {
      TNE.debug("yeah, proceed");
      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.INITIATOR)) {
        TNE.debug("first if");
        UUID id = IDFinder.getID(transaction.initiator());
        if(MISCUtils.isOnline(id)) {
          TNE.saveManager().addSkip(id);
          Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), new Runnable() {
            @Override
            public void run() {
              TNE.manager().getAccount(id).saveItemCurrency(WorldFinder.getWorld(id, WorldVariant.BALANCE));
              TNE.saveManager().removeSkip(id);
            }
          }, 5L);
        }
        TNE.instance().api().getAccount(transaction.initiator()).handleCharge(transaction.initiatorCharge());
      }

      if(affected().equals(TransactionAffected.BOTH) || affected().equals(TransactionAffected.RECIPIENT)) {
        TNE.debug("second if");
        UUID id = IDFinder.getID(transaction.recipient());
        if(MISCUtils.isOnline(id)) {
          TNE.saveManager().addSkip(id);
          Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), new Runnable() {
            @Override
            public void run() {
              TNE.manager().getAccount(id).saveItemCurrency(WorldFinder.getWorld(id, WorldVariant.BALANCE));
              TNE.saveManager().removeSkip(id);
            }
          }, 5L);
        }
        TNE.instance().api().getAccount(transaction.recipient()).handleCharge(transaction.recipientCharge());
      }
      TNE.debug("=====ENDSUCCESS TNETransactionType.perform =====");
      return success();
    }
    TNE.debug("=====ENDFAIL TNETransactionType.perform =====");
    return fail();
  }
}
