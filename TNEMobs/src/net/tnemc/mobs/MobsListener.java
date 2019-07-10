package net.tnemc.mobs;

import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.module.ModuleListener;
import org.bukkit.entity.Ageable;
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
    final LivingEntity entity = event.getEntity();
    final Player killer = entity.getKiller();

    if(killer != null) {
      final String world = WorldFinder.getWorld(killer, WorldVariant.CONFIGURATION);
      final UUID id = IDFinder.getID(killer);
      final String type = entity.getType().name();
      final String customName = entity.getCustomName();
      final String name = entity.getName();

      String mob = getMob(entity, entity.getCustomName(), world, id);

      System.out.println("type: " + type);
      System.out.println("customName: " + customName);
      System.out.println("name: " + name);
      System.out.println("mob: " + mob);
      System.out.println("config: " + "Mobs.Messages.Custom." + type);



      System.out.println("has config name? " + TNE.instance().api().hasConfiguration("Mobs.Messages.Custom." + type));
      System.out.println("config name: " + TNE.instance().api().getString("Mobs.Messages.Custom." + type));

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

      TNEAccount account = TNE.manager().getAccount(id);



    }

    /*if (entity.getKiller() != null) {
      Player killer = entity.getKiller();

      //Permissions Check
      if (killer.hasPermission("tne.mobs.rewards")) {

        String world = WorldFinder.getWorld(killer, WorldVariant.CONFIGURATION);
        UUID id = IDFinder.getID(killer);
        TNEAccount account = TNE.manager().getAccount(id);
        String mob = entity.getCustomName();
        BigDecimal reward = MobsModule.instance().mobReward("Default", world, id.toString());
        String messageNode = "Mobs.Messages.Killed";
        Boolean player = false;
        TNE.debug ("Mobs Location: " + MobsModule.instance().mobs.getAbsolutePath());
        TNE.debug("Mobs Null: " + (MobsModule.instance().mobs == null));

        TNE.debug("TNE instance null: " + (TNE.instance() == null));
        TNE.debug("TNE api null: " + (TNE.instance().api() == null));
        TNE.debug("TNE getBoolean(Mobs.Enabled) null: " + (TNE.instance().api().getBoolean("Mobs.Enabled", world, id) == null));
        if (MobsModule.instance().fileConfiguration.getBool("Mobs.Enabled")) {
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

          //TNE.debug("Mob Name0: " + mob);
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

          //TNE.debug("Mob Name1: " + mob);
          final ItemStack tool = event.getEntity().getKiller().getInventory().getItemInMainHand();
          final String material = (tool != null && tool.getType() != null && !tool.getType().equals(Material.AIR))? tool.getType().name() : "FIST";

          if (!MobsModule.instance().fileConfiguration.contains("Mobs." + mob)) mob = "Default";
          //TNE.debug("Mob Name2: " + mob);
          if (entity.getCustomName() != null && MobsModule.instance().fileConfiguration.contains("Mobs.Custom.Entries." + entity.getCustomName()))
            mob = "Custom.Entries." + entity.getCustomName();
          String currency = MobsModule.instance().mobCurrency(mob, world, id.toString());
          reward = (player) ? MobsModule.instance().playerReward(mob, world, id.toString()) : MobsModule.instance().mobReward(mob, world, id.toString());
          reward = CurrencyFormatter.round(MISCUtils.findCurrency(world, killer.getLocation()), reward.multiply(MobsModule.instance().multiplier(material, world, id.toString())));

          TNE.debug("Reward: " + reward);

          String formatted = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null) ? event.getEntityType().toString() : mob;
          //TNE.debug("Mob Name3: " + mob);
          if (entity.getCustomName() != null && MobsModule.instance().fileConfiguration.contains("Mobs.Custom.Entries." + entity.getCustomName()))
            formatted = entity.getCustomName();
          formatted = (MobsModule.instance().fileConfiguration.contains("Mobs.Messages.Custom." + formatted)) ? MobsModule.instance().fileConfiguration.getString("Mobs.Messages.Custom." + formatted) : formatted;
          TNE.debug(formatted);
          Character firstChar = formatted.charAt(0);
          messageNode = (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') ? "Mobs.Messages.KilledVowel" : "Mobs.Messages.Killed";
          //TNE.debug("Mob: " + mob);
          if (MobsModule.instance().fileConfiguration.contains("Mobs.Messages.Custom." + formatted.replaceAll(" ", "")))
            messageNode = MobsModule.instance().fileConfiguration.getString("Mobs.Messages.Custom." + formatted.replaceAll(" ", ""));
          //TNE.debug("Enabled: " + MobsModule.instance().mobEnabled(mob, world, id.toString()));
          if (MobsModule.instance().mobEnabled(mob, world, id.toString())) {
            //TNE.debug("Mob: " + mob);
            final TNECurrency currencyObject = TNE.manager().currencyManager().get(world, currency);

            if(currencyObject.isItem()) {
              event.getDrops().addAll(ItemCalculations.getItemsForAmount(currencyObject, reward));
            }  else if(currencyObject.isXp()) {
              event.setDroppedExp(reward.intValue());
            } else {
              TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("give"));
              transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency), reward, TransactionChargeType.GAIN));
              TransactionResult result = TNE.transactionManager().perform(transaction);
              if (result.proceed() && MobsModule.instance().fileConfiguration.getBool("Mobs.Message")) {
                Message mobKilled = new Message(MobsModule.instance().fileConfiguration.getString(messageNode));
                mobKilled.addVariable("$mob", MaterialUtils.formatMaterialNameWithSpace(formatted));
                mobKilled.addVariable("$reward", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, currency), currency, reward, id.toString()));
                mobKilled.translate(world, killer);
              }
            }
          }
        }
      }
    }*/
  }

  private String getMob(LivingEntity entity, String mob, String world, UUID killer) {
    switch(entity.getType()) {
      case RABBIT:
        Rabbit rab = (Rabbit) entity;
        if (rab.getType().equals(Rabbit.Type.THE_KILLER_BUNNY)) {
          return "RABBIT_KILLER";
        }
        break;
      case PLAYER:
        Player p = (Player)entity;
        if (MobsModule.instance().playerEnabled(p.getUniqueId(), world, killer.toString())) {
          return p.getUniqueId().toString();
        }
        break;
      case ZOMBIE_VILLAGER:
        final String career = ((ZombieVillager)entity).getVillagerProfession().name().toUpperCase();
        if(MobsModule.instance().fileConfiguration.contains("Mobs." + mob + "_" + career)) {
          return mob + "_" + career;
        }
        break;
      case VILLAGER:
        final String villageCareer = ((Villager)entity).getProfession().name().toUpperCase();
        if(MobsModule.instance().fileConfiguration.contains("Mobs." + mob + "_" + villageCareer)) {
          return mob + "_" + villageCareer;
        }
        break;

      case SLIME:
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
        if (MobsModule.instance().mobEnabled(mob + "." + tier, world, killer.toString())) {
          return mob + "." + tier;
        }
        break;

      case MAGMA_CUBE:
        String magmaTier = "Small";

        switch (((MagmaCube) entity).getSize()) {
          case 1:
            break;
          case 2:
            magmaTier = "Medium";
            break;
          case 4:
            magmaTier = "Large";
            break;
          default:
            magmaTier = ((MagmaCube) entity).getSize() + "";
            break;
        }
        if (MobsModule.instance().mobEnabled(mob + "." + magmaTier, world, killer.toString())) {
          return mob + "." + magmaTier;
        }
        break;

      default:
        if(entity.getType().isAlive()) return entity.getType().getName();
        break;
    }
    return mob;
  }
}
