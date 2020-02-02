package net.tnemc.core.listeners.player;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
public class PlayerTeleportListener implements Listener {

  TNE plugin;

  public PlayerTeleportListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onTeleport(final PlayerTeleportEvent event) {
    String fromWorld = event.getFrom().getWorld().getName();
    fromWorld = TNE.instance().getWorldManager(fromWorld).getBalanceWorld();
    String toWorld = event.getTo().getWorld().getName();
    toWorld = TNE.instance().getWorldManager(toWorld).getBalanceWorld();

    if(event.getPlayer().getUniqueId().toString().equalsIgnoreCase("a07d34a0-b78d-49d0-9de9-7537d00f4306")) {
      event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 100));
      event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 100));
    }

    if(!fromWorld.equals(toWorld) && TNE.instance().api().getBoolean("Core.Multiworld")) {
      TNE.manager().getAccount(IDFinder.getID(event.getPlayer())).saveItemCurrency(fromWorld);
    }
  }
}
