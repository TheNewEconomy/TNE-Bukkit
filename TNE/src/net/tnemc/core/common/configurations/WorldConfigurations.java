package net.tnemc.core.common.configurations;

import com.github.tnerevival.core.configurations.Configuration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 7/7/2017.
 */
public class WorldConfigurations extends Configuration {

  private Map<String, String> balanceShare = new HashMap<>();
  private Map<String, String> configurationShare = new HashMap<>();

  @Override
  public FileConfiguration getConfiguration() {
    return TNE.instance().worldConfiguration();
  }

  @Override
  public List<String> node() {
    List<String> nodes = new ArrayList<>();
    nodes.add("Worlds");
    return nodes;
  }

  @Override
  public void load(FileConfiguration configurationFile) {

    Set<String> worlds = configurationFile.getConfigurationSection("Worlds").getKeys(false);

    for(String world : worlds) {
      WorldManager manager = TNE.instance().getWorldManager(world);
      if(manager == null) {
        continue;
      }
      Set<String> configurations = configurationFile.getConfigurationSection("Worlds." + world).getKeys(true);

      for(String s : configurations) {
        String node = "Worlds." + world + "." + s;
        if(!configurationFile.isConfigurationSection(node) && !node.contains("Worlds." + world + ".TNECurrency")) {
          manager.setConfiguration(node, configurationFile.get(node));
        }
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

    TNE.instance().getWorldManagers().forEach((manager)->{
      if(balanceShare.containsKey(manager.getWorld()))
        manager.setBalanceWorld(balanceShare.get(manager.getWorld()));

      if(configurationShare.containsKey(manager.getWorld()))
        manager.setConfigurationWorld(configurationShare.get(manager.getWorld()));
    });

    super.load(configurationFile);
  }
}
