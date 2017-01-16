package com.github.tnerevival.core.configurations.impl;

import com.github.tnerevival.core.configurations.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfiguration extends Configuration {

  @Override
  public void load(FileConfiguration configurationFile) {
    configurations.put("Core.UUID", true);
    configurations.put("Core.Multiworld", false);
    configurations.put("Core.Metrics", true);
    configurations.put("Core.Pins.Enabled", true);
    configurations.put("Core.Pins.Force", true);
    configurations.put("Core.Commands.PayShort", true);
    configurations.put("Core.Commands.BalanceShort", true);
    configurations.put("Core.Update.Check", true);
    configurations.put("Core.Update.Notify", true);
    configurations.put("Core.Transactions.Track", true);
    configurations.put("Core.Transactions.Format", "M, d y");
    configurations.put("Core.Transactions.Timezone", "US/Eastern");
    configurations.put("Core.AutoSaver.Enabled", true);
    configurations.put("Core.AutoSaver.Interval", 600);

    configurations.put("Core.Auctions.Enabled", true);
    configurations.put("Core.Auctions.Cost", 10.00);
    configurations.put("Core.Auctions.AllowWorld", false);
    configurations.put("Core.Auctions.Multiple", false);
    configurations.put("Core.Auctions.MaxMultiple", 3);
    configurations.put("Core.Auctions.PersonalQueue", 3);
    configurations.put("Core.Auctions.MaxQueue", 10);
    configurations.put("Core.Auctions.MaxStart", 2000);
    configurations.put("Core.Auctions.MinStart", 1);
    configurations.put("Core.Auctions.MaxIncrement", 1000);
    configurations.put("Core.Auctions.MinIncrement", 1);
    configurations.put("Core.Auctions.MaxTime", 60);
    configurations.put("Core.Auctions.MinTime", 30);
    configurations.put("Core.Auctions.AntiSnipe", true);
    configurations.put("Core.Auctions.SnipePeriod", 30);
    configurations.put("Core.Auctions.SnipeTime", 20);
    configurations.put("Core.Auctions.Announce", true);
    configurations.put("Core.Auctions.Interval", 10);
    configurations.put("Core.Auctions.Countdown", true);
    configurations.put("Core.Auctions.CountdownTime", 10);

    configurations.put("Core.Shops.Enabled", true);
    configurations.put("Core.Shops.Cost", 10.00);
    configurations.put("Core.Shops.Max", 5);
    configurations.put("Core.Shops.Rows", 3);
    configurations.put("Core.Shops.Shoppers", 10);
    configurations.put("Core.Shops.Shares.Enabled", true);
    configurations.put("Core.Shops.Shares.Max", 3);

    configurations.put("Core.Signs.Bank.Enabled", false);
    configurations.put("Core.Signs.Bank.Place", 20.0);
    configurations.put("Core.Signs.Bank.Use", 20.0);
    configurations.put("Core.Signs.Shop.Enabled", false);
    configurations.put("Core.Signs.Shop.Place", 20.0);
    configurations.put("Core.Signs.Shop.Use", 20.0);
    configurations.put("Core.Signs.Sell.Enabled", false);
    configurations.put("Core.Signs.Sell.Place", 20.0);
    configurations.put("Core.Signs.Sell.Use", 20.0);
    configurations.put("Core.Signs.Sell.Max", 10);
    configurations.put("Core.Signs.Buy.Enabled", false);
    configurations.put("Core.Signs.Buy.Place", 20.0);
    configurations.put("Core.Signs.Buy.Use", 20.0);
    configurations.put("Core.Signs.Buy.Max", 10);

    configurations.put("Core.Death.Lose", false);
    configurations.put("Core.Death.Bank.Drop", 0);
    configurations.put("Core.Death.Bank.IncludeEmpty", true);
    configurations.put("Core.Death.Bank.PlayerOnly", true);

    configurations.put("Core.Bank.Enabled", false);
    configurations.put("Core.Bank.Sign", false);
    configurations.put("Core.Bank.Command", true);
    configurations.put("Core.Bank.NPC", false);
    configurations.put("Core.Bank.Connected", false);
    configurations.put("Core.Bank.Cost", 20.0);
    configurations.put("Core.Bank.Rows", 3);
    configurations.put("Core.Bank.Interest.Enabled", false);
    configurations.put("Core.Bank.Interest.Rate", 0.2);
    configurations.put("Core.Bank.Interest.Interval", 1800);

    configurations.put("Core.World.EnableChangeFee", false);
    configurations.put("Core.World.ChangeFee", 5.0);

    configurations.put("Core.Database.Type", "FlatFile");
    configurations.put("Core.Database.Prefix", "TNE");
    configurations.put("Core.Database.Backup", true);
    configurations.put("Core.Database.FlatFile.File", "economy.tne");
    configurations.put("Core.Database.Transactions.Cache", true);
    configurations.put("Core.Database.Transactions.Update", 600);
    configurations.put("Core.Database.MySQL.Host", "localhost");
    configurations.put("Core.Database.MySQL.Port", 3306);
    configurations.put("Core.Database.MySQL.Database", "TheNewEconomy");
    configurations.put("Core.Database.MySQL.User", "user");
    configurations.put("Core.Database.MySQL.Password", "password");
    configurations.put("Core.Database.H2.File", "Economy");
    configurations.put("Core.Database.SQLite.File", "economy.db");

    configurations.put("Core.Conversion.Convert", false);
    configurations.put("Core.Conversion.Name", "iConomy");
    configurations.put("Core.Conversion.Format", "MySQL");
    configurations.put("Core.Conversion.Options.Host", "localhost");
    configurations.put("Core.Conversion.Options.Port", 3306);
    configurations.put("Core.Conversion.Options.Database", "sql_eco");
    configurations.put("Core.Conversion.Options.User", "root");
    configurations.put("Core.Conversion.Options.Password", "Password");

    super.load(configurationFile);
  }

}