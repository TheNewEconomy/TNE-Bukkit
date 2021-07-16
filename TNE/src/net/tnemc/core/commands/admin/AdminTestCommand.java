package net.tnemc.core.commands.admin;

import net.tnemc.commands.core.CommandExecution;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class AdminTestCommand implements CommandExecution {

  @Override
  public boolean execute(PlayerProvider provider, String label, String[] arguments) {
    CommandSender sender = MISCUtils.getSender(provider);
    if(sender instanceof Player) {
      String world = (arguments.length >= 1)? arguments[0] : TNE.instance().defaultWorld;
      String nether = (arguments.length >= 2)? arguments[1] : world + "_nether";
      String end = (arguments.length >= 3)? arguments[2] : world + "_the_end";
      Player player = (Player)sender;
      Location startingLocation = player.getLocation();

      String[] ids = new String[5];
      ids[0] = IDFinder.getID(sender).toString();

      BigDecimal startingBalance = TNE.manager().getAccount(UUID.fromString(ids[0])).getHoldings();
      TNE.manager().getAccount(UUID.fromString(ids[0])).setHoldings(BigDecimal.ZERO);

      player.chat("Automated TNE Message. Running Economy tests currently.");
      sender.sendMessage(ChatColor.WHITE + "Starting tests, please do not move.");
      player.performCommand("bal");
      if(Bukkit.getWorld(nether) == null) {
        sender.sendMessage(ChatColor.RED + "Invalid nether world. Skipping nether tests.");
      } else {
        player.teleport(Bukkit.getWorld(nether).getSpawnLocation());
        ids[1] = IDFinder.getID(sender).toString();
        TNE.manager().getAccount(UUID.fromString(ids[0])).addHoldings(new BigDecimal("2000"));
        player.performCommand("bal");
        player.teleport(startingLocation);
        ids[2] = IDFinder.getID(sender).toString();
        TNE.manager().getAccount(UUID.fromString(ids[0])).setHoldings(BigDecimal.ZERO);
        player.performCommand("bal");
      }

      if(Bukkit.getWorld(end) == null) {
        sender.sendMessage(ChatColor.RED + "Invalid end world. Skipping end tests.");
      } else {
        player.teleport(Bukkit.getWorld(end).getSpawnLocation());
        ids[3] = IDFinder.getID(sender).toString();
        TNE.manager().getAccount(UUID.fromString(ids[0])).addHoldings(new BigDecimal("2000"));
        player.performCommand("bal");
        player.teleport(startingLocation);
        ids[4] = IDFinder.getID(sender).toString();
        TNE.manager().getAccount(UUID.fromString(ids[0])).setHoldings(BigDecimal.ZERO);
        player.performCommand("bal");
      }
      TNE.manager().getAccount(UUID.fromString(ids[0])).setHoldings(startingBalance);
      player.performCommand("bal");
      sender.sendMessage(ChatColor.WHITE + "Results of world test are: " + String.join(", ", ids));

      sender.sendMessage(ChatColor.WHITE + "Starting command test.");
      boolean isOp = player.isOp();
      try {
        if(!isOp) player.setOp(true);
        MISCUtils.commandTest(player);
      } catch(Exception e) {
        TNE.debug(e.getStackTrace());
      } finally {
        if(!isOp) player.setOp(false);
      }

      sender.sendMessage(ChatColor.WHITE + "Starting IDFinder test. This will now run in the background.");
      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->MISCUtils.idTest(player, sender));
      return true;
    }
    return false;
  }
}