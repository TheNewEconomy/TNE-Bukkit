package net.tnemc.bounty.command;

import net.tnemc.bounty.BountyData;
import net.tnemc.bounty.inventory.RewardCenterHolder;
import net.tnemc.bounty.model.RewardCenter;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/2/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BountyRewardsCommand extends TNECommand {
  public BountyRewardsCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "rewards";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.bounty.rewards";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/bounty rewards - Opens your bounty rewards center.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    final UUID id = getPlayer(sender).getUniqueId();
    final RewardCenter rewards = BountyData.getRewards(id);

    if(rewards == null || rewards.getRewards().size() == 0) {
      sender.sendMessage(ChatColor.RED + "You currently have no bounty rewards.");
      return false;
    }

    RewardCenterHolder holder = new RewardCenterHolder(rewards);
    holder.open(getPlayer(sender));
    return true;
  }
}