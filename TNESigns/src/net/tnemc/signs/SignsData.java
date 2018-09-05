package net.tnemc.signs;

import com.github.tnerevival.core.db.SQLDatabase;
import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.Location;

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

  private static final String prefix = TNE.saveManager().getTNEManager().getPrefix();

  public static final String SIGNS_TABLE = "CREATE TABLE IF NOT EXISTS " + prefix + "_SIGNS (" +
      "`sign_location` VARCHAR(420) NOT NULL UNIQUE," +
      "`sign_attached` VARCHAR(420)," +
      "`sign_owner` VARCHAR(36) NOT NULL," +
      "`sign_type` VARCHAR(100) NOT NULL," +
      "`sign_creator` VARCHAR(36) NOT NULL," +
      "`sign_created` BIGINT(60)," +
      "`sign_data` TEXT NOT NULL" +
      ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";

  public static final String SIGNS_TABLE_H2 = "CREATE TABLE IF NOT EXISTS " + prefix + "_SIGNS (" +
      "`sign_location` VARCHAR(420) NOT NULL UNIQUE," +
      "`sign_chest` VARCHAR(420)," +
      "`sign_offer` TEXT NOT NULL," +
      "`sign_type` VARCHAR(100) NOT NULL," +
      "`sign_creator` VARCHAR(36) NOT NULL," +
      "`sign_created` BIGINT(60)," +
      "`sign_data` TEXT NOT NULL" +
      ") ENGINE = INNODB;";

  private static final String SIGNS_SAVE = "INSERT INTO " + prefix + "_SIGNS (sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_data) VALUES(?, ?, ?, ?, ?, ?, ?) " +
                                           "ON DUPLICATE KEY UPDATE sign_attached = ?, sign_owner = ?, sign_type = ?, sign_creator = ?, sign_created = ?, sign_data = ?;";
  private static final String SIGNS_LOAD_OWNER = "SELECT sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_data FROM " + prefix + "_SIGNS WHERE sign_owner = ? AND sign_type = ?";
  private static final String SIGNS_LOAD_CREATOR = "SELECT sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_data FROM " + prefix + "_SIGNS WHERE sign_creator = ? AND sign_type = ?";
  private static final String SIGNS_LOAD_LOCATION = "SELECT sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_data FROM " + prefix + "_SIGNS WHERE sign_location = ?";
  private static final String SIGNS_LOAD_ATTACHED = "SELECT sign_location, sign_attached, sign_owner, sign_type, sign_creator, sign_created, sign_data FROM " + prefix + "_SIGNS WHERE sign_attached = ?";
  private static final String SIGNS_DELETE = "DELETE FROM " + prefix + "_SIGNS WHERE sign_location = ?";

  public static void saveSign(TNESign sign) {
    database().executePreparedUpdate(SIGNS_SAVE, new Object[] {
        new SerializableLocation(sign.getLocation()).toString(),
        new SerializableLocation(sign.getAttached()).toString(),
        sign.getOwner().toString(),
        sign.getType(),
        sign.getCreator().toString(),
        sign.getCreationDate(),
        sign.saveExtraData(),
        new SerializableLocation(sign.getAttached()).toString(),
        sign.getOwner().toString(),
        sign.getType(),
        sign.getCreator().toString(),
        sign.getCreationDate(),
        sign.saveExtraData()
    });
  }

  public static TNESign loadSign(Location location) {
    TNESign sign = null;
    int result = -1;

    try {
      result = database().executePreparedQuery(SIGNS_LOAD_LOCATION, new Object[] { new SerializableLocation(location).toString() });
      if(database().results(result).next()) {
        sign = new TNESign(SerializableLocation.fromString(database().results(result).getString("sign_location")).getLocation(),
            SerializableLocation.fromString(database().results(result).getString("sign_attached")).getLocation(),
            database().results(result).getString("sign_type"),
            UUID.fromString(database().results(result).getString("sign_owner")),
            UUID.fromString(database().results(result).getString("sign_creator")),
            database().results(result).getLong("sign_created"));
        sign.loadExtraData(database().results(result).getString("sign_data"));
      }
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      if(result != -1) {
        database().closeResult(result);
      }
    }
    return sign;
  }

  public static TNESign loadSignAttached(Location location) {
    TNESign sign = null;
    int result = -1;

    try {
      result = database().executePreparedQuery(SIGNS_LOAD_ATTACHED, new Object[] { new SerializableLocation(location).toString() });
      if(database().results(result).next()) {
        sign = new TNESign(SerializableLocation.fromString(database().results(result).getString("sign_location")).getLocation(),
            SerializableLocation.fromString(database().results(result).getString("sign_attached")).getLocation(),
            database().results(result).getString("sign_type"),
            UUID.fromString(database().results(result).getString("sign_owner")),
            UUID.fromString(database().results(result).getString("sign_creator")),
            database().results(result).getLong("sign_created"));
        sign.loadExtraData(database().results(result).getString("sign_data"));
      }
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      if(result != -1) {
        database().closeResult(result);
      }
    }
    return sign;
  }

  public static Collection<TNESign> loadSignsCreator(String creator, String type) {
    List<TNESign> signs = new ArrayList<>();
    int result = -1;

    try {
      result = database().executePreparedQuery(SIGNS_LOAD_CREATOR, new Object[] { creator, type });
      while(database().results(result).next()) {
        signs.add(
            new TNESign(SerializableLocation.fromString(database().results(result).getString("sign_location")).getLocation(),
                SerializableLocation.fromString(database().results(result).getString("sign_attached")).getLocation(),
                database().results(result).getString("sign_type"),
                UUID.fromString(database().results(result).getString("sign_owner")),
                UUID.fromString(database().results(result).getString("sign_creator")),
                database().results(result).getLong("sign_created"),
                database().results(result).getString("sign_data"))
        );
      }
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      if(result != -1) {
        database().closeResult(result);
      }
    }

    return signs;
  }

  public static Collection<TNESign> loadSigns(String owner, String type) {
    List<TNESign> signs = new ArrayList<>();
    int result = -1;

    try {
      result = database().executePreparedQuery(SIGNS_LOAD_OWNER, new Object[] { owner, type });
      while(database().results(result).next()) {
        signs.add(
            new TNESign(SerializableLocation.fromString(database().results(result).getString("sign_location")).getLocation(),
                SerializableLocation.fromString(database().results(result).getString("sign_attached")).getLocation(),
                database().results(result).getString("sign_type"),
                UUID.fromString(database().results(result).getString("sign_owner")),
                UUID.fromString(database().results(result).getString("sign_creator")),
                database().results(result).getLong("sign_created"),
                database().results(result).getString("sign_data"))
        );
      }
    } catch(Exception e) {
      TNE.debug(e);
    } finally {
      if(result != -1) {
        database().closeResult(result);
      }
    }

    return signs;
  }

  public static void deleteSign(Location location) {
    database().executePreparedUpdate(SIGNS_DELETE, new Object[] { new SerializableLocation(location).toString()});
  }

  public static SQLDatabase database() {
    SQLDatabase db = ((SQLDatabase)TNE.saveManager().getTNEManager().getTNEProvider().connector());
    if(!db.connected(TNE.saveManager().getTNEManager())) {
      db.connect(TNE.saveManager().getTNEManager());
    }
    return db;
  }
}