package net.tnemc.signs.signs;

import net.tnemc.core.TNE;

import java.math.BigDecimal;

public enum SignType {

  UNKNOWN("unknown", "", "", ""),
  VAULT("vault", "tne.place.vault", "tne.use.vault", "Signs.Vault"),
  ITEM("item", "tne.place.item", "tne.use.item", "Signs.Item"),
  BALANCE("balance", "tne.place.balance", "tne.use.balance", "Signs.Balance");

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
    return TNE.instance().api().getBigDecimal(configuration + ".Place", world, player);
  }

  public BigDecimal use(String world, String player) {
    return TNE.instance().api().getBigDecimal(configuration + ".Use", world, player);
  }

  public Integer max(String world, String player) {
    if(TNE.instance().api().hasConfiguration(configuration + ".Max")) {
      return TNE.instance().api().getInteger(configuration + ".Max", world, player);
    }
    return -1;
  }
}