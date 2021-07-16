package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.AccountStatus;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminStatusCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length >= 1) {
      if(TNE.manager().exists(IDFinder.getID(arguments[0]))) {
        UUID target = IDFinder.getID(arguments[0]);
        TNEAccount acc = TNE.manager().getAccount(target);

        AccountStatus status = (arguments.length >= 2)? AccountStatus.fromName(arguments[1]) : acc.getStatus();
        TNE.debug("AdminStatusCommand.java(71): status != null - " + (status != null));
        TNE.debug("AdminStatusCommand.java(72): acc.getStatus != null - " + (acc.getStatus() != null));
        boolean changed = !status.getName().equalsIgnoreCase(acc.getStatus().getName());
        TNE.debug("AccountStatus: " + acc.getStatus().getName());
        TNE.debug("Status: " + status.getName());
        TNE.debug("Changed?: " + changed);

        if(changed) {
          acc.setStatus(status);
          TNE.manager().addAccount(acc);
        }
        String message = (changed)? "Messages.Admin.StatusChange" : "Messages.Admin.Status";

        if(changed && MISCUtils.getPlayer(target) != null && Bukkit.getOnlinePlayers().contains(MISCUtils.getPlayer(target))) {
          String world = WorldFinder.getWorld(target, WorldVariant.ACTUAL);
          Message m = new Message("Messages.Account.StatusChange");
          m.addVariable("$status", status.getName());
          m.translate(world, target);
        }

        Message m = new Message(message);
        m.addVariable("$player", arguments[0]);
        m.addVariable("$status", status.getName());
        m.translate("", sender);
        return true;
      }


      Message m = new Message("Messages.General.Player");
      m.addVariable("$player", arguments[0]);
      m.translate("", sender);
      return false;
    }
    MISCUtils.help(sender, label, arguments);
    return false;
  }
}