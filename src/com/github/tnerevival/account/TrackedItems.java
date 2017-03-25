/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.account;

import com.github.tnerevival.core.material.MaterialHelper;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by creatorfromhell on 2/5/2017.
 **/
public class TrackedItems {
  private Map<Integer, Material> materialMap = new HashMap<>();

  private Location location;

  public TrackedItems(Location location) {
    this.location = location;
  }

  public Map<Integer, Material> getMaterialMap() {
    return materialMap;
  }

  public void setMaterialMap(Map<Integer, Material> materialMap) {
    this.materialMap = materialMap;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void materialsFromString(String data) {
    String[] parsed = data.split(",");

    for(String s : parsed) {
      String[] parts = s.split("-");
      Integer slot = Integer.valueOf(parts[0]);
      Material material = MaterialHelper.getMaterial(parts[1]);
    }
  }

  public String materialsToString() {
    String data = "";

    for(Map.Entry<Integer, Material> entry : materialMap.entrySet()) {
      if(data.length() > 0) data += ",";
      data += entry.getKey() + "-" + entry.getValue().name();
    }
    return data;
  }
}