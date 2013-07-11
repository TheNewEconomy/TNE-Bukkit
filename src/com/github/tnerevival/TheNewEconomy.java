package com.github.tnerevival;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.tnerevival.core.Economy;
import com.github.tnerevival.core.listeners.PlayerListener;

public class TheNewEconomy extends JavaPlugin {
	
	public static TheNewEconomy instance;
	public Economy eco;
	
	File configFile;
	File banksFolder;
	public YamlConfiguration config;

	public void onEnable() {
		instance = this;
		//configFile = new File(getDataFolder(), "config.yml");
		banksFolder = new File(getDataFolder(), "Banks");
		//config = new YamlConfiguration();
		
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		/*if(!configFile.exists()) {
			configFile.mkdir();
		}*/
		if(!banksFolder.exists()) {
			banksFolder.mkdir();
		}
		
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		//loadYamlFiles();
		eco = new Economy();
	}
	
	public void onDisable() {
		//saveYamlFiles();
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