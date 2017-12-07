package net.tnemc.core.menu.icons.amountselection;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
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
 * Created by Daniel on 11/7/2017.
 */
public class ConfirmIcon extends Icon {
  public ConfirmIcon(Integer slot) {
    super(slot, Material.STAINED_GLASS_PANE, "Confirm Transaction", (byte)13);
  }

  @Override
  public void onClick(String menu, Player player) {
    TNE.debug("=====START Confirm.onClick =====");
    UUID id = IDFinder.getID(player);
    String world = (String)TNE.menuManager().getViewerData(id, "action_world");
    UUID recipient = (UUID) TNE.menuManager().getViewerData(id, "action_player");
    String currency = (String)TNE.menuManager().getViewerData(id, "action_currency");
    String type = (String)TNE.menuManager().getViewerData(id, "action_type");
    BigDecimal amount = (BigDecimal)TNE.menuManager().getViewerData(id, "action_amount");
    TNECurrency cur = TNE.manager().currencyManager().get(world, currency);

    TNE.debug("recipient Null: " + (recipient == null));
    TNE.debug("ID Null: " + (id == null));
    TNE.debug("World Null: " + (world == null));
    TNETransaction transaction = new TNETransaction(id, recipient, world, TNE.transactionManager().getType(type));
    TransactionCharge recipientCharge = new TransactionCharge(world, cur, amount);
    if(type.equalsIgnoreCase("pay")) {
      transaction.setInitiatorCharge(new TransactionCharge(world, cur, amount));
    }

    if(type.equalsIgnoreCase("give") || type.equalsIgnoreCase("pay")) {
      recipientCharge.setType(TransactionChargeType.GAIN);
    }

    if(type.equalsIgnoreCase("set")) {
      BigDecimal balance = TNE.instance().api().getHoldings(recipient.toString(), world, cur);
      TransactionChargeType recipientChargeType = (balance.compareTo(amount) >= 0)? TransactionChargeType.LOSE
          : TransactionChargeType.GAIN;

      BigDecimal newBalance = (recipientChargeType.equals(TransactionChargeType.GAIN))? amount.subtract(balance) : balance.subtract(amount);
      recipientCharge = new TransactionCharge(world, cur, newBalance, recipientChargeType);
    }
    transaction.setRecipientCharge(recipientCharge);
    TransactionResult result = transaction.perform();
    Message m = new Message(result.initiatorMessage());
    m.addVariable("$player", IDFinder.getUsername(recipient.toString()));
    m.addVariable("$world", world);
    m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, transaction.recipientCharge().getCurrency().name()), world, amount));
    this.message = m.grab(world, player);

    super.onClick(menu, player);
  }
}
