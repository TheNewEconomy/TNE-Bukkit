package net.tnemc.discord.command;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.tnemc.core.common.api.IDFinder;

import java.util.Arrays;
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

  public abstract EmbedBuilder help(String command);

  public boolean execute(User user, Guild guild, UUID minecraft, boolean fake, MessageChannel channel, String command, String[] arguments) {

    System.out.println("Command searching");
    if(arguments.length == 0 || arguments[0].equalsIgnoreCase("help") || arguments[0].equalsIgnoreCase("?")
    || !subCommands.containsKey(arguments[0])) {
      channel.sendMessage(help(command).build()).queue();
      return true;
    }

    DiscordCommand sub = findSub(arguments[0]);

    if(arguments.length >= 2 && arguments[1].equalsIgnoreCase("help") || arguments.length >= 2 &&  arguments[1].equalsIgnoreCase("?")) {
      channel.sendMessage(sub.help(command).build()).queue();
      return true;
    }

    final Role role = guild.getRoleById(sub.role());
    if(!sub.role().equalsIgnoreCase("") && !sub.role().equalsIgnoreCase("RoleID") && role != null) {
      if(!guild.getMember(user).getRoles().contains(role)) {
        channel.sendMessage("Invalid permissions.").queue();
        return false;
      }
    } else {
      channel.sendMessage("Role could not be found with configured ID! Fix this!").queue();
      return false;
    }
    return sub.execute(user, guild, minecraft, fake, channel, command, Arrays.copyOfRange(arguments, 1, arguments.length));
  }

  public static boolean validateDiscordID(String id) {
    return DiscordUtil.getMemberById(id) != null;
  }

  public static UUID getID(final String discordID, boolean fake) {
    return (fake)? IDFinder.getID("discord-" + discordID) : DiscordSRV.getPlugin().getAccountLinkManager().getUuid(discordID);
  }

  public void addSub(DiscordCommand command) {
    subCommands.put(command.name(), command);
  }

  public DiscordCommand findSub(String name) {
    return subCommands.get(name);
  }
}