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

import java.util.ArrayList;

/**
 * Created by creatorfromhell on 11/2/2016.
 **/
public class EventList<E> extends ArrayList<E> {
  public ListListener<E> listener;
  public ArrayList<E> list;

  public boolean add(E item) {
    boolean added = list.add(item);
    if(added) listener.add(item);
    return added;
  }

  public boolean remove(Object item) {
    listener.preRemove(item);
    boolean removed = list.remove(item);
    if(removed) listener.remove(item);
    return removed;
  }

  public EventListIterator<E> getIterator() {
    return new EventListIterator<>(list.iterator(), listener);
  }

  public void setListener(ListListener<E> listener) {
    this.listener = listener;
  }
}