package com.github.tnerevival.core.material;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.MaterialUtils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialHelper {

  private static List<MaterialNameHelper> validNames = new ArrayList<>();

  static {
    for(Material mat : Material.values()) {
      List<String> nameList = (TNE.instance().itemConfigurations.contains("Items." + mat.name() + ".Names"))? TNE.instance().itemConfigurations.getStringList("Items." + mat.name() + ".Names") : new ArrayList<String>();
      String[] names = nameList.toArray(new String[nameList.size()]);
      validNames.add(new MaterialNameHelper(mat, MaterialUtils.formatMaterialName(mat), names));
    }
    MISCUtils.debug("Materials Using: " + validNames.size());
  }


  public static Material getMaterial(String search) {
    if(!Material.getMaterial(search.toUpperCase()).equals(Material.AIR)) {
      return Material.getMaterial(search.toUpperCase());
    }

    for(MaterialNameHelper helper : validNames) {
      if(helper.validName(search)) {
        return helper.material;
      }
    }
    return Material.AIR;
  }
}