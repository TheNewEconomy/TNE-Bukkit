package com.github.tnerevival.commands.admin;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.AccountStatus;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
 * Created by creatorfromhell on 10/21/2016.
 */
public class AdminStatusCommand extends TNECommand {

  public AdminStatusCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "status";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.status";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      if(AccountUtils.exists(MISCUtils.getID(arguments[0]))) {
        Player target = MISCUtils.getPlayer(arguments[0]);

        String status = AccountStatus.fromName(arguments[1]).getName();

        AccountUtils.getAccount(MISCUtils.getID(target)).setStatus(status);

        if(Bukkit.getOnlinePlayers().contains(target)) {
          String world = MISCUtils.getWorld(target);
          Message m = new Message("Messages.Account.StatusChange");
          m.addVariable("$status", status);
          m.translate(world, target);
        }

        Message m = new Message("Messages.Admin.Status");
        m.addVariable("$player", arguments[0]);
        m.addVariable("$status", arguments[1]);
        m.translate("", sender);
        return true;
      }


      Message m = new Message("Messages.General.Player");
      m.addVariable("$player", arguments[0]);
      m.translate("", sender);
      return false;
    }
    help(sender);
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[]{
        "/theneweconomy status <username> <status> - Set <username>'s account status.",
        "<status> - The status to set this account to. Valid options: Normal, Locked, BalanceLocked, and BankLocked."
    };
  }
}