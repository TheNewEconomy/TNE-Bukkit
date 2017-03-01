package com.github.tnerevival.core.material;

import org.bukkit.Material;

public class MaterialNameHelper {

  private Material material;
  private String minecraftName;
  private String shopName;
  private String[] commonNames;

  public MaterialNameHelper(Material material) {
    this(material, new String[0]);
  }

  public MaterialNameHelper(Material material, String[] commonNames) {
    this(material, material.name(), commonNames);
  }

  public MaterialNameHelper(Material material, String shopName, String[] commonNames) {
    this.material = material;
    this.minecraftName = material.name();
    this.shopName = shopName;
    this.commonNames = commonNames;
  }

  public boolean validName(String name) {
    if(this.minecraftName.equalsIgnoreCase(name)) return true;
    if(this.shopName.equalsIgnoreCase(name)) return true;
    for(String s : commonNames) {
      if(s.equalsIgnoreCase(name)) return true;
    }
    return false;
  }

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
  }

  public String getMinecraftName() {
    return minecraftName;
  }

  public void setMinecraftName(String minecraftName) {
    this.minecraftName = minecraftName;
  }

  public String getShopName() {
    return shopName;
  }

  public void setShopName(String shopName) {
    this.shopName = shopName;
  }

  public String[] getCommonNames() {
    return commonNames;
  }

  public void setCommonNames(String[] commonNames) {
    this.commonNames = commonNames;
  }
}