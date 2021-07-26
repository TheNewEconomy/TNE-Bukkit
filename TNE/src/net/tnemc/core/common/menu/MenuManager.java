package net.tnemc.core.common.menu;

import net.tnemc.core.common.menu.design.icon.IconType;
import net.tnemc.core.common.menu.session.SessionViewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/25/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MenuManager {

  private Map<String, Menu> menus = new HashMap<>();
  private Map<String, IconType> iconTypes = new HashMap<>();
  private ConcurrentHashMap<UUID, SessionViewer> viewers = new ConcurrentHashMap<>();

  private static MenuManager instance;

  public MenuManager() {
    instance = this;
  }

  /**
   * Used to get a menu based on its name.
   * @param name The name of the menu we're looking for.
   * @return An optional containing the menu, if it exists, otherwise an empty
   * optional.
   */
  public Optional<Menu> getMenu(String name) {
    return Optional.of(menus.get(name));
  }

  /**
   * Used to get a {@link IconType} based on its name.
   * @param name The name of the {@link IconType} we're looking for.
   * @return An optional containing the {@link IconType}, if it exists, otherwise an empty
   * optional.
   */
  public Optional<IconType> getType(String name) {
    return Optional.of(iconTypes.get(name));
  }

  public static MenuManager getInstance() {
    return instance;
  }
}