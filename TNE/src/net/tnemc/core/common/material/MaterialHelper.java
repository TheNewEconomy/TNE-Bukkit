package net.tnemc.core.common.material;

import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
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

  public static void initialize() {
    for(Material mat : Material.values()) {
      List<String> nameList = (TNE.instance().itemConfiguration().contains("Items." + mat.name() + ".Names"))? TNE.instance().itemConfiguration().getStringList("Items." + mat.name() + ".Names") : Collections.singletonList(mat.name());
      String[] names = nameList.toArray(new String[nameList.size()]);
      validNames.add(new MaterialNameHelper(mat, MaterialUtils.formatMaterialName(mat), names));
    }
  }

  public static String getShopName(Material material) {
    for(MaterialNameHelper helper : validNames) {
      if(helper.getMaterial().equals(material) && helper.getShopName() != null) {
        return helper.getShopName();
      }
    }
    return MaterialUtils.formatMaterialName(material);
  }

  public static Material getMaterial(final String search) {
    //TNE.debug("MaterialHelper.getMaterial(" + search + ")");
    final Material matched = Material.getMaterial(search.toUpperCase());
    if(matched != null && !matched.equals(Material.AIR)) {
      return matched;
    }

    for(MaterialNameHelper helper : validNames) {
      if(helper.validName(search)) {
        return helper.getMaterial();
      }
    }
    //TNE.debug("Material Helper returning air.");
    return Material.AIR;
  }
}