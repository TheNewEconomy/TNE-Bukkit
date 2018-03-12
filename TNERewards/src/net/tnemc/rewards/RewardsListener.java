package net.tnemc.rewards;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.rewards.event.InteractionType;
import net.tnemc.rewards.event.TNEObjectInteractionEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 10/21/2017.
 */
public class RewardsListener implements ModuleListener {

  private TNE plugin;

  public RewardsListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onEnchant(final EnchantItemEvent event) {
    if (TNE.instance().api().getBoolean("Materials.Enabled", WorldFinder.getWorld(event.getEnchanter(), WorldVariant.CONFIGURATION), IDFinder.getID(event.getEnchanter()))) {
      if (event.getItem() != null && !event.getItem().getType().equals(Material.AIR)) {

        ItemStack result = event.getItem();
        String name = result.getType().name();
        BigDecimal cost = InteractionType.ENCHANT.getCost(name, WorldFinder.getWorld(event.getEnchanter(), WorldVariant.CONFIGURATION), IDFinder.getID(event.getEnchanter()).toString());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Enchanting Cost: " + ChatColor.GOLD + cost);

        ItemMeta meta = result.getItemMeta();
        meta.setLore(lore);

        for (Enchantment e : event.getEnchantsToAdd().keySet()) {
          meta.addEnchant(e, event.getEnchantsToAdd().get(e), false);
        }

        result.setItemMeta(meta);
        event.getInventory().setItem(0, result);
      }
    }
  }

  @EventHandler
  public void onBreak(final BlockBreakEvent event) {
    String world = event.getBlock().getWorld().getName();
    String name = event.getBlock().getType().name();

    if(!event.isCancelled()) {
      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(event.getPlayer(), new ItemStack(event.getBlock().getType()), name, InteractionType.CRAFTING);
      Bukkit.getServer().getPluginManager().callEvent(e);

      if (e.isCancelled()) {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onPlace(final BlockPlaceEvent event) {
    String name = event.getBlock().getType().name();

    TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(event.getPlayer(), new ItemStack(event.getBlock().getType()), name, InteractionType.PLACING);
    Bukkit.getServer().getPluginManager().callEvent(e);

    if(e.isCancelled()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onSmelt(final FurnaceSmeltEvent event) {
    if (TNE.instance().api().getBoolean("Materials.Enabled", TNE.instance().defaultWorld, "")) {
      if (event.getResult() != null && !event.getResult().getType().equals(Material.AIR)) {
        String name = event.getBlock().getType().name();
        if (event.getBlock().getState() instanceof Furnace) {
          Furnace f = (Furnace) event.getBlock().getState();

          int amount = (f.getInventory().getResult() != null) ? f.getInventory().getResult().getAmount() : 1;

          BigDecimal cost = InteractionType.SMELTING.getCost(name, event.getBlock().getWorld().toString(), "").multiply(new BigDecimal(amount));

          List<String> lore = new ArrayList<>();
          lore.add(ChatColor.WHITE + "Smelting Cost: " + ChatColor.GOLD + cost);

          ItemStack result = event.getResult();
          ItemMeta meta = result.getItemMeta();
          meta.setLore(lore);

          result.setItemMeta(meta);
          event.setResult(result);
        }
      }
    }
  }

  @EventHandler
  public void onPreCraft(final PrepareItemCraftEvent event) {
    if(event.getInventory().getResult() != null) {
      Player player = (Player)event.getView().getPlayer();

      if (TNE.instance().api().getBoolean("Materials.Enabled", WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player))) {
        String name = event.getInventory().getResult().getType().name();
        BigDecimal cost = InteractionType.CRAFTING.getCost(name, WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player).toString());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Crafting Cost: " + ChatColor.GOLD + cost);

        ItemStack result = event.getInventory().getResult();
        ItemMeta meta = result.getItemMeta();
        meta.setLore(lore);
        result.setItemMeta(meta);
        event.getInventory().setResult(result);
      }
    }
  }

  @EventHandler
  public void onCraft(final CraftItemEvent event) {

    Player player = (Player) event.getWhoClicked();

    if (TNE.instance().api().getBoolean("Materials.Enabled", WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), IDFinder.getID(player))) {

      String name = event.getInventory().getResult().getType().name();
      ItemStack result = event.getCurrentItem().clone();
      ItemMeta meta = result.getItemMeta();
      List<String> newLore = new ArrayList<>();
      for(String s : meta.getLore()) {
        if(!s.contains("Crafting Cost")) {
          newLore.add(s);
        }
      }
      meta.setLore(newLore);
      result.setItemMeta(meta);

      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(player, result, name, InteractionType.CRAFTING);
      Bukkit.getServer().getPluginManager().callEvent(e);

      if (e.isCancelled()) {
        event.setCancelled(true);
        return;
      }
      if(event.getClick().isShiftClick()) {
        final Player p = player;
        final ItemStack stack = result;
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
          @Override
          public void run() {
            ItemStack[] contents = p.getInventory().getContents().clone();
            for(int i = 0; i < contents.length; i++) {
              if(contents[i] != null && contents[i].getType().equals(stack.getType())) {
                ItemStack cloneStack = contents[i].clone();
                ItemMeta meta = cloneStack.getItemMeta();
                List<String> newLore = new ArrayList<>();
                if(meta.getLore() != null) {
                  for (String s : meta.getLore()) {
                    if (!s.contains("Crafting Cost")) {
                      newLore.add(s);
                    }
                  }
                }
                meta.setLore(newLore);
                cloneStack.setItemMeta(meta);
                contents[i] = cloneStack;
              }
            }
            p.getInventory().setContents(contents);
          }
        }, 5L);
      } else {
        event.setCurrentItem(result);
      }
    }
  }

  @EventHandler
  public void onClick(final InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) return;

    final Player player = (Player) event.getWhoClicked();
    final boolean bottom = (event.getRawSlot() != event.getView().convertSlot(event.getRawSlot()));
    final Inventory inventory = (bottom) ? event.getView().getBottomInventory() : event.getView().getTopInventory();

    if (inventory.getType().equals(InventoryType.ENCHANTING) || inventory.getType().equals(InventoryType.FURNACE)) {
      TNE.debug("Inventory is enchanting OR smelting");
      InteractionType intType = (inventory.getType().equals(InventoryType.ENCHANTING)) ? InteractionType.ENCHANT : InteractionType.SMELTING;
      String item = (event.getCurrentItem() != null) ? event.getCurrentItem().getType().name() : Material.AIR.name();
      TNEObjectInteractionEvent e = new TNEObjectInteractionEvent(player, event.getCurrentItem(), item, intType);
      Bukkit.getServer().getPluginManager().callEvent(e);

      TNE.debug("Event called");
      RewardsModule.handleObjectEvent(e);

      TNE.debug("Event handled internally");
      if (e.isCancelled()) {
        TNE.debug("Event Cancelled");
        event.setCancelled(true);
      }
      TNE.debug("Exiting click event");
    }
  }
}