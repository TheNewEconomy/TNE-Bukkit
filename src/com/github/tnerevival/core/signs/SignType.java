package com.github.tnerevival.core.signs;

import com.github.tnerevival.TNE;

import java.math.BigDecimal;

public enum SignType {

  UNKNOWN("unknown", "", "", ""),
  BANK("bank", "tne.place.bank", "tne.use.bank", "Core.Signs.Bank"),
  VAULT("vault", "tne.place.vault", "tne.use.vault", "Core.Signs.Vault"),
  ITEM("item", "tne.place.item", "tne.use.item", "Core.Signs.Item"),
  SHOP("shop", "tne.place.shop", "tne.use.shop", "Core.Signs.Shop"),
  BALANCE("balance", "tne.place.balance", "tne.use.balance", "Core.Signs.Balance");

  private String name;
  private String placePermission;
  private String usePermission;
  private String configuration;

  SignType(String name, String placePermission, String usePermission, String configuration) {
    this.name = name;
    this.placePermission = placePermission;
    this.usePermission = usePermission;
    this.configuration = configuration;
  }

  public static SignType fromName(String name) {
    for(SignType type : values()) {
      if(type.getName().equalsIgnoreCase(name.trim())) {
        return type;
      }
    }
    return UNKNOWN;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPlacePermission() {
    return placePermission;
  }

  public void setPlacePermission(String placePermission) {
    this.placePermission = placePermission;
  }

  public String getUsePermission() {
    return usePermission;
  }

  public void setUsePermission(String usePermission) {
    this.usePermission = usePermission;
  }

  public String getConfiguration() {
    return configuration;
  }

  public void setConfiguration(String configuration) {
    this.configuration = configuration;
  }

  public Boolean enabled(String world, String player) {
    return TNE.instance().api().getBoolean(configuration + ".Enabled", world, player);
  }

  public BigDecimal place(String world, String player) {
    return new BigDecimal(TNE.instance().api().getDouble(configuration + ".Place", world, player));
  }

  public BigDecimal use(String world, String player) {
    return new BigDecimal(TNE.instance().api().getDouble(configuration + ".Use", world, player));
  }

  public Integer max(String world, String player) {
    if(TNE.instance().api().hasConfiguration(configuration + ".Max")) {
      return TNE.instance().api().getInteger(configuration + ".Max", world, player);
    }
    return -1;
  }
}