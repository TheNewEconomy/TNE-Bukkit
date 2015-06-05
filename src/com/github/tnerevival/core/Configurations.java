package com.github.tnerevival.core;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.configuration.file.FileConfiguration;

public class Configurations {
	HashMap<String, Object> mainConfigurations = new HashMap<String, Object>();
	HashMap<String, Object> mobsConfigurations = new HashMap<String, Object>();
	
	public Configurations() {
		initialize();
	}
	
	private void initialize() {
		mainConfigurations.put("Core.Multiworld", false);
		mainConfigurations.put("Core.Balance", 200.0);
		mainConfigurations.put("Core.Shorten", true);
		mainConfigurations.put("Core.Metrics", true);
		mainConfigurations.put("Core.AutoSaver.Enabled", true);
		mainConfigurations.put("Core.AutoSaver.Interval", 600);
		mainConfigurations.put("Core.Currency.Advanced", false);
		mainConfigurations.put("Core.Currency.ItemCurrency", false);
		mainConfigurations.put("Core.Currency.ItemMajor", "GOLD_INGOT");
		mainConfigurations.put("Core.Currency.ItemMinor", "IRON_INGOT");
		mainConfigurations.put("Core.Currency.MajorName.Single", "Dollar");
		mainConfigurations.put("Core.Currency.MajorName.Plural", "Dollars");
		mainConfigurations.put("Core.Currency.MinorName.Single", "Cent");
		mainConfigurations.put("Core.Currency.MinorName.Plural", "Cents");
		mainConfigurations.put("Core.Death.Lose", false);
		mainConfigurations.put("Core.Death.Robbing", false);
		mainConfigurations.put("Core.Death.RobbingInterval", 600);
		mainConfigurations.put("Core.Company.Enabled", true);
		mainConfigurations.put("Core.Company.Cost", 20.0);
		mainConfigurations.put("Core.Company.InitialBalance", 100.0);
		mainConfigurations.put("Core.Company.PayRate", 2400);
		mainConfigurations.put("Core.Company.Failure", -2000.0);
		mainConfigurations.put("Core.Bank.Enabled", false);
		mainConfigurations.put("Core.Bank.Sign", false);
		mainConfigurations.put("Core.Bank.Command", true);
		mainConfigurations.put("Core.Bank.NPC", false);
		mainConfigurations.put("Core.Bank.Cost", 20.0);
		mainConfigurations.put("Core.Bank.Rows", 3);
		mainConfigurations.put("Core.Bank.Interest.Enabled", false);
		mainConfigurations.put("Core.Bank.Interest.Rate", 0.2);
		mainConfigurations.put("Core.Bank.Interest.Interval", 1800);
		mainConfigurations.put("Core.World.EnableChangeFee", false);
		mainConfigurations.put("Core.World.ChangeFee", 5.0);
		mainConfigurations.put("Core.Database.Type", "FlatFile");
		mainConfigurations.put("Core.Database.Prefix", "TNE");
		mainConfigurations.put("Core.Database.Backup", true);
		mainConfigurations.put("Core.Database.FlatFile.File", "economy.tne");
		mainConfigurations.put("Core.Database.MySQL.Host", "localhost");
		mainConfigurations.put("Core.Database.MySQL.Port", "3306");
		mainConfigurations.put("Core.Database.MySQL.Database", "TheNewEconomy");
		mainConfigurations.put("Core.Database.MySQL.User", "user");
		mainConfigurations.put("Core.Database.MySQL.Password", "password");
		mainConfigurations.put("Core.Database.SQLite.File", "economy.db");
		
		mobsConfigurations.put("Mobs.Enabled", true);
		mobsConfigurations.put("Mobs.Default.Enabled", true);
		mobsConfigurations.put("Mobs.Default.Reward", 10.00);
		mobsConfigurations.put("Mobs.Bat.Enabled", true);
		mobsConfigurations.put("Mobs.Bat.Reward", 10.00);
		mobsConfigurations.put("Mobs.Blaze.Enabled", true);
		mobsConfigurations.put("Mobs.Blaze.Reward", 10.00);
		mobsConfigurations.put("Mobs.CaveSpider.Enabled", true);
		mobsConfigurations.put("Mobs.CaveSpider.Reward", 10.00);
		mobsConfigurations.put("Mobs.Chicken.Enabled", true);
		mobsConfigurations.put("Mobs.Chicken.Reward", 10.00);
		mobsConfigurations.put("Mobs.Cow.Enabled", true);
		mobsConfigurations.put("Mobs.Cow.Reward", 10.00);
		mobsConfigurations.put("Mobs.Creeper.Enabled", true);
		mobsConfigurations.put("Mobs.Creeper.Reward", 10.00);
		mobsConfigurations.put("Mobs.EnderDragon.Enabled", true);
		mobsConfigurations.put("Mobs.EnderDragon.Reward", 10.00);
		mobsConfigurations.put("Mobs.Enderman.Enabled", true);
		mobsConfigurations.put("Mobs.Enderman.Reward", 10.00);
		mobsConfigurations.put("Mobs.Ghast.Enabled", true);
		mobsConfigurations.put("Mobs.Ghast.Reward", 10.00);
		mobsConfigurations.put("Mobs.Giant.Enabled", true);
		mobsConfigurations.put("Mobs.Giant.Reward", 10.00);
		mobsConfigurations.put("Mobs.Horse.Enabled", true);
		mobsConfigurations.put("Mobs.Horse.Reward", 10.00);
		mobsConfigurations.put("Mobs.IronGolem.Enabled", true);
		mobsConfigurations.put("Mobs.IronGolem.Reward", 10.00);
		mobsConfigurations.put("Mobs.MagmaCube.Enabled", true);
		mobsConfigurations.put("Mobs.MagmaCube.Reward", 10.00);
		mobsConfigurations.put("Mobs.Mooshroom.Enabled", true);
		mobsConfigurations.put("Mobs.Mooshroom.Reward", 10.00);
		mobsConfigurations.put("Mobs.Ocelot.Enabled", true);
		mobsConfigurations.put("Mobs.Ocelot.Reward", 10.00);
		mobsConfigurations.put("Mobs.Pig.Enabled", true);
		mobsConfigurations.put("Mobs.Pig.Reward", 10.00);
		mobsConfigurations.put("Mobs.Sheep.Enabled", true);
		mobsConfigurations.put("Mobs.Sheep.Reward", 10.00);
		mobsConfigurations.put("Mobs.Silverfish.Enabled", true);
		mobsConfigurations.put("Mobs.Silverfish.Reward", 10.00);
		mobsConfigurations.put("Mobs.Skeleton.Enabled", true);
		mobsConfigurations.put("Mobs.Skeleton.Reward", 10.00);
		mobsConfigurations.put("Mobs.Slime.Enabled", true);
		mobsConfigurations.put("Mobs.Slime.Reward", 10.00);
		mobsConfigurations.put("Mobs.SnowGolem.Enabled", true);
		mobsConfigurations.put("Mobs.SnowGolem.Reward", 10.00);
		mobsConfigurations.put("Mobs.Spider.Enabled", true);
		mobsConfigurations.put("Mobs.Spider.Reward", 10.00);
		mobsConfigurations.put("Mobs.Squid.Enabled", true);
		mobsConfigurations.put("Mobs.Squid.Reward", 10.00);
		mobsConfigurations.put("Mobs.Villager.Enabled", true);
		mobsConfigurations.put("Mobs.Villager.Reward", 10.00);
		mobsConfigurations.put("Mobs.Witch.Enabled", true);
		mobsConfigurations.put("Mobs.Witch.Reward", 10.00);
		mobsConfigurations.put("Mobs.Wither.Enabled", true);
		mobsConfigurations.put("Mobs.Wither.Reward", 10.00);
		mobsConfigurations.put("Mobs.WitherSkeleton.Enabled", true);
		mobsConfigurations.put("Mobs.WitherSkeleton.Reward", 10.00);
		mobsConfigurations.put("Mobs.Wolf.Enabled", true);
		mobsConfigurations.put("Mobs.Wolf.Reward", 10.00);
		mobsConfigurations.put("Mobs.Zombie.Enabled", true);
		mobsConfigurations.put("Mobs.Zombie.Reward", 10.00);
		mobsConfigurations.put("Mobs.ZombiePigman.Enabled", true);
		mobsConfigurations.put("Mobs.ZombiePigman.Reward", 10.00);
		mobsConfigurations.put("Mobs.ZombieVillager.Enabled", true);
		mobsConfigurations.put("Mobs.ZombieVillager.Reward", 10.00);
	}
	
