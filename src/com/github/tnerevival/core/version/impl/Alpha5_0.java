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
import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.auction.Claim;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.db.FlatFile;
import com.github.tnerevival.core.db.H2;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.SQLDatabase;
import com.github.tnerevival.core.db.flat.Article;
import com.github.tnerevival.core.db.flat.Entry;
import com.github.tnerevival.core.db.flat.FlatFileConnection;
import com.github.tnerevival.core.db.flat.Section;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.core.transaction.Record;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.core.transaction.TransactionHistory;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.core.version.Version;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.MISCUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by creatorfromhell on 11/15/2016.
 **/
public class Alpha5_0 extends Version {
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
    Map<String, TransactionHistory> transactions = new HashMap<>();

    String table = prefix + "_TRANSACTIONS";
    try {
      int transactionIndex = sql().executeQuery("SELECT * FROM `" + table + "`;");
      while (sql().results(transactionIndex).next()) {
        Record r = new Record(
            sql().results(transactionIndex).getString("trans_id"),
            sql().results(transactionIndex).getString("trans_initiator"),
            sql().results(transactionIndex).getString("trans_player"),
            sql().results(transactionIndex).getString("trans_world"),
            sql().results(transactionIndex).getString("trans_type"),
            new BigDecimal(sql().results(transactionIndex).getDouble("trans_cost")),
            new BigDecimal(sql().results(transactionIndex).getDouble("trans_oldBalance")),
            new BigDecimal(sql().results(transactionIndex).getDouble("trans_balance")),
            sql().results(transactionIndex).getLong("trans_time")
        );
        TransactionHistory history = (transactions.containsKey(r.getInitiator()))? transactions.get(r.getInitiator()) : new TransactionHistory();
        history.add(r, true);
        transactions.put(r.getInitiator(), history);
      }
      sql().close();
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return transactions;
  }

