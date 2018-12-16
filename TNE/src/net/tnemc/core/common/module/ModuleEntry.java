package net.tnemc.core.common.module;

import net.tnemc.core.TNE;

import java.util.Arrays;
import java.util.List;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */
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