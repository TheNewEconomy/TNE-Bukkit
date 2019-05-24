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
public class VaultTransferCommand extends TNECommand {
  public VaultTransferCommand(TNELib plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "transfer";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.vault.transfer";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
      "/vault transfer <player> [owner] - Transfers vault ownership to another player."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    final boolean self = arguments.length < 2;
    final UUID id = (!self)? IDFinder.getID(arguments[0]) : IDFinder.getID(sender);
    String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);

    if(!self && !sender.hasPermission("tne.vault.transfer.admin")) {
      sender.sendMessage(ChatColor.RED + "You do not have permission to transfer other players' vaults.");
      return false;
    }

    Vault vault = null;
    if(!self) {
      if(!VaultsModule.instance().manager().hasVault(id, world)) {
        sender.sendMessage(ChatColor.RED + "That player currently does not have a vault.");
        return false;
      }
      vault = VaultsModule.instance().manager().getVault(id, world);

      VaultsModule.instance().manager().close(id, world);

      vault.setOwner(IDFinder.getID(arguments[1]));
      VaultsModule.instance().manager().removeVault(id, world);
      VaultsModule.instance().manager().addVault(vault);
      sender.sendMessage(ChatColor.WHITE + "Successfully transferred \"" + arguments[0] + "\"'s vault to \"" + arguments[1] + "\".");
      return true;
    }

    if(!VaultsModule.instance().manager().hasVault(id, world)) {
      sender.sendMessage(ChatColor.RED + "You do not have a vault. Please use /vault buy to get one.");
      return false;
    }
    vault = VaultsModule.instance().manager().getVault(id, world);

    VaultsModule.instance().manager().close(id, world);

    vault.setOwner(IDFinder.getID(arguments[0]));

    VaultsModule.instance().manager().removeVault(id, world);
    VaultsModule.instance().manager().addVault(vault);
    sender.sendMessage(ChatColor.WHITE + "Successfully transferred vault to \"" + arguments[0] + "\".");
    return true;
  }
}