package net.tnemc.vaults.command;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/10/2017.
 */
public class VaultCommand extends TNECommand {

  public VaultCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new VaultAddCommand(plugin));
    subCommands.add(new VaultCreateCommand(plugin));
    subCommands.add(new VaultDepositCommand(plugin));
    subCommands.add(new VaultMembersCommand(plugin));
    subCommands.add(new VaultPeekCommand(plugin));
    subCommands.add(new VaultRemoveCommand(plugin));
    subCommands.add(new VaultResizeCommand(plugin));
    subCommands.add(new VaultTransferCommand(plugin));
    subCommands.add(new VaultViewCommand(plugin));
  }

  @Override
  public String getName() {
    return "vault";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.vault";
  }

  @Override
  public boolean console() {
    return false;
  }
}