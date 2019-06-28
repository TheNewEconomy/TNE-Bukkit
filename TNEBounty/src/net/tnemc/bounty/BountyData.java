package net.tnemc.bounty;

import net.tnemc.bounty.model.Bounty;
import net.tnemc.bounty.model.RewardCenter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

import static net.tnemc.core.common.utils.MISCUtils.offHand;

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
public class BountyData {

  public static final String BOUNTY_EXISTS = "SELECT bounty_id FROM " + BountyModule.prefix + "_BOUNTY WHERE bounty_target = ? AND bounty_claimed = 0";
  public static final String BOUNTY_LOAD_ACTIVE = "SELECT bounty_id, bounty_target, bounty_benefactor, bounty_head, bounty_currency, bounty_amount, bounty_reward FROM " +
      BountyModule.prefix + "_BOUNTY WHERE bounty_claimed = 0";
  public static final String BOUNTY_LOAD_TARGET = "SELECT bounty_id, bounty_head, bounty_currency, bounty_amount, bounty_reward FROM " + BountyModule.prefix + "_BOUNTY WHERE bounty_target = ? AND bounty_claimed = 0";
  public static final String BOUNTY_SAVE = "INSERT INTO " + BountyModule.prefix + "_BOUNTY (bounty_id, bounty_target, bounty_benefactor, bounty_created, bounty_head, bounty_currency, bounty_amount, bounty_reward) VALUES(?, ?, ?, ?, ?, ?, ?, ?) " +
      "ON DUPLICATE KEY UPDATE bounty_claimed = ?, bounty_claimant = ?, bounty_finished = ?";

  public static final String HUNTER_LOAD = "SELECT hunter_experience, hunter_level FROM " + BountyModule.prefix + "_BOUNTY_HUNTER WHERE hunter_id = ?";
  public static final String HUNTER_SAVE = "INSERT INTO " + BountyModule.prefix + "_BOUNTY_HUNTER (hunter_id, hunter_experience, hunter_level) VALUES(?, ?, ?) " +
      "ON DUPLICATE KEY UPDATE hunter_experience = ?, hunter_level = ?";

  public static final String REWARDS_LOAD = "SELECT hunter_rewards FROM " + BountyModule.prefix + "_BOUNTY_REWARD WHERE hunter_id = ?";
  public static final String REWARDS_SAVE = "INSERT INTO " + BountyModule.prefix + "_BOUNTY_REWARD (hunter_id, hunter_rewards) VALUES(?, ?) ON DUPLICATE KEY UPDATE hunter_rewards = ?";

  public static boolean hasBounty(UUID id) {
    return false;
  }

  public static void saveBounty(Bounty bounty) {

  }

  public static Bounty getBounty(UUID id) {
    return null;
  }

  public static void deleteBounty(UUID id) {

  }

  public static void claimBounty(UUID id) {

  }

  public static boolean hasBountyProfile(UUID id) {
    return false;
  }

  public static int getHunterLevel(UUID id) {
    return 1;
  }

  public static long getHunterExperience(UUID id) {
    return 0;
  }

  public static boolean online(UUID id) {
    return Bukkit.getPlayer(id) != null;
  }

  public static RewardCenter getRewards(UUID id) {
    return null;
  }

  public static void setRewards(UUID id, List<String> rewards) {

  }

  public static ItemStack getItemInHand(Player player) {
    if(offHand()) {
      return player.getInventory().getItemInMainHand();
    }
    return player.getInventory().getItemInHand();
  }
}