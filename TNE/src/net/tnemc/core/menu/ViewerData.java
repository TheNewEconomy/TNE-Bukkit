package net.tnemc.core.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/5/2017.
 */
public class ViewerData {
  private Map<String, Object> data = new HashMap<>();
  private UUID viewer;

  public ViewerData(UUID viewer) {
    this.viewer = viewer;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public UUID getViewer() {
    return viewer;
  }

  public void setViewer(UUID viewer) {
    this.viewer = viewer;
  }

  public Object getValue(String identifier) {
    return data.get(identifier);
  }

  public void setValue(String identifier, Object value) {
    data.put(identifier, value);
  }
}