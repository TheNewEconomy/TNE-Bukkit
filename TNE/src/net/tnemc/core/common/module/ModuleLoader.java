package net.tnemc.core.common.module;

import net.tnemc.core.TNE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
public class ModuleLoader {

  Map<String, ModuleWrapper> modules = new HashMap<>();

  private List<String> supportedEvents = new ArrayList<>();

  public boolean hasModule(String moduleName) {
    return modules.containsKey(moduleName);
  }

  public boolean hasModuleWithoutCase(String moduleName) {
    for (String key : modules.keySet()) {
      if(key.equalsIgnoreCase(moduleName)) return true;
    }
    return false;
  }

  public ModuleWrapper getModule(String name) {
    return modules.get(name);
  }

  public Map<String, ModuleWrapper> getModules() {
    return modules;
  }

  public void load() {
    File directory = new File(TNE.instance().getDataFolder(), "modules");

    if(directory.exists()) {

      File[] jars = directory.listFiles((dir, name) -> name.endsWith(".jar"));

      if (jars != null) {
        for (File jar : jars) {

          try {
            ModuleWrapper wrapper = loadModuleWrapper(jar.getAbsolutePath());

            if (wrapper.getModule() == null || wrapper.getModule().getClass() == null) {
              TNE.logger().info("Skipping file due to invalid module: " + jar.getName());
              continue;
            }

            if (jar.getName().contains("old-")) continue;

            if (!wrapper.getModule().getClass().isAnnotationPresent(net.tnemc.core.common.module.ModuleInfo.class)) {
              TNE.instance().getLogger().info("Invalid module format! Module File: " + jar.getName());
              continue;
            }

            wrapper.setInfo(wrapper.getModule().getClass().getAnnotation(ModuleInfo.class));
            TNE.instance().getLogger().info("Found module: " + wrapper.name() + " version: " + wrapper.version());
            modules.put(wrapper.name(), wrapper);

            if (!wrapper.getInfo().updateURL().trim().equalsIgnoreCase("")) {
              TNE.logger().info("Checking for updates for module " + wrapper.info.name());
              ModuleUpdateChecker checker = new ModuleUpdateChecker(wrapper.info.name(), wrapper.info.updateURL(), wrapper.version());
              checker.check();
            }
          } catch(Exception ignore) {
            TNE.logger().info("Unable to load module file: " + jar.getName() + ". Are you sure it's up to date?");
          }
        }
      }
    } else {
      directory.mkdir();
    }
  }

  public boolean load(String moduleName) {
    final String path = findPath(moduleName);
    if(path != null) {
      try {
        ModuleWrapper wrapper = loadModuleWrapper(path);
        if (!wrapper.getModule().getClass().isAnnotationPresent(ModuleInfo.class)) {
          TNE.instance().getLogger().info("Invalid module format! Module File: " + moduleName);
          return false;
        }

        wrapper.setInfo(wrapper.getModule().getClass().getAnnotation(ModuleInfo.class));
        TNE.instance().getLogger().info("Found module: " + wrapper.name() + " version: " + wrapper.version());
        modules.put(wrapper.name(), wrapper);

        if(!wrapper.getInfo().updateURL().trim().equalsIgnoreCase("")) {
          TNE.logger().info("Checking for updates for module " + moduleName);
          ModuleUpdateChecker checker = new ModuleUpdateChecker(moduleName, wrapper.info.updateURL(), wrapper.version());
          checker.check();
        }
        return true;
      } catch(Exception ignore) {
        TNE.logger().info("Unable to load module: " + moduleName + ". Are you sure it exists?");
      }
    }
    return false;
  }

