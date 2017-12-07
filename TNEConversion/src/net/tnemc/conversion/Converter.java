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
package net.tnemc.conversion;

import com.github.tnerevival.core.db.DatabaseConnector;
import com.github.tnerevival.core.db.FlatFile;
import com.github.tnerevival.core.db.sql.H2;
import com.github.tnerevival.core.db.sql.MySQL;
import com.github.tnerevival.core.db.sql.SQLite;
import net.tnemc.core.TNE;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by creatorfromhell on 11/8/2016.
 **/
public abstract class Converter {
  protected String mysqlHost = TNE.configurations().getString("Core.Conversion.Options.Host");
  protected Integer mysqlPort = TNE.configurations().getInt("Core.Conversion.Options.Port");
  protected String mysqlDatabase = TNE.configurations().getString("Core.Conversion.Options.Database");
  protected String mysqlUser = TNE.configurations().getString("Core.Conversion.Options.User");
  protected String mysqlPassword = TNE.configurations().getString("Core.Conversion.Options.Password");

  protected String type = TNE.configurations().getString("Core.Conversion.Format");
  protected DatabaseConnector db;

  public MySQL mysqlDB() {
    return (MySQL)db;
  }

  public SQLite sqliteDB() {
    return (SQLite)db;
  }

  public H2 h2DB() {
    return (H2)db;
  }

  public FlatFile flatfileDB() {
    return (FlatFile)db;
  }

  public abstract String name();

  public void convert() {
    try {
      new File(TNE.instance().getDataFolder(), "conversion.yml").createNewFile();
    } catch(Exception e) {
      TNE.debug(e);
    }
    try {
      switch (type.toLowerCase()) {
        case "mysql":
          mysql();
          break;
        case "sqlite":
          sqlite();
          break;
        case "h2":
          h2();
          break;
        case "postgre":
          postgre();
          break;
        case "flatfile":
          flatfile();
          break;
        case "mini":
          flatfile();
          break;
        case "yaml":
          yaml();
          break;
        case "inventory":
          inventoryDB();
          break;
        case "experience":
          expDB();
          break;
      }
    } catch(InvalidDatabaseImport exception) {
      TNE.instance().getLogger().log(Level.WARNING, exception.getMessage());
    }
  }

  public void mysql() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("MySQL", name());
  }

  public void sqlite() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("SQLite", name());
  }

  public void h2() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("H2", name());
  }

  public void postgre() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("PostgreSQL", name());
  }

  public void flatfile() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("FlatFile", name());
  }

  public void yaml() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("YAML", name());
  }

  //iConomy Specific
  public void inventoryDB() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("InventoryDB", name());
  }

  //iConomy Specific.
  public void expDB() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("ExperienceDB", name());
  }
}