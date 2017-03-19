package com.github.tnerevival.utils;

import org.bukkit.Material;

/**
 * Created by creatorfromhell on 2/28/2017.
 * All rights reserved.
 **/
public class MaterialUtils {
  public static String formatMaterialName(Material material) {
    String[] wordsSplit = material.name().split("_");
    String sReturn = "";
    for(String w: wordsSplit) {
      String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
      sReturn += word;
    }
    return sReturn;
  }

  public static String deformatMaterialName(String name) {
    String[] split = name.split("(?=[A-Z])");
    String sReturn = "";
    int count = 1;
    for(String s : split) {
      sReturn += s.toUpperCase();
      if(count < split.length && count > 1) {
        sReturn += "_";
      }
      count++;
    }
    return sReturn;
  }

  public static String formatMaterialNameWithSpace(Material material) {
    String[] wordsSplit = material.name().split("_");
    String sReturn = "";
    int count = 1;
    for(String w: wordsSplit) {
      String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
      sReturn += word;
      if(count < wordsSplit.length) {
        sReturn += " ";
      }
    }
    return sReturn;
  }

  public static String deformatMaterialNameWithSpace(String name) {
    String upperCase = name.toUpperCase();
    return upperCase.replace(" ", "_");
  }
}