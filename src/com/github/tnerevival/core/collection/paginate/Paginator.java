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

import java.util.List;
import java.util.TreeMap;

/**
 * Created by creatorfromhell on 1/8/2017.
 **/
public class Paginator {

  TreeMap<Integer, Page> pages = new TreeMap<>();

  List<Object> collection;
  int perPage;

  public Paginator(List<Object> collection, int perPage) {
    this.perPage = perPage;
    this.collection = collection;
    populate();
  }

  public void populate() {
    Object[] values = collection.toArray();
    Page p = new Page(1);
    for(int i = 0; i < collection.size(); i++) {
      if(i % perPage == 0 && i != 0) {
        pages.put(p.getPage(), p);
        Integer highest = pages.lastKey();
        p = new Page(highest++);
      }
      p.addElement(values[i]);
    }
    pages.put(p.getPage(), p);
  }

  public Page getPage(int page) {
    return pages.get(page);
  }

  public Integer getMaxPages() {
    return pages.lastKey();
  }
}