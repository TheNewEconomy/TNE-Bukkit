package com.github.tnerevival.core.configurations;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.configuration.file.FileConfiguration;


public abstract class Configuration {
	
	public static HashMap<String, Object> configurations = new HashMap<String, Object>();
	
	public void load(FileConfiguration configurationFile) {
		Iterator<java.util.Map.Entry<String, Object>> it = configurations.entrySet().iterator();
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(configurationFile.contains(entry.getKey())) {
				setValue(entry.getKey(), configurationFile.get(entry.getKey()));
			}
		}
	}
	
	public void save(FileConfiguration configurationFile) {
		Iterator<java.util.Map.Entry<String, Object>> it = configurations.entrySet().iterator();
		
		while(it.hasNext()) {
			java.util.Map.Entry<String, Object> entry = it.next();
			if(configurationFile.contains(entry.getKey())) {
				configurationFile.set(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public Object getValue(String node) {
		return configurations.get(node);
	}
	
	public void setValue(String node, Object value) {
		configurations.put(node, value);
	}
	
}