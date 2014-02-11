package com.github.tnerevival.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.PlayerUtils;

public class InteractionListener implements Listener {
	
	TNE plugin;
	
	public InteractionListener(TNE plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		if(event.getLine(0).contains("[bank]")) {
			if(!player.hasPermission("tne.bank.create")) {
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		String username = player.getDisplayName();
		if(event.getInventory().getTitle().contains(username)) {
			Bank bank = TNE.instance.manager.banks.get(username);
			List<SerializableItemStack> items = new ArrayList<SerializableItemStack>();
			Integer slot = 0;
			for(ItemStack i : event.getInventory().getContents()) {
				if(i != null) {
					items.add(new SerializableItemStack(slot, i));
				} else {
					items.add(new SerializableItemStack(slot));
				}
				slot++;
			}
			bank.setItems(items);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onRightClick(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		String username = player.getDisplayName();
		Block block = event.getClickedBlock();
		
		if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
			if (action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.WALL_SIGN) || action.equals(Action.RIGHT_CLICK_BLOCK) && block.getType().equals(Material.SIGN_POST)) {
				Sign sign = (Sign) block.getState();
				if (sign.getLine(0).contains("bank")) {
					if(player.hasPermission("tne.bank.use") || player.hasPermission("tne.bank.*")) {
						if(BankUtils.sign(PlayerUtils.getWorld(username))) {
							if(BankUtils.hasBank(username)) {
								Inventory bankInventory = BankUtils.getBankInventory(username);
								player.openInventory(bankInventory);
							} else {
								player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but accessing banks via signs has been disabled in this world!");
						}
					} else {
						player.sendMessage(ChatColor.DARK_RED + "I'm sorry, but you do not have permission to do that.");
					}
				}
			}
		}
	}
}