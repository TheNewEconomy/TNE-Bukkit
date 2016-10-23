package com.github.tnerevival.core.configurations.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.configurations.Configuration;
import com.github.tnerevival.core.objects.MaterialObject;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Set;

public class MaterialsConfiguration extends Configuration {

  private HashMap<String, MaterialObject> materials = new HashMap<>();

  @Override
  public void load(FileConfiguration configurationFile) {
    Set<String> identifiers = TNE.instance.worldConfigurations.getConfigurationSection("Worlds").getKeys(false);

    //Load Materials
    loadMaterials(configurationFile, "", null, true);
    loadMaterials(configurationFile, "", null, false);
    for(String identifier : identifiers) {
      loadMaterials(TNE.instance.worldConfigurations, "Worlds." + identifier + ".", identifier, true);
      loadMaterials(TNE.instance.worldConfigurations, "Worlds." + identifier + ".", identifier, false);
    }

    identifiers = TNE.instance.playerConfigurations.getConfigurationSection("Players").getKeys(false);
    for(String identifier : identifiers) {
      loadMaterials(TNE.instance.playerConfigurations, "Players." + identifier + ".", identifier, true);
      loadMaterials(TNE.instance.playerConfigurations, "Players." + identifier + ".", identifier, false);
    }
    super.load(configurationFile);
  }

  private void loadMaterials(FileConfiguration configuration, String baseNode, String identifier, boolean item) {
    String base = baseNode + ((item)? "Materials.Items" : "Materials.Blocks");
    if(configuration.contains(base)) {
      Boolean zero = (configuration.contains(base + ".ZeroMessage"))? configuration.getBoolean(base + ".ZeroMessage") : true;
      configurations.put(base + ".ZeroMessage", zero);

      Set<String> materialNames = configuration.getConfigurationSection(base).getKeys(false);

      for(String materialName : materialNames) {
        String id = (identifier != null)? identifier + ":" + materialName : materialName;
        String node = base + "." + materialName;

        MaterialObject material = new MaterialObject(materialName);

        double buyCost = (configuration.contains(node + ".Buy"))? configuration.getDouble(node + ".Buy") : 0.0;
        double sellCost = (configuration.contains(node + ".Sell"))? configuration.getDouble(node + ".Sell") : 0.0;
        double useCost = (configuration.contains(node + ".Use"))? configuration.getDouble(node + ".Use") : 0.0;
        double craftingCost = (configuration.contains(node + ".Crafting"))? configuration.getDouble(node + ".Crafting") : 0.0;
        double enchantCost = (configuration.contains(node + ".Enchant"))? configuration.getDouble(node + ".Enchant") : 0.0;
        double placeCost = (configuration.contains(node + ".Place"))? configuration.getDouble(node + ".Place") : 0.0;
        double mineCost = (configuration.contains(node + ".Mine"))? configuration.getDouble(node + ".Mine") : 0.0;
        double smeltCost = (configuration.contains(node + ".Smelt"))? configuration.getDouble(node + ".Smelt") : 0.0;

        material.setItem(item);
        material.setCost(buyCost);
        material.setValue(sellCost);
        material.setUse(useCost);
        material.setCrafting(craftingCost);
        material.setEnchant(enchantCost);
        material.setPlace(placeCost);
        material.setMine(mineCost);
        material.setSmelt(smeltCost);

        materials.put(id, material);
      }
    }
  }

  public Boolean containsMaterial(String name, String world, String player) {
    return materials.containsKey(player + ":" + name) ||
           materials.containsKey(world + ":" + name) ||
           materials.containsKey(name);
  }

  public MaterialObject getMaterial(String name, String world, String player) {
    if(materials.containsKey(player + ":" + name)) return materials.get(player + ":" + name);
    if(materials.containsKey(world + ":" + name)) return materials.get(world + ":" + name);
    return materials.get(name);
  }
}