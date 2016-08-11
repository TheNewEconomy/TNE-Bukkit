package com.github.tnerevival.listeners;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Access;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.ObjectConfiguration;
import com.github.tnerevival.core.inventory.View;
import com.github.tnerevival.core.potion.PotionHelper;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.MaterialUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
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
				
				if(!AccountUtils.hasFunds(MISCUtils.getID(player), cost)) {
					message = "Messages.Money.Insufficient";
					event.setCancelled(true);
				}
				Message m = new Message(message);
				m.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.round(cost)));
				m.addVariable("$type", config.inventoryType(type));
				player.sendMessage(m.translate());
				
				if(!event.isCancelled()) {
					AccountUtils.removeFunds(MISCUtils.getID(player), cost);
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
								if(AccountUtils.hasFunds(MISCUtils.getID(player), cost)) {
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
						if(AccountUtils.hasFunds(MISCUtils.getID(player), cost)) {
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
						if(AccountUtils.hasFunds(MISCUtils.getID(player), cost)) {
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
				if(AccountUtils.hasFunds(MISCUtils.getID(player), cost)) {
					AccountUtils.removeFunds(MISCUtils.getID(player), MISCUtils.getWorld(player), cost);
				} else {
					event.setCancelled(true);
				}
			} else {
				AccountUtils.addFunds(MISCUtils.getID(player), MISCUtils.getWorld(player), cost);
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
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onRightClick(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		String world = player.getWorld().getName();
		Block block = event.getClickedBlock();
		
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.WALL_SIGN) || action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.SIGN_POST)) {
				Sign sign = (Sign) block.getState();
				if(sign.getLine(0).toLowerCase().contains("[tne]")) {
					if (sign.getLine(1).toLowerCase().contains("[bank]")) {
						if(player.hasPermission("tne.bank.use")) {
							if(BankUtils.enabled(world)) {
								if(BankUtils.sign(world)) {
									if(BankUtils.hasBank(MISCUtils.getID(player))) {
										Inventory bankInventory = BankUtils.getBankInventory(MISCUtils.getID(player));
										player.openInventory(bankInventory);
									} else {
										player.sendMessage(new Message("Messages.Bank.None").translate());
									}
								} else {
									player.sendMessage(new Message("Messages.Bank.NoSign").translate());
								}
							} else {
								player.sendMessage(new Message("Messages.Bank.Disabled").translate());
							}
						} else {
							player.sendMessage(new Message("Messages.General.NoPerm").translate());
						}
					}
				}
			} else {
				Double cost = 0.0;
				String name = MaterialUtils.formatMaterialNameWithoutSpace(event.getMaterial()).toLowerCase();
				boolean potion = false;
				if(event.getItem() != null) {
					if(event.getItem().getType().equals(Material.POTION)) {
						potion = true;
						
						name = PotionHelper.getName(event.getItem()).toLowerCase() ;
						
						if(TNE.configurations.getMaterialsConfiguration().containsPotion(name)) {
							cost = TNE.configurations.getMaterialsConfiguration().getPotion(name).getUse();
						}
					} else {
						if(TNE.configurations.getMaterialsConfiguration().containsItem(name)) {
							cost = TNE.configurations.getMaterialsConfiguration().getItem(name).getUse();
						}
					}
					
					if(!potion && TNE.configurations.getMaterialsConfiguration().containsItem(name) || potion && TNE.configurations.getMaterialsConfiguration().containsPotion(name)) {
					
						String message = (potion)? "Messages.Objects.PotionUseCharged" : "Messages.Objects.ItemUseCharged";
						if(cost > 0.0) {
							if(AccountUtils.hasFunds(MISCUtils.getID(player), cost)) {
								AccountUtils.removeFunds(MISCUtils.getID(player), MISCUtils.getWorld(player), cost);
							} else {
								event.setCancelled(true);
								Message insufficient = new Message("Messages.Money.Insufficient");
								insufficient.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.round(cost)));
								player.sendMessage(insufficient.translate());
								return;
							}
						} else {
							AccountUtils.addFunds(MISCUtils.getID(player), MISCUtils.getWorld(player), cost);
							message = (potion)? "Messages.Objects.PotionUsePaid" : "Messages.Objects.ItemUsePaid";
						}
						
						if(cost > 0.0 || cost < 0.0  || cost == 0.0 && !potion && TNE.configurations.getBoolean("Materials.Items.ZeroMessage")  || cost == 0.0 && potion && TNE.configurations.getBoolean("Materials.Potions.ZeroMessage")) {
							
							Message m = new Message(message);
							m.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.round(cost)));
							m.addVariable("$item", name);
							player.sendMessage(m.translate());
						}
					}
				}
			}
		}
	}
}