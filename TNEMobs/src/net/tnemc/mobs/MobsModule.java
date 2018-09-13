package net.tnemc.mobs;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * All rights reserved.
 **/
@ModuleInfo(
    name = "Mobs",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class MobsModule extends Module {

  File mobs;
  FileConfiguration fileConfiguration;
  MobConfiguration configuration;

  private static MobsModule instance;

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Mobs Module loaded!");
    instance = this;
    listeners.add(new MobsListener(tne));
    messages.put("Mobs.Messages.Killed", "<white>You received $reward <white>for killing a <green>$mob<white>.");
    messages.put("Mobs.Messages.KilledVowel", "<white>You received $reward <white>for killing an <green>$mob<white>.");
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Mobs Module unloaded!");
    if(!mobs.exists()) {
      configuration.save(fileConfiguration);
    }
  }

  @Override
  public void initializeConfigurations() {
    super.initializeConfigurations();
    mobs = new File(TNE.instance().getDataFolder(), "mobs.yml");
    fileConfiguration = YamlConfiguration.loadConfiguration(mobs);
  }

  @Override
  public void loadConfigurations() {
    super.loadConfigurations();
    fileConfiguration.options().copyDefaults(true);
    configuration = new MobConfiguration();
    configurations.put(configuration, "Mobs");
  }

  @Override
  public void saveConfigurations() {
    super.saveConfigurations();
    if(!mobs.exists()) {
      Reader mobsStream = null;
      try {
        mobsStream = new InputStreamReader(TNE.instance().getResource("mobs.yml"), "UTF8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      if (mobsStream != null) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mobsStream);
        fileConfiguration.setDefaults(config);
      }
    }
    try {
      fileConfiguration.save(mobs);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static MobsModule instance() {
    return instance;
  }

  /**
   * Helper Methods
   */


  public Boolean playerEnabled(UUID id, String world, String player) {
    TNE.debug("ConfigurationManager.playerEnabled(" + id.toString() + ", " + world + "," + player + ")");
    if(TNE.instance().api().getConfiguration("Mobs.Player.Individual." + id.toString() + ".Enabled", world, player) == null) {
      return false;
    }
    return TNE.instance().api().getBoolean("Mobs.Player.Individual." + id.toString() + ".Enabled", world, player);
  }

  public BigDecimal playerReward(String id, String world, String player) {
    TNE.debug("ConfigurationManager.playerReward(" + id + ", " + world + "," + player + ")");
    return TNE.instance().api().getBigDecimal("Mobs.Player.Individual." + id + ".Reward", world, player);
  }

  public Boolean mobAge(String world, String player) {
    TNE.debug("ConfigurationManager.mobAge(" + world + "," + player + ")");
    return TNE.instance().api().getBoolean("Mobs.EnableAge", world, player);
  }

  public BigDecimal multiplier(String material, String world, String player) {
    if(TNE.instance().api().getConfiguration("Mobs.Multipliers." + material + ".Chance", world, player) == null
        || TNE.instance().api().getConfiguration("Mobs.Multipliers." + material + ".Multiplier", world, player) == null) {
      return BigDecimal.ONE;
    }
    final int chance = TNE.instance().api().getInteger("Mobs.Multipliers." + material + ".Chance", world, player);
    if(chance > 0) {
      if(new Random().nextFloat() <= (chance/100)) {
        return TNE.instance().api().getBigDecimal("Mobs.Multipliers." + material + ".Multiplier", world, player);
      }
    }
    return BigDecimal.ONE;
  }

  public Boolean mobEnabled(String mob, String world, String player) {
    //System.out.println("ConfigurationManager.mobEnabled(" + mob + ", " + world + "," + player + ")");
    TNE.debug(TNE.instance().api().getConfiguration("Mobs." + mob + ".Enabled", world, player) + "");
    if(TNE.instance().api().getConfiguration("Mobs." + mob + ".Enabled", world, player) == null) {
      return false;
    }
    return TNE.instance().api().getBoolean("Mobs." + mob + ".Enabled", world, player);
  }

  public BigDecimal mobReward(String mob, String world, String player) {
    //System.out.println("Mob: " + mob);
    TNE.debug("ConfigurationManager.mobReward(" + mob + ", " + world + "," + player + ")");
    TNE.debug(TNE.instance().api().getConfiguration("Mobs." + mob + ".Reward", world, player) + "");
    if(TNE.instance().api().getConfiguration("Mobs." + mob + ".Reward", world, player) == null) {
      return BigDecimal.ZERO;
    }

    if(configuration.getConfiguration().contains("Mobs." + mob + ".Chance.Min") ||
        configuration.getConfiguration().contains("Mobs." + mob + ".Chance.Max")) {
      //System.out.println("Chance found for " + mob);
      BigDecimal min = TNE.instance().api().getBigDecimal("Mobs." + mob + ".Chance.Min", world, player);
      BigDecimal max = TNE.instance().api().getBigDecimal("Mobs." + mob + ".Chance.Max", world, player);
      return generateRandomBigDecimal(min, max);
    }
    return TNE.instance().api().getBigDecimal("Mobs." + mob + ".Reward", world, player);
  }

  public String mobCurrency(String mob, String world, String player) {
    String currency = TNE.instance().api().getString("Mobs." + mob + ".RewardCurrency", world, player);
    return (currency != null)? currency : TNE.instance().api().getDefault(world).name();
  }
  
  public static BigDecimal generateRandomBigDecimal(BigDecimal min, BigDecimal max) {
    BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
    return randomBigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP);
  }
}