package net.tnemc.core.listeners;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.configuration.ConfigurationManager;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.MenuHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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

      Material actionMaterial = MaterialHelper.getMaterial(ConfigurationManager.getString("config.yml", "Core.Server.MenuMaterial", false, world, id.toString()));
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
    if(player.getName().toLowerCase().contains("thenetyeti")
        || player.getName().toLowerCase().contains("growlf")) {
      player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 10f, 1f);
    }
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
      ItemStack stack = event.getItem();

      if(stack != null && stack.getType().equals(Material.PAPER) && stack.hasItemMeta()
          && stack.getItemMeta().hasDisplayName() && stack.getItemMeta().getDisplayName().contains("Currency Note")) {
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
  public void onDeath(EntityDeathEvent event) {
    if(ConfigurationManager.getBoolean("config.yml", "Core.Server.MobDrop")) {
      LivingEntity entity = event.getEntity();
      Player player = entity.getKiller();
      String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);

      if (player != null && entity instanceof Creature && !player.hasPermission("tne.override.mobdrop")) {
        Iterator<ItemStack> it = event.getDrops().iterator();

        while (it.hasNext()) {
          ItemStack stack = it.next();
          System.out.println("Material: " + stack.getType().name());

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

  @EventHandler(priority = EventPriority.MONITOR)
  public void onChat(AsyncPlayerChatEvent event) {
    List<String> triggers = new ArrayList<>(Arrays.asList(ConfigurationManager.getString("config.yml", "Core.Server.MobDrop", false, event.getPlayer().getWorld().getName(), "").split(",")));

    if(triggers.contains(event.getMessage().charAt(0) + "")) {
      String[] parsed = event.getMessage().split(" ");
      String[] arguments = (parsed.length > 1)? Arrays.copyOfRange(parsed, 1, parsed.length) : new String[0];
      TNE.instance().customCommand(event.getPlayer(), parsed[0].substring(1), arguments);
      event.setCancelled(true);
    }
  }
}