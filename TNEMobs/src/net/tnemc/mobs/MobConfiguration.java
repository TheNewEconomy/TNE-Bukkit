package net.tnemc.mobs;

import net.tnemc.core.TNE;
import net.tnemc.core.common.configurations.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 10/21/2017.
 */
public class MobConfiguration extends Configuration {

  @Override
  public FileConfiguration getConfiguration() {
    return MobsModule.instance().fileConfiguration;
  }

  @Override
  public List<String> node() {
    return Collections.singletonList("Mobs");
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    if(!MobsModule.instance().mobs.exists()) MobsModule.instance().saveConfigurations();
    configurations.put("Mobs.Enabled", true);
    configurations.put("Mobs.EnableAge", true);
    configurations.put("Mobs.Message", true);
    configurations.put("Mobs.Default.Enabled", true);
    configurations.put("Mobs.Default.Reward", 10.00);
    configurations.put("Mobs.Custom.Enabled", true);
    configurations.put("Mobs.Custom.Reward", 10.00);
    configurations.put("Mobs.Player.Enabled", true);
    configurations.put("Mobs.Player.Reward", 10.00);configurations.put("Mobs.Messages.Killed", "<white>You received $reward <white>for killing a <green>$mob<white>.");
    configurations.put("Mobs.Messages.KilledVowel", "<white>You received $reward <white>for killing an <green>$mob<white>.");
    configurations.put("Mobs.Messages.NPCTag", "<red>I'm sorry, but you cannot use a name tag on a villager");

    for(EntityType type : EntityType.values()) {
      configurations.put("Mobs." + type.name() + ".Enabled", true);
      configurations.put("Mobs." + type.name() + ".RewardCurrency", "Default");
      configurations.put("Mobs." + type.name() + ".Reward", 10.00);
      configurations.put("Mobs." + type.name() + ".Baby.Enabled", true);
      configurations.put("Mobs." + type.name() + ".Baby.RewardCurrency", "Default");
      configurations.put("Mobs." + type.name() + ".Baby.Reward", 5.00);
    }

    for(Villager.Career career : Villager.Career.values()) {
      configurations.put("Mobs.VILLAGER_" + career.name() + ".Enabled", true);
      configurations.put("Mobs.VILLAGER_" + career.name() + ".RewardCurrency", "Default");
      configurations.put("Mobs.VILLAGER_" + career.name() + ".Reward", 10.00);
      configurations.put("Mobs.VILLAGER_" + career.name() + ".Baby.Enabled", true);
      configurations.put("Mobs.VILLAGER_" + career.name() + ".Baby.RewardCurrency", "Default");
      configurations.put("Mobs.VILLAGER_" + career.name() + ".Baby.Reward", 5.00);
      configurations.put("Mobs.ZOMBIE_VILLAGER_" + career.name() + ".Enabled", true);
      configurations.put("Mobs.ZOMBIE_VILLAGER_" + career.name() + ".RewardCurrency", "Default");
      configurations.put("Mobs.ZOMBIE_VILLAGER_" + career.name() + ".Reward", 10.00);
      configurations.put("Mobs.ZOMBIE_VILLAGER_" + career.name() + ".Baby.Enabled", true);
      configurations.put("Mobs.ZOMBIE_VILLAGER_" + career.name() + ".Baby.RewardCurrency", "Default");
      configurations.put("Mobs.ZOMBIE_VILLAGER_" + career.name() + ".Baby.Reward", 5.00);
    }


    //Load Messages
    String base = "Mobs.Messages.Custom";
    if(configurationFile.contains("Mobs.Messages.Custom")) {
      Set<String> keys = configurationFile.getConfigurationSection(base).getKeys(false);
      for (String s : keys) {
        configurations.put(base + "." + s, configurationFile.getString(base + "." + s));
      }
    }

    for(String s : configurationFile.getConfigurationSection("Mobs").getKeys(false)) {
      addChance(configurationFile, "Mobs." + s);
      addChance(configurationFile, "Mobs." + s + ".Baby");
    }

    loadExtras(configurationFile);
    super.load(configurationFile);
  }

