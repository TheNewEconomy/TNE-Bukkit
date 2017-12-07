package net.tnemc.vaults.command;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.vaults.VaultsModule;
import org.bukkit.command.CommandSender;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = (arguments.length >= 2)? arguments[1] : WorldFinder.getWorld(sender);
    VaultsModule.instance().manager().open(IDFinder.getID(sender), IDFinder.getID(arguments[0]), world, 1, true);

    return true;
  }
}