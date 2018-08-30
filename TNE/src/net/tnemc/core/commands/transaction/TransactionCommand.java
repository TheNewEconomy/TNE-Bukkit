package net.tnemc.core.commands.transaction;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class TransactionCommand extends TNECommand {

  public TransactionCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new TransactionAwayCommand(plugin));
    subCommands.add(new TransactionHistoryCommand(plugin));
    subCommands.add(new TransactionInfoCommand(plugin));
    subCommands.add(new TransactionVoidCommand(plugin));
  }

  @Override
  public String getName() {
    return "transaction";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "trans"
    };
  }

  @Override
  public String getNode() {
    return "";
  }

  @Override
  public boolean console() {
    return true;
  }
}
