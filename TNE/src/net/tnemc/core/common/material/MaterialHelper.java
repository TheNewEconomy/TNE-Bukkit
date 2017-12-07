package net.tnemc.core.common.material;

import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/*
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
 * Created by creatorfromhell on 07/01/2017.
 */
public class MaterialHelper {

  private static List<MaterialNameHelper> validNames = new ArrayList<>();

  static {
    for(Material mat : Material.values()) {
      List<String> nameList = (TNE.instance().itemConfiguration().contains("Items." + mat.name() + ".Names"))? TNE.instance().itemConfiguration().getStringList("Items." + mat.name() + ".Names") : new ArrayList<String>();
      String[] names = nameList.toArray(new String[nameList.size()]);
      validNames.add(new MaterialNameHelper(mat, MaterialUtils.formatMaterialName(mat), names));
    }
    //TNE.debug("Materials Using: " + validNames.size());
  }

  public static String getShopName(Material material) {
    for(MaterialNameHelper helper : validNames) {
      if(helper.getMaterial().equals(material)) {
        if(helper.getShopName() != null) {
          return helper.getShopName();
        }
      }
    }
    return MaterialUtils.formatMaterialName(material);
  }

  public static Material getMaterial(String search) {
    //TNE.debug("MaterialHelper.getMaterial(" + search + ")");
    if(Material.getMaterial(search.toUpperCase()) != null && !Material.getMaterial(search.toUpperCase()).equals(Material.AIR)) {
      return Material.getMaterial(search.toUpperCase());
    }

    for(MaterialNameHelper helper : validNames) {
      if(helper.validName(search)) {
        return helper.getMaterial();
      }
    }
    return Material.AIR;
  }
}