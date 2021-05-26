package net.tnemc.core.listeners.entity;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.TNECurrency;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
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
public class EntityDeathListener implements Listener {

  TNE plugin;

  public EntityDeathListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDeath(EntityDeathEvent event) {
    if(TNE.instance().api().getBoolean("Core.Server.MobDrop")) {
      LivingEntity entity = event.getEntity();
      Player player = entity.getKiller();
      String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);

      if (player != null && entity instanceof Creature && !player.hasPermission("tne.override.mobdrop")) {
        Iterator<ItemStack> it = event.getDrops().iterator();

        while (it.hasNext()) {
          ItemStack stack = it.next();

          if (stack != null) {
            TNE.debug("Material: " + stack.getType().name());
            Optional<TNECurrency> currency = TNE.manager().currencyManager().currencyFromItem(world, stack);

            if (currency.isPresent()) {
              it.remove();
            }
          }
        }
      }
    }
  }
}
