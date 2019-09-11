package net.tnemc.mobs;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.common.utils.MaterialUtils;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
    final LivingEntity entity = event.getEntity();
    final Player killer = entity.getKiller();

    if(killer != null) {

      if(killer.hasPermission("tne.mobs.rewards")) {
        Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{

          final String world = WorldFinder.getWorld(killer, WorldVariant.CONFIGURATION);
          final UUID id = IDFinder.getID(killer);
          final String type = entity.getType().name();
          final String customName = entity.getCustomName();
          final String name = entity.getName();
          Boolean player = (entity instanceof Player && MobsModule.instance().playerEnabled(IDFinder.getID(((Player)entity)), world, id.toString()));

          String mob = getMob(entity, entity.getCustomName(), world, id);

          TNE.debug("type: " + type);
          TNE.debug("customName: " + customName);
          TNE.debug("name: " + name);
          TNE.debug("mob: " + mob);
          TNE.debug("config: " + "Mobs.Messages.Custom." + type);


          TNE.debug("has config name? " + TNE.instance().api().hasConfiguration("Mobs.Messages.Custom." + type));
          TNE.debug("config name: " + TNE.instance().api().getString("Mobs.Messages.Custom." + type));

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
          final ItemStack tool = killer.getInventory().getItemInMainHand();
          final String material = (tool != null && tool.getType() != null && !tool.getType().equals(Material.AIR))? tool.getType().name() : "FIST";


          String messageNode = "Mobs.Messages.Killed";
          String currency = MobsModule.instance().mobCurrency(mob, world, id.toString());
          BigDecimal reward = (player) ? MobsModule.instance().playerReward(mob, world, id.toString()) : MobsModule.instance().mobReward(mob, world, id.toString());
          reward = CurrencyFormatter.round(MISCUtils.findCurrency(world, killer.getLocation()), reward.multiply(MobsModule.instance().multiplier(material, world, id.toString())));

          if (!TNE.instance().api().hasConfiguration("Mobs." + mob)) mob = "Default";

          if(!TNE.instance().api().getBoolean("Mobs." + mob + ".Enabled")) {
            return;
          }

          String formatted = (mob.equalsIgnoreCase("Default") && event.getEntityType().toString() != null) ? event.getEntityType().toString() : mob;

          //TNE.debug("Mob Name3: " + mob);
          if (entity.getCustomName() != null && TNE.instance().api().hasConfiguration("Mobs.Custom.Entries." + entity.getCustomName()))
            formatted = entity.getCustomName();
          formatted = (TNE.instance().api().hasConfiguration("Mobs.Messages.Custom." + formatted)) ? TNE.instance().api().getString("Mobs.Messages.Custom." + formatted) : formatted;
          //TNE.debug(formatted);
          Character firstChar = formatted.charAt(0);
          messageNode = (firstChar == 'a' || firstChar == 'e' || firstChar == 'i' || firstChar == 'o' || firstChar == 'u') ? "Mobs.Messages.KilledVowel" : "Mobs.Messages.Killed";
          //TNE.debug("Mob: " + mob);
          if (TNE.instance().api().hasConfiguration("Mobs.Messages.Entries." + formatted.replaceAll(" ", "")))
            messageNode = "Mobs.Messages.Entries." + formatted.replaceAll(" ", "");
          //TNE.debug("Enabled: " + MobsModule.instance().mobEnabled(mob, world, id.toString()));
          if (MobsModule.instance().mobEnabled(mob, world, id.toString())) {
            //TNE.debug("Mob: " + mob);
            final TNECurrency currencyObject = TNE.manager().currencyManager().get(world, currency);

            AsyncMobRewardEvent mobRewardEvent = new AsyncMobRewardEvent(entity, killer, world, currency, currencyObject.getType(), reward);
            TNE.instance().api().callEvent(mobRewardEvent);

            if(!mobRewardEvent.isCancelled() && mobRewardEvent.getReward().compareTo(BigDecimal.ZERO) > 0) {

              if (mobRewardEvent.currencyType.equalsIgnoreCase("item")) {
                for (ItemStack stack : ItemCalculations.getItemsForAmount(currencyObject, mobRewardEvent.getReward())) {
                  if (stack == null || stack.getType().equals(Material.AIR) || stack.getAmount() == 0) continue;
                  Bukkit.getScheduler().runTask(plugin, () -> entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), stack));
                }
              } else if (mobRewardEvent.currencyType.equalsIgnoreCase("experience")) {
                event.setDroppedExp(mobRewardEvent.getReward().intValue());
              } else {
                if(TNE.instance().api().hasConfiguration("Mobs.Note") && TNE.instance().api().getBoolean("Mobs.Note")) {
                  final BigDecimal rewardFinal = mobRewardEvent.getReward();
                  Bukkit.getScheduler().runTask(plugin, () -> entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), TNE.manager().currencyManager().createNote(currencyObject.name(), world, rewardFinal)));
                } else {
                  TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("give"));
                  transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, currency), mobRewardEvent.getReward(), TransactionChargeType.GAIN));
                  TransactionResult result = TNE.transactionManager().perform(transaction);
                }
              }
            }

            if(!mobRewardEvent.isCancelled() && TNE.instance().api().getBoolean("Mobs.Message")) {
              if (reward.compareTo(BigDecimal.ZERO) > 0 || TNE.instance().api().getBoolean("Mobs.MessageZero")) {
                Message mobKilled = new Message(TNE.instance().api().getString(messageNode));
                mobKilled.addVariable("$mob", MaterialUtils.formatMaterialNameWithSpace(formatted));
                mobKilled.addVariable("$reward", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, currency), currency, mobRewardEvent.getReward(), id.toString()));
                mobKilled.translate(world, killer);
              }
            }
          }
        });
      }
    }
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
        if(TNE.instance().api().hasConfiguration("Mobs." + mob + "_" + career)) {
          return mob + "_" + career;
        }
        break;
      case VILLAGER:
        final String villageCareer = ((Villager)entity).getProfession().name().toUpperCase();
        if(TNE.instance().api().hasConfiguration("Mobs." + mob + "_" + villageCareer)) {
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
        if(entity.getType().isAlive()) return entity.getType().name();
        break;
    }
    return mob;
  }
}
