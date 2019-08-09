package net.tnemc.core.listeners.mcmmo;

import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingTreasureEvent;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.TNECurrency;
import org.bukkit.event.Listener;

import java.util.Optional;

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
public class PlayerFishingTreasureListener implements Listener {

  TNE plugin;

  public PlayerFishingTreasureListener(TNE plugin) {
    this.plugin = plugin;
  }

  public void onFishReward(final McMMOPlayerFishingTreasureEvent event) {
    if(TNE.instance().api().getBoolean("Core.Server.ThirdParty.McMMORewards")) {
      String world = WorldFinder.getWorld(event.getPlayer(), WorldVariant.BALANCE);
      Optional<TNECurrency> currency = TNE.manager().currencyManager().currencyFromItem(world, event.getTreasure());
      if (currency.isPresent()) event.setCancelled(true);
    }
  }
}
