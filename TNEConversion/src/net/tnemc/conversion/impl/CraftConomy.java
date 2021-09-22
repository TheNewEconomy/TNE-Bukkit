package net.tnemc.conversion.impl;

import net.tnemc.conversion.ConversionModule;
import net.tnemc.conversion.Converter;
import net.tnemc.conversion.InvalidDatabaseImport;
import net.tnemc.core.TNE;
import net.tnemc.core.common.data.TNEDataManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 11/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CraftConomy extends Converter {
  private File configFile = new File(TNE.instance().getDataFolder(), "../Craftconomy3/config.yml");
  private FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

  private final String prefix = config.getString("System.Database.Prefix");

  private final String accTable = prefix + "account";
  private final String balTable = prefix + "balance";

  @Override
  public String name() {
    return "CraftConomy";
  }

  @Override
  public String type() {
    return config.getString("System.Database.Type");
  }

  @Override
  public File dataFolder() {
    return new File(TNE.instance().getDataFolder(), "../Craftconomy3/config.yml");
  }

  @Override
  public void mysql() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("System.Database.Address"),
        config.getInt("System.Database.Port"), config.getString("System.Database.Db"),
        config.getString("System.Database.Username"), config.getString("System.Database.Password"),
        accTable, "database.h2.db",
        false, false, 60, false));

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT " + accTable + ".name, " + balTable + ".balance, " +
            balTable + ".worldName, " + balTable + ".currency_id FROM " + balTable + " LEFT JOIN " + accTable + " ON " +
            accTable + ".id = " + balTable + ".username_id;")) {

      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("name"),
            results.getString("worldName"), results.getString("currency_id"),
            new BigDecimal(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }

  @Override
  public void h2() throws InvalidDatabaseImport {
    initialize(new TNEDataManager(type(), config.getString("System.Database.Address"),
        config.getInt("System.Database.Port"), config.getString("System.Database.Db"),
        "sa", "",
        accTable, new File(TNE.instance().getDataFolder(), "../Craftconomy3/database.h2").getAbsolutePath(),
        false, false, 60, false));
    System.out.println("File: " + this.manager.getFile());
    System.out.println("Username: " + this.manager.getUser());
    System.out.println("Password: " + this.manager.getPassword());

    open();
    try(Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT " + accTable + ".name, " + balTable + ".balance, " +
            balTable + ".worldName, " + balTable + ".currency_id FROM " + balTable + " LEFT JOIN " + accTable + " ON " +
            accTable + ".id = " + balTable + ".username_id;")) {

      while(results.next()) {
        ConversionModule.convertedAdd(results.getString("name"),
            results.getString("worldName"), results.getString("currency_id"),
            new BigDecimal(results.getDouble("balance")));
      }
    } catch(SQLException ignore) {}
    close();
  }
}
