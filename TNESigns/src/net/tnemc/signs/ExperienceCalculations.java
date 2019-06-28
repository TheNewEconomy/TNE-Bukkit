package net.tnemc.signs;

import net.tnemc.core.TNE;
import org.bukkit.Bukkit;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/27/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ExperienceCalculations {

  NavigableMap<Integer, Integer> experienceLevels = new TreeMap<>();

  final int maxLevels = 40;

  public ExperienceCalculations() {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), this::populateMap);
    System.out.println("Experience calculations are operational.");
  }

  void populateMap() {
    experienceLevels.put(0, 0);
    for(int i = 1; i <= maxLevels; i++) {

      int amount = 0;
      if(i <= 16) {
        amount = (int)(Math.pow(i, 2) + 6 * i);
      } else if(i <= 31) {
        amount = (int)(2.5 * Math.pow(i, 2) - 40.5 * i + 360);
      } else {
        amount = (int)(4.5 * Math.pow(i, 2) - 162.5 * i + 2220);
      }
      experienceLevels.put(amount, i);
    }
  }

  public int getLevel(int experience) {
    return experienceLevels.floorEntry(experience).getValue();
  }

  public int expTo(int level) {
    if(level <= 15) {
      return 2 * (level + 7);
    } else if(level <= 30) {
      return 5 * level - 38;
    } else {
      return 9 * level - 158;
    }
  }

  public int getXp(int level) {
    if(level <= 16) {
      return (int)(Math.pow(level, 2) + 6 * level);
    } else if(level <= 31) {
      return (int)(2.5 * Math.pow(level, 2) - 40.5 * level + 360);
    } else {
      return (int)(4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
    }
  }
}