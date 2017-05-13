package com.github.tnerevival.core.configurations.file;

import com.github.tnerevival.utils.MISCUtils;
import com.google.common.base.Optional;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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
    try {
      InputStream inputStream = new FileInputStream(this.configurationFile);
      Yaml parser = new Yaml();
      Iterable<Object> it = parser.loadAll(inputStream);
      for(Object object : it) {

      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void loadConfigurationsTesting() {
    //TODO: File parsing.
    try {
      InputStream inputStream = new FileInputStream(this.configurationFile);
      Yaml parser = new Yaml();
      for(Object node : parser.loadAll(inputStream)) {
        if(node instanceof LinkedHashMap) {
          LinkedHashMap<Object, Object> map = (LinkedHashMap)node;
          Iterator<Map.Entry<Object, Object>> it = map.entrySet().iterator();
          while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            if(entry.getValue() instanceof LinkedHashMap) {
              LinkedHashMap<Object, Object> map2 = (LinkedHashMap)entry.getValue();
              Iterator<Map.Entry<Object, Object>> it2 = map2.entrySet().iterator();
              while (it2.hasNext()) {
                Map.Entry<Object, Object> entry2 = it2.next();
                System.out.println(entry2.getKey() + " => " + entry2.getValue() + " instance of " + entry2.getValue().getClass().getSimpleName() + System.lineSeparator());
              }
            } else {
              System.out.println(entry.getKey() + " => " + entry.getValue() + " instance of " + entry.getValue().getClass().getSimpleName() + System.lineSeparator());
            }
          }
        } else {
          System.out.println(node + " instance of " + node.getClass().getSimpleName() + System.lineSeparator());
        }
      }
      /*for(Entry<Object, Object> entry : it.n) {
        System.out.println(node + " instance of " + node.getClass().getSimpleName());
      }*/
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private Optional<File> testFileArgument(Object file) {
    if(!(file instanceof File) && !(file instanceof String)) {
      return Optional.absent();
    }

    if(file instanceof File) {
      return Optional.of((File)file);
    }

    //Must be a String at this point.
    try {
      //For testing purposes only.
      URL url = getClass().getClassLoader().getResource((String)file);
      if(url == null) return Optional.absent();
      return Optional.of(new File(url.toURI()));
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return Optional.absent();
  }
}