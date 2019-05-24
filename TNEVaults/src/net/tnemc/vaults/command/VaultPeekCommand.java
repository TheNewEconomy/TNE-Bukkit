package net.tnemc.vaults.command;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.vaults.VaultsModule;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/11/2017.
 */
public class VaultPeekCommand extends TNECommand {
  public VaultPeekCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "peek";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.vault.peek";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/vault peek <player> [edit] - Open another player's vault, optional edit mode requires additional permissions."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    final UUID id = IDFinder.getID(arguments[0]);
    final String world = (arguments.length >= 2)? arguments[1] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);

    if(!VaultsModule.instance().manager().hasVault(id, world)) {
      sender.sendMessage(ChatColor.RED + "That player does not have a vault.");
      return false;
    }

    final boolean edit = (arguments.length >= 2 && arguments[1].equalsIgnoreCase("true") && sender.hasPermission("tne.vault.peek.edit"));

    VaultsModule.instance().manager().open(IDFinder.getID(sender), id, world, 1, !edit);
    return true;
  }
}