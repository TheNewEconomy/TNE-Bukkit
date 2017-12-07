package net.tnemc.core.common.transaction;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;

import java.math.BigDecimal;
import java.util.Collection;
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
public class MultiTransactionHandler {

  private MultiTransactionData data;
  private String transactionType;
  private BigDecimal amount;
  private TNECurrency currency;
  private String world;
  private UUID initiator;

  public MultiTransactionHandler(Collection<TNEAccount> affected, String transactionType, BigDecimal amount, TNECurrency currency, String world, UUID initiator) {
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

  public BigDecimal initiatorCost() {
    if(transactionType.equalsIgnoreCase("pay")) {
      return amount.multiply(new BigDecimal(data.getAffected().size()));
    }
    return new BigDecimal(0.0);
  }

  public void sendMessages() {
    data.getMessages().forEach((uuid, message)->{
      if(IDFinder.getPlayer(uuid.toString()) != null) {
        String playerVariable = (uuid.equals(getInitiator()))? String.join(", ", data.getSucceed())
                                : IDFinder.getUsername(getInitiator().toString());
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

  public UUID getInitiator() {
    return initiator;
  }

  public void setInitiator(UUID initiator) {
    this.initiator = initiator;
  }
}