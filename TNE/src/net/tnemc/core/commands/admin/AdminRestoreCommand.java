package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

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
 * Created by Daniel on 2/8/2018.
 */
public class AdminRestoreCommand extends TNECommand {

  public AdminRestoreCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "restore";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.restore";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Restore";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    File file = new File(TNE.instance().getDataFolder(), "extracted.yml");
    if(file.exists()) {
      YamlConfiguration original = YamlConfiguration.loadConfiguration(file);
      YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
      Set<String> accounts = configuration.getConfigurationSection("Accounts").getKeys(false);

      accounts.forEach((username)->{
        UUID id = IDFinder.getID(username);
        TNEAccount account = new TNEAccount(id, username);
        Set<String> worlds = configuration.getConfigurationSection("Accounts." + username + ".Balances").getKeys(false);
        worlds.forEach((world)->{
          Set<String> currencies = configuration.getConfigurationSection("Accounts." + username + ".Balances." + world).getKeys(false);
          currencies.forEach((currency)->{
            String balance = original.getString("Accounts." + username + ".Balances." + world + "." + currency);
            System.out.println("Currency: " + currency);
            System.out.println("Balance: " + balance);
            account.setHoldings(world, currency, new BigDecimal(balance));
          });
        });
        TNE.manager().addAccount(account);
      });
      sender.sendMessage(ChatColor.WHITE + "Restored accounts from extracted.yml.");
      return true;
    }
    sender.sendMessage(ChatColor.RED + "Unable to locate extracted.yml file for restoration.");
    return false;
  }
}