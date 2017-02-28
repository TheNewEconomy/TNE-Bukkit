/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.commands.vault;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class VaultCommand extends TNECommand {

  public VaultCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new VaultAddCommand(plugin));
    subCommands.add(new VaultBuyCommand(plugin));
    subCommands.add(new VaultRemoveCommand(plugin));
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

  @Override
  public Boolean locked() {
    return true;
  }

  @Override
  public Boolean confirm() {
    return true;
  }

  @Override
  public Boolean activated(String world, String player) {
    return Vault.command(world, player);
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(MISCUtils.ecoDisabled(getWorld(sender))) {
      Message disabled = new Message("Messages.General.Disabled");
      disabled.translate(getWorld(sender), sender);
      return false;
    }
    Player player = getPlayer(sender);
    if(!Vault.enabled(getWorld(sender), IDFinder.getID(player).toString())) {
      new Message("Messages.Vault.Disabled").translate(IDFinder.getWorld(player), player);
      return false;
    }
    return super.execute(sender, command, arguments);
  }
}