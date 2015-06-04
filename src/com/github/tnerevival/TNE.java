package com.github.tnerevival;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.tnerevival.commands.BankExecutor;
import com.github.tnerevival.commands.CoreExecutor;
import com.github.tnerevival.commands.MoneyExecutor;
import com.github.tnerevival.core.EconomyManager;
import com.github.tnerevival.core.SaveManager;
import com.github.tnerevival.core.TNEVaultEconomy;
import com.github.tnerevival.core.api.TNEAPI;
import com.github.tnerevival.listeners.ConnectionListener;
import com.github.tnerevival.listeners.InteractionListener;
import com.github.tnerevival.listeners.WorldListener;
import com.github.tnerevival.worker.InterestWorker;
import com.github.tnerevival.worker.SaveWorker;

public class TNE extends JavaPlugin {
	
	public static TNE instance;
	public EconomyManager manager;
	public SaveManager saveManager;
	public TNEAPI api = null;
	
	public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.S");
	
	// Files & Custom Configuration Files
	public File mobs;
	public File worlds;
	
	public FileConfiguration mobConfigurations;
	public FileConfiguration worldConfigurations;
	
	public String defaultWorld;
	
	/*
	 * Instances of the main runnables.
	 */
	public SaveWorker saveWorker;
	public InterestWorker interestWorker;
	
	public void onEnable() {
		instance = this;
		defaultWorld = Bukkit.getServer().getWorlds().get(0).getName();
		manager = new EconomyManager();
		saveManager = new SaveManager();
		api = new TNEAPI(this);
		setupVault();
		
		//Configurations
		initializeConfigurations();
		loadConfigurations();
		
		saveWorker = new SaveWorker(this);
		interestWorker = new InterestWorker(this);
		if(getConfig().getBoolean("Core.AutoSaver.Enabled")) {
			saveWorker.runTaskTimer(this, getConfig().getLong("Core.AutoSaver.Interval") * 20, getConfig().getLong("Core.AutoSaver.Interval") * 20);
		}
		
		if(getConfig().getBoolean("Core.Bank.Interest.Enabled")) {
			interestWorker.runTaskTimer(this, getConfig().getLong("Core.Bank.Interest.Interval") * 20, getConfig().getLong("Core.Bank.Interest.Interval") * 20);
		}
		

		//Listeners
		getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
		getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
		getServer().getPluginManager().registerEvents(new WorldListener(this), this);
		
		//Commands
		getCommand("bank").setExecutor(new BankExecutor(this));
		getCommand("money").setExecutor(new MoneyExecutor(this));
		getCommand("theneweconomy").setExecutor(new CoreExecutor(this));
		
		if(getConfig().getBoolean("Core.Metrics")) {
			try {
			    MetricsLite metrics = new MetricsLite(this);
			    metrics.start();
			} catch (IOException e) {
			    getLogger().severe("Error while enabling plugin metrics.");
			}
		}
		
		getLogger().info("The New Economy v0.0.2.1 has been enabled!");
	}
	
	public void onDisable() {
		try {
			saveWorker.cancel();
			interestWorker.cancel();
		} catch(IllegalStateException e) {
			//Task was not scheduled
		}
		saveManager.save();
		getLogger().info("The New Economy v0.0.2.1 has been disabled!");
	}
	
	private void initializeConfigurations() {
		mobs = new File(getDataFolder(), "mobs.yml");
		worlds = new File(getDataFolder(), "worlds.yml");
		mobConfigurations = YamlConfiguration.loadConfiguration(mobs);
		worldConfigurations = YamlConfiguration.loadConfiguration(worlds);
		try {
			setConfigurationDefaults();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
	
	private void setConfigurationDefaults() throws UnsupportedEncodingException {
		Reader mobsStream = new InputStreamReader(this.getResource("mobs.yml"), "UTF8");
		Reader worldsStream = new InputStreamReader(this.getResource("worlds.yml"), "UTF8");
	    if (mobsStream != null) {
	        YamlConfiguration config = YamlConfiguration.loadConfiguration(mobsStream);
	        mobConfigurations.setDefaults(config);
	    }
	    
	    if (worldsStream != null) {
	        YamlConfiguration config = YamlConfiguration.loadConfiguration(worldsStream);
	        worldConfigurations.setDefaults(config);
	    }
	}
	
	private void setupVault() {
		if(getServer().getPluginManager().getPlugin("Vault") == null) {
			return;
		}
		
		RegisteredServiceProvider<Economy> economyService = getServer().getServicesManager().getRegistration(Economy.class);
		if(economyService != null) {
            getServer().getServicesManager().unregister(economyService.getProvider());
		}
        getServer().getServicesManager().register(Economy.class, new TNEVaultEconomy(this), this, ServicePriority.Highest);
	}
}