package net.tnemc.bounty;

import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.bounty.model.Bounty;
import net.tnemc.bounty.model.BountyHunter;
import net.tnemc.bounty.model.RewardCenter;
import net.tnemc.core.TNE;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
  public static final String prefix = TNE.saveManager().getTNEManager().getPrefix();

  public static final String BOUNTY_LOAD_ACTIVE = "SELECT bounty_id, bounty_target, bounty_benefactor, bounty_head, bounty_currency, bounty_currency_name, bounty_world, bounty_amount, bounty_reward FROM " +
      prefix + "_BOUNTY WHERE bounty_claimed = 0";
  public static final String BOUNTY_LOAD_TARGET = "SELECT bounty_id, bounty_target, bounty_benefactor, bounty_head, bounty_currency, bounty_currency_name, bounty_world, bounty_amount, bounty_reward FROM " + prefix + "_BOUNTY WHERE bounty_target = ? AND bounty_claimed = 0";
  public static final String BOUNTY_SAVE = "INSERT INTO " + prefix + "_BOUNTY (bounty_id, bounty_target, bounty_benefactor, bounty_created, bounty_head, bounty_currency, bounty_currency_name, bounty_world, bounty_amount, bounty_reward, bounty_claimant) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
      "ON DUPLICATE KEY UPDATE bounty_claimed = ?, bounty_claimant = ?, bounty_finished = ?";
  public static final String BOUNTY_DELETE = "DELETE FROM " + prefix + "_BOUNTY WHERE bounty_target = ?";
  public static final String HUNTER_LOAD = "SELECT hunter_experience, hunter_last_bounty, hunter_bounties, hunter_last_track, hunter_message, hunter_level FROM " + prefix + "_BOUNTY_HUNTER WHERE hunter_id = ?";
  public static final String HUNTER_SAVE = "INSERT INTO " + prefix + "_BOUNTY_HUNTER (hunter_id, hunter_last_bounty, hunter_experience, hunter_bounties, hunter_last_track, hunter_message, hunter_level) VALUES(?, ?, ?, ?, ?, ?, ?) " +
      "ON DUPLICATE KEY UPDATE hunter_last_bounty = ?, hunter_experience = ?, hunter_bounties = ?, hunter_last_track = ?, hunter_message = ?, hunter_level = ?";

  public static final String REWARDS_LOAD = "SELECT hunter_rewards FROM " + prefix + "_BOUNTY_REWARD WHERE hunter_id = ?";
  public static final String REWARDS_SAVE = "INSERT INTO " + prefix + "_BOUNTY_REWARD (hunter_id, hunter_rewards) VALUES(?, ?) ON DUPLICATE KEY UPDATE hunter_rewards = ?";

  public static boolean hasBounty(UUID id) {
    return getBounty(id) != null;
  }

  public static void saveBounty(Bounty bounty) {
    SQLDatabase.executePreparedUpdate(BOUNTY_SAVE, new Object[] {
        bounty.getId().toString(),
        bounty.getTarget().toString(),
        bounty.getBenefactor().toString(),
        bounty.getCreated(),
        bounty.isRequireHead(),
        bounty.isCurrencyReward(),
        bounty.getCurrency(),
        bounty.getWorld(),
        bounty.getAmount(),
        bounty.getItemReward(),
        bounty.getClaimant(),
        bounty.isClaimed(),
        bounty.getClaimant(),
        bounty.getClaimedTime()
    });

    BountyHunter hunter = getHunter(bounty.getBenefactor());
    hunter.setLastBounty(bounty.getCreated());
    saveHunter(hunter);
  }

  public static Bounty getBounty(UUID id) {
    Bounty bounty = null;

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(BOUNTY_LOAD_TARGET);
        ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
            id.toString()
        })) {
      if(results.next()) {
        bounty = new Bounty(UUID.fromString(results.getString("bounty_target")),
            UUID.fromString(results.getString("bounty_benefactor")));

        bounty.setRequireHead(results.getBoolean("bounty_head"));
        bounty.setCurrencyReward(results.getBoolean("bounty_currency"));
        bounty.setCurrency(results.getString("bounty_currency_name"));
        bounty.setWorld(results.getString("bounty_world"));
        bounty.setAmount(results.getBigDecimal("bounty_amount"));
        bounty.setItemReward(results.getString("bounty_reward"));

      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return bounty;
  }

  public static void deleteBounty(UUID id) {
    SQLDatabase.executePreparedUpdate(BOUNTY_DELETE, new Object[] { id.toString()});
  }

  public static boolean online(UUID id) {
    return Bukkit.getPlayer(id) != null;
  }

  public static RewardCenter getRewards(UUID id) {
    RewardCenter center = new RewardCenter(id);

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(REWARDS_LOAD);
        ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
            id.toString()
        })) {
      if(results.next()) {

        JSONObject object = (JSONObject)new JSONParser().parse(results.getString("hunter_rewards"));

        object.forEach((key, value)->center.getRewards().add(value.toString()));

      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return center;
  }

  public static void setRewards(UUID id, List<String> rewards) {

    JSONObject object = new JSONObject();

    int i = 0;
    for(String str : rewards) {
      object.put(i, str);
      i++;
    }
    SQLDatabase.executePreparedUpdate(REWARDS_SAVE, new Object[] {
        id.toString(),
        object.toJSONString(),
        object.toJSONString()
    });

  }

  public static void saveHunter(BountyHunter hunter) {
    SQLDatabase.executePreparedUpdate(HUNTER_SAVE, new Object[] {
        hunter.getId().toString(),
        hunter.getLastBounty(),
        hunter.getExperience(),
        hunter.getBounties(),
        hunter.getLastTrack(),
        hunter.getMessage(),
        hunter.getLevel(),
        hunter.getLastBounty(),
        hunter.getExperience(),
        hunter.getBounties(),
        hunter.getLastTrack(),
        hunter.getMessage(),
        hunter.getLevel()
    });
  }

  public static BountyHunter getHunter(UUID id) {
    BountyHunter hunter = new BountyHunter(id);

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(HUNTER_LOAD);
        ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
            id.toString()
        })) {
      if(results.next()) {
        hunter.setBounties(results.getLong("hunter_bounties"));
        hunter.setLastBounty(results.getLong("hunter_last_bounty"));
        hunter.setExperience(results.getLong("hunter_experience"));
        hunter.setLastTrack(results.getLong("hunter_last_track"));
        hunter.setMessage(results.getString("hunter_message"));
        hunter.setLevel(results.getInt("hunter_level"));

      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();

    return hunter;
  }

  public static ItemStack getItemInHand(Player player) {
    if(offHand()) {
      return player.getInventory().getItemInMainHand();
    }
    return player.getInventory().getItemInHand();
  }
}