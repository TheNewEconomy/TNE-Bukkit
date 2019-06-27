package net.tnemc.signs.signs.impl;

import com.github.tnerevival.core.db.SQLDatabase;
import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
import net.tnemc.core.item.SerialItem;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.SignsModule;
import net.tnemc.signs.selection.SelectionPlayer;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.command.CommandSelectionStep;
import net.tnemc.signs.signs.impl.command.CostSelectionStep;
import net.tnemc.signs.signs.impl.command.PurchaseSelectionStep;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.tnemc.signs.SignsData.COMMAND_CURRENCY_CHECK;
import static net.tnemc.signs.SignsData.COMMAND_CURRENCY_LOAD;
import static net.tnemc.signs.SignsData.COMMAND_TRADE_LOAD;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/18/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandSign implements SignType {
  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "command";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.command.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.command.create";
  }

  @Override
  public Map<Integer, SignStep> steps() {
    Map<Integer, SignStep> steps = new HashMap<>();
    steps.put(1, new CommandSelectionStep());
    steps.put(2, new CostSelectionStep());
    steps.put(3, new PurchaseSelectionStep());
    return steps;
  }

  @Override
  public Map<String, List<String>> tables() {
    Map<String, List<String>> tables = new HashMap<>();

    tables.put("mysql", Collections.singletonList(
        "CREATE TABLE IF NOT EXISTS " + SignsData.prefix + "_SIGNS_COMMANDS (" +
            "`sign_location` VARCHAR(255) NOT NULL UNIQUE," +
            "`cmd_value` TEXT NOT NULL," +
            "`cmd_currency` BOOLEAN NOT NULL DEFAULT 1," +
            "`cmd_cost` DECIMAL(49,4) DEFAULT 10," +
            "`cmd_offer` TEXT NOT NULL," +
            "`cmd_trade` TEXT NOT NULL DEFAULT ''" +
            ") ENGINE = INNODB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;"
    ));

    tables.put("h2", Collections.singletonList(
        "CREATE TABLE IF NOT EXISTS " + SignsData.prefix + "_SIGNS_COMMANDS (" +
            "`sign_location` VARCHAR(255) NOT NULL UNIQUE," +
            "`cmd_value` TEXT NOT NULL," +
            "`cmd_currency` BOOLEAN NOT NULL DEFAULT 1," +
            "`cmd_cost` DECIMAL(49,4) DEFAULT 10," +
            "`cmd_offer` TEXT NOT NULL," +
            "`cmd_trade` TEXT NOT NULL DEFAULT ''" +
            ") ENGINE = INNODB;"
    ));
    return tables;
  }

  @Override
  public boolean onSignCreate(SignChangeEvent event, Block attached, UUID player) {
    try {

      SignsData.saveSign(new TNESign(event.getBlock().getLocation(), (attached != null)? attached.getLocation() : event.getBlock().getLocation(), "command", player, player, new Date().getTime(), false, 1));
      Bukkit.getPlayer(player).sendMessage(ChatColor.WHITE + "Now type your command in chat, without the slash(/) character.");
      SignsModule.manager().getSelectionManager().addPlayer(player, new SelectionPlayer(player, event.getBlock().getLocation(), "command"));
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static void saveCommand(final Location location, final String command) {
    SQLDatabase.executePreparedUpdate(SignsData.COMMAND_COMMAND_UPDATE, new Object[] {
        new SerializableLocation(location).toString(),
        command,
        command
    });
  }

  public static void saveCost(final Location location, final ItemStack stack, final boolean currency, final BigDecimal amount) {
    SQLDatabase.executePreparedUpdate(SignsData.COMMAND_TRADE_UPDATE, new Object[] {
        currency,
        amount,
        ((stack == null)? "" : new SerialItem(stack).toJSON().toJSONString()),
        new SerializableLocation(location).toString()
    });
  }

  public static BigDecimal getCost(Location location) throws SQLException {
    BigDecimal amount = null;

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(COMMAND_CURRENCY_LOAD);
        ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        amount = results.getBigDecimal("cmd_cost");
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return amount;
  }

  public static ItemStack getTrade(Location location) {
    ItemStack item = null;

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(COMMAND_TRADE_LOAD);
        ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        item = SerialItem.fromJSON((JSONObject)new JSONParser().parse(results.getString("cmd_trade"))).getStack();
        TNE.debug("Trade: " + results.getString("cmd_trade"));
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return item;
  }

  public static boolean isCurrency(Location location) {
    boolean currency = false;

    SQLDatabase.open();
    try(PreparedStatement statement = SQLDatabase.getDb().getConnection().prepareStatement(COMMAND_CURRENCY_CHECK);
        ResultSet results = SQLDatabase.executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        currency = results.getBoolean("cmd_currency");
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    SQLDatabase.close();
    return currency;
  }
}