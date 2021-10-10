package net.tnemc.core.menu.icons.display;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.charge.TransactionCharge;
import net.tnemc.core.common.transaction.result.TransactionResult;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;
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
 * Created by Daniel on 11/5/2017.
 */
public class DisplayCurrencyIcon extends Icon {

  private String currency;

  public DisplayCurrencyIcon(String currency, Integer slot) {
    super(slot, Material.PAPER, currency);

    this.currency = currency;
  }

  @Override
  public void onClick(String menu, Player player, ClickType type) {
    UUID initiatorID = IDFinder.getID(player);

    UUID id = (UUID) TNE.menuManager().getViewerData(initiatorID, "action_player");
    String world = (String)TNE.menuManager().getViewerData(initiatorID, "action_world");
    TNECurrency cur = TNE.manager().currencyManager().get(world, currency);

    TNE.debug("InitiatorID Null: " + (initiatorID == null));
    TNE.debug("ID Null: " + (id == null));
    TNE.debug("World Null: " + (world == null));
    TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(initiatorID), TNE.manager().getAccount(id), world, TNE.transactionManager().getType("inquiry"));
    transaction.setRecipientCharge(new TransactionCharge(world, cur, BigDecimal.ZERO));
    TransactionResult result = transaction.perform();
    Message m = new Message(result.initiatorMessage());
    m.addVariable("$player", IDFinder.getUsername(id.toString()));
    m.addVariable("$world", world);
    m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, transaction.recipientBalance().getCurrency().name()), world, transaction.recipientBalance().getAmount(), ""));
    this.message = m.grab(world, player);

    super.onClick(menu, player, type);
  }
}