  @Override
  public TransactionHistory loadHistory(UUID id) {
    String table = prefix + "_TRANSACTIONS";
    try {
      TransactionHistory history = new TransactionHistory();
      int transactionIndex = sql().executePreparedQuery("SELECT * FROM `" + table + "` WHERE trans_initiator = ?", new Object[] {
        id
      });

      while(sql().results(transactionIndex).next()) {
        Record r = new Record(
            sql().results(transactionIndex).getString("trans_id"),
            sql().results(transactionIndex).getString("trans_initiator"),
            sql().results(transactionIndex).getString("trans_player"),
            sql().results(transactionIndex).getString("trans_world"),
            sql().results(transactionIndex).getString("trans_type"),
            new BigDecimal(sql().results(transactionIndex).getDouble("trans_cost")),
            new BigDecimal(sql().results(transactionIndex).getDouble("trans_oldBalance")),
            new BigDecimal(sql().results(transactionIndex).getDouble("trans_balance")),
            sql().results(transactionIndex).getLong("trans_time")
        );
        history.add(r);
      }
      sql().close();
      return history;
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return null;
  }

  @Override
  public void saveTransaction(Record record) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_TRANSACTIONS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (trans_id, trans_initiator, trans_player, trans_world, trans_type, trans_cost, trans_oldBalance, trans_balance, trans_time) " +
              "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE trans_player = ?, trans_world = ?",
          new Object[]{
              record.getId(),
              record.getInitiator(),
              record.getPlayer(),
              record.getWorld(),
              record.getType(),
              record.getCost(),
              record.getOldBalance(),
              record.getBalance(),
              record.getTime(),
              record.getPlayer(),
              record.getWorld()
          }
      );
      sql().close();
    }
  }

  @Override
  public void deleteTransaction(UUID id) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_TRANSACTIONS WHERE trans_id = ? ", new Object[] { id.toString() });
      sql().close();
    }
  }

  @Override
  public Collection<Account> loadAccounts() {
    List<Account> accounts = new ArrayList<>();

    String table = prefix + "_USERS";
    try {
      int accountIndex = sql().executeQuery("SELECT * FROM " + table + ";");
      while (sql().results(accountIndex).next()) {
        Account account = new Account(UUID.fromString(sql().results(accountIndex).getString("uuid")), sql().results(accountIndex).getInt("accountnumber"));
        account.balancesFromString(sql().results(accountIndex).getString("balances"));
        account.setStatus(sql().results(accountIndex).getString("accountstatus"));
        account.setJoined(sql().results(accountIndex).getString("joinedDate"));
        account.creditsFromString(sql().results(accountIndex).getString("inventory_credits"));
        account.commandsFromString(sql().results(accountIndex).getString("command_credits"));
        account.setPin(sql().results(accountIndex).getString("acc_pin"));

        String bankTable = prefix + "_BANKS";
        int bankIndex = sql().executePreparedQuery("SELECT * FROM " + bankTable + " WHERE uuid = ?;", new Object[]{account.getUid().toString()});

        while (sql().results(bankIndex).next()) {
          //TODO: Convert old banks to new banks and vaults
          //account.getBanks().put(sql().results(bankIndex).getString("world"), Bank.fromString(sql().results(bankIndex).getString("bank")));
        }
        accounts.add(account);
      }
      sql().close();
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return accounts;
  }

  @Override
  public Account loadAccount(UUID id) {
    String table = prefix + "_USERS";
    try {
      int accountIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?", new Object[] {
        id.toString()
      });
      if(sql().results(accountIndex).next()) {
        Account account = new Account(UUID.fromString(sql().results(accountIndex).getString("uuid")), sql().results(accountIndex).getInt("accountnumber"));
        account.balancesFromString(sql().results(accountIndex).getString("balances"));
        account.setStatus(sql().results(accountIndex).getString("accountstatus"));
        account.setJoined(sql().results(accountIndex).getString("joinedDate"));
        account.creditsFromString(sql().results(accountIndex).getString("inventory_credits"));
        account.commandsFromString(sql().results(accountIndex).getString("command_credits"));
        account.setPin(sql().results(accountIndex).getString("acc_pin"));

        table = prefix + "_BANKS";
        int bankIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?;", new Object[] { account.getUid().toString() });

        while(mysql().results(bankIndex).next()) {
          //TODO: Convert old banks to new banks and vaults
          //account.getBanks().put(sql().results(bankIndex).getString("world"), Bank.fromString(sql().results(bankIndex).getString("bank")));
        }
        sql().close();
        return account;
      }
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return null;
  }

  @Override
  public void saveAccount(Account acc) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_USERS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, balances, acc_pin, inventory_credits, command_credits, joinedDate, accountnumber, accountstatus) VALUES(?, ?, ?, ?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE balances = ?, acc_pin = ?, inventory_credits = ?, command_credits = ?, joinedDate = ?, accountnumber = ?, accountstatus = ?",
          new Object[] {
              acc.getUid().toString(),
              acc.balancesToString(),
              acc.getPin(),
              acc.creditsToString(),
              acc.commandsToString(),
              acc.getJoined(),
              acc.getAccountNumber(),
              acc.getStatus().getName(),
              acc.balancesToString(),
              acc.getPin(),
              acc.creditsToString(),
              acc.commandsToString(),
              acc.getJoined(),
              acc.getAccountNumber(),
              acc.getStatus().getName()
          }
      );

      table = prefix + "_BANKS";
      for (Map.Entry<String, Bank> entry : acc.getBanks().entrySet()) {
        sql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, world, bank) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE bank = ?",
            new Object[] {
                acc.getUid().toString(),
                entry.getKey(),
                entry.getValue().toString(),
                entry.getValue().toString()
            }
        );
      }
      sql().close();
    }
  }

  @Override
  public void deleteAccount(UUID id) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_USERS WHERE uuid = ? ", new Object[] { id.toString() });
      sql().close();
    }
  }

  @Override
  public Collection<Shop> loadShops() {
    List<Shop> shops = new ArrayList<>();

    String table = prefix + "_SHOPS";
    try {
      int shopIndex = sql().executeQuery("SELECT * FROM `" + table + "`;");
      while (sql().results(shopIndex).next()) {
        Shop s = new Shop(sql().results(shopIndex).getString("shop_name"), sql().results(shopIndex).getString("shop_world"));
        s.setOwner(UUID.fromString(sql().results(shopIndex).getString("shop_owner")));
        s.setHidden(SQLDatabase.boolFromDB(sql().results(shopIndex).getInt("shop_hidden")));
        s.setAdmin(SQLDatabase.boolFromDB(sql().results(shopIndex).getInt("shop_admin")));
        s.itemsFromString(sql().results(shopIndex).getString("shop_items"));
        s.listFromString(sql().results(shopIndex).getString("shop_blacklist"), true);
        s.listFromString(sql().results(shopIndex).getString("shop_whitelist"), false);
        s.sharesFromString(sql().results(shopIndex).getString("shop_shares"));
        shops.add(s);
      }
      sql().close();
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return shops;
  }

  @Override
  public Shop loadShop(String name, String world) {
    String table = prefix + "_SHOPS";
    try {
      int shopIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE shop_name = ? AND shop_world = ?", new Object[] {
        name,
        world
      });

      if(sql().results(shopIndex).next()) {
        Shop s = new Shop(sql().results(shopIndex).getString("shop_name"), sql().results(shopIndex).getString("shop_world"));
        s.setOwner(UUID.fromString(sql().results(shopIndex).getString("shop_owner")));
        s.setHidden(SQLDatabase.boolFromDB(sql().results(shopIndex).getInt("shop_hidden")));
        s.setAdmin(SQLDatabase.boolFromDB(sql().results(shopIndex).getInt("shop_admin")));
        s.itemsFromString(sql().results(shopIndex).getString("shop_items"));
        s.listFromString(sql().results(shopIndex).getString("shop_blacklist"), true);
        s.listFromString(sql().results(shopIndex).getString("shop_whitelist"), false);
        s.sharesFromString(sql().results(shopIndex).getString("shop_shares"));
        sql().close();
        return s;
      }
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return null;
  }

  @Override
  public void saveShop(Shop shop) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_SHOPS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (shop_name, shop_world, shop_owner, shop_hidden, shop_admin, shop_items, shop_blacklist, shop_whitelist, shop_shares) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE shop_owner = ?, shop_hidden = ?, shop_admin = ?, shop_items = ?, shop_blacklist = ?, shop_whitelist = ?, shop_shares = ?",
          new Object[]{
              shop.getName(),
              shop.getWorld(),
              shop.getOwner().toString(),
              SQLDatabase.boolToDB(shop.isHidden()),
              SQLDatabase.boolToDB(shop.isAdmin()),
              shop.itemsToString(),
              shop.listToString(true),
              shop.listToString(false),
              shop.sharesToString(),
              shop.getOwner().toString(),
              SQLDatabase.boolToDB(shop.isHidden()),
              SQLDatabase.boolToDB(shop.isAdmin()),
              shop.itemsToString(),
              shop.listToString(true),
              shop.listToString(false),
              shop.sharesToString()
          }
      );
      sql().close();
    }
  }

  @Override
  public void deleteShop(Shop shop) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SHOPS WHERE shop_name = ? AND shop_world = ?", new Object[] { shop.getName(), shop.getWorld() });
      sql().close();
    }
  }

  @Override
  public Collection<TNESign> loadSigns() {
    List<TNESign> signs = new ArrayList<>();

    String table = prefix + "_SIGNS";
    try {
      int signIndex = sql().executeQuery("SELECT * FROM `" + table + "`;");
      while (sql().results(signIndex).next()) {
        TNESign sign = TNESign.instance(sql().results(signIndex).getString("sign_type"), UUID.fromString(sql().results(signIndex).getString("sign_owner")), SerializableLocation.fromString(sql().results(signIndex).getString("sign_location")));
        sign.loadMeta(sql().results(signIndex).getString("sign_meta"));
        signs.add(sign);
      }
      sql().close();
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return signs;
  }

  @Override
  public TNESign loadSign(String location) {
    String table = prefix + "_SIGNS";
    try {
      int signIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE sign_location = ?", new Object[] {
        location
      });
      
      if(sql().results(signIndex).next()) {
        TNESign sign = TNESign.instance(sql().results(signIndex).getString("sign_type"), UUID.fromString(sql().results(signIndex).getString("sign_owner")), SerializableLocation.fromString(sql().results(signIndex).getString("sign_location")));
        sign.loadMeta(sql().results(signIndex).getString("sign_meta"));
        sql().close();
        return sign;
      }
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return null;
  }

  @Override
  public void saveSign(TNESign sign) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_SIGNS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (sign_owner, sign_type, sign_location, sign_meta) VALUES(?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE sign_owner = ?, sign_type = ?, sign_meta = ?",
          new Object[] {
              sign.getOwner().toString(),
              sign.getType().getName(),
              sign.getLocation().toString(),
              sign.getMeta(),
              sign.getOwner().toString(),
              sign.getType().getName(),
              sign.getMeta()
          }
      );
      sql().close();
    }
  }

  @Override
  public void deleteSign(TNESign sign) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SIGNS WHERE sign_location = ?", new Object[] { sign.getLocation().toString() });
      sql().close();
    }
  }

  @Override
  public Collection<Auction> loadAuctions() {
    List<Auction> auctions = new ArrayList<>();

    String table = prefix + "_AUCTIONS";
    try {
      int auctionIndex = sql().executeQuery("SELECT * FROM `" + table + "`;");
      while(sql().results(auctionIndex).next()) {
        Auction auction = new Auction(sql().results(auctionIndex).getInt("auction_lot"));
        auction.setAdded(sql().results(auctionIndex).getInt("auction_added"));
        auction.setStartTime(sql().results(auctionIndex).getInt("auction_start"));
        auction.setPlayer(UUID.fromString(sql().results(auctionIndex).getString("auction_owner")));
        auction.setWorld(sql().results(auctionIndex).getString("auction_world"));
        auction.setSilent(SQLDatabase.boolFromDB(sql().results(auctionIndex).getInt("auction_silent")));
        auction.setItem(SerializableItemStack.fromString(sql().results(auctionIndex).getString("auction_item")));
        auction.setCost(new TransactionCost(CurrencyFormatter.translateBigDecimal(sql().results(auctionIndex).getString("auction_cost"), auction.getWorld())));
        auction.setIncrement(new BigDecimal(sql().results(auctionIndex).getDouble("auction_increment")));
        auction.setGlobal(SQLDatabase.boolFromDB(sql().results(auctionIndex).getInt("auction_global")));
        auction.setTime(sql().results(auctionIndex).getInt("auction_time"));
        auction.setNode(sql().results(auctionIndex).getString("auction_node"));

        auctions.add(auction);
      }
      sql().close();
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return auctions;
  }

  @Override
  public Auction loadAuction(Integer lot) {
    String table = prefix + "_AUCTIONS";
    try {
      int auctionIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE auction_lot = ?", new Object[] {
        lot
      });
      if(sql().results(auctionIndex).next()) {
        Auction auction = new Auction(sql().results(auctionIndex).getInt("auction_lot"));
        auction.setAdded(sql().results(auctionIndex).getInt("auction_added"));
        auction.setStartTime(sql().results(auctionIndex).getInt("auction_start"));
        auction.setPlayer(UUID.fromString(sql().results(auctionIndex).getString("auction_owner")));
        auction.setWorld(sql().results(auctionIndex).getString("auction_world"));
        auction.setSilent(SQLDatabase.boolFromDB(sql().results(auctionIndex).getInt("auction_silent")));
        auction.setItem(SerializableItemStack.fromString(sql().results(auctionIndex).getString("auction_item")));
        auction.setCost(new TransactionCost(CurrencyFormatter.translateBigDecimal(sql().results(auctionIndex).getString("auction_cost"), auction.getWorld())));
        auction.setIncrement(new BigDecimal(sql().results(auctionIndex).getDouble("auction_increment")));
        auction.setGlobal(SQLDatabase.boolFromDB(sql().results(auctionIndex).getInt("auction_global")));
        auction.setTime(sql().results(auctionIndex).getInt("auction_time"));
        auction.setNode(sql().results(auctionIndex).getString("auction_node"));
        sql().close();
        return auction;
      }
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return null;
  }

  @Override
  public void saveAuction(Auction auction) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_AUCTIONS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (auction_lot, auction_added, auction_start, auction_owner, auction_world, auction_silent, auction_item, auction_cost, auction_increment, auction_global, auction_time, auction_node) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE auction_added = ?, auction_start = ?, auction_owner = ?, auction_world = ?, auction_silent = ?, auction_item = ?, auction_cost = ?, auction_increment = ?, auction_global = ?, auction_time = ?, auction_node = ?",
          new Object[] {
              auction.getLotNumber(),
              auction.getAdded(),
              auction.getStartTime(),
              auction.getPlayer().toString(),
              auction.getWorld(),
              SQLDatabase.boolToDB(auction.getSilent()),
              auction.getItem().toString(),
              auction.getCost().getAmount(),
              auction.getIncrement(),
              SQLDatabase.boolToDB(auction.getGlobal()),
              auction.getTime(),
              auction.getNode(),
              auction.getAdded(),
              auction.getStartTime(),
              auction.getPlayer().toString(),
              auction.getWorld(),
              SQLDatabase.boolToDB(auction.getSilent()),
              auction.getItem().toString(),
              auction.getCost().getAmount(),
              auction.getIncrement(),
              SQLDatabase.boolToDB(auction.getGlobal()),
              auction.getTime(),
              auction.getNode()
          }
      );
      sql().close();
    }
  }

  @Override
  public void deleteAuction(Auction auction) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_AUCTIONS WHERE auction_lot = ? ", new Object[] { auction.getLotNumber() });
      sql().close();
    }
  }

  @Override
  public List<Claim> loadClaims() {
    List<Claim> claims = new ArrayList<>();

    String table = prefix + "_CLAIMS";
    try {
      int claimIndex = sql().executeQuery("SELECT * FROM `" + table + "`;");
      while (sql().results(claimIndex).next()) {
        Claim claim = new Claim(
            UUID.fromString(sql().results(claimIndex).getString("claim_player")),
            sql().results(claimIndex).getInt("claim_lot"),
            SerializableItemStack.fromString(sql().results(claimIndex).getString("claim_item")),
            new TransactionCost(new BigDecimal(Double.valueOf(sql().results(claimIndex).getString("claim_cost"))))
        );
        claim.setPaid(SQLDatabase.boolFromDB(sql().results(claimIndex).getInt("claim_paid")));

        claims.add(claim);
      }
      sql().close();
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return claims;
  }

  @Override
  public Claim loadClaim(UUID owner, Integer lot) {
    String table = prefix + "_CLAIMS";
    try {
      int claimIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE claim_player = ? AND claim_lot = ?", new Object[] {
        owner.toString(),
        lot
      });
      if(sql().results(claimIndex).next()) {
        Claim claim = new Claim(//uuid, lot, item, cost
            UUID.fromString(sql().results(claimIndex).getString("claim_player")),
            sql().results(claimIndex).getInt("claim_lot"),
            SerializableItemStack.fromString(sql().results(claimIndex).getString("claim_item")),
            new TransactionCost(new BigDecimal(Double.valueOf(sql().results(claimIndex).getString("claim_cost"))))
        );
        claim.setPaid(SQLDatabase.boolFromDB(sql().results(claimIndex).getInt("claim_paid")));
        sql().close();
        return claim;
      }
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return null;
  }

  @Override
  public void saveClaim(Claim claim) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_CLAIMS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (claim_player, claim_lot, claim_item, claim_paid, claim_cost) VALUES(?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE claim_item = ?, claim_paid = ?, claim_cost = ?",
          new Object[] {
              claim.getPlayer().toString(),
              claim.getLot(),
              claim.getItem().toString(),
              SQLDatabase.boolToDB(claim.isPaid()),
              claim.getCost().getAmount(),
              claim.getItem().toString(),
              SQLDatabase.boolToDB(claim.isPaid()),
              claim.getCost().getAmount()
          });
      sql().close();
    }
  }

  @Override
  public void deleteClaim(Claim claim) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_CLAIMS WHERE claim_player = ? AND claim_lot = ?", new Object[] { claim.getLot(), claim.getPlayer().toString() });
      sql().close();
    }
  }

  @Override
  public Map<String, UUID> loadIDS() {
    Map<String, UUID> ids = new HashMap<>();

    String table = prefix + "_ECOIDS";
    try {
      int idIndex = sql().executeQuery("SELECT * FROM " + table + ";");
      while (sql().results(idIndex).next()) {
        ids.put(sql().results(idIndex).getString("username"), UUID.fromString(sql().results(idIndex).getString("uuid")));
      }
      sql().close();
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return ids;
  }

  @Override
  public UUID loadID(String username) {
    String table = prefix + "_TRANSACTIONS";
    try {
      int idIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE username = ?", new Object[] {
        username
      });
      if(sql().results(idIndex).next()) {
        UUID id = UUID.fromString(mysql().results(idIndex).getString("uuid"));
        sql().close();
        return id;
      }
    } catch(Exception e) {
      MISCUtils.debug(e);
    }
    return null;
  }

  @Override
  public void saveID(String username, UUID id) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_ECOIDS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (username, uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?",
          new Object[] {
              username,
              id.toString(),
              username
          });
      sql().close();
    }
  }

  @Override
  public void removeID(String username) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_ECOIDS WHERE username = ?", new Object[] { username });
      sql().close();
    }
  }

  @Override
  public void removeID(UUID id) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_ECOIDS WHERE uuid = ?", new Object[] { id.toString() });
      sql().close();
    }
  }

  @Override
  public void loadFlat(File file) {
    db = new FlatFile(TNE.instance().getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
    FlatFileConnection connection = (FlatFileConnection)db.connection();
    Section accounts = null;
    Section ids = null;
    Section shops = null;
    Section auctions = null;
    Section claims = null;
    Section signs = null;
    Section transactions = null;
    try {
      connection.getOIS().readDouble();
      accounts = (Section) connection.getOIS().readObject();
      ids = (Section) connection.getOIS().readObject();
      shops = (Section) connection.getOIS().readObject();
      auctions = (Section) connection.getOIS().readObject();
      claims = (Section) connection.getOIS().readObject();
      signs = (Section) connection.getOIS().readObject();
      transactions = (Section) connection.getOIS().readObject();
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    Iterator<Map.Entry<String, Article>> it = accounts.getArticle().entrySet().iterator();

    while(it.hasNext()) {
      Map.Entry<String, Article> entry = it.next();
      UUID uid = UUID.fromString(entry.getKey());
      Entry info = entry.getValue().getEntry("info");
      Entry balances = entry.getValue().getEntry("balances");
      Entry banks = entry.getValue().getEntry("banks");

      Account account = new Account(uid, (Integer) info.getData("accountnumber"));
      Map<String, BigDecimal> balanceMap = new HashMap<>();
      Map<String, Bank> bankMap = new HashMap<>();

      account.setAccountNumber((Integer) info.getData("accountnumber"));
      account.setStatus((String) info.getData("status"));
      account.setPin((String) info.getData("pin"));
      account.creditsFromString((String)info.getData("inventory_credits"));
      account.commandsFromString((String)info.getData("command_credits"));

      Iterator<Map.Entry<String, Object>> balanceIterator = balances.getData().entrySet().iterator();

      while(balanceIterator.hasNext()) {
        java.util.Map.Entry<String, Object> balanceEntry = balanceIterator.next();

        balanceMap.put(balanceEntry.getKey(), new BigDecimal((Double)balanceEntry.getValue()));
      }
      account.setBalances(balanceMap);

      Iterator<java.util.Map.Entry<String, Object>> bankIterator = banks.getData().entrySet().iterator();

      while(bankIterator.hasNext()) {
        java.util.Map.Entry<String, Object> bankEntry = bankIterator.next();

        //TODO: Convert old banks to new banks and vaults
        bankMap.put(bankEntry.getKey(), Bank.fromString((String)bankEntry.getValue(), bankEntry.getKey()));
      }
      account.setBanks(bankMap);

      TNE.instance().manager.accounts.put(uid, account);
    }

    Iterator<Map.Entry<String, Article>> idsIterator = ids.getArticle().entrySet().iterator();

    while(idsIterator.hasNext()) {
      Map.Entry<String, Article> idEntry = idsIterator.next();

      Entry info = idEntry.getValue().getEntry("info");

      TNE.instance().manager.ecoIDs.put((String)info.getData("username"), UUID.fromString((String)info.getData("uuid")));
    }

    Iterator<Map.Entry<String, Article>> shopsIterator = shops.getArticle().entrySet().iterator();
    while(shopsIterator.hasNext()) {
      Map.Entry<String, Article> shopEntry = shopsIterator.next();

      Entry info = shopEntry.getValue().getEntry("info");
      Shop s = new Shop(shopEntry.getKey(), (String)info.getData("world"));

      s.setOwner(UUID.fromString((String)info.getData("owner")));
      s.setHidden((boolean)info.getData("hidden"));
      s.setAdmin((boolean)info.getData("admin"));
      s.listFromString((String)info.getData("blacklist"), true);
      s.listFromString((String)info.getData("whitelist"), false);
      s.sharesFromString((String)info.getData("shares"));
      MISCUtils.debug("Items:" + info.getData("items"));

      if(!((String)info.getData("items")).trim().equals("")) {
        s.itemsFromString((String) info.getData("items"));
      }

      TNE.instance().manager.shops.put(shopEntry.getKey() + ":" + s.getWorld(), s);
    }

    for(Article a : auctions.getArticle().values()) {
      Entry info = a.getEntry("info");
      Auction auction = new Auction((int)info.getData("lot"));
      auction.setAdded((int)info.getData("added"));
      auction.setStartTime((int)info.getData("start"));
      auction.setPlayer(UUID.fromString((String)info.getData("player")));
      auction.setWorld((String)info.getData("world"));
      auction.setSilent((boolean)info.getData("silent"));
      auction.setItem(SerializableItemStack.fromString((String)info.getData("item")));
      auction.setCost(new TransactionCost(new BigDecimal((double)info.getData("cost"))));
      auction.setIncrement(new BigDecimal((double)info.getData("increment")));
      auction.setGlobal((boolean)info.getData("global"));
      auction.setTime((int)info.getData("time"));
      auction.setNode((String)info.getData("node"));

      TNE.instance().manager.auctionManager.add(auction);
    }

    for(Article a : claims.getArticle().values()) {
      Entry info = a.getEntry("info");

      Claim claim = new Claim(
          UUID.fromString((String)info.getData("player")),
          (int)info.getData("lot"),
          SerializableItemStack.fromString((String)info.getData("item")),
          new TransactionCost(new BigDecimal((double)info.getData("cost")))
      );
      claim.setPaid((boolean)info.getData("paid"));

      TNE.instance().manager.auctionManager.unclaimed.add(claim);
    }

    Iterator<Map.Entry<String, Article>> signsIterator = signs.getArticle().entrySet().iterator();
    while(signsIterator.hasNext()) {
      Map.Entry<String, Article> signEntry = signsIterator.next();
      Entry info = signEntry.getValue().getEntry("info");

      TNESign sign = TNESign.instance((String)info.getData("type"), UUID.fromString((String)info.getData("owner")), SerializableLocation.fromString((String)info.getData("location")));
      sign.loadMeta((String)info.getData("meta"));

      TNE.instance().manager.signs.put(sign.getLocation(), sign);
    }

    if(transactions != null) {
      for (Article a : transactions.getArticle().values()) {
        Entry info = a.getEntry("info");

        TNE.instance().manager.transactions.add(
            (String) info.getData("id"),
            (String) info.getData("initiator"),
            (String) info.getData("player"),
            (String) info.getData("world"),
            TransactionType.fromID((String) info.getData("type")),
            new TransactionCost(new BigDecimal((double) info.getData("cost"))),
            new BigDecimal((Double) info.getData("oldBalance")),
                new BigDecimal((Double) info.getData("balance")),
            (Long) info.getData("time")
        );
      }
    }
  }

  @Override
  public void saveFlat(File file) {
    //We no longer need to save 5.0 in 5.2 :3
  }

  @Override
  public void loadMySQL() {
    if(TNE.instance().saveManager.cache || TNE.instance().saveManager.updating) {
      Collection<Account> accounts = loadAccounts();
      Collection<Shop> shops = loadShops();
      Collection<Auction> auctions = loadAuctions();
      Collection<TNESign> signs = loadSigns();

      for(Account account : accounts) {
        TNE.instance().manager.accounts.put(account.getUid(), account, true);
      }

      for(Shop s : shops) {
        TNE.instance().manager.shops.put(s.getName() + ":" + s.getWorld(), s, true);
      }

      for(Auction auction : auctions) {
        TNE.instance().manager.auctionManager.auctionQueue.put(auction.getLotNumber(), auction, true);
      }

      for(TNESign sign : signs) {
        TNE.instance().manager.signs.put(sign.getLocation(), sign, true);
      }

      TNE.instance().manager.ecoIDs.putAll(loadIDS(), true);
      TNE.instance().manager.transactions.transactionHistory = loadTransactions();
      TNE.instance().manager.auctionManager.unclaimed.addAll(loadClaims(), true);
    }
  }

  @Override
  public void saveMySQL() {
    //We no longer need to save 5.0 in 5.2 :3
  }

  @Override
  public void loadH2() {
    if(TNE.instance().saveManager.cache || TNE.instance().saveManager.updating) {
      Collection<Account> accounts = loadAccounts();
      Collection<Shop> shops = loadShops();
      Collection<Auction> auctions = loadAuctions();
      Collection<TNESign> signs = loadSigns();

      for(Account account : accounts) {
        TNE.instance().manager.accounts.put(account.getUid(), account, true);
      }

      for(Shop s : shops) {
        TNE.instance().manager.shops.put(s.getName() + ":" + s.getWorld(), s, true);
      }

      for(Auction auction : auctions) {
        TNE.instance().manager.auctionManager.auctionQueue.put(auction.getLotNumber(), auction, true);
      }

      for(TNESign sign : signs) {
        TNE.instance().manager.signs.put(sign.getLocation(), sign, true);
      }

      TNE.instance().manager.ecoIDs.putAll(loadIDS(), true);
      TNE.instance().manager.transactions.transactionHistory = loadTransactions();
      TNE.instance().manager.auctionManager.unclaimed.addAll(loadClaims(), true);
    }
  }

  @Override
  public void saveH2() {
    //We no longer need to save 5.0 in 5.2 :3
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
              TNE.instance().getServer().getServerName(),
              versionNumber(),
              TNE.instance().getServer().getServerName()
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
              TNE.instance().getServer().getServerName(),
              versionNumber(),
              TNE.instance().getServer().getServerName()
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