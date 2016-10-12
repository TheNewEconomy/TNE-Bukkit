package com.github.tnerevival.core.configurations.impl;

import com.github.tnerevival.core.configurations.Configuration;
import com.github.tnerevival.core.objects.TNEAccessPackage;
import com.github.tnerevival.core.objects.TNECommandObject;
import com.github.tnerevival.core.objects.TNEInventoryObject;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;

public class ObjectConfiguration extends Configuration {
	
	private Map<String, TNECommandObject> commands = new HashMap<>();
	private Map<String, TNEInventoryObject> inventories = new HashMap<>();
	
	@Override
	public void load(FileConfiguration configurationFile) {		
		//Load Command Objects
		String baseNode = "Objects.Commands";
		configurations.put(baseNode + ".Enabled", true);
		configurations.put(baseNode + ".ZeroMessage", false);
		
		Set<String> commandNames = configurationFile.getConfigurationSection(baseNode).getKeys(false);
		
		for(String commandName : commandNames) {
			
			TNECommandObject command = new TNECommandObject(commandName.toLowerCase(), configurationFile.getDouble(baseNode + "." + commandName + ".Cost"));
			
			if(configurationFile.contains(baseNode + "." + commandName + ".SubCommands")) {
				Set<String> subCommands = configurationFile.getConfigurationSection(baseNode + "." + commandName + ".SubCommands").getKeys(false);
				
				for(String subCommand : subCommands) {
					TNECommandObject sub = new TNECommandObject(subCommand.toLowerCase(), configurationFile.getDouble(baseNode + "." + commandName + ".SubCommands." + subCommand + ".Cost"));
					command.addSub(sub);
				}
			}
			
			commands.put(command.getIdentifier(), command);			
		}
		
		//Load Inventory Objects
		baseNode = "Objects.Inventories";
		configurations.put(baseNode + ".Enabled", true);
		configurations.put(baseNode + ".PerWorld", true);
		configurations.put(baseNode + ".ZeroMessage", false);
		
		Set<String> inventoryNames = configurationFile.getConfigurationSection(baseNode).getKeys(false);
		
		for(String inventoryName : inventoryNames) {
			
			boolean enabled = configurationFile.getBoolean(baseNode + "." + inventoryName + ".Enabled");
			boolean timed = configurationFile.getBoolean(baseNode + "." + inventoryName + ".Timed");
			double cost = configurationFile.getDouble(baseNode + "." + inventoryName + ".Cost");
			
			TNEInventoryObject inventory = new TNEInventoryObject(inventoryName, enabled, timed, cost);
			
			if(configurationFile.contains(baseNode + "." + inventoryName + ".Packages")) {
				Set<String> packageNames = configurationFile.getConfigurationSection(baseNode + "." + inventoryName + ".Packages").getKeys(false);
				
				for(String packageName : packageNames) {
					
					long packageTime = configurationFile.getLong(baseNode + "." + inventoryName + ".Packages" + "." + packageName + ".Time");
					double packageCost = configurationFile.getDouble(baseNode + "." + inventoryName + ".Packages" + "." + packageName + ".Cost");
					
					inventory.addPackage(new TNEAccessPackage(packageName, packageTime, packageCost));
				}
			}
			inventories.put(inventoryName, inventory);
		}
		super.load(configurationFile);
	}
	
	public String inventoryType(InventoryType type) {
		switch(type) {
			case ANVIL:
				return "Anvil";
			case BEACON:
				return "Beacon";
			case BREWING:
				return "BrewStand";
			case CHEST:
				return "Chest";
			case CRAFTING:
				return "Crafting";
			case CREATIVE:
				return "CreativeInventory";
			case DISPENSER:
				return "Dispenser";
			case DROPPER:
				return "Dropper";
			case ENCHANTING:
				return "EnchantmentTable";
			case ENDER_CHEST:
				return "EnderChest";
			case FURNACE:
				return "Furnace";
			case HOPPER:
				return "Hopper";
			case MERCHANT:
				return "Villager";
			case PLAYER:
				return "SurvivalInventory";
			case WORKBENCH:
				return "Workbench";
			default:
				return "Chest";
		}
	}
	
	public List<TNEAccessPackage> getInventoryPackages(String type) {
		if(inventories.containsKey(type)) {
			return inventories.get(type).getPackages();
		}
		return new ArrayList<>();
	}
	
	public boolean isTimed(InventoryType type) {
		return inventories.get(inventoryType(type)).isTimed();
	}
	
	public boolean inventoryEnabled(InventoryType type) {
		String typeString = inventoryType(type);
		TNEInventoryObject inventoryObject = inventories.get(typeString);
		return ((Boolean)configurations.get("Objects.Inventories.Enabled") && inventoryObject != null && inventoryObject.isEnabled());
	}
	
	public double getInventoryCost(InventoryType type) {
		String typeString = inventoryType(type);
		TNEInventoryObject inventoryObject = inventories.get(typeString);
		if((Boolean)configurations.get("Objects.Inventories.Enabled") && inventoryObject != null && inventoryObject.isEnabled()) {
			return inventories.get(typeString).getCost();
		}
		return 0.0;
	}
	
	public double getCommandCost(String command, String[] arguments) {
		TNECommandObject commandObject = commands.get(command);
		if(commandObject != null) {
			if(arguments.length > 0) {
				TNECommandObject sub = commandObject.findSub(arguments[0]);
				if(sub != null) {
					return sub.getCost();
				}
			}
			return commandObject.getCost();
		}
		return 0;
	}
}