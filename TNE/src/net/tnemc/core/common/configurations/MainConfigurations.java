package net.tnemc.core.common.configurations;

import net.tnemc.core.TNE;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
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
  public File getFile() {
    return new File(TNE.instance().getDataFolder(), "config.yml");
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    configurations.put("Core.UUID", true);
    configurations.put("Core.Multiworld", false);
    configurations.put("Core.Server.MenuMaterial", "GOLD_INGOT");
    configurations.put("Core.Server.Name", "Main Server");
    configurations.put("Core.Server.TNEMod", false);
    configurations.put("Core.Server.Consolidate", false);
    configurations.put("Core.Server.ExperienceGain", false);
    configurations.put("Core.Server.CurrencyCrafting", true);
    configurations.put("Core.Server.CurrencyTrading", true);
    configurations.put("Core.Server.MobDrop", true);
    configurations.put("Core.Server.Account.Enabled", true);
    configurations.put("Core.Server.Account.Name", "Server_Account");
    configurations.put("Core.Server.Account.Balance", 500);
    configurations.put("Core.Server.ThirdParty.Stats", true);
    configurations.put("Core.Server.ThirdParty.Town", "town-");
    configurations.put("Core.Server.ThirdParty.Nation", "nation-");
    configurations.put("Core.Server.ThirdParty.Faction", "faction-");
    configurations.put("Core.Server.ThirdParty.McMMORewards", true);
    configurations.put("Core.Server.ThirdParty.TopThirdParty", true);
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
    
    configurations.put("Core.Currency.Info.Advanced", false);
    configurations.put("Core.Currency.Info.FormatMoney", true);
    configurations.put("Core.Currency.Info.FormatTop", true);
    configurations.put("Core.Currency.Basic.Server", "Main Server");
    configurations.put("Core.Currency.Basic.Major_Single", "Dollar");
    configurations.put("Core.Currency.Basic.Major_Plural", "Dollars");
    configurations.put("Core.Currency.Basic.Minor_Single", "Cent");
    configurations.put("Core.Currency.Basic.Minor_Plural", "Cents");
    configurations.put("Core.Currency.Basic.Prefixes", "kMGTPEZYXWVUN₮");
    configurations.put("Core.Currency.Basic.Symbol", "$");
    configurations.put("Core.Currency.Basic.ItemCurrency", false);
    configurations.put("Core.Currency.Basic.ExperienceCurrency", false);


    configurations.put("Core.Currency.Basic.Options.Format", "<symbol><major.amount><decimal><minor.amount>");
    configurations.put("Core.Currency.Basic.Options.MaxBalance", "900000000000000000000000000000000000000000000");
    configurations.put("Core.Currency.Basic.Options.Balance", "200.0");
    configurations.put("Core.Currency.Basic.Options.Decimal", ".");
    configurations.put("Core.Currency.Basic.Options.EnderChest", true);
    configurations.put("Core.Currency.Basic.Options.Major_Separate", true);
    configurations.put("Core.Currency.Basic.Options.Major_Separator", ",");
    configurations.put("Core.Currency.Basic.Options.Minor_Weight", 100);

    configurations.put("Core.Currency.Basic.Note.Notable", false);
    configurations.put("Core.Currency.Basic.Note.Fee", 0.00);
    configurations.put("Core.Currency.Basic.Note.Minimum", 0.00);

    configurations.put("Core.World.EnableChangeFee", false);
    configurations.put("Core.World.ChangeFee", 5.0);

    configurations.put("Core.Database.Type", "h2");
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
