package com.github.tnerevival.core;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.configuration.file.FileConfiguration;

public class Configurations {
	HashMap<String, Object> mainConfigurations = new HashMap<String, Object>();
	HashMap<String, Object> messageConfigurations = new HashMap<String, Object>();
	HashMap<String, Object> mobsConfigurations = new HashMap<String, Object>();
	
	public Configurations() {
		initialize();
	}
	
	private void initialize() {
		mainConfigurations.put("Core.Multiworld", false);
		mainConfigurations.put("Core.Balance", 200.0);
		mainConfigurations.put("Core.Shorten", true);
		mainConfigurations.put("Core.Metrics", true);
		mainConfigurations.put("Core.Update.Check", true);
		mainConfigurations.put("Core.Update.Notify", true);
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
		
		messageConfigurations.put("Messages.Command.Unable", "<red>I'm sorry, but you're not allowed to use that command.");
		messageConfigurations.put("Messages.Command.None", "<yellow>Command $command $arguments could not be found! Try using $command help.");
		messageConfigurations.put("Messages.General.NoPerm", "<red>I'm sorry, but you do not have permission to do that.");
		messageConfigurations.put("Messages.General.Saved", "<yellow>Successfully saved all TNE Data!");
		messageConfigurations.put("Messages.Money.Given", "<white>You were given <gold>$amount<white>.");
		messageConfigurations.put("Messages.Money.Received", "<white>You were paid <gold>$amount <white> by <white> $from.");
		messageConfigurations.put("Messages.Money.Taken", "<white>$from took <gold>$amount<white> from you.");
		messageConfigurations.put("Messages.Money.Insufficient", "<red>I'm sorry, but you do not have <gold>$amount<red>.");
		messageConfigurations.put("Messages.Money.Balance", "<white>You currently have <gold>$amount<white> on you.");
		messageConfigurations.put("Messages.Money.Gave", "<white>Successfully gave $player <gold>$amount<white>.");
		messageConfigurations.put("Messages.Money.Paid", "<white>Successfully paid $player <gold>$amount<white>.");
		messageConfigurations.put("Messages.Money.Took", "<white>Successfully took <gold>$amount<white> from $player.");
		messageConfigurations.put("Messages.Money.Negative", "<red>Amount cannot be a negative value!");
		messageConfigurations.put("Messages.Money.SelfPay", "<red>You can't pay yourself!");
		messageConfigurations.put("Messages.Bank.Already", "<red>You already own a bank!");
		messageConfigurations.put("Messages.Bank.Bought", "<white>Congratulations! You have successfully purchased a bank!");
		messageConfigurations.put("Messages.Bank.Insufficient", "<red>I'm sorry, but you need at least <gold>$amount<red> to create a bank.");
		messageConfigurations.put("Messages.Bank.Overdraw", "<red>I'm sorry, but your bank does not have <gold>$amount<red>.");
		messageConfigurations.put("Messages.Bank.None", "<red>I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
		messageConfigurations.put("Messages.Bank.NoNPC", "<red>I'm sorry, but accessing banks via NPCs has been disabled in this world!");
		messageConfigurations.put("Messages.Bank.NoSign", "<red>I'm sorry, but accessing banks via signs has been disabled in this world!");
		messageConfigurations.put("Messages.Bank.NoCommand", "<red>I'm sorry, but accessing banks via /bank has been disabled in this world!");
		messageConfigurations.put("Messages.Bank.Disabled", "<red>I'm sorry, but banks are disabled in this world.");
		messageConfigurations.put("Messages.Bank.Balance", "<white>You currently have <gold>$amount<white> in your bank.");
		messageConfigurations.put("Messages.Bank.Deposit", "<white>You have deposited <gold>$amount<white> into your bank.");
		messageConfigurations.put("Messages.Bank.Withdraw", "<white>You have withdrawn <gold>$amount<gold> from your bank.");
		messageConfigurations.put("Messages.Bank.Cost", "<white>A bank is currently <gold>$amount<white>.");
		messageConfigurations.put("Messages.Mob.Killed", "<white>You received $reward for killing a <green>$mob<white>.");
		messageConfigurations.put("Messages.Mob.KilledVowel", "<white>You received $reward for killing an <green>$mob<white>.");
		messageConfigurations.put("Messages.World.Change", "<white>You have been charged <gold> $amount<white> for changing worlds.");
		messageConfigurations.put("Messages.World.ChangeFailed", "<red>I'm sorry, but you need at least <gold>$amount<red> to change worlds.");
		
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
		mobsConfigurations.put("Mobs.Endermite.Enabled", true);
		mobsConfigurations.put("Mobs.Endermite.Reward", 10.00);
		mobsConfigurations.put("Mobs.Ghast.Enabled", true);
		mobsConfigurations.put("Mobs.Ghast.Reward", 10.00);
		mobsConfigurations.put("Mobs.Giant.Enabled", true);
		mobsConfigurations.put("Mobs.Giant.Reward", 10.00);
		mobsConfigurations.put("Mobs.Guardian.Enabled", true);
		mobsConfigurations.put("Mobs.Guardian.Reward", 10.00);
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
		mobsConfigurations.put("Mobs.Rabbit.Enabled", true);
		mobsConfigurations.put("Mobs.Rabbit.Reward", 10.00);
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
	
	public Object getValue(String node, String file) {
		Iterator<java.util.Map.Entry<String, Object>> it = getIterator(file);
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(entry.getKey().equalsIgnoreCase(node)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public void setValue(String node, Object value, String file) {
		Iterator<java.util.Map.Entry<String, Object>> it = getIterator(file);
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(entry.getKey().equalsIgnoreCase(node)) {
				entry.setValue(value);
			}
		}
	}
	
	public Boolean getBoolean(String node) {
		return (Boolean)getValue(node, "main");
	}
	
	public Integer getInt(String node) {
		return (Integer)getValue(node, "main");
	}
	
	public Double getDouble(String node) {
		return (Double)getValue(node, "main");
	}
	
	public Long getLong(String node) {
		return Long.valueOf(((Integer)getValue(node, "main")).longValue());
	}
	
	public String getString(String node) {
		return (String)getValue(node, "main");
	}
	
	public String getMessage(String node) {
		return (String)getValue(node, "messages");
	}
	
	public Boolean mobEnabled(String mob) {
		return (Boolean)getValue("Mobs." + mob + ".Enabled", "mob");
	}
	
	public Double mobReward(String mob) {
		return (Double)getValue("Mobs." + mob + ".Reward", "mob");
	}
	
	public void load(FileConfiguration configuration, String file) {
		Iterator<java.util.Map.Entry<String, Object>> it = getIterator(file);
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(configuration.contains(entry.getKey())) {
				setValue(entry.getKey(), configuration.get(entry.getKey()), file);
			}
		}
	}
	
	public void save(FileConfiguration configuration, String file) {
		Iterator<java.util.Map.Entry<String, Object>> it = getIterator(file);
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(configuration.contains(entry.getKey())) {
				configuration.set(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private Iterator<java.util.Map.Entry<String, Object>> getIterator(String file) {
		Iterator<java.util.Map.Entry<String, Object>> it = mainConfigurations.entrySet().iterator();
		switch(file) {
			case "main":
				it = mainConfigurations.entrySet().iterator();
				break;
			case "mob":
				it = mobsConfigurations.entrySet().iterator();
				break;
			case "messages":
				it = messageConfigurations.entrySet().iterator();
				break;
			default:
				break;
		}
		return it;
	}
}