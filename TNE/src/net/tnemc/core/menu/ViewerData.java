package net.tnemc.core.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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