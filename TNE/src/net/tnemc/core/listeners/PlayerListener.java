package net.tnemc.core.listeners;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.MenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
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
 * Created by Daniel on 7/12/2017.
 */
public class PlayerListener implements Listener {

  TNE plugin;

  public PlayerListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPickup(final PlayerPickupItemEvent event) {
    TNE.debug("=====START PlayerListener.onPickup =====");
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy) {
      TNEAccount account = TNEAccount.getAccount(id.toString());
      TNE.debug("Account ID: " + account.identifier().toString());
      ItemStack stack = event.getItem().getItemStack();
      TNE.debug("Stack: " + stack.toString());

      Optional<TNECurrency> currency = TNE.manager().currencyManager().currencyFromItem(world, stack);
      TNE.debug("Present: " + currency.isPresent());
      currency.ifPresent((cur)->{
        Bukkit.getScheduler().scheduleSyncDelayedTask(TNE.instance(), ()->ItemCalculations.recalculateItemHoldings(account, world, cur), 5L);
      });
      TNE.manager().addAccount(account);
    }
    TNE.debug("=====END PlayerListener.onPickup =====");
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDrop(final PlayerDropItemEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy) {
      TNEAccount account = TNEAccount.getAccount(id.toString());
      Optional<TNECurrency> currency = TNE.manager().currencyManager().currencyFromItem(world, event.getItemDrop().getItemStack());
      currency.ifPresent((cur)->{
        ItemCalculations.recalculateItemHoldings(account, world, cur);
      });
      TNE.manager().addAccount(account);
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventoryClose(final InventoryCloseEvent event) {
    TNE.debug("=====START PlayerListener.onInventoryClose =====");
    Player player = (Player)event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy) {
      TNE.debug("Account Exists: " + TNE.manager().exists(id));
      TNEAccount account = TNEAccount.getAccount(id.toString());
      ItemCalculations.recalculateItemHoldings(account, world);
      TNE.manager().addAccount(account);
    }
    TNE.debug("=====END PlayerListener.onInventoryClose =====");
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteractEntityEvent(final PlayerInteractEntityEvent event) {
    TNE.debug("=====START PlayerListener.onInteractEntityEvent =====");
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    TNE.debug("World: " + world);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy && event.getRightClicked() instanceof Player) {

      TNE.menuManager().open("main", player);
      TNE.menuManager().setViewerData(id, "action_player", IDFinder.getID((Player)event.getRightClicked()));
      TNE.menuManager().setViewerData(id, "action_world", world);

      if(((Player)event.getRightClicked()).getDisplayName().toLowerCase().contains("thenetyeti")) {
        player.sendMessage(ChatColor.GREEN + "Congratulations you have found the Yediot.");
        MaterialUtils.spawnRandomFirework(player.getLocation());
        MaterialUtils.spawnRandomFirework(player.getLocation());
        MaterialUtils.spawnRandomFirework(player.getLocation());
        MaterialUtils.spawnRandomFirework(player.getLocation());
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
      }

      if(((Player)event.getRightClicked()).getDisplayName().toLowerCase().contains("growlf")) {
        player.sendMessage(ChatColor.GREEN + "Congratulations you have found the disguised Yediot.");
        MaterialUtils.spawnRandomFirework(player.getLocation());
        MaterialUtils.spawnRandomFirework(player.getLocation());
        MaterialUtils.spawnRandomFirework(player.getLocation());
        MaterialUtils.spawnRandomFirework(player.getLocation());
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 10f, 1f);
      }
    }
    TNE.debug("=====END PlayerListener.onInteractEntityEvent =====");
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInteract(final PlayerInteractEvent event) {
    TNE.debug("=====START PlayerListener.onInteract =====");
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    if(player.getDisplayName().toLowerCase().contains("thenetyeti")
        || player.getDisplayName().toLowerCase().contains("growlf")) {
      player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 10f, 1f);
    }
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy) {
      if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
        ItemStack stack = event.getItem();

        if(stack != null && stack.getType().equals(Material.PAPER) && stack.hasItemMeta()) {
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
              player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10f, 1f);
              if(stack.getAmount() > 1) {
                stack.setAmount(stack.getAmount() - 1);
              } else {
                if(player.getInventory().getItemInOffHand() != null
                   && !player.getInventory().getItemInOffHand().getType().equals(Material.AIR)
                   && MaterialUtils.itemsEqual(stack, player.getInventory().getItemInOffHand())) {
                  player.getInventory().setItemInOffHand(null);
                } else {
                  player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
                }
              }
              TNE.debug("=====END PlayerListener.onInteract->proceed");
            }
            note.translate(world, id);
          }
        }
      }
    }
    TNE.debug("=====END PlayerListener.onInteract =====");
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventoryClick(final InventoryClickEvent event) {
    if (event.getInventory().getHolder() instanceof MenuHolder) {
      event.setCancelled(true);

      int slot = event.getRawSlot();
      Menu menu = ((MenuHolder)event.getInventory().getHolder()).getMenuInstance();

      menu.click((Player)event.getWhoClicked(), slot);
    }
  }
}