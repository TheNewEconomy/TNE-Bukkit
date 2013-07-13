package com.github.tnerevival;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.tnerevival.core.Economy;
import com.github.tnerevival.core.listeners.PlayerListener;
import com.github.tnerevival.core.listeners.SignListener;

/**
 * Main Class for TNE.
 * @author creatorfromhell
 *
 */
public class TheNewEconomy extends JavaPlugin {
	
	public static TheNewEconomy instance;
	public Economy eco;
	
	File configFile;
	public YamlConfiguration config;

	public void onEnable() {
		instance = this;
		configFile = new File(getDataFolder(), "config.yml");
		config = new YamlConfiguration();
		
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		if(!configFile.exists()) {
			configFile.mkdir();
		}
		
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
		loadYamlFiles();
		eco = new Economy();
	}
	
	public void onDisable() {
		saveYamlFiles();
	}
	
	private void loadYamlFiles() {
		try {
			config.load(configFile);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveYamlFiles() {
		try {
			config.save(configFile);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}