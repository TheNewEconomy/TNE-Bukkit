package net.tnemc.core.common.newmodule;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/25/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ModuleInformation {

  ModuleInfo info;
  Module module;
  ClassLoader loader;

  public ModuleInformation(ModuleInfo info, Module module) {
    this.info = info;
    this.module = module;
  }

  public void unload() {
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

  public ClassLoader getLoader() {
    return loader;
  }

  public void setLoader(ClassLoader loader) {
    this.loader = loader;
  }
}