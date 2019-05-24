package net.tnemc.vaults.command;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.vaults.VaultManager;
import net.tnemc.vaults.VaultsModule;
import net.tnemc.vaults.vault.Vault;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/15/2017.
 */
public class VaultDepositCommand extends TNECommand {
  public VaultDepositCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "deposit";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.vault.deposit";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
      "/vault deposit [tab] [player] - Deposits the itemstack in your hand into your Vault."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    final boolean self = arguments.length < 2;
    final UUID id = (!self)? IDFinder.getID(arguments[1]) : IDFinder.getID(sender);
    final String world = WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    int tab = 1;

    if(arguments.length >= 1) {
      try {
        tab = Integer.parseInt(arguments[0]);
        if(tab < 1) tab = 1;
      } catch(Exception ignore) {}
    }

    Player player = getPlayer(sender);

    Vault vault = null;
    if(!self) {
      if(!VaultsModule.instance().manager().hasVault(id, world)) {
        sender.sendMessage(ChatColor.RED + "That player currently does not have a vault.");
        return false;
      }
      vault = VaultsModule.instance().manager().getVault(id, world);
      if(!vault.getMembers().containsKey(IDFinder.getID(sender))) {
        sender.sendMessage(ChatColor.RED + "You do not have access to that player's vault.");
        return false;
      }
    }

    if(!VaultsModule.instance().manager().hasVault(id, world)) {
      if(VaultManager.cost.compareTo(BigDecimal.ZERO) > 0) {
        sender.sendMessage(ChatColor.RED + "you do not have a vault. Please use /vault buy to get one.");
        return false;
      }
    }


    if(player != null) {
      vault = VaultsModule.instance().manager().getVault(id, world);
      if(tab > vault.getMaxTabs()) tab = 1;
      final ItemStack stack = player.getInventory().getItemInMainHand();

      if(!stack.getType().getKey().getKey().toLowerCase().contains("air")) {

        final int freeSlot = vault.getTabs().get(tab).buildInventory().firstEmpty();

        if(freeSlot == -1) {
          sender.sendMessage(ChatColor.RED + "No free slots in the specific tab of \"" + tab + "\".");
          return false;
        }
        vault.getTabs().get(tab).addItem(freeSlot, stack);
        VaultsModule.instance().manager().addVault(vault);
        VaultsModule.instance().manager().updateItem(id, world, tab, freeSlot, stack);

        vault.updateTab(tab);

        player.getInventory().setItemInMainHand(null);

        sender.sendMessage(ChatColor.WHITE + "Successfully added the item to the vault.");
        return true;
      }
    }

    sender.sendMessage(ChatColor.RED + "Something went wrong while trying to deposit the item.");
    return false;
  }
}