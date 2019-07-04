package net.tnemc.vaults.command;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.vaults.VaultManager;
import net.tnemc.vaults.VaultsModule;
import net.tnemc.vaults.vault.Vault;
import org.bukkit.Bukkit;
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
  public String[] getHelpLines() {
    return new String[] {
        "/vault view [owner] - Views your vault, or another players if you have the permissions to do so."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      final boolean self = arguments.length < 1;
      final UUID id = (!self)? IDFinder.getID(arguments[0]) : IDFinder.getID(sender);
      String world = (arguments.length >= 2)? arguments[1] : WorldFinder.getWorld(sender, WorldVariant.BALANCE);

      if(!self) {
        if(!VaultsModule.instance().manager().hasVault(id, world)) {
          sender.sendMessage(ChatColor.RED + "That player currently does not have a vault.");
          return;
        }
        Vault vault = VaultsModule.instance().manager().getVault(id, world);
        if(!vault.getMembers().containsKey(IDFinder.getID(sender))) {
          sender.sendMessage(ChatColor.RED + "You do not have access to that player's vault.");
          return;
        }

        VaultsModule.instance().manager().open(IDFinder.getID(sender), id, world, 1, false);
        return;
      }

      if(!VaultsModule.instance().manager().hasVault(id, world)) {
        if(VaultManager.cost.compareTo(BigDecimal.ZERO) > 0) {
          sender.sendMessage(ChatColor.RED + "you do not have a vault. Please use /vault buy to get one.");
          return;
        }
        VaultsModule.instance().manager().addVault(new Vault(id, world));
      }

      VaultsModule.instance().manager().open(IDFinder.getID(sender), id, world, 1, false);
    });
    return true;
  }
}