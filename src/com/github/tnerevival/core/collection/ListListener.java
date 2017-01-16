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

import java.util.Collection;
import java.util.List;

/**
 * Created by creatorfromhell on 11/6/2016.
 **/
public interface ListListener<E> {
  void update();
  List<E> changed();
  void clearChanged();
  Collection<E> getAll();
  Collection<E> getAll(Object identifier);
  boolean add(E item);
  int size();
  boolean isEmpty();
  boolean contains(Object item);
  void preRemove(Object item);
  boolean remove(Object item);
}
