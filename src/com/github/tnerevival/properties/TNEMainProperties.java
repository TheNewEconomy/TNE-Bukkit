package com.github.tnerevival.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

public class TNEMainProperties {
	
	String mainPropertiesFile = "economy.properties";
	TNEProperties mainProperties = new TNEProperties(mainPropertiesFile);
	public static Map<String, String> propertiesMap;
	
	public TNEMainProperties() {
		
		loadProperties(mainProperties);
		
	}

	void loadProperties(TNEProperties prop) {
		
		try {
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	void saveProperties(TNEProperties prop) {
		
		try {
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	void createProperties(TNEProperties prop) {
		
		prop.setString("Currency-Singular", "Dollar");
		prop.setString("Currency-Plural", "Dollars");
		prop.setBoolean("Mob-Money-Drops", false);
		prop.setDouble("Start-Money", 10.0);
		prop.setDouble("Starting-Public-Fund", 1500.0);
		prop.setInteger("Max-Trade-Area-Length", 15);
		prop.setInteger("Max-Trade-Area-Size", 144);
		prop.setInteger("Max-Bank-Area-Length", 15);
		prop.setInteger("Max-Bank-Area-Size", 400);
		prop.setDouble("Trade-Area-Cost", 1.0);
		prop.setDouble("Personal-Area-Cost", 3.0);
		prop.setDouble("Bank-Area-Cost", 5.0);
		prop.setDouble("Transfer-Fee", 0.15);
		prop.setDouble("Rename-Fee", 0.15);
		prop.setInteger("Max-Offers", 10);
		prop.setDouble("Vertical-Cost", 0.01);
		prop.setDouble("Lottery-Interval", 60.0);
		prop.setInteger("Players-Until-Half", 5);
		prop.setInteger("Max-Areas", 20);
		prop.setDouble("Sales-Tax", 0.01);
		prop.setDouble("Min-Auction-Length", 15.0);
		prop.setDouble("Max-Auction-Length", 60.0);
		
	}
	
}