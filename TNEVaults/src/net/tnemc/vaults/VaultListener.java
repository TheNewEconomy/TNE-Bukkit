package net.tnemc.vaults;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.vaults.inventory.ShulkerPeekInventoryHolder;
import net.tnemc.vaults.inventory.VaultInventoryHolder;
import net.tnemc.vaults.vault.Vault;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class VaultListener implements ModuleListener {

  TNE plugin;

  public VaultListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onClick(final InventoryClickEvent event) {
    VaultManager manager = VaultsModule.instance().manager();
    UUID id = IDFinder.getID(event.getWhoClicked());
    int slot = event.getRawSlot();
    if(event.getInventory().getHolder() instanceof VaultInventoryHolder) {
      VaultInventoryHolder holder = (VaultInventoryHolder)event.getInventory().getHolder();
      if(manager.isReadOnly(id)) {
        event.setCancelled(true);
      }

      Vault vault = manager.getVault(holder.getOwner(), holder.getWorld());
      if(vault.getTabs().containsKey(slot + 1)) {
        event.setCancelled(true);

        if(event.getClick().equals(ClickType.RIGHT)
           && event.getCursor() != null
           && !event.getCursor().getType().equals(Material.AIR)) {
          ItemStack clone = event.getCursor().clone();
          clone.setAmount(1);
          vault.setIcon(slot + 1, clone);
          manager.addVault(vault);
          manager.updateIcon(holder.getOwner(), holder.getWorld(), slot + 1);
          TNE.debug("Changing icon for tab: " + (slot + 1));
          TNE.debug("New Tab Item: " + event.getCursor().toString());
        } else {
          manager.open(id, holder.getOwner(), holder.getWorld(), (slot + 1), manager.isReadOnly(id));
        }
      }

    } else if(event.getInventory().getHolder() instanceof ShulkerPeekInventoryHolder) {
      ShulkerPeekInventoryHolder holder = (ShulkerPeekInventoryHolder)event.getInventory().getHolder();
      if(holder.isReadOnly()) event.setCancelled(true);
      if(slot == 0) {
        VaultsModule.instance().manager().open(id, holder.getVault(), holder.getWorld(), holder.getPreviousTab(), holder.isReadOnly());
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onClose(final InventoryCloseEvent event) {
    if(event.getInventory().getHolder() instanceof ShulkerPeekInventoryHolder) {
      ShulkerPeekInventoryHolder holder = ((ShulkerPeekInventoryHolder)event.getInventory().getHolder());
      VaultsModule.instance().manager().close(holder.getVault(), holder.getWorld());
    } else if(event.getInventory().getHolder() instanceof VaultInventoryHolder) {
      VaultInventoryHolder holder = ((VaultInventoryHolder)event.getInventory().getHolder());
      VaultsModule.instance().manager().removeViewer(IDFinder.getID(event.getPlayer()), holder.getOwner(), holder.getWorld(), holder.getTab());
    }
  }
}