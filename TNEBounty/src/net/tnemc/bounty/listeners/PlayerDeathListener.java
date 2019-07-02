package net.tnemc.bounty.listeners;

import net.tnemc.bounty.BountyData;
import net.tnemc.bounty.model.Bounty;
import net.tnemc.bounty.model.RewardCenter;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.item.SerialItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.parser.ParseException;

import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerDeathListener implements Listener {

  private TNE plugin;

  public PlayerDeathListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEvent(final PlayerDeathEvent event) {
    Player player = event.getEntity();
    final UUID id = IDFinder.getID(player);
    Player killer = player.getKiller();
    if(killer != null && BountyData.hasBounty(id)) {
      final UUID killerID = IDFinder.getID(killer);
      Bounty bounty = BountyData.getBounty(id);
      final String world = WorldFinder.getWorld(killer, WorldVariant.BALANCE);

      killer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10f, 1f);
      player.playSound(player.getLocation(), Sound.ENTITY_ARMOR_STAND_BREAK, 10f, 1f);
      Bukkit.broadcastMessage(ChatColor.YELLOW + killer.getDisplayName() + ChatColor.YELLOW + " has claimed the bounty for " + player.getDisplayName());

      if(bounty.isCurrencyReward()) {
        final TNECurrency currency = TNE.manager().currencyManager().get(bounty.getWorld(), bounty.getCurrency());
        plugin.api().addHoldings(killerID.toString(), bounty.getAmount(), currency, bounty.getWorld());
        killer.sendMessage(ChatColor.YELLOW + "You received " + CurrencyFormatter.format(currency, bounty.getAmount(), killer.getLocation(), "") + ChatColor.YELLOW + " for claiming a bounty.");
      } else {
        RewardCenter rewards = BountyData.getRewards(killerID);

        if(rewards.getRewards().size() >= 54) {
          try {
            Map<Integer, ItemStack> left = killer.getInventory().addItem(SerialItem.unserialize(bounty.getItemReward()).getStack());
            if(left.size() <= 0) {
              killer.sendMessage(ChatColor.YELLOW + "Your bounty reward center is full, your reward has been added to your inventory.");
            } else {
              killer.getLocation().getWorld().dropItemNaturally(killer.getLocation(), left.get(0));
              killer.sendMessage(ChatColor.YELLOW + "Your bounty reward center and inventory is full, your reward has been dropped on the ground.");
            }
          } catch (ParseException ignore) {
          }
        } else {
          rewards.getRewards().add(bounty.getItemReward());
          BountyData.setRewards(killerID, rewards.getRewards());
          killer.sendMessage(ChatColor.YELLOW + "Your bounty reward has been sent to your reward center. Type \"/bounty rewards\" to claim it.");
        }
      }

      ItemStack skull = TNE.item().build("PLAYER_HEAD");
      SkullMeta meta = (SkullMeta) skull.getItemMeta();
      meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
      skull.setItemMeta(meta);

      if(bounty.isRequireHead()) {
        Player benefactor = Bukkit.getPlayer(bounty.getBenefactor());

        RewardCenter benRewards = BountyData.getRewards(bounty.getBenefactor());

        benRewards.getRewards().add(new SerialItem(skull).serialize());
        BountyData.setRewards(bounty.getBenefactor(), benRewards.getRewards());

        if(benefactor != null) {
          benefactor.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10f, 1f);
          benefactor.sendMessage(ChatColor.YELLOW + "You have a new head waiting from a bounty claim. Type \"/bounty rewards\" to claim it.");
        }
      } else {
        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), skull);
      }
      BountyData.deleteBounty(id);
    }
  }
}