package net.tnemc.core.common.configurations;

import net.tnemc.core.TNE;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
    configurations.put("Core.Server.MenuMaterial", "GOLD_INGOT");
    configurations.put("Core.Server.Name", "Main Server");
    configurations.put("Core.Server.Consolidate", false);
    configurations.put("Core.Server.CurrencyCrafting", true);
    configurations.put("Core.Server.CurrencyTrading", true);
    configurations.put("Core.Server.MobDrop", true);
    configurations.put("Core.Server.McMMORewards", true);
    configurations.put("Core.Server.Account.Enabled", true);
    configurations.put("Core.Server.Account.Name", "Server_Account");
    configurations.put("Core.Server.Account.Balance", 500);
    configurations.put("Core.Server.ThirdParty.Town", "town-");
    configurations.put("Core.Server.ThirdParty.Nation", "nation-");
    configurations.put("Core.Server.ThirdParty.Faction", "faction-");
    configurations.put("Core.Commands.Triggers", "/");
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
    configurations.put("Core.Database.MySQL.Host", "localhost");
    configurations.put("Core.Database.MySQL.Port", 3306);
    configurations.put("Core.Database.MySQL.Database", "TheNewEconomy");
    configurations.put("Core.Database.MySQL.User", "user");
    configurations.put("Core.Database.MySQL.Password", "password");

    super.load(configurationFile);
  }
}
