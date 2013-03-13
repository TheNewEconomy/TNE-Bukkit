package com.github.tnerevival;

import java.io.File;
import java.util.Map;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class TheNewEconomy extends JavaPlugin {
	
	/**
	 * The plugin's directory.
	 */
	public static File pluginDirectory;
	public PluginDescriptionFile pdf = getDescription();
	public static Map<String, String> properties;
	
	@Override
	public void onEnable() {
		
		TheNewEconomy.pluginDirectory = getDataFolder();
        if(!pluginDirectory.exists()) pluginDirectory.mkdir();
        
        // Register our commands
        getCommand("area").setExecutor(new TheNewEconomyCommands(this));
        getCommand("auction").setExecutor(new TheNewEconomyCommands(this));
        getCommand("bank").setExecutor(new TheNewEconomyCommands(this));
        getCommand("buy").setExecutor(new TheNewEconomyCommands(this));
        getCommand("economy").setExecutor(new TheNewEconomyCommands(this));
        getCommand("lottery").setExecutor(new TheNewEconomyCommands(this));
        getCommand("money").setExecutor(new TheNewEconomyCommands(this));
        getCommand("offer").setExecutor(new TheNewEconomyCommands(this));
        
        getLogger().info(pdf.getName() + " version " + pdf.getVersion() + " is enabled!");
	}
	
	@Override
	public void onDisable(){
		getLogger().info(pdf.getName() + " version " + pdf.getVersion() + " is now disabled!");
	}

}
