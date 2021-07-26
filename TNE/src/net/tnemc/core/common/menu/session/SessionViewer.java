package net.tnemc.core.common.menu.session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by creatorfromhell.
 *
 * The New Plugin Core Minecraft Server Plugin
 *
 * All rights reserved.
 *
 * Some Details about what is acceptable use of this software:
 *
 * This project accepts user contributions.
 *
 * Direct redistribution of this software is not allowed without written permission. However,
 * compiling this project into your software to utilize it as a library is acceptable as long
 * as it's not used for commercial purposes.
 *
 * Commercial usage is allowed if a commercial usage license is bought and verification of the
 * purchase is able to be provided by both parties.
 *
 * By contributing to this software you agree that your rights to your contribution are handed
 * over to the owner of the project.
 */
public class SessionViewer {

  private List<Integer> allowedSlots = new ArrayList<>();

  private UUID id;

  private boolean limited = false;

  public SessionViewer(UUID id) {
    this.id = id;
  }

  public SessionViewer(List<Integer> allowedSlots, UUID id) {
    this.allowedSlots = allowedSlots;
    this.id = id;
  }

  public List<Integer> getAllowedSlots() {
    return allowedSlots;
  }

  public void setAllowedSlots(List<Integer> allowedSlots) {
    this.allowedSlots = allowedSlots;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public boolean isLimited() {
    return limited;
  }

  public void setLimited(boolean limited) {
    this.limited = limited;
  }
}