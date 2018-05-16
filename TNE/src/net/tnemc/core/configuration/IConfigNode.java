package net.tnemc.core.configuration;

public interface IConfigNode {

  String getNode();

  default String getDefaultValue() {
    return "";
  }

  default String[] getComments() {
    return new String[] { "" };
  }
}