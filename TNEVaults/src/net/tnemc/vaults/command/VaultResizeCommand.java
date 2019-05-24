package net.tnemc.vaults.command;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.vaults.VaultsModule;
import net.tnemc.vaults.vault.Vault;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 5/24/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class VaultResizeCommand extends TNECommand {
  public VaultResizeCommand(TNELib plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "resize";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.vault.resize";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
      "/vault resize <player> <size> - Sets the amount of slots per tab for a player's vault.",
      "Valid sizes: 9, 18, 27, 36, 45, 54"
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length < 2) {
      help(sender);
      return false;
    }

    final UUID id = IDFinder.getID(arguments[0]);
    final String world = WorldFinder.getWorld(id, WorldVariant.BALANCE);

    if(!validSize(arguments[1])) {
      sender.sendMessage(ChatColor.RED + "Valid Sizes: 9, 18, 27, 36, 45");
      return false;
    }

    if(!VaultsModule.instance().manager().hasVault(id, world)) {
      sender.sendMessage(ChatColor.RED + "That player does not have a vault.");
      return false;
    }

    Vault vault = VaultsModule.instance().manager().getVault(id, world);
    VaultsModule.instance().manager().close(id, world);
    vault.setSize(Integer.parseInt(arguments[1]));
    VaultsModule.instance().manager().addVault(vault);

    sender.sendMessage(ChatColor.WHITE + "Successfully set the size of \"" + arguments[0] + "\"'s vault to " + arguments[1] + ".");
    return true;
  }

  private boolean validSize(String size) {
    return size.equalsIgnoreCase("9") ||
        size.equalsIgnoreCase("18") ||
        size.equalsIgnoreCase("27") ||
        size.equalsIgnoreCase("36") ||
        size.equalsIgnoreCase("45");
  }
}