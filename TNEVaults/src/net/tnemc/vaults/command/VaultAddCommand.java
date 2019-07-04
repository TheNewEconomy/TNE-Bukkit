package net.tnemc.vaults.command;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.vaults.VaultManager;
import net.tnemc.vaults.VaultsModule;
import net.tnemc.vaults.vault.Vault;
import net.tnemc.vaults.vault.member.VaultMember;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class VaultAddCommand extends TNECommand {
  public VaultAddCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "add";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.vault.add";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/vault add <player> - Adds a player to your vault."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    final UUID id = IDFinder.getID(sender);
    final UUID target = IDFinder.getID(arguments[0]);
    final String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);

    if(!VaultsModule.instance().manager().hasVault(id, world)) {
      if(VaultManager.cost.compareTo(BigDecimal.ZERO) > 0) {
        sender.sendMessage(ChatColor.RED + "You do not have a vault. Please use /vault buy to get one.");
        return false;
      }
      VaultsModule.instance().manager().addVault(new Vault(id, world));
    }

    Vault vault = VaultsModule.instance().manager().getVault(id, world);
    vault.getMembers().put(target, new VaultMember(target, id, world));

    VaultsModule.instance().manager().addVault(vault);

    sender.sendMessage(ChatColor.WHITE + "Successfully added \"" + arguments[0] + "\" to your vault.");
    return true;
  }
}