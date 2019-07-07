package net.tnemc.discord.command.deco;

import github.scarsz.discordsrv.dependencies.jda.core.EmbedBuilder;
import net.tnemc.core.TNE;
import net.tnemc.discord.command.DiscordCommand;

import java.awt.*;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/7/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class DiscordDecoSetCommand extends DiscordCommand {
  @Override
  public String name() {
    return "set";
  }

  @Override
  public String role() {
    return TNE.configurations().getString("Discord.Roles.Set");
  }

  @Override
  public EmbedBuilder help() {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setTitle("!deco set");
    builder.setDescription("Usage: !deco set <discord id> <amount>");
    builder.addField("Description", "Sets the balance of the specified discord user to the amount.", false);
    builder.setColor(new Color(190, 57, 0));
    return builder;
  }
}