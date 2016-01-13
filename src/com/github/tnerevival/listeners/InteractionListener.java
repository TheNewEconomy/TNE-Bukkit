package com.github.tnerevival.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Access;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.configurations.ObjectConfiguration;
import com.github.tnerevival.core.potion.PotionHelper;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.MaterialUtils;

public class InteractionListener implements Listener {
	
	TNE plugin;
	
	public InteractionListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if(TNE.configurations.getBoolean("Objects.Commands.Enabled", "objects")) {
			
			ObjectConfiguration configuration = TNE.configurations.getObjectConfiguration();
			
			Player player = event.getPlayer();
			String command = event.getMessage().substring(1);
			String[] commandSplit = command.split(" ");
			String commandName = commandSplit[0];
			String commandFirstArg = commandSplit[0] + ((commandSplit.length > 1) ? " " + commandSplit[1] : "");
			double cost = configuration.getCommandCost(commandName.toLowerCase(), (commandSplit.length > 1) ? new String[] { commandSplit[1].toLowerCase() } : new String[0]);
			
			Message commandCost = new Message("Messages.Command.Charge");
			commandCost.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(event.getPlayer()), AccountUtils.round(cost)));
			commandCost.addVariable("$command", commandFirstArg);
			
			if(cost > 0.0) {
				event.setCancelled(true);
				
				boolean paid = false;
				if(MISCUtils.hasCommandCredit(MISCUtils.getID(player), commandFirstArg)) {
					MISCUtils.removeCommandCredit(MISCUtils.getID(player), commandFirstArg);
				} else {
					if(TNE.instance.api.fundsHas(player, player.getWorld().getName(), cost)) {
						TNE.instance.api.fundsRemove(player, player.getWorld().getName(), cost);
						paid = true;
					}
				}
				
				if(paid) {
					if(!player.performCommand(command)) {
						MISCUtils.addCommandCredit(MISCUtils.getID(player), commandFirstArg);
						return;
					}		
					
					player.sendMessage(commandCost.translate());
				}
				return;
			}
			
			if(TNE.configurations.getBoolean("Objects.Commands.ZeroMessage", "objects")) {
				player.sendMessage(commandCost.translate());
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		String name = MaterialUtils.formatMaterialNameWithoutSpace(event.getBlock().getType());
		
		if(TNE.configurations.getMaterialsConfiguration().containsBlock(name)) {
			Player player = event.getPlayer();
			Double cost = TNE.configurations.getMaterialsConfiguration().getBlock(name).getMine();
			

			String message = "Messages.Objects.MiningCharged";
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
				message = "Messages.Objects.MiningPaid";
			}
			
			if(cost > 0.0 || cost < 0.0 || cost == 0.0 && TNE.configurations.getBoolean("Materials.Blocks.ZeroMessage")) {
				
				Message m = new Message(message);
				m.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.round(cost)));
				m.addVariable("$name", name);
				player.sendMessage(m.translate());
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		String name = MaterialUtils.formatMaterialNameWithoutSpace(event.getBlock().getType());
		
		if(TNE.configurations.getMaterialsConfiguration().containsBlock(name)) {
			Player player = event.getPlayer();
			Double cost = TNE.configurations.getMaterialsConfiguration().getBlock(name).getPlace();
			

			String message = "Messages.Objects.PlacingCharged";
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
				message = "Messages.Objects.PlacingPaid";
			}
			
			if(cost > 0.0 || cost < 0.0 || cost == 0.0 && TNE.configurations.getBoolean("Materials.Blocks.ZeroMessage")) {
				
				Message m = new Message(message);
				m.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.round(cost)));
				m.addVariable("$name", name);
				player.sendMessage(m.translate());
			}
		}
	}
	
	@EventHandler
	public void onSmelt(FurnaceSmeltEvent event) {
		if(event.getResult() != null && !event.getResult().getType().equals(Material.AIR)) {
			String name = MaterialUtils.formatMaterialNameWithoutSpace(event.getSource().getType());
			Double cost = 0.0;
			
			if(TNE.configurations.getMaterialsConfiguration().containsItem(name)) {
				cost = TNE.configurations.getMaterialsConfiguration().getItem(name).getCrafting();
			} else if(TNE.configurations.getMaterialsConfiguration().containsBlock(name)) {
				cost = TNE.configurations.getMaterialsConfiguration().getBlock(name).getCrafting();
			} else {
				return;
			}
			
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "Smelting Cost: " + ChatColor.GOLD + cost);
			
			ItemStack result = event.getResult();
			ItemMeta meta = result.getItemMeta();
			meta.setLore(lore);
			
			result.setItemMeta(meta);
			event.setResult(result);
		}
	}
	
	@EventHandler
	public void onBrew(BrewEvent event) {
		
		Double cost = 0.0;
		
		if(event.getContents().getItem(0) != null && !event.getContents().getItem(0).getType().equals(Material.AIR)) {
			String name = PotionHelper.getName(event.getContents().getItem(0));
			
			if(TNE.configurations.getMaterialsConfiguration().containsPotion(name)) {
				cost = TNE.configurations.getMaterialsConfiguration().getPotion(name).getBrew();
			} else {
				return;
			}
			
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "Brewing Cost: " + ChatColor.GOLD + cost);
			
			ItemStack result = event.getContents().getItem(0);
			ItemMeta meta = result.getItemMeta();
			meta.setLore(lore);
			
			result.setItemMeta(meta);
			event.getContents().setItem(0, result);
		}
		
		if(event.getContents().getItem(1) != null && !event.getContents().getItem(1).getType().equals(Material.AIR)) {
			String name = PotionHelper.getName(event.getContents().getItem(1));
			
			if(TNE.configurations.getMaterialsConfiguration().containsPotion(name)) {
				cost = TNE.configurations.getMaterialsConfiguration().getPotion(name).getBrew();
			} else {
				return;
			}
			
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "Brewing Cost: " + ChatColor.GOLD + cost);
			
			ItemStack result = event.getContents().getItem(1);
			ItemMeta meta = result.getItemMeta();
			meta.setLore(lore);
			
			result.setItemMeta(meta);
			event.getContents().setItem(1, result);
		}
		
		if(event.getContents().getItem(2) != null && !event.getContents().getItem(2).getType().equals(Material.AIR)) {
			String name = PotionHelper.getName(event.getContents().getItem(2));
			
			if(TNE.configurations.getMaterialsConfiguration().containsPotion(name)) {
				cost = TNE.configurations.getMaterialsConfiguration().getPotion(name).getBrew();
			} else {
				return;
			}
			
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "Brewing Cost: " + ChatColor.GOLD + cost);
			
			ItemStack result = event.getContents().getItem(2);
			ItemMeta meta = result.getItemMeta();
			meta.setLore(lore);
			
			result.setItemMeta(meta);
			event.getContents().setItem(2, result);
		}
	}
	@EventHandler
	public void onEnchant(EnchantItemEvent event) {
		if(event.getItem() != null && !event.getItem().getType().equals(Material.AIR)) {
			
			ItemStack result = event.getItem();
			String name = MaterialUtils.formatMaterialNameWithoutSpace(result.getType());
			Double cost = 0.0;
			
			if(TNE.configurations.getMaterialsConfiguration().containsItem(name)) {
				cost = TNE.configurations.getMaterialsConfiguration().getItem(name).getCrafting();
			} else {
				return;
			}
			
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "Enchanting Cost: " + ChatColor.GOLD + cost);
			
			ItemMeta meta = result.getItemMeta();
			meta.setLore(lore);
			
			for(Enchantment e : event.getEnchantsToAdd().keySet()) {
				meta.addEnchant(e, event.getEnchantsToAdd().get(e), false);
			}
			
			result.setItemMeta(meta);
			event.getInventory().setItem(0, result);
		}
	}
	
	@EventHandler
	public void onPreCraft(PrepareItemCraftEvent event) {
		if(event.getInventory().getResult() != null) {
			String name = MaterialUtils.formatMaterialNameWithoutSpace(event.getInventory().getResult().getType());
			Double cost = 0.0;
			
			if(TNE.configurations.getMaterialsConfiguration().containsItem(name)) {
				cost = TNE.configurations.getMaterialsConfiguration().getItem(name).getCrafting();
			} else if(TNE.configurations.getMaterialsConfiguration().containsBlock(name)) {
				cost = TNE.configurations.getMaterialsConfiguration().getBlock(name).getCrafting();
			} else {
				return;
			}
			
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.WHITE + "Crafting Cost: " + ChatColor.GOLD + cost);
			
			ItemStack result = event.getInventory().getResult();
			ItemMeta meta = result.getItemMeta();
			meta.setLore(lore);
			result.setItemMeta(meta);
			event.getInventory().setResult(result);
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent event) {
		
		String name = MaterialUtils.formatMaterialNameWithoutSpace(event.getInventory().getResult().getType());
		Double cost = 00.00;
		boolean item = false;
		
		if(TNE.configurations.getMaterialsConfiguration().containsItem(name)) {
			cost = TNE.configurations.getMaterialsConfiguration().getItem(name).getCrafting();
			item = true;
		} else if(TNE.configurations.getMaterialsConfiguration().containsBlock(name)) {
			cost = TNE.configurations.getMaterialsConfiguration().getBlock(name).getCrafting();
		}
		
		ItemStack result = event.getInventory().getResult();
		ItemMeta meta = result.getItemMeta();
		meta.setLore(new ArrayList<String>());
		result.setItemMeta(meta);
		
		Player player = (Player)event.getWhoClicked();
		String message = "Messages.Objects.CraftingCharged";
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
			message = "Messages.Objects.CraftingPaid";
		}
		
		if(cost > 0.0 || cost < 0.0  || cost == 0.0 && item && TNE.configurations.getBoolean("Materials.Items.ZeroMessage") || cost == 0.0 && !item && TNE.configurations.getBoolean("Materials.Blocks.ZeroMessage")) {
			String newName = (result.getAmount() > 1)? name + "'s" : name;
			
			Message m = new Message(message);
			m.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(player), AccountUtils.round(cost)));
			m.addVariable("$stack_size", result.getAmount() + "");
			m.addVariable("$item", newName);
			player.sendMessage(m.translate());
		}
		
		event.getInventory().setResult(result);
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		int slot = event.getRawSlot();
		InventoryType type = event.getInventory().getType();
		ItemStack result = event.getCurrentItem();
		ItemMeta meta;
		String name;
		Boolean charge = false;
		String message = "Messages.Money.Insufficient";
		Double cost = 0.0;
		
		if(type.equals(InventoryType.BREWING)) {
			if(slot == 0 || slot == 1 || slot == 2) {
				if(!result.getType().equals(Material.AIR)) {
					meta = result.getItemMeta();
					if(chargeClick(meta.getLore(), "brew")) {
						name = PotionHelper.getName(result);
						
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
		} else if(type.equals(InventoryType.ENCHANTING)) {
			if(slot == 0 && !result.getType().equals(Material.AIR)) {
				meta = result.getItemMeta();
				if(chargeClick(meta.getLore(), "enchant")) {
					name = MaterialUtils.formatMaterialNameWithoutSpace(result.getType());
					
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
				if(chargeClick(meta.getLore(), "smelt")) {
					name = MaterialUtils.formatMaterialNameWithoutSpace(getFurnaceSource(result).getType());
					
					if(TNE.configurations.getMaterialsConfiguration().containsItem(name)) {
						cost = TNE.configurations.getMaterialsConfiguration().getItem(name).getCrafting();
					} else if(TNE.configurations.getMaterialsConfiguration().containsBlock(name)) {
						cost = TNE.configurations.getMaterialsConfiguration().getBlock(name).getCrafting();
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
					m.addVariable("$item", MaterialUtils.formatMaterialNameWithSpace(result.getType()));
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
	
	@EventHandler
	public void onChange(SignChangeEvent event) {
		Message noPerm = new Message("Messages.General.NoPerm");
		Player player = event.getPlayer();
		if(event.getLine(0).toLowerCase().contains("[tne]")) {
			if(!player.hasPermission("tne.sign.main")) {
				event.getPlayer().sendMessage(noPerm.translate());
				event.setCancelled(true);
			}
		}
		if(event.getLine(1).toLowerCase().contains("[bank]")) {
			if(!player.hasPermission("tne.sign.bank")) {
				event.getPlayer().sendMessage(noPerm.translate());
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("bank")) {
			UUID playerID = player.getUniqueId();
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
			TNE.instance.manager.accessing.remove(player.getUniqueId());
		} else {
			
		}
	}
	
	@EventHandler
	public void onInteractWithEntity(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();
		String world = TNE.instance.defaultWorld;
		
		if(MISCUtils.multiWorld()) {
			world = player.getWorld().getName();
		}
		
		
		if(entity instanceof Villager) {
			Villager villager = (Villager)entity;
			if(player.getItemInHand().getType().equals(Material.NAME_TAG) && !player.hasPermission("tne.bypass.nametag")) {
				event.setCancelled(true);
				player.sendMessage("I'm sorry, but you cannot use a name tag on a villager.");
			}
			
			if(villager.getCustomName() != null && villager.getCustomName().equalsIgnoreCase("banker")) {
				event.setCancelled(true);
				if(player.hasPermission("tne.bank.use")) {
					if(BankUtils.enabled(world)) {
						if(BankUtils.npc(world)) {
							if(BankUtils.hasBank(player.getUniqueId())) {
								Inventory bankInventory = BankUtils.getBankInventory(player.getUniqueId());
								player.openInventory(bankInventory);
							} else {
								player.sendMessage(new Message("Messages.Bank.None").translate());
							}
						} else {
							player.sendMessage(new Message("Messages.Bank.NoNPC").translate());
						}
					} else {
						player.sendMessage(new Message("Messages.Bank.Disabled").translate());
					}
				} else {
					player.sendMessage(new Message("Messages.General.NoPerm").translate());
				}
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
									if(BankUtils.hasBank(player.getUniqueId())) {
										Inventory bankInventory = BankUtils.getBankInventory(player.getUniqueId());
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
				String name = MaterialUtils.formatMaterialNameWithoutSpace(event.getMaterial());
				boolean potion = false;
				
				if(event.getMaterial().equals(Material.POTION)) {
					potion = true;
					
					name = PotionHelper.getName(event.getItem());
					
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

	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		
		if(entity.getKiller() != null) {
			Player killer = entity.getKiller();
			String world = MISCUtils.getWorld(killer.getUniqueId());
			String mob = entity.getCustomName();
			Double reward = TNE.configurations.mobReward("Default");
			String messageNode = "Messages.Mob.Killed";
			
			if((TNE.configurations.getBoolean("Mobs.Enabled", "mob"))) {
				switch(entity.getType()) {
					case BAT:
						mob = "Bat";
						break;
					case BLAZE:
						mob = "Blaze";
						break;
					case CAVE_SPIDER:
						mob = "CaveSpider";
						break;
					case CHICKEN:
						mob = "Chicken";
						break;
					case COW:
						mob = "Cow";
						break;
					case CREEPER:
						mob = "Creeper";
						break;
					case ENDER_DRAGON:
						mob = "EnderDragon";
						break;
					case ENDERMAN:
						mob = "Enderman";
						break;
					case ENDERMITE:
						mob = "Endermite";
						break;
					case GHAST:
						mob = "Ghast";
						break;
					case GIANT:
						mob = "Giant";
						break;
					case GUARDIAN:
						mob = "Guardian";
						break;
					case HORSE:
						mob = "Horse";
						break;
					case IRON_GOLEM:
						mob = "IronGolem";
						break;
					case MAGMA_CUBE:
						mob = "MagmaCube";
						break;
					case MUSHROOM_COW:
						mob = "Mooshroom";
						break;
					case OCELOT:
						mob = "Ocelot";
						break;
					case PIG:
						mob = "Pig";
						break;
					case PIG_ZOMBIE:
						mob = "ZombiePigman";
						break;
					case RABBIT:
						mob = "Rabbit";
						break;
					case SHEEP:
						mob = "Sheep";
						break;
					case SILVERFISH:
						mob = "Silverfish";
						break;
					case SKELETON:
						Skeleton skelly = (Skeleton)entity;
						if(skelly.getSkeletonType().equals(SkeletonType.WITHER)) {
							mob = "WitherSkeleton";
							break;
						} 
						mob = "Skeleton";
						break;
					case SLIME:
						mob = "Slime";
						break;
					case SNOWMAN:
						mob = "SnowMan";
						break;
					case SPIDER:
						mob = "Spider";
						break;
					case SQUID:
						mob = "Squid";
						break;
					case VILLAGER:
						mob = "Villager";
						break;
					case WITCH:
						mob = "Witch";
						break;
					case WITHER:
						mob = "Wither";
						break;
					case WOLF:
						mob = "Wolf";
						break;
					case ZOMBIE:
						Zombie zombles = (Zombie)entity;
						if(zombles.isVillager()) {
							mob = "ZombieVillager";
							break;
						}
						mob = "Zombie";
						break;
					default:
						mob = "Default";
						break;
				}
				mob = (mob.equalsIgnoreCase("Default")) ? (entity.getCustomName() != null) ? entity.getCustomName() : mob : mob;
				Character firstChar = mob.charAt(0);
				reward = TNE.configurations.mobReward(mob);
				messageNode = (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') ? "Messages.Mob.KilledVowel" : "Messages.Mob.Killed";
				if(TNE.configurations.mobEnabled(mob)) {
					AccountUtils.addFunds(killer.getUniqueId(), reward);
					Message mobKilled = new Message(messageNode);
					mobKilled.addVariable("$mob", mob);
					mobKilled.addVariable("$reward", MISCUtils.formatBalance(world, reward));
					killer.sendMessage(mobKilled.translate());
				}
			}
		}
	}
	
	private ItemStack getFurnaceSource(ItemStack result) {
		List<Recipe> recipes = TNE.instance.getServer().getRecipesFor(result);
		for(Recipe r : recipes) {
			if(r instanceof FurnaceRecipe) {
				return ((FurnaceRecipe) r).getInput();
			}
		}
		return new ItemStack(Material.AIR, 1);
	}
	
	private Boolean chargeClick(List<String> lore, String inv) {
		if(lore != null) {
			String search = "Enchanting Cost:";
			switch(inv) {
				case "smelt":
					search = "Smelting Cost:";
					break;
				case "brew":
					search = "Brewing Cost:";
					break;
				default:
					search = "Enchanting Cost:";
					break;
			}
			
			for(String s : lore) {
				if(s.contains(search)) {
					return true;
				}
			}
		}
		return false;
	}
}