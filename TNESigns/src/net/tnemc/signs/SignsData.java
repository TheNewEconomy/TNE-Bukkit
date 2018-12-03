package net.tnemc.signs;

import com.github.tnerevival.core.db.SQLDatabase;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 6/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SignsData {
  //sign_location <- unique
  //sign_attached
  //sign_owner
  //sign_type

  public static final String prefix = TNE.saveManager().getTNEManager().getPrefix();

  public static final String SIGNS_TABLE = "CREATE TABLE IF NOT EXISTS " + prefix + "_SIGNS (" +
      "`sign_location` VARCHAR(420) NOT NULL UNIQUE," +
      "`sign_attached` VARCHAR(420)," +
      "`sign_chest` VARCHAR(420) NOT NULL DEFAULT ''," +
      "`sign_owner` VARCHAR(36) NOT NULL," +
      "`sign_type` VARCHAR(100) NOT NULL," +
      "`sign_creator` VARCHAR(36) NOT NULL," +
      "`sign_created` BIGINT(60)," +
      "`sign_step` INTEGER," +
      "`sign_data` TEXT NOT NULL" +
      ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";

  public static final String SIGNS_TABLE_H2 = "CREATE TABLE IF NOT EXISTS " + prefix + "_SIGNS (" +
      "`sign_location` VARCHAR(420) NOT NULL UNIQUE," +
      "`sign_attached` VARCHAR(420)," +
      "`sign_chest` VARCHAR(420) NOT NULL DEFAULT ''," +
      "`sign_owner` VARCHAR(36) NOT NULL," +
      "`sign_type` VARCHAR(100) NOT NULL," +
      "`sign_creator` VARCHAR(36) NOT NULL," +
      "`sign_created` BIGINT(60)," +
      "`sign_step` INTEGER," +
      "`sign_data` TEXT NOT NULL" +
      ") ENGINE = INNODB;";

  private static final String SIGNS_UPDATE_STEP = "UPDATE " + prefix + "_SIGNS SET sign_step = ? WHERE sign_location = ?";
  private static final String SIGNS_UPDATE_CHEST = "UPDATE " + prefix + "_SIGNS SET sign_chest = ? WHERE sign_location = ?";
  private static final String SIGNS_LOAD_CHEST = "SELECT sign_chest FROM " + prefix + "_SIGNS WHERE sign_location = ?";
  public static final String SIGNS_CHEST_CHECK = "SELECT sign_owner FROM " + SignsData.prefix + "_SIGNS WHERE sign_chest = ?";
  private static final String SIGNS_SAVE = "INSERT INTO " + prefix + "_SIGNS (sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_step, sign_data) VALUES(?, ?, ?, ?, ?, ?, ?, ?) " +
                                           "ON DUPLICATE KEY UPDATE sign_attached = ?, sign_owner = ?, sign_type = ?, sign_creator = ?, sign_created = ?, sign_step = ?, sign_data = ?;";
  private static final String SIGNS_LOAD_OWNER = "SELECT sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_step, sign_data FROM " + prefix + "_SIGNS WHERE sign_owner = ? AND sign_type = ?";
  private static final String SIGNS_LOAD_CREATOR = "SELECT sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_step, sign_data FROM " + prefix + "_SIGNS WHERE sign_creator = ? AND sign_type = ?";
  private static final String SIGNS_LOAD_LOCATION = "SELECT sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_step, sign_data FROM " + prefix + "_SIGNS WHERE sign_location = ?";
  private static final String SIGNS_LOAD_ATTACHED = "SELECT sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_step, sign_data FROM " + prefix + "_SIGNS WHERE sign_attached = ?";
  private static final String SIGNS_DELETE = "DELETE FROM " + prefix + "_SIGNS WHERE sign_location = ?";

  public static final String ITEM_CHECK = "SELECT item_amount FROM " + SignsData.prefix + "_SIGNS_ITEMS WHERE sign_location = ?";
  public static final String ITEM_OFFER_LOAD = "SELECT item_offer, item_amount FROM " + SignsData.prefix + "_SIGNS_ITEMS WHERE sign_location = ?";
  public static final String ITEM_TRADE_LOAD = "SELECT item_trade FROM " + SignsData.prefix + "_SIGNS_ITEMS WHERE sign_location = ?";
  public static final String ITEM_CURRENCY_LOAD = "SELECT item_cost FROM " + SignsData.prefix + "_SIGNS_ITEMS WHERE sign_location = ?";
  public static final String ITEM_CURRENCY_CHECK = "SELECT item_currency FROM " + SignsData.prefix + "_SIGNS_ITEMS WHERE sign_location = ?";
  public static final String ITEM_SELLING_CHECK = "SELECT item_selling FROM " + SignsData.prefix + "_SIGNS_ITEMS WHERE sign_location = ?";
  public static final String ITEM_OFFER_UPDATE = "UPDATE " + SignsData.prefix + "_SIGNS_ITEMS SET item_offer = ?, item_amount = ?, item_selling = ? WHERE sign_location = ?";
  public static final String ITEM_OFFER_ADD = "INSERT INTO " + SignsData.prefix + "_SIGNS_ITEMS (item_offer, item_amount, item_selling, sign_location) VALUES(?, ?, ?, ?)";
  public static final String ITEM_TRADE_UPDATE = "UPDATE " + SignsData.prefix + "_SIGNS_ITEMS SET item_currency = ?, item_cost = ?, item_trade = ? WHERE sign_location = ?";

  public static void saveSign(TNESign sign) throws SQLException {
    database().executePreparedUpdate(SIGNS_SAVE, new Object[] {
        new SerializableLocation(sign.getLocation()).toString(),
        new SerializableLocation(sign.getAttached()).toString(),
        sign.getOwner().toString(),
        sign.getType(),
        sign.getCreator().toString(),
        sign.getCreationDate(),
        sign.getStep(),
        sign.saveExtraData(),
        new SerializableLocation(sign.getAttached()).toString(),
        sign.getOwner().toString(),
        sign.getType(),
        sign.getCreator().toString(),
        sign.getCreationDate(),
        sign.getStep(),
        sign.saveExtraData()
    });
  }

  public static void updateStep(final Location location, final int step) throws SQLException {
    database().executePreparedUpdate(SIGNS_UPDATE_STEP, new Object[] {
        step,
        new SerializableLocation(location).toString()
    });
  }

  public static void updateChest(final Location location, final Location chest) throws SQLException {
    database().executePreparedUpdate(SIGNS_UPDATE_CHEST, new Object[] {
        new SerializableLocation(chest).toString(),
        new SerializableLocation(location).toString()
    });
  }

  public static TNESign loadSign(Location location) throws SQLException {
    TNESign sign = null;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(SIGNS_LOAD_LOCATION);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {
      if(results.next()) {
        sign = new TNESign(SerializableLocation.fromString(results.getString("sign_location")).getLocation(),
            SerializableLocation.fromString(results.getString("sign_attached")).getLocation(),
            results.getString("sign_type"),
            UUID.fromString(results.getString("sign_owner")),
            UUID.fromString(results.getString("sign_creator")),
            results.getLong("sign_created"),
            results.getInt("sign_step"));
        sign.loadExtraData(results.getString("sign_data"));
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return sign;
  }

  public static TNESign loadSignAttached(Location location) throws SQLException {
    TNESign sign = null;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(SIGNS_LOAD_ATTACHED);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {
      if(results.next()) {
        sign = new TNESign(SerializableLocation.fromString(results.getString("sign_location")).getLocation(),
            SerializableLocation.fromString(results.getString("sign_attached")).getLocation(),
            results.getString("sign_type"),
            UUID.fromString(results.getString("sign_owner")),
            UUID.fromString(results.getString("sign_creator")),
            results.getLong("sign_created"),
            results.getInt("sign_step"));
        sign.loadExtraData(results.getString("sign_data"));
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return sign;
  }

  public static Collection<TNESign> loadSignsCreator(String creator, String type) throws SQLException {
    List<TNESign> signs = new ArrayList<>();

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(SIGNS_LOAD_CREATOR);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            creator, type
        })) {
      while(results.next()) {
        signs.add(
          new TNESign(SerializableLocation.fromString(results.getString("sign_location")).getLocation(),
              SerializableLocation.fromString(results.getString("sign_attached")).getLocation(),
              results.getString("sign_type"),
              UUID.fromString(results.getString("sign_owner")),
              UUID.fromString(results.getString("sign_creator")),
              results.getLong("sign_created"),
              results.getInt("sign_step"),
              results.getString("sign_data"))
        );
      }
    } catch(Exception e) {
      TNE.debug(e);
    }

    return signs;
  }

  public static Collection<TNESign> loadSigns(String owner, String type) throws SQLException {
    List<TNESign> signs = new ArrayList<>();

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(SIGNS_LOAD_OWNER);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            owner, type
        })) {
      while(results.next()) {
        signs.add(
            new TNESign(SerializableLocation.fromString(results.getString("sign_location")).getLocation(),
                SerializableLocation.fromString(results.getString("sign_attached")).getLocation(),
                results.getString("sign_type"),
                UUID.fromString(results.getString("sign_owner")),
                UUID.fromString(results.getString("sign_creator")),
                results.getLong("sign_created"),
                results.getInt("sign_step"),
                results.getString("sign_data"))
        );
      }
    } catch(Exception e) {
      TNE.debug(e);
    }

    return signs;
  }

  public static ItemStack getItem(Location location) throws SQLException {
    ItemStack item = null;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(ITEM_OFFER_LOAD);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location)
        })) {

      if(results.next()) {
        item = SerializableItemStack.fromString(results.getString("item_offer")).toItemStack();
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return item;
  }

  public static ItemStack getTrade(Location location) throws SQLException {
    ItemStack item = null;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(ITEM_TRADE_LOAD);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location)
        })) {

      if(results.next()) {
        item = SerializableItemStack.fromString(results.getString("item_trade")).toItemStack();
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return item;
  }

  public static Chest chest(Location location) throws SQLException {
    Location chestLocation = null;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(SIGNS_LOAD_CHEST);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        chestLocation = SerializableLocation.fromString(results.getString("sign_chest")).getLocation();
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return (Chest)(chestLocation.getBlock().getState());
  }

  public static void deleteSign(Location location) throws SQLException {
    database().executePreparedUpdate(SIGNS_DELETE, new Object[] { new SerializableLocation(location).toString()});
  }

  public static SQLDatabase database() throws SQLException {
    return ((SQLDatabase)TNE.saveManager().getTNEManager().getTNEProvider().connector());
  }
}