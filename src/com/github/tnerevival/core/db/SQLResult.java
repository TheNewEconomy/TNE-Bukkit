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
package com.github.tnerevival.core.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by creatorfromhell on 1/10/2017.
 **/
public class SQLResult {

  private int id;
  private Statement statement;
  private ResultSet result;

  public SQLResult(int id, Statement statement, ResultSet result) {
    this.id = id;
    this.statement = statement;
    this.result = result;
  }

  public void close() {
    try {
      result.close();
      statement.close();
    } catch(SQLException e) {
      e.printStackTrace();
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ResultSet getResult() {
    return result;
  }

  public void setResult(ResultSet result) {
    this.result = result;
  }
}
