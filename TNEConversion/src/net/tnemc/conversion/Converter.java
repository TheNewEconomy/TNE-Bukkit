package net.tnemc.conversion;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DataProvider;
import com.github.tnerevival.core.db.sql.SQLite;
import net.tnemc.core.TNE;
import org.javalite.activejdbc.DB;

import javax.sql.DataSource;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
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

  protected DB db;
  protected DataManager manager;


  public void initialize(DataManager manager) {
    db = new DB("Conversion");

    this.manager = manager;

    DataProvider provider = TNE.saveManager().getDataManager().getProviders().get(manager.getFormat());

    try {
      if(manager.getFormat().equalsIgnoreCase("sqlite")) {
        final SQLite sqlite = new SQLite(manager);
        db.open(sqlite.getDriver(), sqlite.getURL(manager.getFile(), manager.getHost(), manager.getPort(), manager.getDatabase()), null);
      } else {
        db.open(provider.connector().getDriver(), provider.connector().getURL(manager.getFile(), manager.getHost(), manager.getPort(), manager.getDatabase()) + ((manager.getFormat().equalsIgnoreCase("mysql")) ? "?useSSL=false" : ""), manager.getUser(), manager.getPassword());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public Map<String, Object> hikariProperties(String format) {
    Map<String, Object> properties = new HashMap<>();

    if(format.equalsIgnoreCase("mysql")) {
      properties.put("autoReconnect", true);
      properties.put("cachePrepStmts", true);
      properties.put("prepStmtCacheSize", 250);
      properties.put("prepStmtCacheSqlLimit", 2048);
      properties.put("rewriteBatchedStatements", true);
      properties.put("useServerPrepStmts", true);
      properties.put("cacheResultSetMetadata", true);
      properties.put("cacheServerConfiguration", true);
      properties.put("useSSL", false);
    }

    return properties;
  }

  public void open() {
    if(db.hasConnection()) return;
    DataProvider provider = TNE.saveManager().getDataManager().getProviders().get(manager.getFormat());

    try {
      db.open(provider.connector().getDriver(), provider.connector().getURL(manager.getFile(), manager.getHost(), manager.getPort(), manager.getDatabase()), manager.getUser(), manager.getPassword());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    if(!db.hasConnection()) return;
    db.close();
  }

  public void open(DataSource datasource) {
    if(db.hasConnection()) return;
    db.open(datasource);
  }

  public ResultSet executeQuery(Statement statement, String query) {
    try(ResultSet results = db.getConnection().createStatement().executeQuery(query)) {
      return results;
    } catch(SQLException ignore) {}

    return null;
  }

  public ResultSet executePreparedQuery(PreparedStatement statement, Object[] variables) {
    try {
      for(int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      return statement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void executeUpdate(String query) {
    open();
    try {
      db.getConnection().createStatement().executeUpdate(query);
    } catch (SQLException ignore) {}
    close();
  }

  public void executePreparedUpdate(String query, Object[] variables) {
    open();
    try(PreparedStatement statement = db.getConnection().prepareStatement(query)) {

      for(int i = 0; i < variables.length; i++) {
        statement.setObject((i + 1), variables[i]);
      }
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    close();
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
          System.out.println("MySQL Conversion for : " + name());
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