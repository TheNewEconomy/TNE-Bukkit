package com.github.tnerevival.core.configurations;

/**
 * Created by Daniel on 10/11/2016.
 */
public enum ConfigurationType {
  UNKNOWN("", ""),
  MAIN("main", "Core"),
  Mobs("mob", "Mobs"),
  MESSAGES("messages", "Messages"),
  OBJECTS("objects", "Objects"),
  MATERIALS("materials", "Materials"),
  PLAYERS("players", "Players"),
  WORLDS("worlds", "Worlds");

  private String identifier;

  private String prefix;

  ConfigurationType(String identifier, String prefix) {
    this.identifier = identifier;
    this.prefix = prefix;
  }

  public static ConfigurationType fromID(String identifier) {
    for(ConfigurationType type : values()) {
      if(type.getIdentifier().equalsIgnoreCase(identifier)) {
        return type;
      }
    }
    return UNKNOWN;
  }

  public static ConfigurationType fromPrefix(String prefix) {
    for(ConfigurationType type : values()) {
      if(type.getPrefix().equalsIgnoreCase(prefix)) {
        return type;
      }
    }
    return UNKNOWN;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getPrefix() {
    return prefix;
  }
}
