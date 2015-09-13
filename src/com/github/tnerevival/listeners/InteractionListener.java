package com.github.tnerevival.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
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
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.Message;
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
			
			if(((boolean)TNE.configurations.getValue("Mobs.Enabled", "mob"))) {
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
							mob = "Skeleton";
							break;
						} 
						mob = "WitherSkeleton";
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
}