package com.github.tnerevival.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class TNEProperties extends Properties {
	
	private static final long serialVersionUID = 1L;
	
	private String propertiesFileName;
	private String propertiesFileTitle;
	
	public TNEProperties(String name, String title) {
		this.propertiesFileName = name;
		this.propertiesFileTitle = title;
	}
	
	public void initiate() {
		if(exists()) {
			load();
		} else {
			save();
		}
	}
	
	public void load() {
		
			try {
				load(new FileInputStream(this.propertiesFileName));
			} catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public void save() {
		
			try {
				store(new FileOutputStream(this.propertiesFileName), this.propertiesFileTitle);
			} catch(Exception e) {
				e.printStackTrace();
			}
		
	}
	
	public Boolean exists() {
		
		File f = new File(this.propertiesFileName);
		if(f.exists()) {
			return true;
		}
		return false;
		
	}
	
	public void setComment(String propertyKey, String comment) {
		
		//String propertyComment = "#" + comment;
		
	}
	
	public double getDouble(String key, double value) {
		
		if (containsKey(key)) {
			return Double.parseDouble(getProperty(key));
		}
		return value;
		
	}
	
	public int getInteger(String key, int value) {
		
		if (containsKey(key)) {
			return Integer.parseInt(getProperty(key));
		}
		return value;
		
	}
	
	public String getString(String key, String value) {
		
		if (containsKey(key)) {
			return getProperty(key);
		}
		return value;
		
	}
	
	public Boolean getBoolean(String key, Boolean value) {
		
		if (containsKey(key)) {
			return Boolean.parseBoolean(getProperty(key));
		}
		return value;
		
	}
	
}