  private void loadExtras(FileConfiguration configurationFile) {
    String base = "Mobs.PLAYER.Individual";
    Set<String> identifiers;
    if(configurationFile.contains(base)) {
      identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

      for (String s : identifiers) {
        String temp = base + "." + s;
        TNE.debug(temp);
        Boolean enabled = configurationFile.getBoolean(temp + ".Enabled", true);
        String currency = configurationFile.getString(temp + ".RewardCurrency", "Default");
        Double reward = configurationFile.getDouble(temp + ".Reward", 10.0);
        addChance(configurationFile, temp);
        configurations.put(temp + ".Enabled", enabled);
        configurations.put(temp + ".RewardCurrency", currency);
        configurations.put(temp + ".Reward", reward);
      }
    }

    base = "Mobs.Custom.Entries";
    if(configurationFile.contains(base)) {
      identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

      for (String s : identifiers) {
        String temp = base + "." + s;
        TNE.debug(temp);
        addChance(configurationFile, temp);
        Boolean enabled = configurationFile.getBoolean(temp + ".Enabled", true);
        String currency = configurationFile.getString(temp + ".RewardCurrency", "Default");
        Double reward = configurationFile.getDouble(temp + ".Reward", 10.0);
        configurations.put(temp + ".Enabled", enabled);
        configurations.put(temp + ".RewardCurrency", currency);
        configurations.put(temp + ".Reward", reward);

        if (configurationFile.contains(temp + ".Baby")) {
          addChance(configurationFile, temp + ".Baby");
          Boolean babyEnabled = configurationFile.getBoolean(temp + ".Baby.Enabled", true);
          String babyCurrency = configurationFile.getString(temp + ".Baby.RewardCurrency", "Default");
          Double babyReward = configurationFile.getDouble(temp + ".Baby.Reward", 10.0);
          configurations.put(temp + ".Baby.Enabled", babyEnabled);
          configurations.put(temp + ".Baby.RewardCurrency", babyCurrency);
          configurations.put(temp + ".Baby.Reward", babyReward);
        }
      }
    }

    base = "Mobs.SLIME";
    if(configurationFile.contains(base)) {
      identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

      for (String s : identifiers) {
        if (s.equalsIgnoreCase("Enabled") || s.equalsIgnoreCase("Reward")) continue;
        String temp = base + "." + s;
        TNE.debug(temp);
        addChance(configurationFile, temp);
        Boolean enabled = configurationFile.getBoolean(temp + ".Enabled", true);
        String currency = configurationFile.getString(temp + ".RewardCurrency", "Default");
        Double reward = configurationFile.getDouble(temp + ".Reward", 10.0);
        TNE.debug("configurations.put(" + temp + ".Enabled, " + enabled + ")");
        TNE.debug("configurations.put(" + temp + ".Reward, " + reward + ")");
        configurations.put(temp + ".Enabled", enabled);
        configurations.put(temp + ".RewardCurrency", currency);
        configurations.put(temp + ".Reward", reward);
      }
    }

    base = "Mobs.MAGMA_CUBE";
    if(configurationFile.contains(base)) {
      identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

      for (String s : identifiers) {
        if (s.equalsIgnoreCase("Enabled") || s.equalsIgnoreCase("Reward")) continue;
        String temp = base + "." + s;
        TNE.debug(temp);
        addChance(configurationFile, temp);
        Boolean enabled = configurationFile.getBoolean(temp + ".Enabled", true);
        String currency = configurationFile.getString(temp + ".RewardCurrency", "Default");
        Double reward = configurationFile.getDouble(temp + ".Reward", 10.0);
        TNE.debug("configurations.put(" + temp + ".Enabled, " + enabled + ")");
        TNE.debug("configurations.put(" + temp + ".Reward, " + reward + ")");
        configurations.put(temp + ".Enabled", enabled);
        configurations.put(temp + ".RewardCurrency", currency);
        configurations.put(temp + ".Reward", reward);
      }
    }
  }

  private void addChance(FileConfiguration configurationFile, String base) {
    if(configurationFile.contains(base + ".Chance.Min") || configurationFile.contains(base + ".Chance.Max")) {
      //System.out.println("Chance Added!");
      Double min = configurationFile.getDouble(base + ".Chance.Min", configurationFile.getDouble(base + ".Chance.Max") - 5.0);
      Double max = configurationFile.getDouble(base + ".Chance.Max", configurationFile.getDouble(base + ".Chance.Max") - 5.0);
      configurations.put(base + ".Chance.Min", min);
      configurations.put(base + ".Chance.Max", max);
    }
  }
}