package net.tnemc.core.event.account;

import net.tnemc.core.event.TNEEvent;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class TNEAccountEvent extends TNEEvent {
  private UUID id;

  public TNEAccountEvent(UUID id) {
    super(false);
    this.id = id;
  }

  public TNEAccountEvent(UUID id, boolean async) {
    super(async);
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}