package net.tnemc.core.menu.impl.player;

import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.main.DisplayIcon;
import net.tnemc.core.menu.icons.main.GiveIcon;
import net.tnemc.core.menu.icons.main.PayIcon;
import net.tnemc.core.menu.icons.main.SetIcon;
import net.tnemc.core.menu.icons.main.TakeIcon;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/5/2017.
 */
public class MainMenu extends Menu {
  public MainMenu() {
    super("main", "[TNE]Action", 1);

    icons.put(2, new GiveIcon());
    icons.put(3, new SetIcon());
    icons.put(4, new TakeIcon());
    icons.put(5, new PayIcon());
    icons.put(6, new DisplayIcon());
  }
}