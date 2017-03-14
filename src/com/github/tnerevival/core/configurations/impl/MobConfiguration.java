package com.github.tnerevival.core.configurations.impl;

import com.github.tnerevival.core.configurations.Configuration;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class MobConfiguration extends Configuration {

  @Override
  public void load(FileConfiguration configurationFile) {
    configurations.put("Mobs.Enabled", true);
    configurations.put("Mobs.EnableAge", true);
    configurations.put("Mobs.Message", true);
    configurations.put("Mobs.Multiplier", 1.0);
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
    configurations.put("Mobs.Chicken.Baby.Enabled", true);
    configurations.put("Mobs.Chicken.Baby.Reward", 5.00);
    configurations.put("Mobs.Cow.Enabled", true);
    configurations.put("Mobs.Cow.Reward", 10.00);
    configurations.put("Mobs.Cow.Baby.Enabled", true);
    configurations.put("Mobs.Cow.Baby.Reward", 5.00);
    configurations.put("Mobs.Creeper.Enabled", true);
    configurations.put("Mobs.Creeper.Reward", 10.00);
    configurations.put("Mobs.Custom.Enabled", true);
    configurations.put("Mobs.Custom.Reward", 10.00);
    configurations.put("Mobs.Donkey.Enabled", true);
    configurations.put("Mobs.Donkey.Reward", 10.00);
    configurations.put("Mobs.Donkey.Baby.Enabled", true);
    configurations.put("Mobs.Donkey.Baby.Reward", 5.00);
    configurations.put("Mobs.EnderDragon.Enabled", true);
    configurations.put("Mobs.EnderDragon.Reward", 10.00);
    configurations.put("Mobs.Enderman.Enabled", true);
    configurations.put("Mobs.Enderman.Reward", 10.00);
    configurations.put("Mobs.Endermite.Enabled", true);
    configurations.put("Mobs.Endermite.Reward", 10.00);
    configurations.put("Mobs.Evoker.Enabled", true);
    configurations.put("Mobs.Evoker.Reward", 10.00);
    configurations.put("Mobs.Ghast.Enabled", true);
    configurations.put("Mobs.Ghast.Reward", 10.00);
    configurations.put("Mobs.Giant.Enabled", true);
    configurations.put("Mobs.Giant.Reward", 10.00);
    configurations.put("Mobs.Guardian.Enabled", true);
    configurations.put("Mobs.Guardian.Reward", 10.00);
    configurations.put("Mobs.GuardianElder.Enabled", true);
    configurations.put("Mobs.GuardianElder.Reward", 10.00);
    configurations.put("Mobs.Horse.Enabled", true);
    configurations.put("Mobs.Horse.Reward", 10.00);
    configurations.put("Mobs.Horse.Baby.Enabled", true);
    configurations.put("Mobs.Horse.Baby.Reward", 5.00);
    configurations.put("Mobs.Husk.Enabled", true);
    configurations.put("Mobs.Husk.Reward", 10.00);
    configurations.put("Mobs.Husk.Baby.Enabled", true);
    configurations.put("Mobs.Husk.Baby.Reward", 5.00);
    configurations.put("Mobs.IronGolem.Enabled", true);
    configurations.put("Mobs.IronGolem.Reward", 10.00);
    configurations.put("Mobs.Llama.Enabled", true);
    configurations.put("Mobs.Llama.Reward", 10.00);
    configurations.put("Mobs.Llama.Baby.Enabled", true);
    configurations.put("Mobs.Llama.Baby.Reward", 5.00);
    configurations.put("Mobs.MagmaCube.Enabled", true);
    configurations.put("Mobs.MagmaCube.Reward", 10.00);
    configurations.put("Mobs.Mule.Enabled", true);
    configurations.put("Mobs.Mule.Reward", 10.00);
    configurations.put("Mobs.Mule.Baby.Enabled", true);
    configurations.put("Mobs.Mule.Baby.Reward", 5.00);
    configurations.put("Mobs.Mooshroom.Enabled", true);
    configurations.put("Mobs.Mooshroom.Reward", 10.00);
    configurations.put("Mobs.Mooshroom.Baby.Enabled", true);
    configurations.put("Mobs.Mooshroom.Baby.Reward", 5.00);
    configurations.put("Mobs.Ocelot.Enabled", true);
    configurations.put("Mobs.Ocelot.Reward", 10.00);
    configurations.put("Mobs.Ocelot.Baby.Enabled", true);
    configurations.put("Mobs.Ocelot.Baby.Reward", 5.00);
    configurations.put("Mobs.Pig.Enabled", true);
    configurations.put("Mobs.Pig.Reward", 10.00);
    configurations.put("Mobs.Pig.Baby.Enabled", true);
    configurations.put("Mobs.Pig.Baby.Reward", 5.00);
    configurations.put("Mobs.Player.Enabled", true);
    configurations.put("Mobs.Player.Reward", 10.00);
    configurations.put("Mobs.PolarBear.Enabled", true);
    configurations.put("Mobs.PolarBear.Reward", 10.00);
    configurations.put("Mobs.PolarBear.Baby.Enabled", true);
    configurations.put("Mobs.PolarBear.Baby.Reward", 5.00);
    configurations.put("Mobs.Rabbit.Enabled", true);
    configurations.put("Mobs.Rabbit.Reward", 10.00);
    configurations.put("Mobs.Rabbit.Baby.Enabled", true);
    configurations.put("Mobs.Rabbit.Baby.Reward", 5.00);
    configurations.put("Mobs.RabbitKiller.Enabled", true);
    configurations.put("Mobs.RabbitKiller.Reward", 10.00);
    configurations.put("Mobs.Sheep.Enabled", true);
    configurations.put("Mobs.Sheep.Reward", 10.00);
    configurations.put("Mobs.Sheep.Baby.Enabled", true);
    configurations.put("Mobs.Sheep.Baby.Reward", 5.00);
    configurations.put("Mobs.Shulker.Enabled", true);
    configurations.put("Mobs.Shulker.Reward", 10.00);
    configurations.put("Mobs.Silverfish.Enabled", true);
    configurations.put("Mobs.Silverfish.Reward", 10.00);
    configurations.put("Mobs.Skeleton.Enabled", true);
    configurations.put("Mobs.Skeleton.Reward", 10.00);
    configurations.put("Mobs.SkeletonHorse.Enabled", true);
    configurations.put("Mobs.SkeletonHorse.Reward", 10.00);
    configurations.put("Mobs.SkeletonHorse.Baby.Enabled", true);
    configurations.put("Mobs.SkeletonHorse.Baby.Reward", 5.00);
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
    configurations.put("Mobs.Vex.Enabled", true);
    configurations.put("Mobs.Vex.Reward", 10.00);
    configurations.put("Mobs.Villager.Enabled", true);
    configurations.put("Mobs.Villager.Reward", 10.00);
    configurations.put("Mobs.Villager.Baby.Enabled", true);
    configurations.put("Mobs.Villager.Baby.Reward", 5.00);
    configurations.put("Mobs.Vindicator.Enabled", true);
    configurations.put("Mobs.Vindicator.Reward", 10.00);
    configurations.put("Mobs.Witch.Enabled", true);
    configurations.put("Mobs.Witch.Reward", 10.00);
    configurations.put("Mobs.Wither.Enabled", true);
    configurations.put("Mobs.Wither.Reward", 10.00);
    configurations.put("Mobs.WitherSkeleton.Enabled", true);
    configurations.put("Mobs.WitherSkeleton.Reward", 10.00);
    configurations.put("Mobs.Wolf.Enabled", true);
    configurations.put("Mobs.Wolf.Reward", 10.00);
    configurations.put("Mobs.Wolf.Baby.Enabled", true);
    configurations.put("Mobs.Wolf.Baby.Reward", 5.00);
    configurations.put("Mobs.Zombie.Enabled", true);
    configurations.put("Mobs.Zombie.Reward", 10.00);
    configurations.put("Mobs.Zombie.Baby.Enabled", true);
    configurations.put("Mobs.Zombie.Baby.Reward", 5.00);
    configurations.put("Mobs.ZombieHorse.Enabled", true);
    configurations.put("Mobs.ZombieHorse.Reward", 10.00);
    configurations.put("Mobs.ZombieHorse.Baby.Enabled", true);
    configurations.put("Mobs.ZombieHorse.Baby.Reward", 5.00);
    configurations.put("Mobs.ZombiePigman.Enabled", true);
    configurations.put("Mobs.ZombiePigman.Reward", 10.00);
    configurations.put("Mobs.ZombiePigman.Baby.Enabled", true);
    configurations.put("Mobs.ZombiePigman.Baby.Reward", 5.00);
    configurations.put("Mobs.ZombieVillager.Enabled", true);
    configurations.put("Mobs.ZombieVillager.Reward", 10.00);
    configurations.put("Mobs.ZombieVillager.Baby.Enabled", true);
    configurations.put("Mobs.ZombieVillager.Baby.Reward", 5.00);

    for(String s : configurationFile.getConfigurationSection("Mobs").getKeys(false)) {
      addChance(configurationFile, "Mobs." + s);
      addChance(configurationFile, "Mobs." + s + ".Baby");
    }

    loadExtras(configurationFile);
    super.load(configurationFile);
  }

  private void loadExtras(FileConfiguration configurationFile) {
    String base = "Mobs.Player.Individual";
    Set<String> identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

    for(String s : identifiers) {
      String temp = base + "." + s;
      MISCUtils.debug(temp);
      Boolean enabled = !configurationFile.contains(temp + ".Enabled") || configurationFile.getBoolean(temp + ".Enabled");
      Double reward = (!configurationFile.contains(temp + ".Reward"))? 10.0 : configurationFile.getDouble(temp + ".Reward");
      addChance(configurationFile, temp);
      configurations.put(temp + ".Enabled", enabled);
      configurations.put(temp + ".Reward", reward);
    }

    base = "Mobs.Custom.Entries";
    if(configurationFile.contains(base)) {
      identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

      for (String s : identifiers) {
        String temp = base + "." + s;
        MISCUtils.debug(temp);
        addChance(configurationFile, temp);
        Boolean enabled = !configurationFile.contains(temp + ".Enabled") || configurationFile.getBoolean(temp + ".Enabled");
        Double reward = (!configurationFile.contains(temp + ".Reward")) ? 10.0 : configurationFile.getDouble(temp + ".Reward");
        configurations.put(temp + ".Enabled", enabled);
        configurations.put(temp + ".Reward", reward);

        if (configurationFile.contains(temp + ".Baby")) {
          addChance(configurationFile, temp + ".Baby");
          Boolean babyEnabled = !configurationFile.contains(temp + ".Baby.Enabled") || configurationFile.getBoolean(temp + ".Baby.Enabled");
          Double babyReward = (!configurationFile.contains(temp + ".Baby.Reward")) ? 10.0 : configurationFile.getDouble(temp + ".Baby.Reward");
          configurations.put(temp + ".Baby.Enabled", babyEnabled);
          configurations.put(temp + ".Baby.Reward", babyReward);
        }
      }
    }

    base = "Mobs.Slime";
    identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

    for(String s : identifiers) {
      if(s.equalsIgnoreCase("Enabled") || s.equalsIgnoreCase("Reward")) continue;
      String temp = base + "." + s;
      MISCUtils.debug(temp);
      addChance(configurationFile, temp);
      Boolean enabled = !configurationFile.contains(temp + ".Enabled") || configurationFile.getBoolean(temp + ".Enabled");
      Double reward = (!configurationFile.contains(temp + ".Reward"))? 10.0 : configurationFile.getDouble(temp + ".Reward");
      MISCUtils.debug("configurations.put(" + temp + ".Enabled, " + enabled + ")");
      MISCUtils.debug("configurations.put(" + temp + ".Reward, " + reward + ")");
      configurations.put(temp + ".Enabled", enabled);
      configurations.put(temp + ".Reward", reward);
    }
  }
  
  private void addChance(FileConfiguration configurationFile, String base) {
    MISCUtils.debug("MobConfiguration.addChance(mobs.yml, " + base + ")");
    if(configurationFile.contains(base + ".Multiplier")) {
      configurations.put(base + ".Multiplier", configurationFile.getDouble(base + ".Multiplier"));
    }

    if(configurationFile.contains(base + ".Chance.Min") || configurationFile.contains(base + ".Chance.Max")) {
      Double min = (!configurationFile.contains(base + ".Chance.Min"))? configurationFile.getDouble(base + ".Chance.Max") - 5.0 : configurationFile.getDouble(base + ".Chance.Min");
      Double max = (!configurationFile.contains(base + ".Chance.Max"))? configurationFile.getDouble(base + ".Chance.Min") + 5.0 : configurationFile.getDouble(base + ".Chance.Max");
      configurations.put(base + ".Chance.Min", min);
      configurations.put(base + ".Chance.Max", max);
    }
  }
}