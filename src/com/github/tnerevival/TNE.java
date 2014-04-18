package com.github.tnerevival;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.tnerevival.commands.BankExecutor;
import com.github.tnerevival.commands.CoreExecutor;
import com.github.tnerevival.commands.MoneyExecutor;
import com.github.tnerevival.core.Economy;
import com.github.tnerevival.core.SaveManager;
import com.github.tnerevival.listeners.ConnectionListener;
import com.github.tnerevival.listeners.InteractionListener;
import com.github.tnerevival.listeners.WorldListener;
import com.github.tnerevival.worker.InterestWorker;
import com.github.tnerevival.worker.SaveWorker;

public class TNE extends JavaPlugin {
	
	public static TNE instance;
	public Economy manager;
	public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.S");
	
	// Files & Custom Configuration Files
	//File currency;
	public File mobs;
	public File worlds;
	
	//public FileConfiguration currencyConfigurations;
	public FileConfiguration mobConfigurations;
	public FileConfiguration worldConfigurations;
	
	public String defaultWorld;
	
	public Integer saveWorkerID;
	public Integer interestWorkerID;
	
	/*
	 * Instances of the main runnables.
	 */
	public SaveWorker saveWorker;
	public InterestWorker interestWorker;
	
	public void onEnable() {
		instance = this;
		defaultWorld = getServer().getWorlds().get(0).getName();
		//Configurations
		initializeConfigurations();
		loadConfigurations();
		
		saveWorkerID = -1;
		interestWorkerID = -1;
		
		manager = new Economy();
		manager.saveManager = new SaveManager();
		saveWorker = new SaveWorker(this);
		interestWorker = new InterestWorker(this);
		if(getConfig().getBoolean("Core.AutoSaver.Enabled")) {
			startSaveWorker();
		}
		
		if(getConfig().getBoolean("Core.Bank.Interest.Enabled")) {
			startInterestWorker();
		}
		

		//Listeners
		getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
		getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
		getServer().getPluginManager().registerEvents(new WorldListener(this), this);
		
		//Commands
		getCommand("bank").setExecutor(new BankExecutor(this));
		getCommand("money").setExecutor(new MoneyExecutor(this));
		getCommand("theneweconomy").setExecutor(new CoreExecutor(this));
		
		getLogger().info("TheNewEconomy v2.0 has been enabled!");
	}
	
	public void onDisable() {
		stopSaveWorker();
		stopInterestWorker();
		manager.saveManager.save();
		getLogger().info("TheNewEconomy v2.0 has been disabled!");
	}
	
	public void startSaveWorker() {
		if(saveWorkerID == -1) {
			saveWorkerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, saveWorker, getConfig().getLong("Core.AutoSaver.Interval") * 20, getConfig().getLong("Core.AutoSaver.Interval") * 20);
		}
	}
	
	public void startInterestWorker() {
		if(interestWorkerID == -1) {
			interestWorkerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, interestWorker, getConfig().getLong("Core.Bank.Interest.Interval") * 20, getConfig().getLong("Core.Bank.Interest.Interval") * 20);
		}
	}
	
	public void stopSaveWorker() {
		if(saveWorkerID != null && saveWorkerID != -1) {
			Bukkit.getScheduler().cancelTask(saveWorkerID);
			saveWorkerID = -1;
		}
	}
	
	public void stopInterestWorker() {
		if(interestWorkerID != null && interestWorkerID != -1) {
			Bukkit.getScheduler().cancelTask(interestWorkerID);
			interestWorkerID = -1;
		}
	}
	
	private void initializeConfigurations() {
		//currency = new File(getDataFolder(), "currency.yml");
		mobs = new File(getDataFolder(), "mobs.yml");
		worlds = new File(getDataFolder(), "worlds.yml");
		//currencyConfigurations = YamlConfiguration.loadConfiguration(currency);
		mobConfigurations = YamlConfiguration.loadConfiguration(mobs);
		mobConfigurations.setDefaults(YamlConfiguration.loadConfiguration(getResource("mobs.yml")));
		worldConfigurations = YamlConfiguration.loadConfiguration(worlds);
		worldConfigurations.setDefaults(YamlConfiguration.loadConfiguration(getResource("worlds.yml")));
	}
	
	private void loadConfigurations() {
	     getConfig().options().copyDefaults(true);
	     mobConfigurations.options().copyDefaults(true);
	     worldConfigurations.options().copyDefaults(true);
	     //currencyConfigurations.options().copyDefaults(true);
	     saveConfigurations();
	}
	
	private void saveConfigurations() {
		saveConfig();
		try {
			//currencyConfigurations.save(currency);
			mobConfigurations.save(mobs);
			worldConfigurations.save(worlds);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}