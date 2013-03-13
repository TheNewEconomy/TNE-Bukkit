package com.github.tnerevival.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class TNEProperties extends Properties {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String propertiesFileName;
	
	public TNEProperties(String name) {
		this.propertiesFileName = name;
	}
	
	public void load() {
		
		File f = new File(this.propertiesFileName);
		if(f.exists()) {
			try {
				load(new FileInputStream(this.propertiesFileName));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void save(String title) {
		
		File f = new File(this.propertiesFileName);
		if(!f.exists()) {
			try {
				store(new FileOutputStream(this.propertiesFileName), title);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void setComment(String propertyKey, String comment) {
		
		String propertyComment = "#" + comment;
		
	}
	
	public double getDouble(String key, double value) {
		
		if (containsKey(key)) {
			return Double.parseDouble(getProperty(key));
		}
		return value;
		
	}
	
	public double setDouble(String key, double value) {
		
		if(!containsKey(key)) {
			put(key, String.valueOf(value));
			return value;
		}
		return value;
		
	}
	
	public int getInteger(String key, int value) {
		
		if (containsKey(key)) {
			return Integer.parseInt(getProperty(key));
		}
		return value;
		
	}
	
	public int setInteger(String key, int value) {
		
		if (!containsKey(key)) {
			put(key, String.valueOf(value));
			return value;
		}
		return value;
		
	}
	
	public String getString(String key, String value) {
		
		if (containsKey(key)) {
			return getProperty(key);
		}
		return value;
		
	}
	
	public String setString(String key, String value) {
		
		if (!containsKey(key)) {
			put(key, value);
			return value;
		}
		return value;
		
	}
	
	public Boolean getBoolean(String key, Boolean value) {
		
		if (containsKey(key)) {
			return Boolean.parseBoolean(getProperty(key));
		}
		return value;
		
	}
	
	public Boolean setBoolean(String key, Boolean value) {
		
		if (!containsKey(key)) {
			put(key, String.valueOf(value));
			return value;
		}
		return value;
		
	}
	
}