  public void unload(String moduleName) {
    if(hasModule(moduleName)) {
      ModuleWrapper wrapper = getModule(moduleName);
      wrapper.getModule().listeners(TNE.instance()).forEach(ModuleListener::unregister);
      //wrapper.getModule().commands().forEach(TNECommand::unregister);
      wrapper.getModule().configurations().values().forEach(config->TNE.configurations().configurations.remove(config));
      wrapper.getModule().unload(TNE.instance());

      try {
        Field f = ClassLoader.class.getDeclaredField("classes");
        f.setAccessible(true);

        Vector<Class> classes =  (Vector<Class>) f.get(TNE.loader().getModule(moduleName).getLoader());
        for(Class clazz : classes) {
          TNE.debug("Loaded: " + clazz.getName());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      wrapper.unload();
      wrapper.setModule(null);
      wrapper.setInfo(null);
      wrapper.setLoader(null);
      wrapper = null;
      System.gc();

      modules.remove(moduleName);
    }
  }

  protected String findPath(String moduleName) {
    File directory = new File(TNE.instance().getDataFolder(), "modules");
    File[] jars = directory.listFiles((dir, name) -> name.endsWith(".jar"));

    if(jars != null) {
      for (File jar : jars) {
        if(jar.getAbsolutePath().toLowerCase().contains(moduleName.toLowerCase() + ".jar")) {
          return jar.getAbsolutePath();
        }
      }
    }
    return null;
  }

  private ModuleWrapper loadModuleWrapper(String modulePath) {
    ModuleWrapper wrapper;

    Module module = null;

    final File file = new File(modulePath);
    Class<? extends Module> moduleClass;

    URLClassLoader classLoader = null;
    try {
      classLoader = new URLClassLoader(new URL[]{ file.toURI().toURL() }, TNE.instance().getClass().getClassLoader());
      moduleClass = classLoader.loadClass(getModuleMain(new File(modulePath))).asSubclass(Module.class);
      module = moduleClass.newInstance();
      supportedEvents.addAll(module.events());
    } catch (Exception ignore) {
      TNE.logger().info("Unable to locate module main class for file " + file.getName());
    }
    wrapper = new ModuleWrapper(module);
    wrapper.setLoader(classLoader);
    return wrapper;
  }

  public boolean downloadModule(String module) {
    if(modules.containsKey(module)) {
      try {
        final String fileURL = modules.get(module).info.updateURL();
        final URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());

          InputStream in = httpConn.getInputStream();
          File file = new File(TNE.instance().getDataFolder() + File.separator + "modules", fileName);

          if(file.exists()) {
            if(!file.renameTo(new File(TNE.instance().getDataFolder() + File.separator + "modules", "outdated-" + fileName))) {
              return false;
            }
          }

          FileOutputStream out = new FileOutputStream(file);

          int bytesRead = -1;
          byte[] buffer = new byte[4096];
          while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
          }

          out.close();
          in.close();
        }
      } catch (Exception e) {
        return false;
      }
      return true;
    }
    return false;
  }

  private String getModuleMain(File jarFile) {
    String main = "";
    JarFile jar = null;
    InputStream in = null;
    BufferedReader reader = null;

    try {
      jar = new JarFile(jarFile);
      JarEntry infoFile = jar.getJarEntry("module.tne");

      if(infoFile == null) {
        TNE.instance().getLogger().info("TNE encountered an error while loading a module! No module.tne file!");
        return "";
      }

      in = jar.getInputStream(infoFile);
      reader = new BufferedReader(new InputStreamReader(in));

      main = reader.readLine().split("=")[1].trim();
    } catch (IOException e) {
      TNE.debug(e);
    } finally {
      if(jar != null) {
        try {
          jar.close();
        } catch(IOException e) {
          TNE.debug(e);
        }
      }

      if(in != null) {
        try {
          in.close();
        } catch(IOException e) {
          TNE.debug(e);
        }
      }

      if(reader != null) {
        try {
          reader.close();
        } catch(IOException e) {
          TNE.debug(e);
        }
      }
    }
    return main;
  }

  public boolean hasModuleEvent(String name) {
    return supportedEvents.contains(name);
  }
}