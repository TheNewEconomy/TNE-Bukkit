package net.tnemc.core.listeners.player;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerQuitListener implements Listener {

  TNE plugin;

  public PlayerQuitListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onQuit(final PlayerQuitEvent event) {
    final UUID id = event.getPlayer().getUniqueId();
    final Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      if(TNE.manager().exists(id)) {
        TNEAccount account = TNEAccount.getAccount(id.toString());
        if(player == null) TNE.debug("Player is null");
        account.saveItemCurrency(WorldFinder.getWorld(player, WorldVariant.BALANCE), true, player.getInventory());
        account.setLastOnline(new Date().getTime());
        account.getHistory().clearAway();
        TNE.manager().addAccount(account);
        TNE.manager().removeAccount(id);
      }
    });
  }
}
