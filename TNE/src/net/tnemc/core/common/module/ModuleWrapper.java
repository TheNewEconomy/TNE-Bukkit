package net.tnemc.core.common.module;

import net.tnemc.core.TNE;

import java.io.IOException;
import java.net.URLClassLoader;

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
public class ModuleWrapper {

  ModuleInfo info;
  Module module;
  URLClassLoader loader;

  public ModuleWrapper(Module module) {
    this.module = module;
  }

  public void unload() {
    try {
      loader.close();
      loader = null;
      System.gc();
    } catch (IOException ignore) {
      TNE.logger().info("Module " + info.name() + " unloaded incorrectly.");
    }
    info = null;
  }

  public String name() {
    if(info == null) return "unknown";
    return info.name();
  }

  public String version() {
    if(info == null) return "unknown";
    return info.version();
  }

  public String author() {
    if(info == null) return "unknown";
    return info.author();
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

  public URLClassLoader getLoader() {
    return loader;
  }

  public void setLoader(URLClassLoader loader) {
    this.loader = loader;
  }
}