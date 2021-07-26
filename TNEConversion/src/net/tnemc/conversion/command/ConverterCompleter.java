package net.tnemc.conversion.command;

import net.tnemc.commands.core.CommandSearchInformation;
import net.tnemc.commands.core.TabCompleter;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.conversion.ConversionModule;

import java.util.LinkedList;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/25/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ConverterCompleter implements TabCompleter {
  @Override
  public LinkedList<String> complete(PlayerProvider sender, Optional<CommandSearchInformation> search, String argument) {
    LinkedList<String> values = new LinkedList<>();

    for(String str : ConversionModule.instance().manager().getConverters().keySet()) {
      if(!argument.trim().equalsIgnoreCase("")) {
        if(str.startsWith(argument)) values.add(str);
      }
      values.add(str);
    }
    return values;
  }
}