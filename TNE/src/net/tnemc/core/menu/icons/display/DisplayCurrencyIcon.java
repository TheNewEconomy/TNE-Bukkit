package net.tnemc.core.menu.icons.display;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
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
 * Created by Daniel on 11/5/2017.
 */
public class DisplayCurrencyIcon extends Icon {

  private String currency;

  public DisplayCurrencyIcon(String currency, Integer slot) {
    super(slot, Material.PAPER, currency);

    this.currency = currency;
  }

  @Override
  public void onClick(String menu, Player player) {
    UUID initiatorID = IDFinder.getID(player);

    UUID id = (UUID) TNE.menuManager().getViewerData(initiatorID, "action_player");
    String world = (String)TNE.menuManager().getViewerData(initiatorID, "action_world");
    TNECurrency cur = TNE.manager().currencyManager().get(world, currency);

    TNE.debug("InitiatorID Null: " + (initiatorID == null));
    TNE.debug("ID Null: " + (id == null));
    TNE.debug("World Null: " + (world == null));
    TNETransaction transaction = new TNETransaction(initiatorID, id, world, TNE.transactionManager().getType("inquiry"));
    transaction.setRecipientCharge(new TransactionCharge(world, cur, new BigDecimal(0.0)));
    TransactionResult result = transaction.perform();
    Message m = new Message(result.initiatorMessage());
    m.addVariable("$player", IDFinder.getUsername(id.toString()));
    m.addVariable("$world", world);
    m.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, transaction.recipientBalance().getCurrency().name()), world, transaction.recipientBalance().getAmount()));
    this.message = m.grab(world, player);

    super.onClick(menu, player);
  }
}