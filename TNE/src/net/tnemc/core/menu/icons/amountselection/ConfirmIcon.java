package net.tnemc.core.menu.icons.amountselection;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.charge.TransactionCharge;
import net.tnemc.core.common.transaction.charge.TransactionChargeType;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/7/2017.
 */
public class ConfirmIcon extends Icon {
  public ConfirmIcon(Integer slot) {
    super(slot, TNE.item().build("GREEN_STAINED_GLASS_PANE"), "Confirm Transaction");
  }

  @Override
  public void onClick(String menu, Player player, ClickType clickType) {
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
    TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(id), TNE.manager().getAccount(recipient), world, TNE.transactionManager().getType(type));
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
    m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, transaction.recipientCharge().getCurrency().name()), world, amount, recipient.toString()));
    this.message = m.grab(world, player);

    super.onClick(menu, player, clickType);
  }
}
