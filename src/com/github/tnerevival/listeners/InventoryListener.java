package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Access;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.ObjectConfiguration;
import com.github.tnerevival.core.inventory.View;
import com.github.tnerevival.core.potion.PotionHelper;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.MaterialUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryListener implements Listener {
	
	TNE plugin;
	
	public InventoryListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player)event.getPlayer();
		InventoryType type = event.getInventory().getType();
		ObjectConfiguration config = TNE.configurations.getObjectConfiguration();

    if(event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("bank") || event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("shop")) {
      return;
    }
		
		if(config.inventoryEnabled(type)) {
			if(config.isTimed(type)) {
				if(AccountUtils.getAccount(MISCUtils.getID(player)).getTimeLeft(MISCUtils.getWorld(player), TNE.configurations.getObjectConfiguration().inventoryType(type)) > 0) {
					TNE.instance.manager.viewers.put(MISCUtils.getID(player), new View(System.nanoTime(), type));
				} else {
					event.setCancelled(true);
					Message message = new Message("Messages.Package.Unable");
					message.addVariable("$type",  TNE.configurations.getObjectConfiguration().inventoryType(type));
					player.sendMessage(message.translate());
				}
			} else {
				String message = "Messages.Inventory.Charge";
				double cost = config.getInventoryCost(type);
				
				if(!AccountUtils.transaction(MISCUtils.getID(player).toString(), null, cost, TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(player))) {
					message = "Messages.Money.Insufficient";
					event.setCancelled(true);
				}
				Message m = new Message(message);
				m.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.round(cost)));
				m.addVariable("$type", config.inventoryType(type));
				player.sendMessage(m.translate());
				
				if(!event.isCancelled()) {
					AccountUtils.transaction(MISCUtils.getID(player).toString(), null, cost, TransactionType.MONEY_REMOVE, MISCUtils.getWorld(player));
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("bank")) {
			UUID playerID = MISCUtils.getID(player);
			String world = MISCUtils.getWorld(playerID);
			if(TNE.instance.manager.accessing.containsKey(playerID)) {
				Access acc = TNE.instance.manager.accessing.get(playerID);
				if(!acc.getSave()) {
					TNE.instance.manager.accessing.remove(playerID);
					return;
				}
				playerID = acc.getAccessing();
				world = acc.getWorld();
			}
			
			Bank bank = TNE.instance.manager.accounts.get(playerID).getBank(world);
			List<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
			Integer slot = 0;
			for(ItemStack i : event.getInventory().getContents()) {
				if(i != null) {
					items.add(new SerializableItemStack(slot, i));
				}
				slot++;
			}
			bank.setItems(items);
			TNE.instance.manager.accessing.remove(MISCUtils.getID(player));
		} else {
		  if(event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("shop")) {
		    return;
      }
			InventoryType type = event.getInventory().getType();
			ObjectConfiguration config = TNE.configurations.getObjectConfiguration();
			if(config.inventoryEnabled(type) && TNE.instance.manager.viewers.containsKey(MISCUtils.getID(player))) {
				if(config.isTimed(type)) {
					View view = TNE.instance.manager.viewers.get(MISCUtils.getID(player));
					view.setClose(true);
					TNE.instance.manager.viewers.put(MISCUtils.getID(player), view);
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		int slot = event.getRawSlot();
		InventoryType type = event.getInventory().getType();
		ItemStack result = event.getCurrentItem();
		ItemMeta meta;
		String name = "unknown";
		Boolean charge = false;
		String message = "Messages.Money.Insufficient";
		Double cost = 0.0;

    if(event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("shop")) {
      event.setCancelled(true);
      return;
    }
		
		if(type.equals(InventoryType.BREWING)) {
			if(slot == 0 || slot == 1 || slot == 2) {
				if(!result.getType().equals(Material.AIR)) {
					meta = result.getItemMeta();
					if(MISCUtils.chargeClick(meta.getLore(), "brew")) {
						name = PotionHelper.getName(result);
						if(name != null) {
							if(TNE.configurations.getMaterialsConfiguration().containsPotion(name)) {
								cost = TNE.configurations.getMaterialsConfiguration().getPotion(name).getBrew();
							}
							
							if(cost > 0.0) {
								if(AccountUtils.transaction(MISCUtils.getID(player).toString(), null, cost, TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(player))) {
									message = "Messages.Objects.BrewingCharged";
								}
							} else {
								message = "Messages.Objects.BrewingPaid";
							}
							charge = true;
						}
					}
				}
			}
		} else if(type.equals(InventoryType.ENCHANTING)) {
			if(slot == 0 && !result.getType().equals(Material.AIR)) {
				meta = result.getItemMeta();
				if(MISCUtils.chargeClick(meta.getLore(), "enchant")) {
					name = MaterialUtils.formatMaterialNameWithoutSpace(result.getType()).toLowerCase();
					
					if(TNE.configurations.getMaterialsConfiguration().containsItem(name)) {
						cost = TNE.configurations.getMaterialsConfiguration().getItem(name).getCrafting();
					}
					
					if(cost > 0.0) {
						if(AccountUtils.transaction(MISCUtils.getID(player).toString(), null, cost, TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(player))) {
							message = "Messages.Objects.EnchantingCharged";
						}
					} else {
						message = "Messages.Objects.EnchantingPaid";
					}
					charge = true;
				}
			}
		} else if(type.equals(InventoryType.FURNACE)) {
			if(slot == 2 && !result.getType().equals(Material.AIR)) {
				meta = result.getItemMeta();
				if(MISCUtils.chargeClick(meta.getLore(), "smelt")) {
					name = MaterialUtils.formatMaterialNameWithoutSpace(MISCUtils.getFurnaceSource(result).getType()).toLowerCase();
					
					if(TNE.configurations.getMaterialsConfiguration().containsItem(name)) {
						cost = TNE.configurations.getMaterialsConfiguration().getItem(name).getSmelt() * result.getAmount();
					} else if(TNE.configurations.getMaterialsConfiguration().containsBlock(name)) {
						cost = TNE.configurations.getMaterialsConfiguration().getBlock(name).getSmelt() * result.getAmount();
					}
					
					if(cost > 0.0) {
						if(AccountUtils.transaction(MISCUtils.getID(player).toString(), null, cost, TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(player))) {
							message = "Messages.Objects.SmeltingCharged";
						}
					} else {
						message = "Messages.Objects.SmeltingPaid";
					}
					charge = true;
				}
			}
		}
		
		if(charge) {
			if(cost > 0.0) {
				if(AccountUtils.transaction(MISCUtils.getID(player).toString(), null, cost, TransactionType.MONEY_INQUIRY, MISCUtils.getWorld(player))) {
					AccountUtils.transaction(MISCUtils.getID(player).toString(), null, cost, TransactionType.MONEY_REMOVE, MISCUtils.getWorld(player));
				} else {
					event.setCancelled(true);
				}
			} else {
				AccountUtils.transaction(MISCUtils.getID(player).toString(), null, cost, TransactionType.MONEY_GIVE, MISCUtils.getWorld(player));
			}
			
			if(cost > 0.0 || cost < 0.0  || cost == 0.0 && TNE.configurations.getBoolean("Materials.Items.ZeroMessage")) {
				Message m = new Message(message);
				m.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.round(cost)));
				if(result != null) {
					m.addVariable("$stack_size", result.getAmount() + "");
					m.addVariable("$item", name);
				}
				player.sendMessage(m.translate());
			}
			
			if(!event.isCancelled()) {
				meta = result.getItemMeta();
				meta.setLore(new ArrayList<String>());
				result.setItemMeta(meta);
				event.setCurrentItem(result);
			}
		}
	}
}