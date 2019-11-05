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

    TNE.debug("Loading world configurations");

    Set<String> worlds = configurationFile.getSection("Worlds").getKeys(false);

    for(String world : worlds) {
      TNE.debug("Iteration for World: " + world);
      WorldManager manager = TNE.instance().getWorldManager(world);

      if(balanceShare.containsKey(world)) {
        manager.setBalanceWorld(balanceShare.get(world));
      }

      if(configurationShare.containsKey(world)) {
        manager.setBalanceWorld(configurationShare.get(world));
      }

      if(manager == null) {
        TNE.debug("World manager = null");
        continue;
      }

      Set<String> configurations = configurationFile.getSection("Worlds." + world).getKeys(true);

      for(String s : configurations) {
        String node = "Worlds." + world + "." + s;
        if(!configurationFile.isConfigurationSection(node) && !node.contains("Worlds." + world + ".Currency")) {
          manager.setConfiguration(s, configurationFile.getString(node));
        }
      }

      if(configurationFile.contains("Worlds." + world + ".ChangeFee")) {
        String currency = configurationFile.getString("Worlds." + world + ".ChangeFee.Currency", "Default");
        BigDecimal amount = new BigDecimal(configurationFile.getString("Worlds." + world + ".ChangeFee.Amount", "10.0"));
        manager.setChangeFeeCurrency(currency);
        manager.setChangeFee(amount);
      }

      List<String> balances = new ArrayList<>();

      TNE.debug("Worlds." + world + ".Share.Balances");

      if(configurationFile.contains("Worlds." + world + ".Share.Balances")) {
        balances = configurationFile.getStringList("Worlds." + world + ".Share.Balances");
        TNE.debug(world + " shared balanced: " + balances);
      }
      TNE.debug("Balance Share Size: " + balances.size());
      TNE.debug("Balance Share: " + String.join(", ", balances));

      if(balances.size() > 0) {
        balances.forEach((sharedWorld)->{
          TNE.debug("Looping " + world + "->" + sharedWorld);
          balanceShare.put(sharedWorld, world);

          TNE.debug("Has Manager for " + sharedWorld + "? " + TNE.instance().hasWorldManager(sharedWorld));
          if(TNE.instance().hasWorldManager(sharedWorld)) {
            TNE.instance().getWorldManager(sharedWorld).setBalanceWorld(world);
            TNE.debug(sharedWorld + " setting balance share to " + world);
          }
        });
      }

      List<String> configWorlds = new ArrayList<>();
      if(configurationFile.contains("Worlds." + world + ".Share.Configurations")) {
        configWorlds = configurationFile.getStringList("Worlds." + world + ".Share.Configurations");
      }

      if(configWorlds.size() > 0) {
        configWorlds.forEach((sharedWorld)->{
          configurationShare.put(sharedWorld, world);

          if(TNE.instance().hasWorldManager(sharedWorld)) {
            TNE.instance().getWorldManager(sharedWorld).setConfigurationWorld(world);
          }
        });
      }

      TNE.instance().addWorldManager(manager);
    }
  }
}
