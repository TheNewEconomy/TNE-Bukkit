package net.tnemc.conversion;

import com.github.tnerevival.core.db.DatabaseConnector;
import com.github.tnerevival.core.db.sql.H2;
import com.github.tnerevival.core.db.sql.MySQL;
import com.github.tnerevival.core.db.sql.SQLite;
import net.tnemc.core.TNE;

import java.io.File;
import java.util.logging.Level;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public abstract class Converter {
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

  public abstract String name();

  public abstract String type();

  public void convert() {
    try {
      new File(TNE.instance().getDataFolder(), "extracted.yml").createNewFile();
    } catch(Exception e) {
      TNE.debug(e);
    }
    try {
      switch (type().toLowerCase()) {
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
        case "json":
          json();
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

  //This is the dumbest trend ever.
  public void json() throws InvalidDatabaseImport {
    throw new InvalidDatabaseImport("JSON", name());
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