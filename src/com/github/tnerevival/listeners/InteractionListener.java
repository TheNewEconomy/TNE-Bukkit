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
			
			if(TNE.instance.mobConfigurations.getBoolean("Mobs.Enabled")) {
				if(entity.getType().equals(EntityType.BAT)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Bat.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Bat.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Bat" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.BLAZE)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Blaze.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Blaze.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Blaze" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.CAVE_SPIDER)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.CaveSpider.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.CaveSpider.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Cave Spider" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.CHICKEN)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Chicken.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Chicken.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Chicken" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.COW)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Cow.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Cow.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Cow" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.CREEPER)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Creeper.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Creeper.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Creeper" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.ENDER_DRAGON)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.EnderDragon.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.EnderDragon.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing an " + ChatColor.GREEN + "Ender Dragon" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.ENDERMAN)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Enderman.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Enderman.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing an " + ChatColor.GREEN + "Enderman" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.GHAST)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Ghast.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Ghast.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Ghast" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.GIANT)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Giant.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Giant.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Giant" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.HORSE)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Horse.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Horse.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Horse" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.IRON_GOLEM)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.IronGolem.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.IronGolem.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing an " + ChatColor.GREEN + "Iron Golem" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.MAGMA_CUBE)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.MagmaCube.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.MagmaCube.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "MagmaCube" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.MUSHROOM_COW)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Mooshroom.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Mooshroom.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Mooshroom" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.OCELOT)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Ocelot.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Ocelot.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing an " + ChatColor.GREEN + "Ocelot" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.PIG)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Pig.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Pig.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Pig" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.PIG_ZOMBIE)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.ZombiePigman.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.ZombiePigman.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Zombie Pigman" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SHEEP)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Sheep.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Sheep.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Sheep" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SILVERFISH)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Silverfish.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Silverfish.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Silverfish" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SKELETON)) {
					Skeleton skelly = (Skeleton)entity;
					if(skelly.getSkeletonType().equals(SkeletonType.NORMAL)) {
						if(TNE.instance.mobConfigurations.getBoolean("Mobs.Skeleton.Enabled")) {
							Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Skeleton.Reward");
							AccountUtils.addFunds(killer.getUniqueId(), reward);
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Skeleton" + ChatColor.WHITE + ".");
						}
					} else {
						if(TNE.instance.mobConfigurations.getBoolean("Mobs.WitherSkeleton.Enabled")) {
							Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.WitherSkeleton.Reward");
							AccountUtils.addFunds(killer.getUniqueId(), reward);
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Wither Skeleton" + ChatColor.WHITE + ".");
						}
					}
				} else if(entity.getType().equals(EntityType.SLIME)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Slime.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Slime.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Slime" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SNOWMAN)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.SnowGolem.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.SnowGolem.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Snow Golem" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SPIDER)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Spider.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Spider.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Spider" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.SQUID)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Squid.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Squid.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Squid" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.VILLAGER)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Villager.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Villager.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Villager" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.WITCH)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Witch.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Witch.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Witch" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.WITHER)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Wither.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Wither.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Wither" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.WOLF)) {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Wolf.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Wolf.Reward");
						AccountUtils.addFunds(killer.getUniqueId(), reward);
						killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Wolf" + ChatColor.WHITE + ".");
					}
				} else if(entity.getType().equals(EntityType.ZOMBIE)) {
					Zombie zombles = (Zombie)entity;
					if(zombles.isVillager()) {
						if(TNE.instance.mobConfigurations.getBoolean("Mobs.ZombieVillager.Enabled")) {
							Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.ZombieVillager.Reward");
							AccountUtils.addFunds(killer.getUniqueId(), reward);
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Zombie Villager" + ChatColor.WHITE + ".");
						}
					} else {
						if(TNE.instance.mobConfigurations.getBoolean("Mobs.Zombie.Enabled")) {
							Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Zombie.Reward");
							AccountUtils.addFunds(killer.getUniqueId(), reward);
							killer.sendMessage(ChatColor.WHITE + "You received " + MISCUtils.formatBalance(world, reward) + " for killing a " + ChatColor.GREEN + "Zombie" + ChatColor.WHITE + ".");
						}
					}
				} else {
					if(TNE.instance.mobConfigurations.getBoolean("Mobs.Default.Enabled")) {
						Double reward = TNE.instance.mobConfigurations.getDouble("Mobs.Default.Reward");
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