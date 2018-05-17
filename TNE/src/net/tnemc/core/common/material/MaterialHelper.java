package net.tnemc.core.common.material;

import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 07/01/2017.
 */
public class MaterialHelper {

  private static List<MaterialNameHelper> validNames = new ArrayList<>();

  static {
    for(Material mat : Material.values()) {
     //TODO: List<String> nameList = (TNE.instance().itemConfiguration().contains("Items." + mat.name() + ".Names"))? TNE.instance().itemConfiguration().getStringList("Items." + mat.name() + ".Names") : new ArrayList<String>();
      //String[] names = nameList.toArray(new String[nameList.size()]);
      //validNames.add(new MaterialNameHelper(mat, MaterialUtils.formatMaterialName(mat), names));
    }
    //TNE.debug("Materials Using: " + validNames.size());
  }

  public static String getShopName(Material material) {
    for(MaterialNameHelper helper : validNames) {
      if(helper.getMaterial().equals(material) && helper.getShopName() != null) {
        return helper.getShopName();
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