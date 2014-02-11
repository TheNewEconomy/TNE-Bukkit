package com.github.tnerevival;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.tnerevival.commands.BankExecutor;
import com.github.tnerevival.commands.MoneyExecutor;
import com.github.tnerevival.core.Economy;
import com.github.tnerevival.listeners.ConnectionListener;
import com.github.tnerevival.listeners.InteractionListener;
import com.github.tnerevival.listeners.WorldListener;
import com.github.tnerevival.run.AutoSaver;

public class TNE extends JavaPlugin {
	
	public static TNE instance;
	public Economy manager;
	
	// Files & Custom Configuration Files
	File mobs;
	File worlds;
	
	public FileConfiguration mobConfigurations;
	public FileConfiguration worldConfigurations;
	
	public String defaultWorld = getServer().getWorlds().get(0).getName();
	
	/*
	 * Instances of the main runnables.
	 */
	public AutoSaver autoSave;
	
	public void onEnable() {
		instance = this;
		manager = new Economy();
		autoSave = new AutoSaver(this);
		if(getConfig().getBoolean("Core.AutoSaver.Enabled")) {
			autoSave.startTask(getConfig().getLong("Core.AutoSaver.Interval") * 20);
		}
		
		//Configurations
		initializeConfigurations();
		loadConfigurations();
		

		//Listeners
		getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
		getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
		getServer().getPluginManager().registerEvents(new WorldListener(this), this);
		
		//Commands
		getCommand("bank").setExecutor(new BankExecutor(this));
		getCommand("money").setExecutor(new MoneyExecutor(this));
		
		getLogger().info("TheNewEconomy v2.0 has been enabled!");
		getLogger().info("Default World: " + defaultWorld);
	}
	
	public void onDisable() {
		if(getConfig().getBoolean("Core.AutoSaver.Enabled")) {
			autoSave.cancelTask();
		}
		manager.saveData();
		getLogger().info("TheNewEconomy v2.0 has been disabled!");
	}
	
	private void initializeConfigurations() {
		mobs = new File(getDataFolder(), "mobs.yml");
		worlds = new File(getDataFolder(), "worlds.yml");
		mobConfigurations = YamlConfiguration.loadConfiguration(mobs);
		worldConfigurations = YamlConfiguration.loadConfiguration(worlds);
	}
	
	private void loadConfigurations() {
	     getConfig().options().copyDefaults(true);
	     mobConfigurations.options().copyDefaults(true);
	     worldConfigurations.options().copyDefaults(true);
	     saveConfigurations();
	}
	
	private void saveConfigurations() {
		saveConfig();
		try {
			mobConfigurations.save(mobs);
			worldConfigurations.save(worlds);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}