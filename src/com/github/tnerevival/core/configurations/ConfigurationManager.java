package com.github.tnerevival.core.configurations;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.tnerevival.TNE;

public class ConfigurationManager {
	
	public HashMap<String, Configuration> configurations = new HashMap<String, Configuration>();
	
	public ConfigurationManager() {
		MainConfiguration main = new MainConfiguration();
		MessageConfiguration message = new MessageConfiguration();
		MobConfiguration mob = new MobConfiguration();
		ObjectConfiguration objects = new ObjectConfiguration();
		MaterialsConfiguration materials = new MaterialsConfiguration();
		main.load(TNE.instance.getConfig());
		message.load(TNE.instance.messageConfigurations);
		mob.load(TNE.instance.mobConfigurations);
		objects.load(TNE.instance.objectConfigurations);
		materials.load(TNE.instance.materialConfigurations);
		
		configurations.put("main", main);
		configurations.put("mob", mob);
		configurations.put("messages", message);
		configurations.put("objects", objects);
		configurations.put("materials", materials);
	}
	
	public Boolean mobEnabled(String mob) {
		return getBoolean("Mobs." + mob + ".Enabled", "mob");
	}
	
	public Double mobReward(String mob) {
		return getDouble("Mobs." + mob + ".Reward", "mob");
	}
	
	public Configuration getConfiguration(String id) {
		return configurations.get(id);
	}
	
	public ObjectConfiguration getObjectConfiguration() {
		return (ObjectConfiguration)getConfiguration("objects");
	}
	
	public MaterialsConfiguration getMaterialsConfiguration() {
		return (MaterialsConfiguration)getConfiguration("materials");
	}
	
	public void load(FileConfiguration configurationFile, String configID) {
		getConfiguration(configID).load(configurationFile);
	}
	
	public void save(FileConfiguration configurationFile, String configID) {
		getConfiguration(configID).save(configurationFile);
	}
	
	public String getMessage(String node) {
		return getString(node,"messages");
	}
	
	public Boolean getBoolean(String node) {
		return getBoolean(node, "main");
	}
	
	public Integer getInt(String node) {
		return getInt(node, "main");
	}
	
	public Double getDouble(String node) {
		return getDouble(node, "main");
	}
	
	public Long getLong(String node) {
		return getLong(node, "main");
	}
	
	public String getString(String node) {
		return getString(node, "main");
	}
	
	public Boolean getBoolean(String node, String configID) {
		return (Boolean)getConfiguration(configID).getValue(node);
	}
	
	public Integer getInt(String node, String configID) {
		return (Integer)getConfiguration(configID).getValue(node);
	}
	
	public Double getDouble(String node, String configID) {
		return (Double)getConfiguration(configID).getValue(node);
	}
	
	public Long getLong(String node, String configID) {
		return Long.valueOf(((Integer)getConfiguration(configID).getValue(node)).longValue());
	}
	
	public String getString(String node, String configID) {
		return (String)getConfiguration(configID).getValue(node);
	}
}