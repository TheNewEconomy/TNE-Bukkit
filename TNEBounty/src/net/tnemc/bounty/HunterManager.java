package net.tnemc.bounty;

import net.tnemc.bounty.model.HunterLevel;
import net.tnemc.config.ConfigSection;

import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class HunterManager {

  private final HunterLevel DEFAULT_LEVEL = new HunterLevel(1, 2, false, "<red>$target got rekt by $killer.", "say $player has reached level $level.");;

  private TreeMap<Integer, Long> experience = new TreeMap<>();
  private TreeMap<Integer, HunterLevel> levels = new TreeMap<>();

  public HunterManager() {
    loadExperience();
    loadHunterObjects();
  }

  public void loadExperience() {
    for(String str : BountyModule.instance().getHunterFileConfiguration().getSection("Hunter.Experience").getKeys()) {
      experience.put(Integer.valueOf(str), Long.valueOf(BountyModule.instance().getHunterFileConfiguration().getString("Hunter.Experience." + str)));
    }
  }

  public void loadHunterObjects() {
    ConfigSection hunterSection = BountyModule.instance().getHunterFileConfiguration().getSection("Hunter.Levels");

    for(String str : hunterSection.getKeys()) {

      final int level = Integer.valueOf(str);

      final long experience = Long.valueOf(hunterSection.getString(str + ".ExperienceGain"));
      final boolean head = hunterSection.getBool(str + ".Head");
      final short headChance = hunterSection.getShort(str + ".HeadChance");
      final String deathMessage = hunterSection.getString(str + ".DeathMessage");
      final String command = hunterSection.getString(str + ".Command");

      HunterLevel levelObject = new HunterLevel(level, experience, head, deathMessage, command);
      levelObject.setHeadChance(headChance);

      levels.put(level, levelObject);
    }
  }

  public HunterLevel getObject(int level) {
    HunterLevel levelObject = levels.floorEntry(level).getValue();

    if(levelObject == null) {
      levelObject = DEFAULT_LEVEL;
    }
    return levelObject;
  }

  public long experience(int level) {
    return experience.get(level);
  }

  public TreeMap<Integer, Long> getExperience() {
    return experience;
  }

  public void setExperience(TreeMap<Integer, Long> experience) {
    this.experience = experience;
  }

  public TreeMap<Integer, HunterLevel> getLevels() {
    return levels;
  }

  public void setLevels(TreeMap<Integer, HunterLevel> levels) {
    this.levels = levels;
  }

  public boolean canLevel(int level, long xp) {
    if(!experience.containsKey(level + 1)) return false;
    return xp >= experience.get(level + 1);
  }
}