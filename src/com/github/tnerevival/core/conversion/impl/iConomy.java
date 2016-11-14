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
package com.github.tnerevival.core.conversion.impl;

import com.github.tnerevival.core.conversion.Converter;
import com.github.tnerevival.core.exception.InvalidDatabaseImport;

/**
 * Created by creatorfromhell on 11/13/2016.
 **/
public class iConomy extends Converter {
  @Override
  public String name() {
    return "iConomy";
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    super.mysql();
  }

  @Override
  public void sqlite() throws InvalidDatabaseImport {
    super.sqlite();
  }

  @Override
  public void postgre() throws InvalidDatabaseImport {
    super.postgre();
  }

  @Override
  public void flatfile() throws InvalidDatabaseImport {
    super.flatfile();
  }

  @Override
  public void inventoryDB() throws InvalidDatabaseImport {
    super.inventoryDB();
  }

  @Override
  public void expDB() throws InvalidDatabaseImport {
    super.expDB();
  }
}