package net.tnemc.banks.command.worldbank;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class WorldBankCommand extends TNECommand {
  public WorldBankCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "worldbank";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "wbank"
    };
  }

  @Override
  public String getNode() {
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }
}