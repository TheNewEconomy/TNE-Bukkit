package net.tnemc.mobs;

import net.tnemc.core.TNE;
import net.tnemc.core.common.configurations.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
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
    return Arrays.asList(new String[] { "Mobs" });
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    if(!MobsModule.instance().mobs.exists()) MobsModule.instance().saveConfigurations();
    configurations.put("Mobs.Enabled", true);
    configurations.put("Mobs.EnableAge", true);
    configurations.put("Mobs.Message", true);
    configurations.put("Mobs.Multiplier", 1.0);
    configurations.put("Mobs.Default.Enabled", true);
    configurations.put("Mobs.Default.Reward", 10.00);
    configurations.put("Mobs.Custom.Enabled", true);
    configurations.put("Mobs.Custom.Reward", 10.00);
    configurations.put("Mobs.Player.Enabled", true);
    configurations.put("Mobs.Player.Reward", 10.00);configurations.put("Mobs.Messages.Killed", "<white>You received $reward <white>for killing a <green>$mob<white>.");
    configurations.put("Mobs.Messages.KilledVowel", "<white>You received $reward <white>for killing an <green>$mob<white>.");
    configurations.put("Mobs.Messages.NPCTag", "<red>I'm sorry, but you cannot use a name tag on a villager");

    for(EntityType type : EntityType.values()) {
      if(type.isAlive() && !type.equals(EntityType.PLAYER)) {
        System.out.println("Adding Mob " + type.name());
        String formatted = formatName(type.getName());
        configurations.put("Mobs." + formatted + ".Enabled", true);
        configurations.put("Mobs." + formatted + ".Reward", 10.00);
        if(type.getEntityClass().isAssignableFrom(Ageable.class)) {
          configurations.put("Mobs." + formatted + ".Baby.Enabled", true);
          configurations.put("Mobs." + formatted + ".Baby.Reward", 5.00);
        }
      }
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

  public static String formatName(String name) {
    String[] wordsSplit = name.split("_");
    String sReturn = "";
    for(String w: wordsSplit) {
      String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
      sReturn += word;
    }
    return sReturn;
  }

  private void loadExtras(FileConfiguration configurationFile) {
    String base = "Mobs.Player.Individual";
    Set<String> identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

    for(String s : identifiers) {
      String temp = base + "." + s;
      TNE.debug(temp);
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
        TNE.debug(temp);
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
      TNE.debug(temp);
      addChance(configurationFile, temp);
      Boolean enabled = !configurationFile.contains(temp + ".Enabled") || configurationFile.getBoolean(temp + ".Enabled");
      Double reward = (!configurationFile.contains(temp + ".Reward"))? 10.0 : configurationFile.getDouble(temp + ".Reward");
      TNE.debug("configurations.put(" + temp + ".Enabled, " + enabled + ")");
      TNE.debug("configurations.put(" + temp + ".Reward, " + reward + ")");
      configurations.put(temp + ".Enabled", enabled);
      configurations.put(temp + ".Reward", reward);
    }
  }

  private void addChance(FileConfiguration configurationFile, String base) {
    System.out.println("MobConfiguration.addChance(mobs.yml, " + base + ")");
    if(configurationFile.contains(base + ".Multiplier")) {
      configurations.put(base + ".Multiplier", configurationFile.getDouble(base + ".Multiplier"));
    }

    if(configurationFile.contains(base + ".Chance.Min") || configurationFile.contains(base + ".Chance.Max")) {
      System.out.println("Chance Added!");
      Double min = configurationFile.getDouble(base + ".Chance.Min", configurationFile.getDouble(base + ".Chance.Max") - 5.0);
      Double max = configurationFile.getDouble(base + ".Chance.Max", configurationFile.getDouble(base + ".Chance.Max") - 5.0);
      configurations.put(base + ".Chance.Min", min);
      configurations.put(base + ".Chance.Max", max);
    }
  }
}