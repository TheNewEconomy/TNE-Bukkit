package net.tnemc.core.cmdnew;

import net.tnemc.core.TNE;
import net.tnemc.core.cmdnew.parameter.CommandParameter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/12/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandLoader {

  public void load() {


    loadCommands();
  }

  public void loadCommands() {
    final Set<String> commands = TNE.commandsConfig().getSection("Commands").getKeys(false);

    for(String command : commands) {
      final String base = "Commands." + command;

      //TODO: add loadCommand(command, base);
    }
  }

  public void loadCompleters() {
    final Set<String> completers = TNE.commandsConfig().getSection("Completion").getKeys(false);

    for(String completer : completers) {
      final String base = "Completion." + completer;

      //TODO: add loadCompleter(completer, base);
    }
  }

  public CommandInformation loadCommand(String name, String base) {
    CommandInformation commandInfo = new CommandInformation(name);

    commandInfo.setAliases(TNE.commandsConfig().getStringList(base + ".Alias"));
    commandInfo.setAuthor(TNE.commandsConfig().getString(base + ".Author", "creatorfromhell"));
    commandInfo.setPermission(TNE.commandsConfig().getString(base + ".Permission", ""));
    commandInfo.setConsole(TNE.commandsConfig().getBool(base + ".Console", true));
    commandInfo.setDeveloper(TNE.commandsConfig().getBool(base + ".Developer", false));
    commandInfo.setDescription(TNE.commandsConfig().getString(base + ".Description", "No description provided."));
    commandInfo.setExecutor(TNE.commandsConfig().getString(base + ".Executor", "hello_exe"));

    if(TNE.commandsConfig().contains(base + ".Short")) {
      commandInfo.setSubShort(TNE.commandsConfig().getStringList(base + ".Short"));
    }

    final LinkedHashSet<String> params = TNE.commandsConfig().getSection(base + ".Params").getKeysLinked();

    for(String parameter : params) {

      final String paramBase = base + ".Params." + parameter;
      CommandParameter param = new CommandParameter(parameter);

      param.setOptional(TNE.commandsConfig().getBool(paramBase + ".Optional", true));
      param.setTabComplete(TNE.commandsConfig().getBool(paramBase + ".Complete", false));
      param.setCompleteType(TNE.commandsConfig().getString(paramBase + ".CompleteType", "Player"));

      commandInfo.addParameter(param);

    }

    final Set<String> sub = TNE.commandsConfig().getSection(base + ".Sub").getKeys();

    for(String subName : sub) {
      commandInfo.addSub(loadCommand(subName, base + ".Sub." + subName));
    }
    return commandInfo;
  }

  public void loadCompleter(String name, String base) {

  }
}