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

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by creatorfromhell on 2/5/2017.
 **/
public class TrackedItems {
  private Map<Material, Integer> materialMap = new HashMap<>();

  private Location location;

  public TrackedItems(Location location) {
    this.location = location;
  }

  public Map<Material, Integer> getMaterialMap() {
    return materialMap;
  }

  public void setMaterialMap(Map<Material, Integer> materialMap) {
    this.materialMap = materialMap;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }
}