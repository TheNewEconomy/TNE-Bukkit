package net.tnemc.core.listeners.player;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerInteractListener implements Listener {

  TNE plugin;

  public PlayerInteractListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteract(final PlayerInteractEvent event) {
    TNE.debug("=====START PlayerListener.onInteract =====");
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
      ItemStack stack = event.getItem();

      if(stack != null && stack.hasItemMeta()) {
        if(stack.getItemMeta().hasDisplayName() && stack.getItemMeta().getDisplayName().contains("Currency Note")) {
          Optional<TNETransaction> transaction = TNE.manager().currencyManager().claimNote(id, stack);
          if(transaction == null) TNE.debug("Transaction is null");
          if(!transaction.isPresent()) TNE.debug("Transaction is empty");
          TransactionResult result = null;
          if(transaction.isPresent()) result = transaction.get().perform();
          boolean proceed = result != null && result.proceed();
          String message = (proceed)? "Messages.Money.NoteClaimed" : "Messages.Money.NoteFailed";

          Message note = new Message(message);
          if(proceed) {
            TNE.debug("=====START PlayerListener.onInteract->proceed");
            TNETransaction trans = transaction.get();
            TNE.debug("World: " + trans.getWorld());
            TNE.debug("RAW: " + trans.recipientCharge().toString());
            TNE.debug("Currency: " + trans.recipientCharge().getCurrency().name());
            TNE.debug("Amount: " + trans.recipientCharge().getAmount().toPlainString());
            note.addVariable("$world", trans.getWorld());
            note.addVariable("$currency", trans.recipientCharge().getCurrency().name());
            note.addVariable("$amount", trans.recipientCharge().getAmount().toPlainString());
            note.addVariable("$balance", TNE.instance().api().getHoldings(id.toString(),
                trans.getWorld(), TNECurrency.fromReserve(trans.recipientCharge().getCurrency())).toPlainString()
            );
            event.setCancelled(true);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10f, 1f);
            if(stack.getAmount() > 1) {
              stack.setAmount(stack.getAmount() - 1);
            } else {
              if(player.getInventory().getItemInOffHand() != null
                  && !player.getInventory().getItemInOffHand().getType().equals(Material.AIR)
                  && MaterialUtils.itemsEqual(stack, player.getInventory().getItemInOffHand())) {
                player.getInventory().setItemInOffHand(null);
              } else {
                player.getInventory().clear(player.getInventory().getHeldItemSlot());
              }
            }
            TNE.debug("=====END PlayerListener.onInteract->proceed");
          }
          note.translate(world, id);
        }
      }
    }

    if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.getUniqueId().toString().equals("18e8ba77-a630-47b0-9b47-20c0996b2f72")) {
      player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 10f, 1f);
    }
    TNE.debug("=====END PlayerListener.onInteract =====");
  }
}
