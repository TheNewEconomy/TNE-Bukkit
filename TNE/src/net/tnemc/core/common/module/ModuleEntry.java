package net.tnemc.core.common.module;

import net.tnemc.core.TNE;

import java.util.Arrays;
import java.util.List;

/**
 * Created by creatorfromhell on 7/4/2017.
 * All rights reserved.
 **/
public class ModuleEntry {
  private ModuleInfo info;
  private Module module;

  public ModuleEntry(ModuleInfo info, Module module) {
    this.info = info;
    this.module = module;
  }
  
  public void unload() {
    TNE.debug("=====START ModuleEntry.unload =====");
    module.getCommands().forEach((command)->{
      List<String> accessors = Arrays.asList(command.getAliases());
      accessors.add(command.getName());
      TNE.instance().unregisterCommand((String[])accessors.toArray());
    });
    module.getMainConfigurations().forEach((node, defaultValue)->{
      TNE.instance().main().configurations.remove(node);
    });
    module.getMessages().forEach((message, defaultValue)->{
      TNE.instance().messages().configurations.remove(message);
    });
    module.getConfigurations().forEach((configuration, identifier)->{
      TNE.configurations().configurations.remove(identifier);
    });
    TNE.debug("=====END ModuleEntry.unload =====");
  }

  public ModuleInfo getInfo() {
    return info;
  }

  public void setInfo(ModuleInfo info) {
    this.info = info;
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }
}