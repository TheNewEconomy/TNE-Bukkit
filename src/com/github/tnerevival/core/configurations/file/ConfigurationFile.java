package com.github.tnerevival.core.configurations.file;

import com.github.tnerevival.utils.MISCUtils;
import com.google.common.base.Optional;

import java.io.File;
import java.net.URL;

/**
 * Created by creatorfromhell on 4/30/2017.
 * All rights reserved.
 **/
public class ConfigurationFile extends ConfigurationNode {

  File configurationFile = null;
  File defaultConfiguration = null;

  boolean copyDefaults = false;
  boolean removeUnused = false;

  public ConfigurationFile(Object configurationFile) {
    this(configurationFile, null, false, false);
  }

  public ConfigurationFile(Object configurationFile, Object defaultConfiguration) {
    this(configurationFile, defaultConfiguration, false, false);
  }

  public ConfigurationFile(Object configurationFile, Object defaultConfiguration, boolean copyDefaults) {
    this(configurationFile, defaultConfiguration, copyDefaults, false);
  }

  public ConfigurationFile(Object configurationFile, Object defaultConfiguration, boolean copyDefaults, boolean removeUnused) {
    this.copyDefaults = copyDefaults;
    this.removeUnused = removeUnused;
    initialize(configurationFile, defaultConfiguration);
  }

  private void initialize(Object configurationFile, Object defaultConfiguration) {
    Optional<File> configuration = testFileArgument(configurationFile);
    Optional<File> defaults = testFileArgument(defaultConfiguration);

    if(configuration.isPresent()) {
      this.configurationFile = configuration.get();
    }

    if(defaults.isPresent()) {
      this.defaultConfiguration = defaults.get();
    }

    if(!configuration.isPresent()) {
      MISCUtils.debug("Unable to load a configuration file.");
      return;
    }
    loadConfigurations();
  }

  private void loadConfigurations() {
    //TODO: File parsing.
  }

  private Optional<File> testFileArgument(Object file) {
    if(!(file instanceof File) && !(file instanceof String)) {
      return Optional.absent();
    }

    if(file instanceof String) {
      try {
        //For testing purposes only.
        URL url = getClass().getClassLoader().getResource((String)file);
        if(url == null) return Optional.absent();
        return Optional.of(new File(url.toURI()));
      } catch(Exception e) {
        MISCUtils.debug(e);
      }
    }
    return Optional.absent();
  }
}