package com.github.tnerevival.core.configurations.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.configurations.Configuration;
import com.github.tnerevival.core.objects.MaterialObject;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

public class MaterialsConfiguration extends Configuration {

  private HashMap<String, MaterialObject> materials = new HashMap<>();

  @Override
  public void load(FileConfiguration configurationFile) {
    Set<String> identifiers = TNE.instance.worldConfigurations.getConfigurationSection("Worlds").getKeys(false);

    //Load Materials
    configurations.put("Materials.Enabled", false);
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

        BigDecimal buyCost = (configuration.contains(node + ".Buy"))? new BigDecimal(configuration.getDouble(node + ".Buy")) : BigDecimal.ZERO;
        BigDecimal sellCost = (configuration.contains(node + ".Sell"))? new BigDecimal(configuration.getDouble(node + ".Sell")) : BigDecimal.ZERO;
        BigDecimal useCost = (configuration.contains(node + ".Use"))? new BigDecimal(configuration.getDouble(node + ".Use")) : BigDecimal.ZERO;
        BigDecimal craftingCost = (configuration.contains(node + ".Crafting"))? new BigDecimal(configuration.getDouble(node + ".Crafting")) : BigDecimal.ZERO;
        BigDecimal enchantCost = (configuration.contains(node + ".Enchant"))? new BigDecimal(configuration.getDouble(node + ".Enchant")) : BigDecimal.ZERO;
        BigDecimal placeCost = (configuration.contains(node + ".Place"))? new BigDecimal(configuration.getDouble(node + ".Place")) : BigDecimal.ZERO;
        BigDecimal mineCost = (configuration.contains(node + ".Mine"))? new BigDecimal(configuration.getDouble(node + ".Mine")) : BigDecimal.ZERO;
        BigDecimal smeltCost = (configuration.contains(node + ".Smelt"))? new BigDecimal(configuration.getDouble(node + ".Smelt")) : BigDecimal.ZERO;

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