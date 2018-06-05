package net.tnemc.tutorial.tutorials;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
public class Learner {

  private ConcurrentHashMap<String, TutorialBlockModification> blockModificationMap = new ConcurrentHashMap<>();

  private UUID id;
  private boolean isOp;

  public Learner(UUID id, boolean isOp) {
    this.id = id;
    this.isOp = isOp;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public boolean isOp() {
    return isOp;
  }

  public void setOp(boolean op) {
    isOp = op;
  }

  public void addBlockModification(TutorialBlockModification modification) {
    synchronized (blockModificationMap) {
      blockModificationMap.put(modification.getIdentifier(), modification);
    }
  }

  public ConcurrentHashMap<String, TutorialBlockModification> getBlockModificationMap() {
    return blockModificationMap;
  }
}