package net.tnemc.discord.command;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.core.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.core.entities.Channel;
import github.scarsz.discordsrv.dependencies.jda.core.entities.User;
import net.tnemc.core.common.api.IDFinder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
public abstract class DiscordCommand {

  protected Map<String, DiscordCommand> subCommands = new HashMap<>();

  public abstract String name();

  public abstract String role();

  public abstract EmbedBuilder help();

  public boolean execute(User user, UUID minecraft, boolean fake, Channel channel, String[] arguments) {
    return true;
  }

  public UUID getID(final String discordID, boolean fake) {
    return (fake)? IDFinder.getID("discord-" + discordID) : DiscordSRV.getPlugin().getAccountLinkManager().getUuid(discordID);
  }

  public DiscordCommand findSub(String name) {
    return subCommands.get(name);
  }
}