package net.tnemc.core.common.module.injectors;

import net.tnemc.core.TNE;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/12/2017.
 */
public class ModuleInjectorHandler {

  private TreeMap<Integer, List<ModuleInjectorWrapper>> injectors = new TreeMap<>(Collections.reverseOrder());

  public void addInjector(ModuleInjector injector, ModuleInjectorWrapper wrapper) {
    int priority = (injector.priority() > 10 || injector.priority() < 0)? 5 : injector.priority();
    List<ModuleInjectorWrapper> wrappers = (injectors.containsKey(priority))? injectors.get(priority) : new ArrayList<>();
    wrappers.add(wrapper);
    injectors.put(priority, wrappers);
  }

  public void call(InjectMethod method) {
    for(List<ModuleInjectorWrapper> tiers : injectors.values()) {
      for(ModuleInjectorWrapper wrapper : tiers) {
        try {
          wrapper.getInjector().invoke(wrapper.getHolder(), method);
        } catch (IllegalAccessException e) {
          TNE.debug(e);
        } catch (InvocationTargetException e) {
          TNE.debug(e);
        }
      }
    }
  }
}