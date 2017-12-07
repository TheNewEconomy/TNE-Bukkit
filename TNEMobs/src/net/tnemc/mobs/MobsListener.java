package net.tnemc.mobs;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import java.math.BigDecimal;

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
 * Created by Daniel on 10/21/2017.
 */
public class MobsListener implements ModuleListener {

  private TNE plugin;

  public MobsListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onEntityDeath(final EntityDeathEvent event) {
    LivingEntity entity = event.getEntity();

    if (entity.getKiller() != null) {
      Player killer = entity.getKiller();

      //Permissions Check
      if (killer.hasPermission("tne.general.mob")) {

        String world = WorldFinder.getWorld(killer);
        String id = IDFinder.getID(killer).toString();
        String mob = entity.getCustomName();
        BigDecimal reward = MobsModule.instance().mobReward("Default", world, id);
        String messageNode = "Mobs.Messages.Killed";
        Boolean player = false;
        TNE.debug("Mobs Location: " + MobsModule.instance().mobs.getAbsolutePath());
        TNE.debug("Mobs Null: " + (MobsModule.instance().mobs == null));

        TNE.debug("TNE instance null: " + (TNE.instance() == null));
        TNE.debug("TNE api null: " + (TNE.instance().api() == null));
        TNE.debug("TNE getBoolean(Mobs.Enabled) null: " + (TNE.instance().api().getBoolean("Mobs.Enabled", world, id) == null));
        if (TNE.instance().api().getBoolean("Mobs.Enabled", world, id)) {
          if (entity.getType().isAlive()) {
            mob = MobConfiguration.formatName(entity.getType().getName());

            if (entity.getType().equals(EntityType.RABBIT)) {
              Rabbit rab = (Rabbit) entity;
              if (rab.getType().equals(Rabbit.Type.THE_KILLER_BUNNY)) {
                mob = "RabbitKiller";
              }
            } else if (entity.getType().equals(EntityType.PLAYER)) {
              Player p = (Player) entity;
              if (p.getUniqueId() != null) {
                if (MobsModule.instance().playerEnabled(p.getUniqueId(), world, id)) {
                  mob = p.getUniqueId().toString();
                  player = true;
                }
              }
            }
          }
          mob = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null) ? "Custom.Entries." + event.getEntityType().toString() : mob;

          if (entity.getCustomName() != null && MobsModule.instance().mobEnabled(entity.getCustomName(), world, id))
            mob = "Custom.Entries." + entity.getCustomName();

          if (MobsModule.instance().mobAge(world, id)) {
            if (entity instanceof Ageable) {
              Ageable e = (Ageable) entity;
              if (!e.isAdult()) {
                mob = mob + ".Baby";
              }
            } else if (entity instanceof Zombie) {
              Zombie e = (Zombie) entity;
              if (e.isBaby()) {
                mob = mob + ".Baby";
              }
            }
          }
          if (entity.getType().equals(EntityType.SLIME)) {
            String tier = "Small";

            switch (((Slime) entity).getSize()) {
              case 1:
                break;
              case 2:
                tier = "Medium";
                break;
              case 4:
                tier = "Large";
                break;
              default:
                tier = ((Slime) entity).getSize() + "";
                break;
            }
            if (MobsModule.instance().mobEnabled(mob + "." + tier, world, id)) {
              mob = mob + "." + tier;
            }
          }

          if (!MobsModule.instance().fileConfiguration.contains("Mobs." + mob)) mob = "Default";
          if (entity.getCustomName() != null && MobsModule.instance().fileConfiguration.contains("Mobs.Custom.Entries." + entity.getCustomName()))
            mob = "Custom.Entries." + entity.getCustomName();
          String currency = MobsModule.instance().mobCurrency(mob, world, id);
          reward = (player) ? MobsModule.instance().playerReward(mob, world, id) : MobsModule.instance().mobReward(mob, world, id);
          reward = CurrencyFormatter.round(world, currency, reward.multiply(MobsModule.instance().getRewardMultiplier(mob, world, id)));
          String formatted = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null) ? event.getEntityType().toString() : mob;
          if (entity.getCustomName() != null && MobsModule.instance().fileConfiguration.contains("Mobs.Custom.Entries." + entity.getCustomName()))
            formatted = entity.getCustomName();
          formatted = (TNE.instance().messageConfiguration().contains("Mobs.Messages.Custom." + formatted)) ? TNE.instance().messageConfiguration().getString("Mobs.Messages.Custom." + formatted) : formatted;
          TNE.debug(formatted);
          Character firstChar = formatted.charAt(0);
          messageNode = (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') ? "Mobs.Messages.KilledVowel" : "Mobs.Messages.Killed";
          if (TNE.instance().messageConfiguration().contains("Mobs.Messages.Custom." + formatted.replaceAll(" ", "")))
            messageNode = TNE.instance().messageConfiguration().getString("Mobs.Messages.Custom." + formatted.replaceAll(" ", ""));
          if (MobsModule.instance().mobEnabled(mob, world, id)) {
            TNETransaction transaction = new TNETransaction(id, id, world, TNE.transactionManager().getType("give"));
            transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency), reward, TransactionChargeType.GAIN));
            TransactionResult result = TNE.transactionManager().perform(transaction);
            if (result.proceed() && TNE.instance().api().getBoolean("Mobs.Message")) {
              Message mobKilled = new Message(messageNode);
              mobKilled.addVariable("$mob", formatted.replace(".", " "));
              mobKilled.addVariable("$reward", CurrencyFormatter.format(WorldFinder.getWorld(killer), currency, reward));
              mobKilled.translate(WorldFinder.getWorld(killer), killer);
            }
          }
        }
      }
    }
  }
}
