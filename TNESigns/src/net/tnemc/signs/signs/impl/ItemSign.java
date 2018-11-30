package net.tnemc.signs.signs.impl;

import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
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
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

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

    tables.put("h2", Collections.singletonList(
        "CREATE TABLE IF NOT EXISTS " + SignsData.prefix + "_SIGNS_ITEMS (" +
        "`sign_location` VARCHAR(420) NOT NULL UNIQUE," +
        "`item_selling` TINYINT(1) NOT NULL DEFAULT 1," +
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
    try {
      SignsData.saveSign(new TNESign(event.getBlock().getLocation(), attached.getLocation(), "item", player, player, new Date().getTime(), 1));
      System.out.println("Created Item Sign");
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
    System.out.println("Item Sign Interaction!");
    try {
      final TNESign loaded = SignsData.loadSign(sign.getLocation());
      System.out.println("Item Sign Interaction! Step: " + loaded.getStep());
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
    try (Connection connection = SignsData.database().connection(TNE.saveManager().getTNEManager());
         PreparedStatement statement = connection.prepareStatement(SignsData.ITEM_CHECK);
         ResultSet results = SignsData.database().executePreparedQuery(statement, new Object[] {
             new SerializableLocation(location).toString()
         })) {

      if(results.next()) exists = true;
    } catch(Exception ignore) {}
    return exists;
  }

  public static void saveItemSelection(final Location location, final ItemStack stack, final int selling) throws SQLException {
    SignsData.database().executePreparedUpdate((offerExists(location))? SignsData.ITEM_OFFER_UPDATE : SignsData.ITEM_OFFER_ADD, new Object[] {
        new SerializableItemStack(1, stack).toString(),
        stack.getAmount(),
        selling,
        new SerializableLocation(location).toString()
    });
  }

  public static void saveItemOffer(final Location location, final ItemStack stack, final boolean currency, final BigDecimal amount) throws SQLException {
    SignsData.database().executePreparedUpdate(SignsData.ITEM_TRADE_UPDATE, new Object[] {
        currency,
        amount,
        ((stack == null)? "" : new SerializableItemStack(1, stack).toString()),
        new SerializableLocation(location).toString()
    });
  }
}