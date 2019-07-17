package net.tnemc.discord;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.discord.command.DiscordCommandManager;
import net.tnemc.discord.command.DiscordManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/5/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
@ModuleInfo(
    name = "Discord",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class DiscordModule extends Module {

  private static DiscordModule instance;

  //configurations

  private File discord;
  private CommentedConfiguration discordFileConfiguration;
  private DiscordConfiguration discordConfiguration;

  private DiscordManager manager;

  public DiscordModule() {
    instance = this;
  }

  @Override
  public void load(TNE tne, String version) {
    //DiscordSRV.getPlugin().getJda().addEventListener(new DiscordMessageListener());

    if(Bukkit.getServer().getPluginManager().isPluginEnabled("DiscordSRV")) {
      manager = new DiscordManager();
      manager.initialize();
      Bukkit.getServer().getPluginManager().registerEvents(new TransactionListener(tne), tne);
    }
    TNE.logger().info("Discord Module loaded!");
  }

  @Override
  public void initializeConfigurations() {
    super.initializeConfigurations();
    discord = new File(TNE.instance().getDataFolder(), "discord.yml");
    discordFileConfiguration = initializeConfiguration(discord, "discord.yml");
  }

  @Override
  public void loadConfigurations() {
    super.loadConfigurations();
    discordConfiguration = new DiscordConfiguration();
    configurations.put(discordConfiguration, "Discord");
  }

  @Override
  public void saveConfigurations() {
    discordFileConfiguration.save(discord);
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

  public static DiscordModule instance() {
    return instance;
  }

  public DiscordCommandManager getCommandManager() {
    return manager.getCommandManager();
  }

  public File getDiscord() {
    return discord;
  }

  public CommentedConfiguration getDiscordFileConfiguration() {
    return discordFileConfiguration;
  }

  public DiscordConfiguration getDiscordConfiguration() {
    return discordConfiguration;
  }
}