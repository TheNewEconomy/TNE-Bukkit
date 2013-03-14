package com.github.tnerevival.properties;

import com.github.tnerevival.TheNewEconomy;


public class TNEMobDropProperties {
	
	String mobPropertiesFile = TheNewEconomy.pluginDirectory + "/mobdrops.properties";
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
	
	/**
	 * @return the dropBat
	 */
	public static double getDropBat() {
		return dropBat;
	}

	/**
	 * @return the dropBlaze
	 */
	public static double getDropBlaze() {
		return dropBlaze;
	}

	/**
	 * @return the dropCaveSpider
	 */
	public static double getDropCaveSpider() {
		return dropCaveSpider;
	}

	/**
	 * @return the dropChicken
	 */
	public static double getDropChicken() {
		return dropChicken;
	}

	/**
	 * @return the dropCow
	 */
	public static double getDropCow() {
		return dropCow;
	}

	/**
	 * @return the dropCreeper
	 */
	public static double getDropCreeper() {
		return dropCreeper;
	}

	/**
	 * @return the dropEnderDragon
	 */
	public static double getDropEnderDragon() {
		return dropEnderDragon;
	}

	/**
	 * @return the dropEnderman
	 */
	public static double getDropEnderman() {
		return dropEnderman;
	}

	/**
	 * @return the dropGhast
	 */
	public static double getDropGhast() {
		return dropGhast;
	}

	/**
	 * @return the dropGiant
	 */
	public static double getDropGiant() {
		return dropGiant;
	}

	/**
	 * @return the dropIronGolem
	 */
	public static double getDropIronGolem() {
		return dropIronGolem;
	}

	/**
	 * @return the dropMagmaCube
	 */
	public static double getDropMagmaCube() {
		return dropMagmaCube;
	}

	/**
	 * @return the dropMushroomCow
	 */
	public static double getDropMushroomCow() {
		return dropMushroomCow;
	}

	/**
	 * @return the dropOcelot
	 */
	public static double getDropOcelot() {
		return dropOcelot;
	}

	/**
	 * @return the dropPig
	 */
	public static double getDropPig() {
		return dropPig;
	}

	/**
	 * @return the dropPigZombie
	 */
	public static double getDropPigZombie() {
		return dropPigZombie;
	}

	/**
	 * @return the dropSheep
	 */
	public static double getDropSheep() {
		return dropSheep;
	}

	/**
	 * @return the dropSilverfish
	 */
	public static double getDropSilverfish() {
		return dropSilverfish;
	}

	/**
	 * @return the dropSkeleton
	 */
	public static double getDropSkeleton() {
		return dropSkeleton;
	}

	/**
	 * @return the dropSlime
	 */
	public static double getDropSlime() {
		return dropSlime;
	}

	/**
	 * @return the dropSnowman
	 */
	public static double getDropSnowman() {
		return dropSnowman;
	}

	/**
	 * @return the dropSpider
	 */
	public static double getDropSpider() {
		return dropSpider;
	}

	/**
	 * @return the dropSquid
	 */
	public static double getDropSquid() {
		return dropSquid;
	}

	/**
	 * @return the dropVillager
	 */
	public static double getDropVillager() {
		return dropVillager;
	}

	/**
	 * @return the dropWitch
	 */
	public static double getDropWitch() {
		return dropWitch;
	}

	/**
	 * @return the dropWither
	 */
	public static double getDropWither() {
		return dropWither;
	}

	/**
	 * @return the dropWolf
	 */
	public static double getDropWolf() {
		return dropWolf;
	}

	/**
	 * @return the dropZombie
	 */
	public static double getDropZombie() {
		return dropZombie;
	}

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
