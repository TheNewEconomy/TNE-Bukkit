package net.tnemc.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class TNEEvent extends Event {
  private static final HandlerList handlers = new HandlerList();

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public TNEEvent(boolean async) {
    super(async);
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }
}
