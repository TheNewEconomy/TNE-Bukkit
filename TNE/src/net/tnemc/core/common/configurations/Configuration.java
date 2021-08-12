package net.tnemc.core.common.configurations;

import net.tnemc.config.CommentedConfiguration;
import net.tnemc.core.TNE;

import java.io.File;
import java.util.List;


public abstract class Configuration {

  public abstract CommentedConfiguration getConfiguration();

  public abstract List<String> node();

  public abstract File getFile();

  public void load(CommentedConfiguration configurationFile) {
    configurationFile.load(false);
  }

  public void save(CommentedConfiguration configurationFile) {
    configurationFile.save(getFile());
    load(configurationFile);
  }

  public Boolean hasNode(String node) {
    return getConfiguration().contains(node);
  }

  public Object getValue(String node) {
    TNE.debug("Value: " + getConfiguration().getString(node));
    return getConfiguration().getString(node);
  }

  public Object getValue(String node, String world) {
    return getValue(node);
  }

  public Object getValue(String node, String world, String player) {
    return getValue(node);
  }

  public void setValue(String node, Object value) {
    getConfiguration().set(node, value.toString());
  }

}