package net.tnemc.signs.signs.impl;

import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.material.MaterialHelper;
import net.tnemc.core.item.SerialItem;
import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import net.tnemc.signs.signs.impl.item.ChestSelectionStep;
import net.tnemc.signs.signs.impl.item.ItemSelectionStep;
import net.tnemc.signs.signs.impl.item.TradeSelectionStep;
import net.tnemc.signs.signs.impl.item.TradeStep;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.tnemc.signs.SignsData.ITEM_CURRENCY_CHECK;
import static net.tnemc.signs.SignsData.ITEM_CURRENCY_LOAD;
import static net.tnemc.signs.SignsData.ITEM_OFFER_LOAD;
import static net.tnemc.signs.SignsData.ITEM_SELLING_CHECK;
import static net.tnemc.signs.SignsData.ITEM_TRADE_LOAD;
import static net.tnemc.signs.SignsData.SIGNS_CHEST_CHECK;
import static net.tnemc.signs.SignsData.database;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 11/27/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ItemSign implements SignType {

  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "item";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.item.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.item.create";
  }

  @Override
  public Map<Integer, SignStep> steps() {
    Map<Integer, SignStep> steps = new HashMap<>();
    steps.put(1, new ItemSelectionStep());
    steps.put(2, new TradeSelectionStep());
    steps.put(3, new ChestSelectionStep());
    steps.put(4, new TradeStep());
    return steps;
  }

  @Override
  public Map<String, List<String>> tables() {
    Map<String, List<String>> tables = new HashMap<>();

    tables.put("mysql", Collections.singletonList(
        "CREATE TABLE IF NOT EXISTS " + SignsData.prefix + "_SIGNS_ITEMS (" +
        "`sign_location` VARCHAR(420) NOT NULL UNIQUE," +
        "`item_selling` BOOLEAN NOT NULL DEFAULT 1," +
        "`item_amount` INTEGER NOT NULL DEFAULT 1," +
        "`item_currency` BOOLEAN NOT NULL DEFAULT 1," +
        "`item_cost` DECIMAL(49,4) DEFAULT 10," +
        "`item_offer` TEXT NOT NULL," +
        "`item_trade` TEXT NOT NULL DEFAULT ''," +
        ") ENGINE = INNODB;"
    ));

    tables.put("h2", Collections.singletonList(
        "CREATE TABLE IF NOT EXISTS " + SignsData.prefix + "_SIGNS_ITEMS (" +
        "`sign_location` VARCHAR(420) NOT NULL UNIQUE," +
        "`item_selling` BOOLEAN NOT NULL DEFAULT 1," +
        "`item_amount` INTEGER NOT NULL DEFAULT 1," +
        "`item_currency` BOOLEAN NOT NULL DEFAULT 1," +
        "`item_cost` DECIMAL(49,4) DEFAULT 10," +
        "`item_offer` TEXT NOT NULL," +
        "`item_trade` TEXT NOT NULL DEFAULT ''," +
        ") ENGINE = INNODB;"
    ));
    return tables;
  }

  @Override
  public boolean onSignCreate(SignChangeEvent event, Block attached, UUID player) {
    final Material material = MaterialHelper.getMaterial(event.getLine(0));
    if(material != null && !material.equals(Material.AIR)) {

    }
    try {
      SignsData.saveSign(new TNESign(event.getBlock().getLocation(), attached.getLocation(), "item", player, player, new Date().getTime(), 1));
      TNE.debug("Created Item Sign");
      Bukkit.getPlayer(player).sendMessage(ChatColor.WHITE + "Left click the sign with an item in the correct amount to buy that item," +
                                             " or right click to sell an item.");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean onSignInteract(Sign sign, UUID player, boolean rightClick, boolean shifting) {
    TNE.debug("Item Sign Interaction!");
    try {
      final TNESign loaded = SignsData.loadSign(sign.getLocation());
      TNE.debug("Item Sign Interaction! Step: " + loaded.getStep());
      return steps().get(loaded.getStep()).onSignInteract(sign, player, rightClick, shifting);
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return false;
  }

  @Override
  public boolean onSignDestroy(UUID owner, UUID player) {
    if(owner.equals(player)) {
      return true;
    }
    return false;
  }

  public static boolean offerExists(final Location location) throws SQLException {
    boolean exists = false;
    try (Connection connection = database().connection(TNE.saveManager().getTNEManager());
         PreparedStatement statement = connection.prepareStatement(SignsData.ITEM_CHECK);
         ResultSet results = database().executePreparedQuery(statement, new Object[] {
             new SerializableLocation(location).toString()
         })) {

      if(results.next()) exists = true;
    } catch(Exception ignore) {}
    return exists;
  }

  public static void saveItemSelection(final Location location, final ItemStack stack, final boolean selling) throws SQLException {
    database().executePreparedUpdate((offerExists(location))? SignsData.ITEM_OFFER_UPDATE : SignsData.ITEM_OFFER_ADD, new Object[] {
        new SerialItem(stack).toJSON().toJSONString(),
        stack.getAmount(),
        selling,
        new SerializableLocation(location).toString()
    });
    Sign sign = (Sign) location.getBlock().getState();
    sign.setLine(1, MaterialHelper.getShopName(stack.getType()) + ":" + stack.getAmount());
    sign.update(true);
  }

  public static UUID chest(Location location) throws SQLException {
    UUID owner = null;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(SIGNS_CHEST_CHECK);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        owner = UUID.fromString("sign_owner");

      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return owner;
  }

  public static ItemStack getItem(Location location) throws SQLException {
    ItemStack item = null;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(ITEM_OFFER_LOAD);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        String str = results.getString("item_offer");

        item = SerialItem.fromJSON((JSONObject)new JSONParser().parse(str)).getStack();
        TNE.debug(item.toString());
        TNE.debug("Null: " + (item != null));

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
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        item = SerialItem.fromJSON((JSONObject)new JSONParser().parse(results.getString("item_trade"))).getStack();
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return item;
  }

  public static boolean isCurrency(Location location) throws SQLException {
    boolean currency = false;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(ITEM_CURRENCY_CHECK);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        currency = results.getBoolean("item_currency");
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return currency;
  }

  public static boolean isSelling(Location location) throws SQLException {
    boolean selling = false;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(ITEM_SELLING_CHECK);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        selling = results.getBoolean("item_selling");
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return selling;
  }

  public static BigDecimal getCost(Location location) throws SQLException {
    BigDecimal amount = null;

    try(Connection connection = database().connection(TNE.saveManager().getTNEManager());
        PreparedStatement statement = connection.prepareStatement(ITEM_CURRENCY_LOAD);
        ResultSet results = database().executePreparedQuery(statement, new Object[] {
            new SerializableLocation(location).toString()
        })) {

      if(results.next()) {
        amount = results.getBigDecimal("item_cost");
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return amount;
  }

  public static void saveItemOffer(final Location location, final ItemStack stack, final boolean currency, final BigDecimal amount) throws SQLException {
    database().executePreparedUpdate(SignsData.ITEM_TRADE_UPDATE, new Object[] {
        currency,
        amount,
        ((stack == null)? "" : new SerialItem(stack).toJSON().toJSONString()),
        new SerializableLocation(location).toString()
    });
    Sign sign = (Sign) location.getBlock().getState();
    if(stack != null) {
      sign.setLine(2, MaterialHelper.getShopName(stack.getType()) + ":" + stack.getAmount());
    } else {
      sign.setLine(2, ChatColor.GOLD + CurrencyFormatter.format(TNE.manager().currencyManager().get(location.getWorld().getName()), location.getWorld().getName(),
                                                amount, "<symbol><short.amount>"));
    }
    sign.update(true);
  }

  public static boolean canOffer(final Player player, final Material material) {
    if(TNE.hasPermssion(player, "tne.item.sign")) return true;
    if(TNE.instance().itemConfiguration().contains("tne.item." + material.name().toLowerCase() + ".sign")) {
      return TNE.hasPermssion(player, TNE.instance().itemConfiguration().getString(""));
    }
    return false;
  }
}