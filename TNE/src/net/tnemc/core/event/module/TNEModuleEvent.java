package net.tnemc.core.event.module;

import net.tnemc.core.event.TNEEvent;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public abstract class TNEModuleEvent extends TNEEvent {
  protected String module;
  protected String version;

  public TNEModuleEvent(String module, String version, boolean async) {
    super(async);
    this.module = module;
    this.version = version;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }
}