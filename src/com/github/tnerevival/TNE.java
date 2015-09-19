package com.github.tnerevival;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.tnerevival.commands.CommandManager;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Configurations;
import com.github.tnerevival.core.EconomyManager;
import com.github.tnerevival.core.SaveManager;
import com.github.tnerevival.core.TNEVaultEconomy;
import com.github.tnerevival.core.UpdateChecker;
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
	private CommandManager commandManager;
	public TNEAPI api = null;
	
	public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.S");
	
	// Files & Custom Configuration Files
	public File mobs;
	public File messages;
	public File worlds;
	
	public FileConfiguration mobConfigurations;
	public FileConfiguration messageConfigurations;
	public FileConfiguration worldConfigurations;
	
	public static Configurations configurations;
	public static UpdateChecker updater;
	
	public String defaultWorld;
	
	/*
	 * Instances of the main runnables.
	 */
	public SaveWorker saveWorker;
	public InterestWorker interestWorker;
	
	public void onLoad() {
		api = new TNEAPI(this);
		setupVault();
	}
	
	public void onEnable() {
		instance = this;
		defaultWorld = Bukkit.getServer().getWorlds().get(0).getName();
		updater = new UpdateChecker();
		
		//Configurations
		initializeConfigurations();
		loadConfigurations();
		configurations = new Configurations();
		configurations.load(getConfig(), "main");
		configurations.load(mobConfigurations, "mob");
		configurations.load(messageConfigurations, "messages");
		
		manager = new EconomyManager();
		saveManager = new SaveManager();
		commandManager = new CommandManager();
		
		saveWorker = new SaveWorker(this);
		interestWorker = new InterestWorker(this);
		if(configurations.getBoolean("Core.AutoSaver.Enabled")) {
			saveWorker.runTaskTimer(this, configurations.getLong("Core.AutoSaver.Interval") * 20, configurations.getLong("Core.AutoSaver.Interval") * 20);
		}
		
		if(configurations.getBoolean("Core.Bank.Interest.Enabled")) {
			interestWorker.runTaskTimer(this, configurations.getLong("Core.Bank.Interest.Interval") * 20, configurations.getLong("Core.Bank.Interest.Interval") * 20);
		}
		

		//Listeners
		getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
		getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
		getServer().getPluginManager().registerEvents(new WorldListener(this), this);
		
		if(configurations.getBoolean("Core.Metrics")) {
			try {
			    MetricsLite metrics = new MetricsLite(this);
			    metrics.start();
			} catch (IOException e) {
			    getLogger().severe("Error while enabling plugin metrics.");
			}
		}
		
		getLogger().info("The New Economy v0.0.2.2 Dev Build 5 has been enabled!");
		
		String updateMessage = (updater.latest()) ? "Using the latest version: " + updater.getCurrentBuild() : "Outdated! The current build is " + updater.getCurrentBuild();
		getLogger().info(updateMessage);
	}
	
	public void onDisable() {
		configurations.save(getConfig(), "main");
		configurations.save(mobConfigurations, "mob");
		configurations.save(messageConfigurations, "messages");
		saveConfigurations();
		try {
			saveWorker.cancel();
			interestWorker.cancel();
		} catch(IllegalStateException e) {
			//Task was not scheduled
		}
		saveManager.save();
		getLogger().info("The New Economy v0.0.2.2 has been disabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
		TNECommand ecoCommand = commandManager.Find(label);
		if(ecoCommand != null) {
			if(!ecoCommand.canExecute(sender)) {
				sender.sendMessage(ChatColor.RED + "I'm sorry, but you're not allowed to use that command.");
				return false;
			}
			return ecoCommand.execute(sender, arguments);
		}
		return false;
	}
	
	private void initializeConfigurations() {
		mobs = new File(getDataFolder(), "mobs.yml");
		messages = new File(getDataFolder(), "messages.yml");
		worlds = new File(getDataFolder(), "worlds.yml");
		mobConfigurations = YamlConfiguration.loadConfiguration(mobs);
		messageConfigurations = YamlConfiguration.loadConfiguration(messages);
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
	     messageConfigurations.options().copyDefaults(true);
	     worldConfigurations.options().copyDefaults(true);
	     saveConfigurations();
	}
	
	private void saveConfigurations() {
		saveConfig();
		try {
			mobConfigurations.save(mobs);
			messageConfigurations.save(messages);
			worldConfigurations.save(worlds);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setConfigurationDefaults() throws UnsupportedEncodingException {
		Reader mobsStream = new InputStreamReader(this.getResource("mobs.yml"), "UTF8");
		Reader messagesStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
		Reader worldsStream = new InputStreamReader(this.getResource("worlds.yml"), "UTF8");
	    if (mobsStream != null) {
	        YamlConfiguration config = YamlConfiguration.loadConfiguration(mobsStream);
	        mobConfigurations.setDefaults(config);
	    }
	    
	    if (messagesStream != null) {
	        YamlConfiguration config = YamlConfiguration.loadConfiguration(messagesStream);
	        messageConfigurations.setDefaults(config);
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
        getServer().getServicesManager().register(Economy.class, new TNEVaultEconomy(this), this, ServicePriority.Highest);
        getLogger().info("Hooked into Vault");
	}
}