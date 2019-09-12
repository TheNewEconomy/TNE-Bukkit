package net.tnemc.core.common.module;

import net.tnemc.core.TNE;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/26/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ModuleClassLoader extends URLClassLoader {

  public ModuleClassLoader(URL url) {
    super(new URL[]{url}, TNE.instance().getClass().getClassLoader());
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    try {
      return super.loadClass(name, resolve);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  @Override
  protected void finalize() throws Throwable {
    super.finalize();

    TNE.debug("Module Class Loader has been GC'd");
  }
}