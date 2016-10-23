package com.github.tnerevival.core.configurations.impl;

import com.github.tnerevival.core.configurations.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class MobConfiguration extends Configuration {

  @Override
  public void load(FileConfiguration configurationFile) {
    configurations.put("Mobs.Enabled", true);
    configurations.put("Mobs.Default.Enabled", true);
    configurations.put("Mobs.Default.Reward", 10.00);
    configurations.put("Mobs.Bat.Enabled", true);
    configurations.put("Mobs.Bat.Reward", 10.00);
    configurations.put("Mobs.Blaze.Enabled", true);
    configurations.put("Mobs.Blaze.Reward", 10.00);
    configurations.put("Mobs.CaveSpider.Enabled", true);
    configurations.put("Mobs.CaveSpider.Reward", 10.00);
    configurations.put("Mobs.Chicken.Enabled", true);
    configurations.put("Mobs.Chicken.Reward", 10.00);
    configurations.put("Mobs.Cow.Enabled", true);
    configurations.put("Mobs.Cow.Reward", 10.00);
    configurations.put("Mobs.Creeper.Enabled", true);
    configurations.put("Mobs.Creeper.Reward", 10.00);
    configurations.put("Mobs.EnderDragon.Enabled", true);
    configurations.put("Mobs.EnderDragon.Reward", 10.00);
    configurations.put("Mobs.Enderman.Enabled", true);
    configurations.put("Mobs.Enderman.Reward", 10.00);
    configurations.put("Mobs.Endermite.Enabled", true);
    configurations.put("Mobs.Endermite.Reward", 10.00);
    configurations.put("Mobs.Ghast.Enabled", true);
    configurations.put("Mobs.Ghast.Reward", 10.00);
    configurations.put("Mobs.Giant.Enabled", true);
    configurations.put("Mobs.Giant.Reward", 10.00);
    configurations.put("Mobs.Guardian.Enabled", true);
    configurations.put("Mobs.Guardian.Reward", 10.00);
    configurations.put("Mobs.Horse.Enabled", true);
    configurations.put("Mobs.Horse.Reward", 10.00);
    configurations.put("Mobs.Husk.Enabled", true);
    configurations.put("Mobs.Husk.Reward", 10.00);
    configurations.put("Mobs.IronGolem.Enabled", true);
    configurations.put("Mobs.IronGolem.Reward", 10.00);
    configurations.put("Mobs.MagmaCube.Enabled", true);
    configurations.put("Mobs.MagmaCube.Reward", 10.00);
    configurations.put("Mobs.Mooshroom.Enabled", true);
    configurations.put("Mobs.Mooshroom.Reward", 10.00);
    configurations.put("Mobs.Ocelot.Enabled", true);
    configurations.put("Mobs.Ocelot.Reward", 10.00);
    configurations.put("Mobs.Pig.Enabled", true);
    configurations.put("Mobs.Pig.Reward", 10.00);
    configurations.put("Mobs.Player.Enabled", true);
    configurations.put("Mobs.Player.Reward", 10.00);
    configurations.put("Mobs.PolarBear.Enabled", true);
    configurations.put("Mobs.PolarBear.Reward", 10.00);
    configurations.put("Mobs.Rabbit.Enabled", true);
    configurations.put("Mobs.Rabbit.Reward", 10.00);
    configurations.put("Mobs.Sheep.Enabled", true);
    configurations.put("Mobs.Sheep.Reward", 10.00);
    configurations.put("Mobs.Shulker.Enabled", true);
    configurations.put("Mobs.Shulker.Reward", 10.00);
    configurations.put("Mobs.Silverfish.Enabled", true);
    configurations.put("Mobs.Silverfish.Reward", 10.00);
    configurations.put("Mobs.Skeleton.Enabled", true);
    configurations.put("Mobs.Skeleton.Reward", 10.00);
    configurations.put("Mobs.Slime.Enabled", true);
    configurations.put("Mobs.Slime.Reward", 10.00);
    configurations.put("Mobs.SnowGolem.Enabled", true);
    configurations.put("Mobs.SnowGolem.Reward", 10.00);
    configurations.put("Mobs.Spider.Enabled", true);
    configurations.put("Mobs.Spider.Reward", 10.00);
    configurations.put("Mobs.Squid.Enabled", true);
    configurations.put("Mobs.Squid.Reward", 10.00);
    configurations.put("Mobs.Stray.Enabled", true);
    configurations.put("Mobs.Stray.Reward", 10.00);
    configurations.put("Mobs.Villager.Enabled", true);
    configurations.put("Mobs.Villager.Reward", 10.00);
    configurations.put("Mobs.Witch.Enabled", true);
    configurations.put("Mobs.Witch.Reward", 10.00);
    configurations.put("Mobs.Wither.Enabled", true);
    configurations.put("Mobs.Wither.Reward", 10.00);
    configurations.put("Mobs.WitherSkeleton.Enabled", true);
    configurations.put("Mobs.WitherSkeleton.Reward", 10.00);
    configurations.put("Mobs.Wolf.Enabled", true);
    configurations.put("Mobs.Wolf.Reward", 10.00);
    configurations.put("Mobs.Zombie.Enabled", true);
    configurations.put("Mobs.Zombie.Reward", 10.00);
    configurations.put("Mobs.ZombiePigman.Enabled", true);
    configurations.put("Mobs.ZombiePigman.Reward", 10.00);
    configurations.put("Mobs.ZombieVillager.Enabled", true);
    configurations.put("Mobs.ZombieVillager.Reward", 10.00);

    loadPlayers(configurationFile);
    super.load(configurationFile);
  }

  public void loadPlayers(FileConfiguration configurationFile) {
    String base = "Mobs.Player.Individual";
    Set<String> identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

    for(String s : identifiers) {
      base = base + "." + s;
      Boolean enabled = !configurationFile.contains(base + ".Enabled") || configurationFile.getBoolean(base + ".Enabled");
      Double reward = (!configurationFile.contains(base + ".Reward"))? 10.0 : configurationFile.getDouble(base + ".Reward");;
      configurations.put(base + ".Enabled", enabled);
      configurations.put(base + ".Reward", reward);
    }
  }
}