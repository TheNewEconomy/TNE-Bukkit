package net.tnemc.core.event.module;

import org.bukkit.event.Cancellable;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class TNEModuleLoadEvent extends TNEModuleEvent implements Cancellable {
  private boolean cancelled = false;

  public TNEModuleLoadEvent(String module, String version, boolean async) {
    super(module, version, async);
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}