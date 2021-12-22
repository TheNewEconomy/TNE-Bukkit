package net.tnemc.discord.command.eco;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import net.tnemc.discord.command.DiscordCommand;

import java.awt.*;

;

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
public class DiscordEcoCommand extends DiscordCommand {

  public DiscordEcoCommand() {
    addSub(new DiscordEcoBalanceCommand());
    addSub(new DiscordEcoBalanceOtherCommand());
    addSub(new DiscordEcoGiveCommand());
    addSub(new DiscordEcoPayCommand());
    addSub(new DiscordEcoSetCommand());
    addSub(new DiscordEcoTakeCommand());
  }

  @Override
  public String name() {
    return "!eco";
  }

  @Override
  public String role() {
    return "";
  }

  @Override
  public EmbedBuilder help(String cmd) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setTitle(cmd);
    final String id = (cmd.contains("deco"))? "Discord" : "Minecraft";
    builder.setDescription("Base command for economy-related commands that correlate to the balance of your " + id + " ID.");
    StringBuilder subs = new StringBuilder();
    for(DiscordCommand command : subCommands.values()) {
      subs.append(command.help(cmd).getDescriptionBuilder());
      subs.append(System.lineSeparator());
    }
    builder.addField("Sub Commands", subs.toString(), false);
    builder.setColor(new Color(190, 57, 0));
    return builder;
  }
}