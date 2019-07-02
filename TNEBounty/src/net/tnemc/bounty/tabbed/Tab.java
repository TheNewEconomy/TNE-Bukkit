package net.tnemc.bounty.tabbed;

import net.tnemc.core.menu.Menu;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/2/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class Tab extends Menu {

  private int page;

  public Tab(String name, Integer page, String title, Integer rows) {
    super(name + "_tab_" + page, title, rows);
    this.page = page;
  }
}