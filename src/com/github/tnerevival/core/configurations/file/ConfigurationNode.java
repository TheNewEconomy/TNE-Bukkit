package com.github.tnerevival.core.configurations.file;

import com.google.common.base.Optional;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by creatorfromhell on 4/30/2017.
 * All rights reserved.
 **/
public class ConfigurationNode {
  private Map<String, Object> children = new LinkedHashMap<>();
  private Set<String> comments = new HashSet<>();

  private ConfigurationNode parent;
  private String path;

  public ConfigurationNode() {
    this(null);
  }

  public ConfigurationNode(ConfigurationNode parent) {
    if(parent != null || this instanceof ConfigurationFile) {
      this.parent = parent;
    }
    //TODO: Handle invalid parent nodes.
  }

  public Set<String> getComments() {
    return comments;
  }

  public Optional<ConfigurationNode> getParent() {
    if(parent != null) {
      return Optional.of(parent);
    }
    return null;
  }

  public String getPath() {
    return path;
  }

  public Optional<Object> get(String path) {
    return Optional.of(children.get(path));
  }

  public Optional<ConfigurationValue> getValue(String path) {
    if(children.containsKey(path) && children.get(path) instanceof ConfigurationValue) {
      return Optional.of((ConfigurationValue)children.get(path));
    }
    return null;
  }


}