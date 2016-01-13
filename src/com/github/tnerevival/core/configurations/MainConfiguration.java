package com.github.tnerevival.core.configurations;

import org.bukkit.configuration.file.FileConfiguration;

public class MainConfiguration extends Configuration {

	@Override
	public void load(FileConfiguration configurationFile) {	
		configurations.put("Core.UUID", true);	
		configurations.put("Core.Multiworld", false);
		configurations.put("Core.Balance", 200.0);
		configurations.put("Core.Shorten", true);
		configurations.put("Core.Metrics", true);
		configurations.put("Core.Update.Check", true);
		configurations.put("Core.Update.Notify", true);
		configurations.put("Core.AutoSaver.Enabled", true);
		configurations.put("Core.AutoSaver.Interval", 600);
		configurations.put("Core.Currency.Advanced", false);
		configurations.put("Core.Currency.ItemCurrency", false);
		configurations.put("Core.Currency.ItemMajor", "GOLD_INGOT");
		configurations.put("Core.Currency.ItemMinor", "IRON_INGOT");
		configurations.put("Core.Currency.MajorName.Single", "Dollar");
		configurations.put("Core.Currency.MajorName.Plural", "Dollars");
		configurations.put("Core.Currency.MinorName.Single", "Cent");
		configurations.put("Core.Currency.MinorName.Plural", "Cents");
		configurations.put("Core.Death.Lose", false);
		configurations.put("Core.Death.Robbing", false);
		configurations.put("Core.Death.RobbingInterval", 600);
		configurations.put("Core.Company.Enabled", true);
		configurations.put("Core.Company.Cost", 20.0);
		configurations.put("Core.Company.InitialBalance", 100.0);
		configurations.put("Core.Company.PayRate", 2400);
		configurations.put("Core.Company.Failure", -2000.0);
		configurations.put("Core.Bank.Enabled", false);
		configurations.put("Core.Bank.Sign", false);
		configurations.put("Core.Bank.Command", true);
		configurations.put("Core.Bank.NPC", false);
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
		configurations.put("Core.Database.MySQL.Host", "localhost");
		configurations.put("Core.Database.MySQL.Port", 3306);
		configurations.put("Core.Database.MySQL.Database", "TheNewEconomy");
		configurations.put("Core.Database.MySQL.User", "user");
		configurations.put("Core.Database.MySQL.Password", "password");
		configurations.put("Core.Database.SQLite.File", "economy.db");
		
		super.load(configurationFile);
	}

}