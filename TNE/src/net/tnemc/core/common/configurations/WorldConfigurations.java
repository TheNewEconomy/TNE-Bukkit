package net.tnemc.core.common.configurations;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldManager;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class WorldConfigurations extends Configuration {

  private Map<String, String> balanceShare = new HashMap<>();
  private Map<String, String> configurationShare = new HashMap<>();

  @Override
  public CommentedConfiguration getConfiguration() {
    return TNE.instance().worldConfiguration();
  }

  @Override
  public List<String> node() {
    List<String> nodes = new ArrayList<>();
    nodes.add("Worlds");
    return nodes;
  }

  @Override
  public File getFile() {
    return TNE.instance().getWorlds();
  }

  @Override
  public void load(CommentedConfiguration configurationFile) {

    Set<String> worlds = configurationFile.getSection("Worlds").getKeys(false);

    for(String world : worlds) {
      WorldManager manager = TNE.instance().getWorldManager(world);
      if(manager == null) {
        continue;
      }
      Set<String> configurations = configurationFile.getSection("Worlds." + world).getKeys(true);

      for(String s : configurations) {
        String node = "Worlds." + world + "." + s;
        if(!configurationFile.isConfigurationSection(node) && !node.contains("Worlds." + world + ".Currency")) {
          manager.setConfiguration(node, configurationFile.getString(node));
        }
      }

      if(configurationFile.contains("Worlds." + world + ".ChangeFee")) {
        String currency = configurationFile.getString("Worlds." + world + ".ChangeFee.Currency", "Default");
        BigDecimal amount = new BigDecimal(configurationFile.getString("Worlds." + world + ".ChangeFee.Amount", "10.0"));
        manager.setChangeFeeCurrency(currency);
        manager.setChangeFee(amount);
      }

      List<String> balances = new ArrayList<>();
      if(configurationFile.contains("Worlds." + world + ".Share.Balances")) {
        balances = configurationFile.getStringList("Worlds." + world + ".Share.Balances");
      }

      if(balances.size() > 0) {
        balances.forEach((sharedWorld)->balanceShare.put(sharedWorld, world));
      }

      List<String> configWorlds = new ArrayList<>();
      if(configurationFile.contains("Worlds." + world + ".Share.Configurations")) {
        configWorlds = configurationFile.getStringList("Worlds." + world + ".Share.Configurations");
      }

      if(configWorlds.size() > 0) {
        configWorlds.forEach((sharedWorld)->configurationShare.put(sharedWorld, world));
      }

      TNE.instance().addWorldManager(manager);
    }

    TNE.instance().getWorldManagersMap().keySet().forEach((world)->{
      WorldManager manager = TNE.instance().getWorldManager(world);
      if(balanceShare.containsKey(manager.getWorld())) {
        TNE.debug("Setting balance share for " + manager.getWorld() + " to " + balanceShare.get(manager.getWorld()));
        manager.setBalanceWorld(balanceShare.get(manager.getWorld()));
      }

      if(configurationShare.containsKey(manager.getWorld())) {
        TNE.debug("Setting config share for " + manager.getWorld() + " to " + configurationShare.get(manager.getWorld()));
        manager.setConfigurationWorld(configurationShare.get(manager.getWorld()));
      }
      TNE.instance().addWorldManager(manager);
    });

    super.load(configurationFile);
  }
}
