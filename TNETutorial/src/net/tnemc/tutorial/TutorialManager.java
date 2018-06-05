package net.tnemc.tutorial;

import net.tnemc.tutorial.tutorials.Learner;
import net.tnemc.tutorial.tutorials.Tutorial;
import net.tnemc.tutorial.tutorials.impl.MoneyTutorial;
import net.tnemc.tutorial.tutorials.impl.SignTutorial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/29/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TutorialManager {

  private Map<UUID, Learner> learners = new HashMap<>();
  private Map<String, Tutorial> tutorials = new HashMap<>();

  public TutorialManager() {
    addTutorial(new MoneyTutorial());
    addTutorial(new SignTutorial());
  }

  public void addLearner(Learner learner) {
    learners.put(learner.getId(), learner);
  }

  public void removeLearner(UUID learner) {
    final Learner learnerInstance = learners.get(learner);
    Player player = Bukkit.getPlayer(learner);
    player.setOp(learnerInstance.isOp());

    learnerInstance.getBlockModificationMap().values().forEach((modification)->{
      modification.getLocation().getBlock().setType(modification.getOldBlock());
    });
    learners.remove(learner);
  }

  public void addTutorial(Tutorial tutorial) {
    tutorials.put(tutorial.name(), tutorial);
  }

  public Map<UUID, Learner> getLearners() {
    return learners;
  }

  public Map<String, Tutorial> getTutorials() {
    return tutorials;
  }
}