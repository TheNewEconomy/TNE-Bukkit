package net.tnemc.core.configuration;

import java.io.File;

public class ConfigurationEntry {

  private IConfigNode[] nodes;
  private String file;
  private boolean module;
  private String owner = "TheNewEconomy";
  private net.tnemc.core.configuration.CommentedConfiguration newConfig;
  private CommentedConfiguration oldConfig;

  public <T extends Enum<T> & IConfigNode> ConfigurationEntry(Class<T> nodes, File file) {
    this(nodes, file, false, "TheNewEconomy");
  }

  public <T extends Enum<T> & IConfigNode> ConfigurationEntry(Class<T> nodes, File file, boolean module, String owner) {
    this(nodes.getEnumConstants(), file, module, owner);
  }

  public <T extends Enum<T> & IConfigNode> ConfigurationEntry(IConfigNode[] nodes, File file, boolean module, String owner) {
    this.nodes = nodes;
    this.file = file.getName();
    this.module = module;
    this.owner = owner;
    this.newConfig = new CommentedConfiguration(file);
    this.oldConfig = new CommentedConfiguration(file);
  }

  public IConfigNode[] getNodes() {
    int length = nodes.length + net.tnemc.core.configuration.CommonNodes.values().length;
    IConfigNode[] nodeArray = new IConfigNode[length];
    int index = 0;
    for(IConfigNode node : net.tnemc.core.configuration.CommonNodes.values()) {
      nodeArray[index] = node;
      index++;
    }

    for(IConfigNode node : nodes) {
      nodeArray[index] = node;
      index++;
    }

    return nodeArray;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public boolean isModule() {
    return module;
  }

  public void setModule(boolean module) {
    this.module = module;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public CommentedConfiguration getNewConfig() {
    return newConfig;
  }

  public void setNewConfig(CommentedConfiguration newConfig) {
    this.newConfig = newConfig;
  }

  public CommentedConfiguration getOldConfig() {
    return oldConfig;
  }

  public void setOldConfig(CommentedConfiguration oldConfig) {
    this.oldConfig = oldConfig;
  }
}