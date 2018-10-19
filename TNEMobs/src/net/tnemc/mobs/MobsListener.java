package net.tnemc.mobs;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

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
            mob = entity.getType().name();

            if (entity.getType().equals(EntityType.RABBIT)) {
              Rabbit rab = (Rabbit) entity;
              if (rab.getType().equals(Rabbit.Type.THE_KILLER_BUNNY)) {
                mob = "RABBIT_KILLER";
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

          //System.out.println("Mob Name0: " + mob);
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

          if (entity.getType().equals(EntityType.MAGMA_CUBE)) {
            String tier = "Small";

            switch (((MagmaCube) entity).getSize()) {
              case 1:
                break;
              case 2:
                tier = "Medium";
                break;
              case 4:
                tier = "Large";
                break;
              default:
                tier = ((MagmaCube) entity).getSize() + "";
                break;
            }
            if (MobsModule.instance().mobEnabled(mob + "." + tier, world, id.toString())) {
              mob = mob + "." + tier;
            }
          }

          if(entity instanceof Villager) {
            final String career = ((Villager)entity).getProfession().name().toUpperCase();
            if(MobsModule.instance().fileConfiguration.contains("Mobs." + mob + "_" + career)) {
              mob = mob + "_" + career;
            }
          }

          if(entity instanceof ZombieVillager) {
            final String career = ((ZombieVillager)entity).getVillagerProfession().name().toUpperCase();
            if(MobsModule.instance().fileConfiguration.contains("Mobs." + mob + "_" + career)) {
              mob = mob + "_" + career;
            }
          }

          //System.out.println("Mob Name1: " + mob);
          final ItemStack tool =  event.getEntity().getKiller().getInventory().getItemInMainHand();
          final String material = (tool != null && tool.getType() != null && !tool.getType().equals(Material.AIR))? tool.getType().name() : "FIST";

          if (!MobsModule.instance().fileConfiguration.contains("Mobs." + mob)) mob = "Default";
          //System.out.println("Mob Name2: " + mob);
          if (entity.getCustomName() != null && MobsModule.instance().fileConfiguration.contains("Mobs.Custom.Entries." + entity.getCustomName()))
            mob = "Custom.Entries." + entity.getCustomName();
          String currency = MobsModule.instance().mobCurrency(mob, world, id.toString());
          reward = (player) ? MobsModule.instance().playerReward(mob, world, id.toString()) : MobsModule.instance().mobReward(mob, world, id.toString());
          reward = CurrencyFormatter.round(world, currency, reward.multiply(MobsModule.instance().multiplier(material, world, id.toString())));
          String formatted = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null) ? event.getEntityType().toString() : mob;
          //System.out.println("Mob Name3: " + mob);
          if (entity.getCustomName() != null && MobsModule.instance().fileConfiguration.contains("Mobs.Custom.Entries." + entity.getCustomName()))
            formatted = entity.getCustomName();
          formatted = (TNE.instance().messageConfiguration().contains("Mobs.Messages.Custom." + formatted)) ? TNE.instance().messageConfiguration().getString("Mobs.Messages.Custom." + formatted) : formatted;
          TNE.debug(formatted);
          Character firstChar = formatted.charAt(0);
          messageNode = (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') ? "Mobs.Messages.KilledVowel" : "Mobs.Messages.Killed";
          //System.out.println("Mob: " + mob);
          if (TNE.instance().messageConfiguration().contains("Mobs.Messages.Custom." + formatted.replaceAll(" ", "")))
            messageNode = TNE.instance().messageConfiguration().getString("Mobs.Messages.Custom." + formatted.replaceAll(" ", ""));
          //System.out.println("Enabled: " + MobsModule.instance().mobEnabled(mob, world, id.toString()));
          if (MobsModule.instance().mobEnabled(mob, world, id.toString())) {
            //System.out.println("Mob: " + mob);
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
