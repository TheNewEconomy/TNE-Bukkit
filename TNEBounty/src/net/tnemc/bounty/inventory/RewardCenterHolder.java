package net.tnemc.bounty.inventory;

import net.tnemc.bounty.BountyData;
import net.tnemc.bounty.model.RewardCenter;
import net.tnemc.core.item.SerialItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
public class RewardCenterHolder implements InventoryHolder {

  private Inventory inventory;
  private RewardCenter center;

  public RewardCenterHolder(RewardCenter center) {
    this.center = center;

  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }

  public void saveRewards() {
    List<String> rewards = new ArrayList<>();

    for(ItemStack item : inventory.getContents()) {
      rewards.add(new SerialItem(item).serialize());
    }
    center.setRewards(rewards);

    BountyData.setRewards(center.getId(), center.getRewards());
  }
}