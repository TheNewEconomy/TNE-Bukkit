package net.tnemc.mobs;

import com.sun.istack.internal.NotNull;
import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.core.common.utils.MISCUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
  CommentedConfiguration fileConfiguration;
  private MobConfiguration configuration;

  private static MobsModule instance;

  @Override
  public void load(TNE tne, String version) {
    TNE.logger().info("Mobs Module loaded!");
    instance = this;
    listeners.add(new MobsListener(tne));
  }

  @Override
  public void unload(TNE tne) {
    TNE.logger().info("Mobs Module unloaded!");
    if(!mobs.exists()) {
      configuration.save(fileConfiguration);
    }
  }

  @Override
  public void initializeConfigurations() {
    super.initializeConfigurations();
    final String mobsFile = (MISCUtils.isOneThirteen())? "mobs.yml" : "mobs-1.12.yml";
    mobs = new File(TNE.instance().getDataFolder(), "mobs.yml");
    fileConfiguration = initializeConfiguration(mobs, mobsFile);
  }

  @Override
  public void loadConfigurations() {
    super.loadConfigurations();
    configuration = new MobConfiguration();
    configurations.put(configuration, "Mobs");
  }

  @Override
  public void saveConfigurations() {
    fileConfiguration.save(mobs);
  }

  static MobsModule instance() {
    return instance;
  }

  public CommentedConfiguration initializeConfiguration(File file, String defaultFile) {
    TNE.debug("Started copying " + file.getName());
    CommentedConfiguration commentedConfiguration = new CommentedConfiguration(file, new InputStreamReader(getResource(defaultFile), StandardCharsets.UTF_8), false);
    TNE.debug("Initializing commented configuration");
    if(commentedConfiguration != null) {
      TNE.debug("Loading commented configuration");
      commentedConfiguration.load();
    }
    TNE.debug("Finished copying " + file.getName());
    return commentedConfiguration;
  }


  public InputStream getResource(@NotNull String filename) {
    try {
      URL url = getClass().getClassLoader().getResource(filename);
      if (url == null) {
        return null;
      } else {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
      }
    } catch (IOException var4) {
      return null;
    }
  }

  /**
   * Helper Methods
   */


  Boolean playerEnabled(UUID id, String world, String player) {
    TNE.debug("ConfigurationManager.playerEnabled(" + id.toString() + ", " + world + "," + player + ")");
    if(TNE.instance().api().getConfiguration("Mobs.Player.Individual." + id.toString() + ".Enabled", world, player) == null) {
      return false;
    }
    return TNE.instance().api().getBoolean("Mobs.Player.Individual." + id.toString() + ".Enabled", world, player);
  }

  BigDecimal playerReward(String id, String world, String player) {
    TNE.debug("ConfigurationManager.playerReward(" + id + ", " + world + "," + player + ")");
    return TNE.instance().api().getBigDecimal("Mobs.Player.Individual." + id + ".Reward", world, player);
  }

  Boolean mobAge(String world, String player) {
    TNE.debug("ConfigurationManager.mobAge(" + world + "," + player + ")");
    return TNE.instance().api().getBoolean("Mobs.EnableAge", world, player);
  }

  BigDecimal multiplier(String material, String world, String player) {
    if(TNE.instance().api().getConfiguration("Mobs.Multipliers." + material + ".Chance", world, player) == null
        || TNE.instance().api().getConfiguration("Mobs.Multipliers." + material + ".Multiplier", world, player) == null
        || TNE.instance().api().getString("Mobs.Multipliers." + material + ".Chance", world, player).equalsIgnoreCase("")
        || TNE.instance().api().getString("Mobs.Multipliers." + material + ".Multiplier", world, player).equalsIgnoreCase("")) {
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

  Boolean mobEnabled(String mob, String world, String player) {
    //TNE.debug("ConfigurationManager.mobEnabled(" + mob + ", " + world + "," + player + ")");
    TNE.debug(TNE.instance().api().getConfiguration("Mobs." + mob + ".Enabled", world, player) + "");
    //TNE.debug("Config null?: " + (TNE.instance().api() == null));
    //TNE.debug("Node null?: " + (TNE.instance().api().getNode("Mobs") == null));
    //TNE.debug("Node null?: " + (TNE.instance().api().getNode("Mobs") == null));
    //TNE.debug("Mob: " + mob);
    if(TNE.instance().api().getConfiguration("Mobs." + mob + ".Enabled") == null) {
      return false;
    }
    //TNE.debug(TNE.instance().api().getBool("Mobs." + mob + ".Enabled"));
    return TNE.instance().api().getBoolean("Mobs." + mob + ".Enabled");
  }

  BigDecimal mobReward(String mob, String world, String player) {
    TNE.debug("Mob: " + mob);
    TNE.debug("ConfigurationManager.mobReward(" + mob + ", " + world + "," + player + ")");
    TNE.debug(TNE.instance().api().getConfiguration("Mobs." + mob + ".Reward", world, player) + "");
    if(!TNE.instance().api().hasConfiguration("Mobs." + mob + ".Reward")) {
      return BigDecimal.ZERO;
    }

    if(TNE.instance().api().hasConfiguration("Mobs." + mob + ".Chance.Min") ||
        TNE.instance().api().hasConfiguration("Mobs." + mob + ".Chance.Max")) {
      TNE.debug("Chance found for " + mob);
      BigDecimal min = TNE.instance().api().getBigDecimal("Mobs." + mob + ".Chance.Min");
      if(min == null) min = BigDecimal.ZERO;
      BigDecimal max = TNE.instance().api().getBigDecimal("Mobs." + mob + ".Chance.Max");
      if(max == null) max = BigDecimal.TEN;
      return generateRandomBigDecimal(min, max);
    }

    BigDecimal reward = TNE.instance().api().getBigDecimal("Mobs." + mob + ".Reward");
    if(reward == null) reward = BigDecimal.ZERO;
    TNE.debug("Reward: " + reward.toPlainString());
    return reward;
  }

  String mobCurrency(String mob, String world, String player) {
    String currency = TNE.instance().api().getString("Mobs." + mob + ".RewardCurrency");
    return (currency != null)? currency : TNE.instance().api().getDefault(world).name();
  }
  
  private static BigDecimal generateRandomBigDecimal(BigDecimal min, BigDecimal max) {
    BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
    return randomBigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP);
  }
}