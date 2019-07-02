package net.tnemc.bounty.menu.amountselection;

import net.tnemc.bounty.BountyData;
import net.tnemc.bounty.model.Bounty;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
  public void onClick(String menu, Player player) {
    TNE.debug("=====START Confirm.onClick =====");
    final UUID id = IDFinder.getID(player);
    final String world = WorldFinder.getWorld(player, WorldVariant.ACTUAL);
    final String currency = (String)TNE.menuManager().getViewerData(id, "action_currency");
    final BigDecimal amount = (BigDecimal)TNE.menuManager().getViewerData(id, "action_amount");
    final UUID target = UUID.fromString((String)TNE.menuManager().getViewerData(id, "bounty_target"));
    final String targetName = (String)TNE.menuManager().getViewerData(id, "bounty_target_name");
    final UUID benefactor = UUID.fromString((String)TNE.menuManager().getViewerData(id, "bounty_benefactor"));
    final TNECurrency currencyObj = TNE.manager().currencyManager().get(world, currency);

    if(!TNE.instance().api().hasHoldings(benefactor.toString(), amount, currencyObj, world)) {
      this.message = ChatColor.RED + "You don't have enough funds to set this bounty.";
    } else {

      if(TNE.instance().api().removeHoldings(benefactor.toString(), amount, currencyObj, world)) {
        Bounty bounty = new Bounty(target, id);
        bounty.setCurrencyReward(true);
        bounty.setAmount(amount);
        bounty.setCurrency(currency);
        bounty.setWorld(world);
        bounty.setItemReward("default");

        BountyData.saveBounty(bounty);

        Bukkit.broadcastMessage(ChatColor.YELLOW + "A bounty in the amount of " + CurrencyFormatter.format(currencyObj, world, amount, "") + " has been placed on " + targetName + ". Type /bounty view " + targetName + " to view it.");
      } else {
        this.message = ChatColor.RED + "An error occurred while setting bounty amount.";
      }
    }

    player.sendMessage(message);
    this.message = "";

    super.onClick(menu, player);
  }
}
