package net.tnemc.core.listeners;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.MenuHolder;
import org.bukkit.*;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
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
  public void onInteractEntityEvent(final PlayerInteractEntityEvent event) {
    TNE.debug("=====START PlayerListener.onInteractEntityEvent =====");
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);
    TNE.debug("World: " + world);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy && event.getRightClicked() instanceof Player) {

      Material actionMaterial = MaterialHelper.getMaterial(TNE.instance().api().getString("Core.Server.MenuMaterial"));
      Material material = player.getInventory().getItemInMainHand().getType();

      if(actionMaterial == null && material == null
         || material == null && actionMaterial.equals(Material.AIR)
         || material.equals(actionMaterial)) {
        TNE.menuManager().open("main", player);
        TNE.menuManager().setViewerData(id, "action_player", IDFinder.getID((Player)event.getRightClicked()));
        TNE.menuManager().setViewerData(id, "action_world", world);
      }

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
    String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);
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

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPickUp(final PlayerPickupItemEvent event) {
    final ItemStack stack = event.getItem().getItemStack();
    final String world = WorldFinder.getWorld(event.getPlayer(), WorldVariant.BALANCE);

    if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
      Optional<TNECurrency> currency = TNE.manager().currencyManager().currencyFromItem(world, stack);
      currency.ifPresent((cur)->{
        UUID id = IDFinder.getID(event.getPlayer());
        TNE.saveManager().addSkip(id);
        Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), new Runnable() {
          @Override
          public void run() {
            TNEAccount account = TNE.manager().getAccount(id);
            account.setHoldings(world, cur.name(), ItemCalculations.getCurrencyItems(account, cur));
            TNE.manager().addAccount(account);
            TNE.saveManager().removeSkip(id);
          }
        }, 5L);
        event.setCancelled(true);
      });
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDrop(final PlayerDropItemEvent event) {
    final ItemStack stack = event.getItemDrop().getItemStack();
    final String world = WorldFinder.getWorld(event.getPlayer(), WorldVariant.BALANCE);
    Optional<TNECurrency> currency = TNE.manager().currencyManager().currencyFromItem(world, stack);
    currency.ifPresent((cur)->{
      UUID id = IDFinder.getID(event.getPlayer());
      TNE.saveManager().addSkip(id);
      Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), new Runnable() {
        @Override
        public void run() {
          TNEAccount account = TNE.manager().getAccount(id);
          account.setHoldings(world, cur.name(), ItemCalculations.getCurrencyItems(account, cur));
          TNE.manager().addAccount(account);
          TNE.saveManager().removeSkip(id);
        }
      }, 5L);
    });
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDeath(EntityDeathEvent event) {
    if(TNE.instance().api().getBoolean("Core.Server.MobDrop")) {
      LivingEntity entity = event.getEntity();
      Player player = entity.getKiller();
      String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);

      if (player != null && entity instanceof Creature && !player.hasPermission("tne.override.mobdrop")) {
        Iterator<ItemStack> it = event.getDrops().iterator();

        while (it.hasNext()) {
          ItemStack stack = it.next();

          if (stack != null) {
            Optional<TNECurrency> currency = TNE.manager().currencyManager().currencyFromItem(world, stack);

            if (currency.isPresent()) {
              it.remove();
            }
          }
        }
      }
    }
  }
}