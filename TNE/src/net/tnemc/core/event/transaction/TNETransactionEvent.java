package net.tnemc.core.event.transaction;

import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.core.event.TNEEvent;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class TNETransactionEvent extends TNEEvent {

  private TNETransaction transaction;
  private TransactionResult result;

  public TNETransactionEvent(TNETransaction transaction, TransactionResult result, boolean async) {
    super(async);
    this.transaction = transaction;
    this.result = result;
  }

  public TNETransaction getTransaction() {
    return transaction;
  }

  public void setTransaction(TNETransaction transaction) {
    this.transaction = transaction;
  }

  public TransactionResult getResult() {
    return result;
  }

  public void setResult(TransactionResult result) {
    this.result = result;
  }
}