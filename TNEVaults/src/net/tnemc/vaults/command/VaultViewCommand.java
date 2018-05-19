package net.tnemc.vaults.command;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.vaults.VaultsModule;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class VaultViewCommand extends TNECommand {
  public VaultViewCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "view";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "open"
    };
  }

  @Override
  public String getNode() {
    return "tne.vault.view";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = (arguments.length >= 2)? arguments[1] : WorldFinder.getWorld(sender, WorldVariant.ACTUAL);
    VaultsModule.instance().manager().open(IDFinder.getID(sender), IDFinder.getID(arguments[0]), world, 1, false);

    return true;
  }
}