	public Object getValue(String node, Boolean main) {
		Iterator<java.util.Map.Entry<String, Object>> it;
		it = (main) ? mainConfigurations.entrySet().iterator() : mobsConfigurations.entrySet().iterator();
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(entry.getKey().equalsIgnoreCase(node)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public void setValue(String node, Object value, Boolean main) {
		Iterator<java.util.Map.Entry<String, Object>> it;
		it = (main) ? mainConfigurations.entrySet().iterator() : mobsConfigurations.entrySet().iterator();
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(entry.getKey().equalsIgnoreCase(node)) {
				entry.setValue(value);
			}
		}
	}
	
	public Boolean getBoolean(String node) {
		return (Boolean)getValue(node, true);
	}
	
	public Integer getInt(String node) {
		return (Integer)getValue(node, true);
	}
	
	public Double getDouble(String node) {
		return (Double)getValue(node, true);
	}
	
	public Long getLong(String node) {
		return Long.valueOf(((Integer)getValue(node, true)).longValue());
	}
	
	public String getString(String node) {
		return (String)getValue(node, true);
	}
	
	public Boolean mobEnabled(String mob) {
		return (Boolean)getValue("Mobs." + mob + ".Enabled", false);
	}
	
	public Double mobReward(String mob) {
		return (Double)getValue("Mobs." + mob + ".Reward", false);
	}
	
	public void load(FileConfiguration configuration, Boolean main) {
		Iterator<java.util.Map.Entry<String, Object>> it;
		it = (main) ? mainConfigurations.entrySet().iterator() : mobsConfigurations.entrySet().iterator();
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(configuration.contains(entry.getKey())) {
				setValue(entry.getKey(), configuration.get(entry.getKey()), main);
			}
		}
	}
	
	public void save(FileConfiguration configuration, Boolean main) {
		Iterator<java.util.Map.Entry<String, Object>> it;
		it = (main) ? mainConfigurations.entrySet().iterator() : mobsConfigurations.entrySet().iterator();
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(configuration.contains(entry.getKey())) {
				configuration.set(entry.getKey(), entry.getValue());
			}
		}
	}
}