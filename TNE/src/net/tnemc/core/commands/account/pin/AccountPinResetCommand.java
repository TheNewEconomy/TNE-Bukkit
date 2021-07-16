package net.tnemc.core.commands.account.pin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/21/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AccountPinResetCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(arguments.length < 1) {
      MISCUtils.help(sender, label, arguments);
      return false;
    }

    final UUID id = IDFinder.getID(arguments[0]);
    final String world = WorldFinder.getWorld(sender, WorldVariant.CONFIGURATION);

    TNEAccount account = TNE.manager().getAccount(id);

    account.setPin("TNEPIN");
    TNE.manager().addAccount(account);

    Message message = new Message("Messages.Account.PinReset");
    message.addVariable("$player", account.displayName());
    message.translate(world, sender);
    return true;
  }
}