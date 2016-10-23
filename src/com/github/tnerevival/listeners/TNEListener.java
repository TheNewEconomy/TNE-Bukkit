package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.event.object.TNEObjectInteractionEvent;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Daniel on 10/12/2016.
 */
public class TNEListener implements Listener {

  TNE plugin;

  public TNEListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onObjectInteract(TNEObjectInteractionEvent event) {

    if(event.isCancelled()) return;

    String id = MISCUtils.getID(event.getPlayer()).toString();
    String world = MISCUtils.getWorld(event.getPlayer());
    double cost = event.getType().getCost(event.getIdentifier(), MISCUtils.getWorld(event.getPlayer()), MISCUtils.getID(event.getPlayer()).toString());
    String message = event.getType().getCharged();

    if(cost != 0.0 && !event.isCancelled()) {
      if(cost > 0.0) {
        if(AccountUtils.transaction(id, null, cost, TransactionType.MONEY_INQUIRY, world)) {
          AccountUtils.transaction(id, null, cost, TransactionType.MONEY_REMOVE, world);
        } else {
          event.setCancelled(true);
          Message insufficient = new Message("Messages.Money.Insufficient");
          insufficient.addVariable("$amount", CurrencyFormatter.format(world, AccountUtils.round(cost)));
          insufficient.translate(world, event.getPlayer());
          return;
        }
      } else {
        AccountUtils.transaction(id, null, cost, TransactionType.MONEY_GIVE, world);
        message = event.getType().getPaid();
      }

      String newName = event.getIdentifier() + ((event.getAmount() > 1 )? "\'s" : "");

      Message m = new Message(message);
      m.addVariable("$amount", CurrencyFormatter.format(world, AccountUtils.round(cost)));
      m.addVariable("$stack_size", event.getAmount() + "");
      m.addVariable("$item", newName);
      m.translate(world, event.getPlayer());
    }
  }
}
