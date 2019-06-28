package net.tnemc.bounty.model;

import java.util.ArrayList;
import java.util.List;
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
public class RewardCenter {

  private List<String> rewards = new ArrayList<>();

  private UUID id;

  public RewardCenter(UUID id) {
    this.id = id;
  }

  public List<String> getRewards() {
    return rewards;
  }

  public void setRewards(List<String> rewards) {
    this.rewards = rewards;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}