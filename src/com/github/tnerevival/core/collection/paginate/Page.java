/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.core.collection.paginate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by creatorfromhell on 1/8/2017.
 **/
public class Page {
  private int page;
  List<Object> elements = new ArrayList<>();

  public Page(int page) {
    this.page = page;
  }

  public void addElement(Object o) {
    elements.add(o);
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public List<Object> getElements() {
    return elements;
  }
}