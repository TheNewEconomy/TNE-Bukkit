package net.tnemc.core.event.transaction;

import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.event.TNEEvent;

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
 * Created by Daniel on 7/7/2017.
 */
public class TNETransactionEvent extends TNEEvent {

  private TNETransaction transaction;
  private TransactionResult result;

  public TNETransactionEvent(TNETransaction transaction, TransactionResult result) {
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