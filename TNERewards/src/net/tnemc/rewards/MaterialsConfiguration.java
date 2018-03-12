package net.tnemc.rewards;

import com.github.tnerevival.core.configurations.Configuration;
import net.tnemc.core.TNE;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 10/21/2017.
 */
public class MaterialsConfiguration extends Configuration {

  private HashMap<String, MaterialObject> materials = new HashMap<>();

  @Override
  public FileConfiguration getConfiguration() {
    return RewardsModule.instance().fConfiguration();
  }

  @Override
  public List<String> node() {
    return Arrays.asList(new String[] { "Materials" });
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    Set<String> identifiers = TNE.instance().worldConfiguration().getConfigurationSection("Worlds").getKeys(false);

    //Load Materials
    configurations.put("Materials.Enabled", false);
    loadMaterials(configurationFile, "", null, true);
    loadMaterials(configurationFile, "", null, false);
    for(String identifier : identifiers) {
      loadMaterials(TNE.instance().worldConfiguration(), "Worlds." + identifier + ".", identifier, true);
      loadMaterials(TNE.instance().worldConfiguration(), "Worlds." + identifier + ".", identifier, false);
    }

    identifiers = TNE.instance().playerConfiguration().getConfigurationSection("Players").getKeys(false);
    for(String identifier : identifiers) {
      loadMaterials(TNE.instance().playerConfiguration(), "Players." + identifier + ".", identifier, true);
      loadMaterials(TNE.instance().playerConfiguration(), "Players." + identifier + ".", identifier, false);
    }
    super.load(configurationFile);
  }

  private void loadMaterials(FileConfiguration configuration, String baseNode, String identifier, boolean item) {
    String base = baseNode + ((item)? "Materials.Items" : "Materials.Blocks");
    if(configuration.contains(base)) {
      Boolean zero = !configuration.contains(base + ".ZeroMessage") || configuration.getBoolean(base + ".ZeroMessage");
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