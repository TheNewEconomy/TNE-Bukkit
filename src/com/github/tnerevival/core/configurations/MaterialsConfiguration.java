package com.github.tnerevival.core.configurations;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.objects.BlockObject;
import com.github.tnerevival.core.objects.ItemObject;
import com.github.tnerevival.core.objects.PotionObject;

public class MaterialsConfiguration extends Configuration {

	HashMap<String, PotionObject> potions = new HashMap<String, PotionObject>();
	HashMap<String, ItemObject> items = new HashMap<String, ItemObject>();
	HashMap<String, BlockObject> blocks = new HashMap<String, BlockObject>();
	
	@Override
	public void load(FileConfiguration configurationFile) {
		//Load Potion Materials
		String baseNode = "Materials.Potions";
		configurations.put(baseNode + ".ZeroMessage", false);
		
		Set<String> potionNames = configurationFile.getConfigurationSection(baseNode).getKeys(false);
		
		for(String potionName : potionNames) {
			String node = baseNode + "." + potionName;
			
			PotionObject potion = new PotionObject(potionName);
			double brewCost = (configurationFile.contains(node + ".Brew"))? configurationFile.getDouble(node + ".Brew") : 0.0;
			double useCost = (configurationFile.contains(node + ".Use"))? configurationFile.getDouble(node + ".Use") : 0.0;
			
			potion.setBrew(brewCost);
			potion.setUse(useCost);
			
			potions.put(potion.getName(), potion);			
		}

		//Load Item Materials
		baseNode = "Materials.Items";
		configurations.put(baseNode + ".ZeroMessage", false);
		
		Set<String> itemNames = configurationFile.getConfigurationSection(baseNode).getKeys(false);
		
		for(String itemName : itemNames) {
			String node = baseNode + "." + itemName;
			
			ItemObject item = new ItemObject(itemName);
			double buyCost = (configurationFile.contains(node + ".Buy"))? configurationFile.getDouble(node + ".Buy") : 0.0;
			double sellCost = (configurationFile.contains(node + ".Sell"))? configurationFile.getDouble(node + ".Sell") : 0.0;
			double useCost = (configurationFile.contains(node + ".Use"))? configurationFile.getDouble(node + ".Use") : 0.0;
			double craftingCost = (configurationFile.contains(node + ".Crafting"))? configurationFile.getDouble(node + ".Crafting") : 0.0;
			double enchantCost = (configurationFile.contains(node + ".Enchant"))? configurationFile.getDouble(node + ".Enchant") : 0.0;
			double smeltCost = (configurationFile.contains(node + ".Smelt"))? configurationFile.getDouble(node + ".Smelt") : 0.0;
			
			item.setCost(buyCost);
			item.setValue(sellCost);
			item.setUse(useCost);
			item.setCrafting(craftingCost);
			item.setEnchant(enchantCost);
			item.setSmelt(smeltCost);
			
			items.put(item.getName(), item);			
		}
		
		//Load Block Materials
		baseNode = "Materials.Blocks";
		configurations.put(baseNode + ".ZeroMessage", false);
		
		Set<String> blockNames = configurationFile.getConfigurationSection(baseNode).getKeys(false);
		
		for(String blockName : blockNames) {
			String node = baseNode + "." + blockName;
			
			BlockObject block = new BlockObject(blockName);
			double buyCost = (configurationFile.contains(node + ".Buy"))? configurationFile.getDouble(node + ".Buy") : 0.0;
			double sellCost = (configurationFile.contains(node + ".Sell"))? configurationFile.getDouble(node + ".Sell") : 0.0;
			double craftingCost = (configurationFile.contains(node + ".Crafting"))? configurationFile.getDouble(node + ".Crafting") : 0.0;
			double placeCost = (configurationFile.contains(node + ".Place"))? configurationFile.getDouble(node + ".Place") : 0.0;
			double mineCost = (configurationFile.contains(node + ".Mine"))? configurationFile.getDouble(node + ".Mine") : 0.0;
			double smeltCost = (configurationFile.contains(node + ".Smelt"))? configurationFile.getDouble(node + ".Smelt") : 0.0;
			
			block.setCost(buyCost);
			block.setValue(sellCost);
			block.setCrafting(craftingCost);
			block.setPlace(placeCost);
			block.setMine(mineCost);
			block.setSmelt(smeltCost);
			
			blocks.put(block.getName(), block);			
		}
		
		
		super.load(TNE.instance.objectConfigurations);
	}
	
	public Boolean containsPotion(String name) {
		return potions.containsKey(name);
	}
	
	public Boolean containsItem(String name) {
		return items.containsKey(name);
	}
	
	public Boolean containsBlock(String name) {
		return blocks.containsKey(name);
	}
	
	public PotionObject getPotion(String name) {
		return potions.get(name);
	}
	
	public ItemObject getItem(String name) {
		return items.get(name);
	}
	
	public BlockObject getBlock(String name) {
		return blocks.get(name);
	}
}