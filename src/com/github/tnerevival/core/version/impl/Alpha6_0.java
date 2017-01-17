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
package com.github.tnerevival.core.version.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.auction.Claim;
import com.github.tnerevival.core.db.H2;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.core.transaction.Record;
import com.github.tnerevival.core.transaction.TransactionHistory;
import com.github.tnerevival.core.version.Version;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class Alpha6_0 extends Version {
  @Override
  public double versionNumber() {
    return 5.1;
  }

  @Override
  public void update(double version, String type) {
    if(version < 4.0 || version == 5.0) return;

    String table = prefix + "_INFO";
    if(type.equalsIgnoreCase("mysql")) {
      mysql().executeUpdate("ALTER TABLE `" + table + "` ADD UNIQUE(id)");
      mysql().executeUpdate("ALTER TABLE `" + table + "` ADD COLUMN `server_name` VARCHAR(250) AFTER `version`");

      table = prefix + "_ECOIDS";
      mysql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `username` VARCHAR(56)");
      mysql().close();
    } else if(type.equalsIgnoreCase("h2")) {
      h2().executeUpdate("ALTER TABLE `" + table + "` ADD UNIQUE(id)");
      h2().executeUpdate("ALTER TABLE `" + table + "` ADD COLUMN `server_name` VARCHAR(250) AFTER `version`");

      table = prefix + "_ECOIDS";
      h2().executeUpdate("ALTER TABLE `" + table + "` MODIFY `username` VARCHAR(56)");
      h2().close();
    }
  }

  @Override
  public Map<String, TransactionHistory> loadTransactions() {
    return null;
  }

  @Override
  public TransactionHistory loadHistory(UUID id) {
    return null;
  }

  @Override
  public void saveTransaction(Record record) {

  }

  @Override
  public void deleteTransaction(UUID id) {

  }

  @Override
  public Collection<Account> loadAccounts() {
    return null;
  }

  @Override
  public Account loadAccount(UUID id) {
    return null;
  }

  @Override
  public void saveAccount(Account acc) {

  }

  @Override
  public void deleteAccount(UUID id) {

  }

  @Override
  public Collection<Shop> loadShops() {
    return null;
  }

  @Override
  public Shop loadShop(String name, String world) {
    return null;
  }

  @Override
  public void saveShop(Shop shop) {

  }

  @Override
  public void deleteShop(Shop shop) {

  }

  @Override
  public Collection<TNESign> loadSigns() {
    return null;
  }

  @Override
  public TNESign loadSign(String location) {
    return null;
  }

  @Override
  public void saveSign(TNESign sign) {

  }

  @Override
  public void deleteSign(TNESign sign) {

  }

  @Override
  public Collection<Auction> loadAuctions() {
    return null;
  }

  @Override
  public Auction loadAuction(Integer lot) {
    return null;
  }

  @Override
  public void saveAuction(Auction auction) {

  }

  @Override
  public void deleteAuction(Auction auction) {

  }

  @Override
  public Collection<Claim> loadClaims() {
    return null;
  }

  @Override
  public Claim loadClaim(UUID owner, Integer lot) {
    return null;
  }

  @Override
  public void saveClaim(Claim claim) {

  }

  @Override
  public void deleteClaim(Claim claim) {

  }

  @Override
  public Map<String, UUID> loadIDS() {
    return null;
  }

  @Override
  public UUID loadID(String username) {
    return null;
  }

  @Override
  public void saveID(String username, UUID id) {

  }

  @Override
  public void removeID(String username) {

  }

  @Override
  public void removeID(UUID id) {

  }

  @Override
  public void loadFlat(File file) {

  }

  @Override
  public void saveFlat(File file) {

  }

  @Override
  public void loadMySQL() {

  }

  @Override
  public void saveMySQL() {

  }

  @Override
  public void loadSQLite() {

  }

  @Override
  public void saveSQLite() {

  }

  @Override
  public void loadH2() {

  }

  @Override
  public void saveH2() {

  }

  @Override
  public void createTables(String type) {
    String table = prefix + "_INFO";

    if(type.equalsIgnoreCase("mysql")) {
      db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);

      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`id` INTEGER NOT NULL UNIQUE," +
          "`version` VARCHAR(10)," +
          "`server_name` VARCHAR(250)" +
          ");");
      mysql().executePreparedUpdate("INSERT INTO `" + table + "` (id, version, server_name) VALUES(1, ?, ?) ON DUPLICATE KEY UPDATE version = ?, server_name = ?",
          new Object[] {
              versionNumber(),
              TNE.instance.getServer().getServerName(),
              versionNumber(),
              TNE.instance.getServer().getServerName()
          });

      table = prefix + "_ECOIDS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
          "`username` VARCHAR(56)," +
          "`uuid` VARCHAR(36) UNIQUE" +
          ");");

      table = prefix + "_USERS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
          "`inventory_credits` LONGTEXT," +
          "`command_credits` LONGTEXT," +
          "`acc_pin` VARCHAR(30)," +
          "`balances` LONGTEXT," +
          "`joinedDate` VARCHAR(60)," +
          "`accountnumber` INTEGER," +
          "`accountstatus` VARCHAR(60)" +
          ");");

      table = prefix + "_SHOPS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_owner` VARCHAR(36)," +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`shop_hidden` TINYINT(1)," +
          "`shop_admin` TINYINT(1)," +
          "`shop_items` LONGTEXT," +
          "`shop_blacklist` LONGTEXT," +
          "`shop_whitelist` LONGTEXT," +
          "`shop_shares` LONGTEXT," +
          "PRIMARY KEY(shop_name, shop_world)" +
          ");");

      table = prefix + "_AUCTIONS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`auction_lot` INT(60) NOT NULL," +
          "`auction_added` BIGINT(60) NOT NULL," +
          "`auction_start` BIGINT(60) NOT NULL," +
          "`auction_owner` VARCHAR(36)," +
          "`auction_world` VARCHAR(36)," +
          "`auction_silent` TINYINT(1)," +
          "`auction_item` LONGTEXT," +
          "`auction_cost` LONGTEXT," +
          "`auction_increment` DOUBLE," +
          "`auction_global` TINYINT(1)," +
          "`auction_time` INT(20)," +
          "`auction_node` LONGTEXT," +
          "PRIMARY KEY(auction_lot)" +
          ");");

      table = prefix +"_CLAIMS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`claim_player` VARCHAR(36)," +
          "`claim_lot` INT(60) NOT NULL," +
          "`claim_item` LONGTEXT," +
          "`claim_paid` TINYINT(1)," +
          "`claim_cost` LONGTEXT," +
          "PRIMARY KEY(claim_player, claim_lot)" +
          ");");

      table = prefix + "_BANKS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`bank` LONGTEXT," +
          "PRIMARY KEY(uuid, world)" +
          ");");

      table = prefix + "_VAULTS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY," +
          "`world` VARCHAR(50) NOT NULL PRIMARY KEY," +
          "`vault` LONGTEXT" +
          ");");

      table = prefix + "_SIGNS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_owner` VARCHAR(36)," +
          "`sign_type` VARCHAR(30) NOT NULL," +
          "`sign_location` VARCHAR(230) NOT NULL UNIQUE," +
          "`sign_meta` LONGTEXT" +
          ");");

      table = prefix + "_TRANSACTIONS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`trans_id` VARCHAR(36)," +
          "`trans_initiator` VARCHAR(36)," +
          "`trans_player` VARCHAR(36)," +
          "`trans_world` VARCHAR(36)," +
          "`trans_type` VARCHAR(36)," +
          "`trans_cost` DOUBLE," +
          "`trans_oldBalance` DOUBLE," +
          "`trans_balance` DOUBLE," +
          "`trans_time` BIGINT(60)," +
          "PRIMARY KEY(trans_id)" +
          ");");
      mysql().close();
    } else {
      File h2DB = new File(h2File);
      if(!h2DB.exists()) {
        try {
          h2DB.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      db = new H2(h2File, mysqlUser, mysqlPassword);

      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`id` INTEGER NOT NULL UNIQUE," +
          "`version` VARCHAR(10)," +
          "`server_name` VARCHAR(250)" +
          ");");
      h2().executePreparedUpdate("INSERT INTO `" + table + "` (id, version, server_name) VALUES(1, ?, ?) ON DUPLICATE KEY UPDATE version = ?, server_name = ?",
          new Object[] {
              versionNumber(),
              TNE.instance.getServer().getServerName(),
              versionNumber(),
              TNE.instance.getServer().getServerName()
          });

      table = prefix + "_ECOIDS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
          "`username` VARCHAR(56)," +
          "`uuid` VARCHAR(36) UNIQUE" +
          ");");

      table = prefix + "_USERS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL UNIQUE," +
          "`inventory_credits` LONGTEXT," +
          "`command_credits` LONGTEXT," +
          "`acc_pin` VARCHAR(30)," +
          "`balances` LONGTEXT," +
          "`joinedDate` VARCHAR(60)," +
          "`accountnumber` INTEGER," +
          "`accountstatus` VARCHAR(60)" +
          ");");

      table = prefix + "_SHOPS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_owner` VARCHAR(36)," +
          "`shop_name` VARCHAR(60) NOT NULL PRIMARY KEY," +
          "`shop_world` VARCHAR(50) NOT NULL PRIMARY KEY," +
          "`shop_hidden` TINYINT(1)," +
          "`shop_admin` TINYINT(1)," +
          "`shop_items` LONGTEXT," +
          "`shop_blacklist` LONGTEXT," +
          "`shop_whitelist` LONGTEXT," +
          "`shop_shares` LONGTEXT" +
          ");");

      table = prefix + "_AUCTIONS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`auction_lot` INT(60) NOT NULL," +
          "`auction_added` BIGINT(60) NOT NULL," +
          "`auction_start` BIGINT(60) NOT NULL," +
          "`auction_owner` VARCHAR(36)," +
          "`auction_world` VARCHAR(36)," +
          "`auction_silent` TINYINT(1)," +
          "`auction_item` LONGTEXT," +
          "`auction_cost` LONGTEXT," +
          "`auction_increment` DOUBLE," +
          "`auction_global` TINYINT(1)," +
          "`auction_time` INT(20)," +
          "`auction_node` LONGTEXT," +
          "PRIMARY KEY(auction_lot)" +
          ");");

      table = prefix +"_CLAIMS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`claim_player` VARCHAR(36)," +
          "`claim_lot` INT(60) NOT NULL," +
          "`claim_item` LONGTEXT," +
          "`claim_paid` TINYINT(1)," +
          "`claim_cost` LONGTEXT," +
          "PRIMARY KEY(claim_player, claim_lot)" +
          ");");

      table = prefix + "_BANKS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY," +
          "`world` VARCHAR(50) NOT NULL PRIMARY KEY," +
          "`bank` LONGTEXT" +
          ");");

      table = prefix + "_VAULTS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY," +
          "`world` VARCHAR(50) NOT NULL PRIMARY KEY," +
          "`vault` LONGTEXT" +
          ");");

      table = prefix + "_SIGNS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_owner` VARCHAR(36)," +
          "`sign_type` VARCHAR(30) NOT NULL," +
          "`sign_location` VARCHAR(230) NOT NULL UNIQUE," +
          "`sign_meta` LONGTEXT" +
          ");");

      table = prefix + "_TRANSACTIONS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`trans_id` VARCHAR(36)," +
          "`trans_initiator` VARCHAR(36)," +
          "`trans_player` VARCHAR(36)," +
          "`trans_world` VARCHAR(36)," +
          "`trans_type` VARCHAR(36)," +
          "`trans_cost` DOUBLE," +
          "`trans_oldBalance` DOUBLE," +
          "`trans_balance` DOUBLE," +
          "`trans_time` BIGINT(60)," +
          "PRIMARY KEY(trans_id)" +
          ");");
      h2().close();
    }
  }
}