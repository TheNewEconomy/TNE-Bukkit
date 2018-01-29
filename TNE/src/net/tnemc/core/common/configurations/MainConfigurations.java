package net.tnemc.core.common.configurations;


import com.github.tnerevival.core.configurations.Configuration;
import net.tnemc.core.TNE;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

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
 * Created by creatorfromhell on 06/30/2017.
 */
public class MainConfigurations  extends Configuration {
  @Override
  public FileConfiguration getConfiguration() {
    return TNE.instance().getConfig();
  }

  @Override
  public List<String> node() {
    List<String> nodes = new ArrayList<>();
    nodes.add("Core");
    return nodes;
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    configurations.put("Core.UUID", true);
    configurations.put("Core.Multiworld", false);
    configurations.put("Core.Metrics", true);
    configurations.put("Core.Modules.Enabled", true);
    configurations.put("Core.Server.Name", "Main Server");
    configurations.put("Core.Server.Consolidate", false);
    configurations.put("Core.Server.Account.Enabled", true);
    configurations.put("Core.Server.Account.Name", "Server_Account");
    configurations.put("Core.Server.Account.Balance", 500);
    configurations.put("Core.Server.ThirdParty.Town", "town-");
    configurations.put("Core.Server.ThirdParty.Nation", "nation-");
    configurations.put("Core.Server.ThirdParty.Faction", "faction-");
    configurations.put("Core.Commands.PayShort", true);
    configurations.put("Core.Commands.BalanceShort", true);
    configurations.put("Core.Commands.TopShort", true);
    configurations.put("Core.Update.Check", true);
    configurations.put("Core.Update.Notify", true);
    configurations.put("Core.Transactions.Format", "M, d y");
    configurations.put("Core.Transactions.Timezone", "US/Eastern");
    configurations.put("Core.AutoSaver.Enabled", true);
    configurations.put("Core.AutoSaver.Interval", 600);

    configurations.put("Core.World.EnableChangeFee", false);
    configurations.put("Core.World.ChangeFee", 5.0);

    configurations.put("Core.Database.Type", "flatfile");
    configurations.put("Core.Database.Prefix", "TNE");
    configurations.put("Core.Database.Backup", true);
    configurations.put("Core.Database.File", "economy.db");
    configurations.put("Core.Database.Transactions.Use", true);
    configurations.put("Core.Database.Transactions.Cache", true);
    configurations.put("Core.Database.Transactions.Update", 600);
    configurations.put("Core.Database.MySQL.Host", "localhost");
    configurations.put("Core.Database.MySQL.Port", 3306);
    configurations.put("Core.Database.MySQL.Database", "TheNewEconomy");
    configurations.put("Core.Database.MySQL.User", "user");
    configurations.put("Core.Database.MySQL.Password", "password");

    super.load(configurationFile);
  }
}
