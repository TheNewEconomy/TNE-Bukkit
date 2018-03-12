package net.tnemc.core.common.module.injectors;

import java.lang.reflect.Method;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/12/2017.
 */
public class ModuleInjectorWrapper {

  private Class holder;
  private Method injector;

  public ModuleInjectorWrapper(Class holder, Method injector) {
    this.holder = holder;
    this.injector = injector;
  }

  public Class getHolder() {
    return holder;
  }

  public Method getInjector() {
    return injector;
  }
}