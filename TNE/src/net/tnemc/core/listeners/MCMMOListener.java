package net.tnemc.core.listeners;

import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingTreasureEvent;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.TNECurrency;
import org.bukkit.event.Listener;

import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 3/5/2018.
 */
public class MCMMOListener implements Listener {

  TNE plugin;

  public MCMMOListener(TNE plugin) {
    this.plugin = plugin;
  }

  public void onFishReward(final McMMOPlayerFishingTreasureEvent event) {
    if(TNE.instance().api().getBoolean("Core.Server.McMMORewards")) {
      String world = WorldFinder.getWorld(event.getPlayer(), WorldVariant.BALANCE);
      Optional<TNECurrency> currency = TNE.manager().currencyManager().currencyFromItem(world, event.getTreasure());
      if (currency.isPresent()) event.setCancelled(true);
    }
  }
}