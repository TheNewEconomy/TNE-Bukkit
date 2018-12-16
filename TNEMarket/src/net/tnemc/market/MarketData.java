package net.tnemc.market;

import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.core.TNE;
import net.tnemc.core.item.SerialItem;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/3/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MarketData {

  public static final String prefix = TNE.saveManager().getTNEManager().getPrefix();

  public static final String OFFERS_TABLE = "CREATE TABLE IF NOT EXISTS " + prefix + "_MARKET (" +
      "`offer_id` VARCHAR(36) UNIQUE," +
      "`offer_time` BIGINT(60)," +
      "`offer_player` VARCHAR(36) NOT NULL," +
      "`offer_item` TEXT NOT NULL" +
      "`offer_currency` VARCHAR(100)," +
      "`offer_cost` DECIMAL(49,4)," +
      "`offer_world` VARCHAR(50) NOT NULL" +
      ") ENGINE = INNODB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;";

  public static final String OFFERS_TABLE_H2 = "CREATE TABLE IF NOT EXISTS " + prefix + "_MARKET (" +
      "`offer_id` VARCHAR(36) UNIQUE," +
      "`offer_time` BIGINT(60)," +
      "`offer_player` VARCHAR(36) NOT NULL," +
      "`offer_item` TEXT NOT NULL" +
      "`offer_currency` VARCHAR(100)," +
      "`offer_cost` DECIMAL(49,4)," +
      "`offer_world` VARCHAR(50) NOT NULL" +
      ") ENGINE = INNODB;";

  private static final String OFFER_CHECK = "SELECT COUNT(*) as offer_count FROM " + prefix + "_MARKET WHERE offer_id = ?";
  private static final String OFFER_COUNT = "SELECT COUNT(*) as offer_count FROM " + prefix + "_MARKET WHERE offer_world = ?";
  private static final String OFFERS_SELECT = "SELECT offer_id, offer_item FROM " + prefix + "_MARKET WHERE offer_world = ? ORDER BY offer_time DESC LIMIT ?,?;";

  private static final String OFFER_INSERT = "INSERT INTO " + prefix + "_MARKET offer_id, offer_time, offer_player, offer_item, offer_currency, offer_cost, offer_world" +
                                              " VALUES(?, ?, ?, ?, ?, ?, ?) ON DUPLICATE UPDATE offer_item = ?, offer_currency = ?, offer_cost = ?;";

  private static final String OFFERS_PLAYER = "SELECT COUNT(*) as offer_count FROM " + prefix + "_MARKET WHERE offer_player = ?";
  private static final String OFFER_PLAYER = "SELECT offer_player FROM " + prefix + "_MARKET WHERE offer_id = ?";
  private static final String OFFER_DELETE = "DELETE FROM " + prefix + "_MARKET WHERE offer_id = ?";

  public static void addOffer(UUID id, UUID player, ItemStack item, String currency, BigDecimal cost, String world) throws SQLException {
    database().executePreparedUpdate(OFFER_INSERT, new Object[] {
        id.toString(),
        new Date().getTime(),
        player.toString(),
        new SerialItem(item).serialize(),
        currency,
        cost,
        world,
        new SerialItem(item).serialize(),
        currency,
        cost
    });
  }

  public static void removeOffer(UUID offer) throws SQLException {
    database().executePreparedUpdate(OFFER_DELETE, new Object[] { offer.toString() });
  }

  public static List<MarketEntry> getItems(String world, int page) {
    int start = 0;
    if(page > 1) start = ((page - 1) * 45);

    List<MarketEntry> items = new ArrayList<>();

    try(Connection connection = database().getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(OFFERS_SELECT);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            world,
            start,
            10
        })) {
      while(results.next()) {
        items.add(new MarketEntry(SerialItem.unserialize(results.getString("offer_item")).getStack(), UUID.fromString(results.getString("offer_id"))));
      }
    } catch(Exception ignore) {}
    return items;
  }

  public static int offerCount(String world) {
    int count = 0;

    try(Connection connection = database().getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(OFFER_COUNT);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            world
        })) {

      if(results.next()) {
        count = results.getInt("offer_count");
      }
    } catch(Exception ignore) {}
    return count;
  }

  public static int playerOffers(UUID player) {
    int count = 0;

    try(Connection connection = database().getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(OFFERS_PLAYER);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            player.toString()
        })) {

      if(results.next()) {
        count = results.getInt("offer_count");
      }
    } catch(Exception ignore) {}
    return count;
  }

  public static UUID player(UUID offer) {
    UUID player = null;

    try(Connection connection = database().getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(OFFER_PLAYER);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            offer.toString()
        })) {

      if(results.next()) {
        player = UUID.fromString(results.getString("offer_player"));
      }
    } catch(Exception ignore) {}
    return player;
  }

  public static UUID freeID() {
    UUID id = UUID.randomUUID();

    while(exists(id)) {
      id = UUID.randomUUID();
    }
    return id;
  }

  public static boolean exists(UUID offer) {
    boolean exists = false;

    try(Connection connection = database().getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(OFFER_CHECK);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            offer.toString()
        })) {

      if(results.next()) {
        exists = true;
      }
    } catch(Exception ignore) {}
    return exists;
  }

  public static SQLDatabase database() throws SQLException {
    return ((SQLDatabase)TNE.saveManager().getTNEManager().getTNEProvider().connector());
  }
}