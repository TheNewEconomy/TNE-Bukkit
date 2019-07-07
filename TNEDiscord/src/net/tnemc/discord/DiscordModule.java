package net.tnemc.discord;

import github.scarsz.discordsrv.DiscordSRV;
import net.tnemc.core.TNE;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.discord.command.DiscordCommandManager;
import org.bukkit.Bukkit;

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

  public static final String transactionLog = "422433392303407115";

  private static DiscordModule instance;
  private DiscordCommandManager commandManager;
  private DiscordListener listener = new DiscordListener();

  public DiscordModule() {
    instance = this;
  }

  @Override
  public void load(TNE tne, String version) {
    Bukkit.getServer().getPluginManager().registerEvents(new TransactionListener(tne), tne);

    DiscordSRV.api.subscribe(listener);
    //DiscordSRV.getPlugin().getJda().addEventListener(new DiscordMessageListener());
    commandManager = new DiscordCommandManager();
    TNE.logger().info("Discord Module loaded!");
  }

  public static DiscordModule instance() {
    return instance;
  }

  public DiscordCommandManager getCommandManager() {
    return commandManager;
  }
}