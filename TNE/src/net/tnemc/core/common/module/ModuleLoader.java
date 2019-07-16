package net.tnemc.core.common.module;

import net.tnemc.core.TNE;
import net.tnemc.core.common.module.injectors.InjectMethod;
import net.tnemc.core.common.module.injectors.ModuleInjector;
import net.tnemc.core.common.module.injectors.ModuleInjectorHandler;
import net.tnemc.core.common.module.injectors.ModuleInjectorWrapper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 07/01/2017.
 */

/**
 * This class loads all modules from the modules directory, and loads the main class based on @Module.
 **/
public class ModuleLoader {

  public static Map<String, String> modulePaths = new HashMap<>();

  static {
    modulePaths.put("conversion", "https://github.com/TheNewEconomy/TNE-Bukkit/releases/download/Conversion/Conversion.jar");
    modulePaths.put("h2", "https://github.com/TheNewEconomy/TNE-Bukkit/releases/download/H2/H2.jar");
    modulePaths.put("mobs", "https://github.com/TheNewEconomy/TNE-Bukkit/releases/download/Mobs/Mobs.jar");
    modulePaths.put("mysql", "https://github.com/TheNewEconomy/TNE-Bukkit/releases/download/mysql/MySQL.jar");
  }

  private File modulesYAML;
  private FileConfiguration moduleConfigurations;
  private Map<String, ModuleEntry> modules = new HashMap<>();

  private Map<String, ModuleInjectorHandler> injectors = new HashMap<>();

  public ModuleLoader() {
    modulesYAML = new File("plugins/TheNewEconomy/modules.yml");
    if(!modulesYAML.exists()) {
      try {
        modulesYAML.createNewFile();
      } catch (IOException e) {
        TNE.logger().info("Error occured while try to create modules.yml.");
        TNE.debug(e);
      }
    }
    moduleConfigurations = YamlConfiguration.loadConfiguration(modulesYAML);
  }

  /**
   * Loads all modules into a map for later usage.
   * @return The map containing every module in format Name, ModuleInstance
   */
  public void load() {
    File directory = new File("plugins/TheNewEconomy/modules");
    File[] jars = directory.listFiles((dir, name) -> name.endsWith(".jar"));

    if(jars != null) {
      for (File jar : jars) {
        Module module = getModuleClass(jar.getAbsolutePath());
        if(module == null || module.getClass() == null) continue;
        if(jar.getName().contains("outdated-")) continue;
        if (!module.getClass().isAnnotationPresent(ModuleInfo.class)) {
          TNE.instance().getLogger().info("Invalid module format! Module File: " + jar.getName());
          continue;
        }
        ModuleInfo info = module.getClass().getAnnotation(ModuleInfo.class);

        if(info.name().equalsIgnoreCase("h2") || info.name().equalsIgnoreCase("mysql")) {
          continue;
        }

        ModuleEntry entry = new ModuleEntry(info, module);
        TNE.instance().getLogger().info("Found module: " + info.name() + " version: " + info.version());
        modules.put(entry.getInfo().name(), entry);
      }
    }
  }

  public boolean hasModule(String moduleName) {
    return modules.containsKey(moduleName);
  }

  public boolean hasModuleWithoutCase(String moduleName) {
    for (String key : modules.keySet()) {
      if(key.equalsIgnoreCase(moduleName)) return true;
    }
    return false;
  }

  public ModuleEntry getModule(String moduleName) {
    return modules.get(moduleName);
  }

  public String findPath(String moduleName) {
    File directory = new File("plugins/TheNewEconomy/modules");
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

  public void unload(String moduleName) {
    if(hasModule(moduleName)) {
      ModuleEntry entry = getModule(moduleName);
      entry.getModule().getListeners(TNE.instance()).forEach(ModuleListener::unregister);
      entry.getModule().unload(TNE.instance());
      entry.unload();


      moduleConfigurations.set("Modules.DONTMODIFY." + entry.getInfo().name(), entry.getInfo().version());
      try {
        moduleConfigurations.save(modulesYAML);
      } catch (IOException e) {
        TNE.logger().info("Error occured while saving modules.yml.");
        TNE.debug(e);
      }

      modules.remove(moduleName);
    }
  }

  public boolean load(String moduleName) {
    String path = findPath(moduleName);
    if(path != null) {
      Module module = getModuleClass(path);
      if (!module.getClass().isAnnotationPresent(ModuleInfo.class)) {
        TNE.instance().getLogger().info("Invalid module format! Module File: " + moduleName);
        return false;
      }
      ModuleInfo info = module.getClass().getAnnotation(ModuleInfo.class);
      ModuleEntry entry = new ModuleEntry(info, module);
      TNE.instance().getLogger().info("Found module: " + info.name() + " version: " + info.version());
      modules.put(entry.getInfo().name(), entry);
      return true;
    }
    return false;
  }

  public void call(InjectMethod method) {
    if(injectors.containsKey(method.getIdentifier())) {
      injectors.get(method.getIdentifier()).call(method);
    }
  }

  private void registerInjectors(Class clazz) {
    for(final Method m : clazz.getMethods()) {
      if(m.isAnnotationPresent(ModuleInjector.class)) {
        ModuleInjector injector = m.getAnnotation(ModuleInjector.class);
        ModuleInjectorWrapper wrapper = new ModuleInjectorWrapper(clazz, m);
        addInjector(injector, wrapper);
      }
    }
  }

  private void addInjector(ModuleInjector injector, ModuleInjectorWrapper wrapper) {
    ModuleInjectorHandler handler = (injectors.containsKey(injector.method()))? injectors.get(injector.method())
                                                                              : new ModuleInjectorHandler();
    handler.addInjector(injector, wrapper);

    injectors.put(injector.method(), handler);
  }

  private Module getModuleClass(String modulePath) {
    Module module = null;

    File file = new File(modulePath);
    String moduleMain = getModuleMain(file);
    Class<? extends Module> moduleClass;
    Class<?> mainClass;

    try {
      mainClass = Class.forName(moduleMain, true, getClass().getClassLoader());
    } catch (ClassNotFoundException e) {
      TNE.debug(e);
    }

    URLClassLoader urlClassLoader = null;
    try {
      urlClassLoader = new URLClassLoader(new URL[] { file.toURI().toURL()}, Module.class.getClassLoader());
      mainClass = urlClassLoader.loadClass(moduleMain);
      moduleClass = mainClass.asSubclass(Module.class);
      module = moduleClass.newInstance();
      module.moduleInjectors().forEach(this::registerInjectors);
    } catch (MalformedURLException | IllegalAccessException | InstantiationException e) {
      TNE.debug(e);
    } catch (ClassNotFoundException e) {
      TNE.logger().info("Unable to locate module main class for file " + file.getName());
    }
    return module;
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

  public Map<String, ModuleEntry> getModules() {
    return modules;
  }

  public String getLastVersion(String name) {
    return moduleConfigurations.getString("Modules.DONTMODIFY." + name, modules.get(name).getInfo().version());
  }

  public static boolean downloadModule(String module) {
    if(modulePaths.containsKey(module)) {
      try {
        final String fileURL = modulePaths.get(module);
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
}