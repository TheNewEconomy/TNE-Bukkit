package net.tnemc.mobs;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
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
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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

        String world = WorldFinder.getWorld(killer, WorldVariant.CONFIGURATION);
        UUID id = IDFinder.getID(killer);
        TNEAccount account = TNE.manager().getAccount(id);
        String mob = entity.getCustomName();
        BigDecimal reward = MobsModule.instance().mobReward("Default", world, id.toString());
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
                if (MobsModule.instance().playerEnabled(p.getUniqueId(), world, id.toString())) {
                  mob = p.getUniqueId().toString();
                  player = true;
                }
              }
            }
          }
          mob = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null) ? "Custom.Entries." + event.getEntityType().toString() : mob;

          if (entity.getCustomName() != null && MobsModule.instance().mobEnabled(entity.getCustomName(), world, id.toString()))
            mob = "Custom.Entries." + entity.getCustomName();

          if (MobsModule.instance().mobAge(world, id.toString())) {
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
            if (MobsModule.instance().mobEnabled(mob + "." + tier, world, id.toString())) {
              mob = mob + "." + tier;
            }
          }

          if (!MobsModule.instance().fileConfiguration.contains("Mobs." + mob)) mob = "Default";
          if (entity.getCustomName() != null && MobsModule.instance().fileConfiguration.contains("Mobs.Custom.Entries." + entity.getCustomName()))
            mob = "Custom.Entries." + entity.getCustomName();
          String currency = MobsModule.instance().mobCurrency(mob, world, id.toString());
          reward = (player) ? MobsModule.instance().playerReward(mob, world, id.toString()) : MobsModule.instance().mobReward(mob, world, id.toString());
          reward = CurrencyFormatter.round(world, currency, reward.multiply(MobsModule.instance().getRewardMultiplier(mob, world, id.toString())));
          String formatted = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null) ? event.getEntityType().toString() : mob;
          if (entity.getCustomName() != null && MobsModule.instance().fileConfiguration.contains("Mobs.Custom.Entries." + entity.getCustomName()))
            formatted = entity.getCustomName();
          formatted = (TNE.instance().messageConfiguration().contains("Mobs.Messages.Custom." + formatted)) ? TNE.instance().messageConfiguration().getString("Mobs.Messages.Custom." + formatted) : formatted;
          TNE.debug(formatted);
          Character firstChar = formatted.charAt(0);
          messageNode = (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') ? "Mobs.Messages.KilledVowel" : "Mobs.Messages.Killed";
          if (TNE.instance().messageConfiguration().contains("Mobs.Messages.Custom." + formatted.replaceAll(" ", "")))
            messageNode = TNE.instance().messageConfiguration().getString("Mobs.Messages.Custom." + formatted.replaceAll(" ", ""));
          if (MobsModule.instance().mobEnabled(mob, world, id.toString())) {
            TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("give"));
            transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency), reward, TransactionChargeType.GAIN));
            TransactionResult result = TNE.transactionManager().perform(transaction);
            if (result.proceed() && TNE.instance().api().getBoolean("Mobs.Message")) {
              Message mobKilled = new Message(messageNode);
              mobKilled.addVariable("$mob", formatted.replace(".", " "));
              mobKilled.addVariable("$reward", CurrencyFormatter.format(world, currency, reward));
              mobKilled.translate(world, killer);
            }
          }
        }
      }
    }
  }
}
