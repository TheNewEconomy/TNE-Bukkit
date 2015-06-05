package com.github.tnerevival.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;

public class InteractionListener implements Listener {
	
	TNE plugin;
	
	public InteractionListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		if(event.getLine(0).toLowerCase().contains("[tne]")) {
			if(!player.hasPermission("tne.sign.main")) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
				event.setCancelled(true);
			}
		}
		if(event.getLine(1).toLowerCase().contains("[bank]")) {
			if(!player.hasPermission("tne.sign.bank")) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(event.getInventory().getTitle() != null && event.getInventory().getTitle().toLowerCase().contains("bank")) {
			Bank bank = TNE.instance.manager.accounts.get(player.getUniqueId()).getBank(MISCUtils.getWorld(player.getUniqueId()));
			List<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
			Integer slot = 0;
			for(ItemStack i : event.getInventory().getContents()) {
				if(i != null) {
					items.add(new SerializableItemStack(slot, i));
				}
				slot++;
			}
			bank.setItems(items);
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
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but accessing banks via NPCs has been disabled in this world!");
						}
					} else {
						player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but banks are disabled in this world.");
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
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
										player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but accessing banks via signs has been disabled in this world!");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but banks are disabled in this world.");
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
						}
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
			
			if(((boolean)TNE.configurations.getValue("Mobs.Enabled", false))) {
				if(entity.getType().equals(EntityType.BAT)) {
					if(TNE.configurations.mobEnabled("Bat")) {
						Double reward = TNE.configurations.mobReward("Bat");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Bat" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.BLAZE)) {
					if(TNE.configurations.mobEnabled("Blaze")) {
						Double reward = TNE.configurations.mobReward("Blaze");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Blaze" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.CAVE_SPIDER)) {
					if(TNE.configurations.mobEnabled("CaveSpider")) {
						Double reward = TNE.configurations.mobReward("CaveSpider");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Cave Spider" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.CHICKEN)) {
					if(TNE.configurations.mobEnabled("Chicken")) {
						Double reward = TNE.configurations.mobReward("Chicken");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Chicken" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.COW)) {
					if(TNE.configurations.mobEnabled("Cow")) {
						Double reward = TNE.configurations.mobReward("Cow");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Cow" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.CREEPER)) {
					if(TNE.configurations.mobEnabled("Creeper")) {
						Double reward = TNE.configurations.mobReward("Creeper");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Creeper" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.ENDER_DRAGON)) {
					if(TNE.configurations.mobEnabled("EnderDragon")) {
						Double reward = TNE.configurations.mobReward("EnderDragon");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing an " + ChatColor.GREEN + "Ender Dragon" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.ENDERMAN)) {
					if(TNE.configurations.mobEnabled("Enderman")) {
						Double reward = TNE.configurations.mobReward("Enderman");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing an " + ChatColor.GREEN + "Enderman" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.GHAST)) {
					if(TNE.configurations.mobEnabled("Ghast")) {
						Double reward = TNE.configurations.mobReward("Ghast");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Ghast" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.GIANT)) {
					if(TNE.configurations.mobEnabled("Giant")) {
						Double reward = TNE.configurations.mobReward("Giant");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Giant" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.HORSE)) {
					if(TNE.configurations.mobEnabled("Horse")) {
						Double reward = TNE.configurations.mobReward("Horse");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Horse" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.IRON_GOLEM)) {
					if(TNE.configurations.mobEnabled("IronGolem")) {
						Double reward = TNE.configurations.mobReward("IronGolem");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing an " + ChatColor.GREEN + "Iron Golem" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.MAGMA_CUBE)) {
					if(TNE.configurations.mobEnabled("MagmaCube")) {
						Double reward = TNE.configurations.mobReward("MagmaCube");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "MagmaCube" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.MUSHROOM_COW)) {
					if(TNE.configurations.mobEnabled("Mooshroom")) {
						Double reward = TNE.configurations.mobReward("Mooshroom");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Mooshroom" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.OCELOT)) {
					if(TNE.configurations.mobEnabled("Ocelot")) {
						Double reward = TNE.configurations.mobReward("Ocelot");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing an " + ChatColor.GREEN + "Ocelot" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.PIG)) {
					if(TNE.configurations.mobEnabled("Pig")) {
						Double reward = TNE.configurations.mobReward("Pig");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Pig" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.PIG_ZOMBIE)) {
					if(TNE.configurations.mobEnabled("ZombiePigman")) {
						Double reward = TNE.configurations.mobReward("ZombiePigman");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Zombie Pigman" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SHEEP)) {
					if(TNE.configurations.mobEnabled("Sheep")) {
						Double reward = TNE.configurations.mobReward("Sheep");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Sheep" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SILVERFISH)) {
					if(TNE.configurations.mobEnabled("Silverfish")) {
						Double reward = TNE.configurations.mobReward("Silverfish");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Silverfish" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SKELETON)) {
					Skeleton skelly = (Skeleton)entity;
					if(skelly.getSkeletonType().equals(SkeletonType.NORMAL)) {
						if(TNE.configurations.mobEnabled("Skeleton")) {
							Double reward = TNE.configurations.mobReward("Skeleton");
							AccountUtils.addFunds(killer.getUniqueId(), reward);
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Skeleton" + ChatColor.WHITE + ".");
						}
					} else {
						if(TNE.configurations.mobEnabled("WitherSkeleton")) {
							Double reward = TNE.configurations.mobReward("WitherSkeleton");
							AccountUtils.addFunds(killer.getUniqueId(), reward);
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Wither Skeleton" + ChatColor.WHITE + ".");
						}
					}
				} else if(entity.getType().equals(EntityType.SLIME)) {
					if(TNE.configurations.mobEnabled("Slime")) {
						Double reward = TNE.configurations.mobReward("Slime");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Slime" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SNOWMAN)) {
					if(TNE.configurations.mobEnabled("SnowGolem")) {
						Double reward = TNE.configurations.mobReward("SnowGolem");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Snow Golem" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SPIDER)) {
					if(TNE.configurations.mobEnabled("Spider")) {
						Double reward = TNE.configurations.mobReward("Spider");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Spider" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SQUID)) {
					if(TNE.configurations.mobEnabled("Squid")) {
						Double reward = TNE.configurations.mobReward("Squid");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Squid" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.VILLAGER)) {
					if(TNE.configurations.mobEnabled("Villager")) {
						Double reward = TNE.configurations.mobReward("Villager");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Villager" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.WITCH)) {
					if(TNE.configurations.mobEnabled("Witch")) {
						Double reward = TNE.configurations.mobReward("Witch");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Witch" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.WITHER)) {
					if(TNE.configurations.mobEnabled("Wither")) {
						Double reward = TNE.configurations.mobReward("Wither");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Wither" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.WOLF)) {
					if(TNE.configurations.mobEnabled("Wolf")) {
						Double reward = TNE.configurations.mobReward("Wolf");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Wolf" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.ZOMBIE)) {
					Zombie zombles = (Zombie)entity;
					if(zombles.isVillager()) {
						if(TNE.configurations.mobEnabled("ZombieVillager")) {
							Double reward = TNE.configurations.mobReward("ZombieVillager");
							AccountUtils.addFunds(killer.getUniqueId(), reward);
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Zombie Villager" + ChatColor.WHITE + ".");
						}
					} else {
						if(TNE.configurations.mobEnabled("Zombie")) {
							Double reward = TNE.configurations.mobReward("Zombie");
							AccountUtils.addFunds(killer.getUniqueId(), reward);
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Zombie" + ChatColor.WHITE + ".");
						}
					}
				} else {
					if(TNE.configurations.mobEnabled("Default")) {
						Double reward = TNE.configurations.mobReward("Default");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						if(entity.getCustomName() != null) {
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + entity.getCustomName() + ChatColor.WHITE + ".");
						} else {
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Mob" + ChatColor.WHITE + ".");
						}
					}
				}
			}
		}
	}
}