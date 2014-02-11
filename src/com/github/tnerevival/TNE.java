package com.github.tnerevival;

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
		
		loadConfiguration();
		

		//Listeners
		getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
		getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
		getServer().getPluginManager().registerEvents(new WorldListener(this), this);
		
		//Commands
		getCommand("bank").setExecutor(new BankExecutor(this));
		getCommand("money").setExecutor(new MoneyExecutor(this));
		
		getLogger().info("[TNE] TheNewEconomy v1.1 has been enabled!");
		getLogger().info("[TNE]Default World: " + defaultWorld);
	}
	
	public void onDisable() {
		if(getConfig().getBoolean("Core.AutoSaver.Enabled")) {
			autoSave.cancelTask();
		}
		manager.saveData();
		getLogger().info("[TNE] TheNewEconomy v1.1 has been disabled!");
	}
	
	private void loadConfiguration(){
	     getConfig().options().copyDefaults(true);
	     saveConfig();
	}
}