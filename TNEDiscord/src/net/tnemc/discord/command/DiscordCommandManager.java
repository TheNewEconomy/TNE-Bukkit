package net.tnemc.discord.command;

import net.tnemc.discord.command.eco.DiscordEcoCommand;

import java.util.HashMap;
import java.util.Map;

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
public class DiscordCommandManager {

  private Map<String, DiscordCommand> commands = new HashMap<>();

  public DiscordCommandManager() {
    addCommand(new DiscordEcoCommand());
  }

  public DiscordCommand find(String command, boolean fake) {
    /*if(!fake && !TNE.instance().api().getBoolean("Discord.Eco")) return null;
    if(fake && !TNE.instance().api().getBoolean("Discord.Deco")) return null;*/

    return commands.get(command);
  }

  public void addCommand(DiscordCommand command) {
    commands.put(command.name(), command);
  }

  public Map<String, DiscordCommand> getCommands() {
    return commands;
  }

  public void setCommands(Map<String, DiscordCommand> commands) {
    this.commands = commands;
  }
}