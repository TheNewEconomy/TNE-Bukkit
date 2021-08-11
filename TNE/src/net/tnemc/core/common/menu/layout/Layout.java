package net.tnemc.core.common.menu.layout;

import net.tnemc.core.common.menu.consumable.menu.layout.LayoutBuild;
import net.tnemc.core.common.menu.consumable.menu.layout.LayoutClick;
import net.tnemc.core.common.menu.icon.IconType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/3/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class Layout {

  Map<Integer, IconType> icons = new HashMap<>();

  private String identifier;

  private String title = "";
  private boolean paginate = false;

  //Consumers
  Consumer<LayoutBuild> onBuild;
  Consumer<LayoutClick> onClick;

  public Layout(String identifier) {
    this.identifier = identifier;
  }

  public Layout title(String title) {
    this.title = title;
    return this;
  }

  public Layout paginate() {
    this.paginate = true;
    return this;
  }

  public Layout withIcons(Map<Integer, IconType> icons) {
    this.icons = icons;
    return this;
  }

  public Layout withIcon(Integer slot, IconType icon) {
    icons.put(slot, icon);
    return this;
  }

  public Layout onBuild(Consumer<LayoutBuild> onBuild) {
    this.onBuild = onBuild;
    return this;
  }

  public Layout onClick(Consumer<LayoutClick> onClick) {
    this.onClick = onClick;
    return this;
  }

  public void click(Player player, Integer slot) {

  }
}