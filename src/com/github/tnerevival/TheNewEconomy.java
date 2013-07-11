package com.github.tnerevival;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TheNewEconomy extends JavaPlugin {
	
	public static TheNewEconomy instance;
	
	File configFile;
	public YamlConfiguration config;

	public void onEnabled() {
		instance = this;
		configFile = new File(getDataFolder(), "config.yml");
		config = new YamlConfiguration();
		
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		if(!configFile.exists()) {
			configFile.mkdir();
		}
		loadYamlFiles();
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