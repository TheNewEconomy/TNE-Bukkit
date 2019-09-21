package net.tnemc.core.event.module;

import net.tnemc.core.event.TNEEvent;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 9/16/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TNEModuleDataEvent extends TNEEvent implements Cancellable {

  private Map<String, Object> data;

  private String eventName;
  private boolean cancelled = false;

  public TNEModuleDataEvent(String event, Map<String, Object> data, boolean async) {
    super(async);
    this.data = data;
    this.eventName = event;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public void addData(String id, Object value) {
    this.data.put(id, value);
  }

  @NotNull
  @Override
  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
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