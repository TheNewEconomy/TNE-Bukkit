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
package com.github.tnerevival.core.conversion;

import com.github.tnerevival.core.exception.InvalidDatabaseImport;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public abstract class Importer {
  public abstract String name();
  public abstract void mysql() throws InvalidDatabaseImport;
  public abstract void sqlite() throws InvalidDatabaseImport;
  public abstract void h2() throws InvalidDatabaseImport;
  public abstract void postgre() throws InvalidDatabaseImport;
  public abstract void flatfile() throws InvalidDatabaseImport;
  public abstract void yaml() throws InvalidDatabaseImport;
  public abstract void inventoryDB() throws InvalidDatabaseImport;//iConomy Specific.
  public abstract void expDB() throws InvalidDatabaseImport;//iConomy Specific.
}