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
package com.github.tnerevival.core.collection;

import java.util.Iterator;

/**
 * Created by creatorfromhell on 11/6/2016.
 **/
public class EventMapIterator<Entry> {

  Iterator<Entry> iterator;
  MapListener listener;
  Entry last;

  public EventMapIterator(Iterator<Entry> iterator, MapListener listener) {
    this.iterator = iterator;
    this.listener = listener;
  }

  public void remove() {
    listener.remove(last);
    iterator.remove();
  }

  public boolean hasNext() {
    return iterator.hasNext();
  }

  public Entry next() {
    last = iterator.next();
    return last;
  }
}