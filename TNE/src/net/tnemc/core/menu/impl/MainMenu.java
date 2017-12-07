package net.tnemc.core.menu.impl;

import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.icons.main.*;

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