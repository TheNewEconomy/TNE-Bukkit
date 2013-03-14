package com.github.tnerevival.properties;


public class TNEMobDropProperties {
	
	String mobPropertiesFile = "mobdrops.properties";
	String mobPropertiesTitle = "~~~The New Economy Mob Drop Properties~~~";
	TNEProperties mobProperties = new TNEProperties(mobPropertiesFile, mobPropertiesTitle);
	
	static double dropBat;
	static double dropBlaze;
	static double dropCaveSpider;
	static double dropChicken;
	static double dropCow;
	static double dropCreeper;
	static double dropEnderDragon;
	static double dropEnderman;
	static double dropGhast;
	static double dropGiant;
	static double dropIronGolem;
	static double dropMagmaCube;
	static double dropMushroomCow;
	static double dropOcelot;
	static double dropPig;
	static double dropPigZombie;
	static double dropSheep;
	static double dropSilverfish;
	static double dropSkeleton;
	static double dropSlime;
	static double dropSnowman;
	static double dropSpider;
	static double dropSquid;
	static double dropVillager;
	static double dropWitch;
	static double dropWither;
	static double dropWolf;
	static double dropZombie;
	
	public TNEMobDropProperties() {
		
		values(mobProperties);
		work(mobProperties);
		
	}

	void work(TNEProperties prop) {
		
		try {
			prop.initiate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	void values(TNEProperties prop) {
		
		dropBat = prop.getDouble("Bat-Drop", 0);
		dropBlaze = prop.getDouble("Blaze-Drop", 0);
		dropCaveSpider = prop.getDouble("CaveSpider-Drop", 0);
		dropChicken = prop.getDouble("Chicken-Drop", 0);
		dropCow = prop.getDouble("Cow-Drop", 0);
		dropCreeper = prop.getDouble("Creeper-Drop", 0);
		dropEnderDragon = prop.getDouble("EnderDragon-Drop", 0);
		dropEnderman = prop.getDouble("Enderman-Drop", 0);
		dropGhast = prop.getDouble("Ghast-Drop", 0);
		dropGiant = prop.getDouble("Giant-Drop", 0);
		dropIronGolem = prop.getDouble("IronGolem-Drop", 0);
		dropMagmaCube = prop.getDouble("MagmaCube-Drop", 0);
		dropMushroomCow = prop.getDouble("Mooshroom-Drop", 0);
		dropOcelot = prop.getDouble("Ocelot-Drop", 0);
		dropPig = prop.getDouble("Pig-Drop", 0);
		dropPigZombie = prop.getDouble("ZombiePigman-Drop", 0);
		dropSheep = prop.getDouble("Sheep-Drop", 0);
		dropSilverfish = prop.getDouble("Silverfish-Drop", 0);
		dropSkeleton = prop.getDouble("Skeleton-Drop", 0);
		dropSlime = prop.getDouble("Slime-Drop", 0);
		dropSnowman = prop.getDouble("Snowman-Drop", 0);
		dropSpider = prop.getDouble("Spider-Drop", 0);
		dropSquid = prop.getDouble("Squid-Drop", 0);
		dropVillager = prop.getDouble("Villager-Drop", 0);
		dropWitch = prop.getDouble("Witch-Drop", 0);
		dropWither = prop.getDouble("Wither-Drop", 0);
		dropWolf = prop.getDouble("Wolf-Drop", 0);
		dropZombie = prop.getDouble("Zombie-Drop", 0);
		
	}
}
