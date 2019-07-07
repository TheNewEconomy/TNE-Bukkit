package net.tnemc.discord;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.ListenerPriority;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessageReceivedEvent;
import github.scarsz.discordsrv.dependencies.jda.core.entities.Role;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.discord.command.DiscordCommand;

import java.util.Arrays;
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
public class DiscordListener {

  @Subscribe(priority = ListenerPriority.MONITOR)
  public void discordMessageReceived(DiscordGuildMessageReceivedEvent event) {

    try {
      final String message = event.getMessage().getContentDisplay();
      final String discordID = event.getMessage().getAuthor().getId();

      String[] split = message.split(" ");

      final String command = split[0];
      if (command.equalsIgnoreCase("!eco") ) {
        final boolean fake = command.equalsIgnoreCase("!deco");
        final String[] arguments = Arrays.copyOfRange(split, 1, split.length);

        DiscordCommand commandInstance = DiscordModule.instance().getCommandManager().find("!eco", fake);

        if (commandInstance == null) {
          event.getMessage().getChannel().sendMessage("Invalid command.").queue();
          return;
        }

        if (!DiscordSRV.getPlugin().getAccountLinkManager().getLinkedAccounts().containsKey(discordID)) {
          event.getMessage().getChannel().sendMessage("You must first link your Minecraft account with your discord.").queue();
          return;
        }

        final UUID id = (fake)? IDFinder.getID("discord-" + discordID) : DiscordSRV.getPlugin().getAccountLinkManager().getUuid(discordID);

        if(!commandInstance.role().equalsIgnoreCase("")) {
          final Role role = event.getGuild().getRoleById(commandInstance.role());
          if(role != null) {
            if(!event.getGuild().getMember(event.getAuthor()).getRoles().contains(role)) {
              event.getMessage().getChannel().sendMessage("Invalid permissions.").queue();
              return;
            }
          }
        }
        commandInstance.execute(event.getMessage().getAuthor(), event.getGuild(), id, fake, event.getChannel(), command, arguments);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}