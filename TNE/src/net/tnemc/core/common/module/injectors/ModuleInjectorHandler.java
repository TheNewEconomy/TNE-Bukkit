package net.tnemc.core.common.module.injectors;

import net.tnemc.core.TNE;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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