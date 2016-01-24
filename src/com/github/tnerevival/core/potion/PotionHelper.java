package com.github.tnerevival.core.potion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PotionHelper {
	//Potion Recipes
	public static List<PotionRecipeHelper> recipes = new ArrayList<PotionRecipeHelper>();
	public static List<PotionIDHelper> ids = new ArrayList<PotionIDHelper>();
	
	static {
		//Base Potions
		recipes.add(new PotionRecipeHelper("Awkward", Material.NETHER_WARTS, 0));
		recipes.add(new PotionRecipeHelper("Weakness", Material.FERMENTED_SPIDER_EYE, 0));
		recipes.add(new PotionRecipeHelper("NightVision", Material.GOLDEN_CARROT, 16));
		recipes.add(new PotionRecipeHelper("Leaping", Material.RABBIT_FOOT, 16));
		recipes.add(new PotionRecipeHelper("FireResistance", Material.MAGMA_CREAM, 16));
		recipes.add(new PotionRecipeHelper("Swiftness", Material.SUGAR, 16));
		recipes.add(new PotionRecipeHelper("WaterBreathing", Material.RAW_FISH, 16));
		recipes.add(new PotionRecipeHelper("Healing", Material.SPECKLED_MELON, 16));
		recipes.add(new PotionRecipeHelper("Poison", Material.SPIDER_EYE, 16));
		recipes.add(new PotionRecipeHelper("Regeneration", Material.GHAST_TEAR, 16));
		recipes.add(new PotionRecipeHelper("Strength", Material.BLAZE_POWDER, 16));
		recipes.add(new PotionRecipeHelper("Invisibility", Material.FERMENTED_SPIDER_EYE, 8198));
		recipes.add(new PotionRecipeHelper("Slowness", Material.FERMENTED_SPIDER_EYE, 8203));
		recipes.add(new PotionRecipeHelper("Slowness", Material.FERMENTED_SPIDER_EYE, 8195));
		recipes.add(new PotionRecipeHelper("Slowness", Material.FERMENTED_SPIDER_EYE, 8194));
		recipes.add(new PotionRecipeHelper("Harming", Material.FERMENTED_SPIDER_EYE, 8205));
		recipes.add(new PotionRecipeHelper("Harming", Material.FERMENTED_SPIDER_EYE, 8197));
		recipes.add(new PotionRecipeHelper("Harming", Material.FERMENTED_SPIDER_EYE, 8196));
		
		//Extended Potions
		recipes.add(new PotionRecipeHelper("NightVisionExtended", Material.REDSTONE, 8198));
		recipes.add(new PotionRecipeHelper("InvisibilityExtended", Material.REDSTONE, 8206));
		recipes.add(new PotionRecipeHelper("LeapingExtended", Material.REDSTONE, 8203));
		recipes.add(new PotionRecipeHelper("LeapingPotent", Material.GLOWSTONE_DUST, 8203));
		recipes.add(new PotionRecipeHelper("FireResistanceExtended", Material.REDSTONE, 8195));
		recipes.add(new PotionRecipeHelper("SwiftnessExtended", Material.REDSTONE, 8194));
		recipes.add(new PotionRecipeHelper("SwiftnessPotent", Material.GLOWSTONE_DUST, 8194));
		recipes.add(new PotionRecipeHelper("SlownessExtended", Material.REDSTONE, 8202));
		recipes.add(new PotionRecipeHelper("SlownessExtended", Material.FERMENTED_SPIDER_EYE, 8258));
		recipes.add(new PotionRecipeHelper("SlownessExtended", Material.FERMENTED_SPIDER_EYE, 8259));
		recipes.add(new PotionRecipeHelper("WaterBreathingExtended", Material.REDSTONE, 8205));
		recipes.add(new PotionRecipeHelper("HealingPotent", Material.GLOWSTONE_DUST, 8197));
		recipes.add(new PotionRecipeHelper("PosionExtended", Material.REDSTONE, 8196));
		recipes.add(new PotionRecipeHelper("PoisonPotent", Material.GLOWSTONE_DUST, 8196));
		recipes.add(new PotionRecipeHelper("HarmingPotent", Material.GLOWSTONE_DUST, 8204));
		recipes.add(new PotionRecipeHelper("HarmingPotent", Material.FERMENTED_SPIDER_EYE, 8229));
		recipes.add(new PotionRecipeHelper("HarmingPotent", Material.FERMENTED_SPIDER_EYE, 8260));
		recipes.add(new PotionRecipeHelper("RegenerationExtended", Material.REDSTONE, 8193));
		recipes.add(new PotionRecipeHelper("RegenerationPotent", Material.GLOWSTONE_DUST, 8193));
		recipes.add(new PotionRecipeHelper("StrengthExtended", Material.REDSTONE, 8201));
		recipes.add(new PotionRecipeHelper("StrengthPotent", Material.GLOWSTONE_DUST, 8201));
		recipes.add(new PotionRecipeHelper("WeaknessExtended", Material.REDSTONE, 8200));
		
		//Splash Potions
		recipes.add(new PotionRecipeHelper("SplashRegeneration", Material.SULPHUR, 8193));
		recipes.add(new PotionRecipeHelper("SplashSwiftness", Material.SULPHUR, 8194));
		recipes.add(new PotionRecipeHelper("SplashFireResistance", Material.SULPHUR, 8195));
		recipes.add(new PotionRecipeHelper("SplashPoison", Material.SULPHUR, 8196));
		recipes.add(new PotionRecipeHelper("SplashHealing", Material.SULPHUR, 8197));
		recipes.add(new PotionRecipeHelper("SplashWeakness", Material.SULPHUR, 8200));
		recipes.add(new PotionRecipeHelper("SplashSlowness", Material.SULPHUR, 8202));
		recipes.add(new PotionRecipeHelper("SplashHarming", Material.SULPHUR, 8204));
		recipes.add(new PotionRecipeHelper("SplashStrength ", Material.SULPHUR, 8201));
		recipes.add(new PotionRecipeHelper("SplashRegenerationPotent", Material.SULPHUR, 8225));
		recipes.add(new PotionRecipeHelper("SplashSwiftnessPotent", Material.SULPHUR, 8226));
		recipes.add(new PotionRecipeHelper("SplashFireResistancePotent", Material.SULPHUR, 8227));
		recipes.add(new PotionRecipeHelper("SplashHealingPotent", Material.SULPHUR, 8229));
		recipes.add(new PotionRecipeHelper("SplashStrengthPotent", Material.SULPHUR, 8233));
		recipes.add(new PotionRecipeHelper("SplashHarmingPotent", Material.SULPHUR, 8236));
		recipes.add(new PotionRecipeHelper("SplashWeaknessExtended", Material.SULPHUR, 8216));
		recipes.add(new PotionRecipeHelper("SplashStrengthExtended ", Material.SULPHUR, 8217));
		recipes.add(new PotionRecipeHelper("SplashSlownessExtended", Material.SULPHUR, 8218));
		recipes.add(new PotionRecipeHelper("SplashRegenerationExtended", Material.SULPHUR, 8209));
		recipes.add(new PotionRecipeHelper("SplashSwiftnessExtended", Material.SULPHUR, 8210));
		recipes.add(new PotionRecipeHelper("SplashFireResistanceExtended", Material.SULPHUR, 8211));
		recipes.add(new PotionRecipeHelper("SplashPoisonExtended", Material.SULPHUR, 8212));
		
		//Base Potions
		ids.add(new PotionIDHelper("Awkward", 16));
		ids.add(new PotionIDHelper("Regeneration", 8193));
		ids.add(new PotionIDHelper("NightVision", 8198));
		ids.add(new PotionIDHelper("Leaping", 8203));
		ids.add(new PotionIDHelper("Invisibility", 8206));
		ids.add(new PotionIDHelper("Swiftness", 8194));
		ids.add(new PotionIDHelper("FireResistance", 8195));
		ids.add(new PotionIDHelper("Poison", 8196));
		ids.add(new PotionIDHelper("Healing", 8197));
		ids.add(new PotionIDHelper("Weakness", 8200));
		ids.add(new PotionIDHelper("WaterBreathing", 8205));
		ids.add(new PotionIDHelper("Slowness", 8202));
		ids.add(new PotionIDHelper("Harming", 8204));
		ids.add(new PotionIDHelper("Strength ", 8201));
		
		//Extended Potions
		ids.add(new PotionIDHelper("LeapingPotent", 8235));
		ids.add(new PotionIDHelper("FireResistancePotent", 8227));
		ids.add(new PotionIDHelper("HealingPotent", 8229));
		ids.add(new PotionIDHelper("StrengthPotent", 8233));
		ids.add(new PotionIDHelper("HarmingPotent", 8236));
		ids.add(new PotionIDHelper("RegenerationPotent", 8225));
		ids.add(new PotionIDHelper("SwiftnessPotent", 8226));
		ids.add(new PotionIDHelper("NightVisionExtended", 8262));
		ids.add(new PotionIDHelper("LeapingExtended", 8267));
		ids.add(new PotionIDHelper("InvisibilityExtended", 8270));
		ids.add(new PotionIDHelper("FireResistanceExtended", 8211));
		ids.add(new PotionIDHelper("PoisonExtended", 8212));
		ids.add(new PotionIDHelper("WeaknessExtended", 8216));
		ids.add(new PotionIDHelper("StrengthExtended ", 8217));
		ids.add(new PotionIDHelper("SlownessExtended", 8218));
		ids.add(new PotionIDHelper("SwiftnessExtended", 8210));
		ids.add(new PotionIDHelper("RegenerationExtended", 8209));
		ids.add(new PotionIDHelper("WaterBreathingExtended", 8269));
		 
		//Splash Potions
		ids.add(new PotionIDHelper("SplashRegeneration", 16385));
		ids.add(new PotionIDHelper("SplashSwiftness", 16386));
		ids.add(new PotionIDHelper("SplashFireResistance", 16387));
		ids.add(new PotionIDHelper("SplashPoison", 16388));
		ids.add(new PotionIDHelper("SplashHealing", 16389));
		ids.add(new PotionIDHelper("SplashWeakness", 16392));
		ids.add(new PotionIDHelper("SplashSlowness", 16394));
		ids.add(new PotionIDHelper("SplashHarming", 16396));
		ids.add(new PotionIDHelper("SplashStrength ", 16393));
		ids.add(new PotionIDHelper("SplashRegenerationPotent", 16417));
		ids.add(new PotionIDHelper("SplashSwiftnessPotent", 16418));
		ids.add(new PotionIDHelper("SplashFireResistancePotent", 16419));
		ids.add(new PotionIDHelper("SplashHealingPotent", 16421));
		ids.add(new PotionIDHelper("SplashStrengthPotent", 16425));
		ids.add(new PotionIDHelper("SplashHarmingPotent", 16428));
		ids.add(new PotionIDHelper("SplashWeaknessExtended", 16408));
		ids.add(new PotionIDHelper("SplashStrengthExtended ", 16409));
		ids.add(new PotionIDHelper("SplashSlownessExtended", 16410));
		ids.add(new PotionIDHelper("SplashRegenerationExtended", 16401));
		ids.add(new PotionIDHelper("SplashSwiftnessExtended", 16402));
		ids.add(new PotionIDHelper("SplashFireResistanceExtended", 16403));
		ids.add(new PotionIDHelper("SplashPoisonExtended", 16404));
	}
	
	public static String getName(Material ingredient, Integer base) {
		for(PotionRecipeHelper recipe : recipes) {
			if(recipe.getIngredient().equals(ingredient) && recipe.getBase().equals(base)) {
				return recipe.getName();
			}
		}
		return null;
	}
	
	public static String getName(ItemStack ingredient, ItemStack base) {
		return getName(ingredient.getType(), (int)base.getDurability());
	}
	
	public static String getName(ItemStack potionStack) {
		for(PotionIDHelper helper : ids) {
			if(helper.getDurability() == potionStack.getDurability()) {
				System.out.println(helper.getName());
				return helper.getName();
			}
		}
		return null;
	}
}