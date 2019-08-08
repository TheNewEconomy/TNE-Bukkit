package net.tnemc.core.common.currency;

import org.bukkit.entity.Player;

public class Experience {

  /**
   * Calculates a player's total exp based on level and progress to next.
   *
   * @param player the Player
   *
   * @return the amount of exp the Player has
   */
  public static int getExperience(Player player) {
    return getExperienceFromLevel(player.getLevel())
        + Math.round(getExperienceToNext(player.getLevel()) * player.getExp());
  }

  /**
   * Calculates total experience based on level.
   *
   * "One can determine how much experience has been collected to reach a level using the equations:
   *
   *  Total Experience = [Level]2 + 6[Level] (at levels 0-15)
   *                     2.5[Level]2 - 40.5[Level] + 360 (at levels 16-30)
   *                     4.5[Level]2 - 162.5[Level] + 2220 (at level 31+)"
   *
   * @param level the level
   *
   * @return the total experience calculated
   */
  public static int getExperienceFromLevel(int level) {
    if (level > 30) {
      return (int) (4.5 * level * level - 162.5 * level + 2220);
    }
    if (level > 15) {
      return (int) (2.5 * level * level - 40.5 * level + 360);
    }
    return level * level + 6 * level;
  }

  /**
   * Calculates level based on total experience.
   *
   * @param exp the total experience
   *
   * @return the level calculated
   */
  public static double getLevelFromExperience(long exp) {
    if (exp > 1395) {
      return (Math.sqrt(72 * exp - 54215) + 325) / 18;
    }
    if (exp > 315) {
      return Math.sqrt(40 * exp - 7839) / 10 + 8.1;
    }
    if (exp > 0) {
      return Math.sqrt(exp + 9) - 3;
    }
    return 0;
  }

  /**
   *
   * "The formulas for figuring out how many experience orbs you need to get to the next level are as follows:
   *  Experience Required = 2[Current Level] + 7 (at levels 0-15)
   *                        5[Current Level] - 38 (at levels 16-30)
   *                        9[Current Level] - 158 (at level 31+)"
   */
  private static int getExperienceToNext(int level) {
    if (level > 30) {
      return 9 * level - 158;
    }
    if (level > 15) {
      return 5 * level - 38;
    }
    return 2 * level + 7;
  }

  /**
   * Change a Player's exp.
   * <p>
   * This method should be used in place of {@link Player#giveExp(int)}, which does not properly
   * account for different levels requiring different amounts of experience.
   *
   * @param player the Player affected
   * @param exp the amount of experience to add or remove
   */
  public static void setExperience(Player player, int exp) {
    final int playerExperience = getExperience(player);
    final boolean remove = exp < playerExperience;

    final int change = (remove)? playerExperience - exp : exp - playerExperience;

    changeExperience(player, change, remove);
  }

  /**
   * Change a Player's exp.
   * <p>
   * This method should be used in place of {@link Player#giveExp(int)}, which does not properly
   * account for different levels requiring different amounts of experience.
   *
   * @param player the Player affected
   * @param exp the amount of experience to add or remove
   */
  public static void changeExperience(Player player, int exp, boolean remove) {
    int xp = ((remove)? getExperience(player) - exp : getExperience(player) + exp);

    if (xp < 0) {
      xp = 0;
    }

    double levelAndExp = getLevelFromExperience(xp);

    int level = (int) levelAndExp;
    player.setLevel(level);
    player.setExp((float) (levelAndExp - level));
  }
}