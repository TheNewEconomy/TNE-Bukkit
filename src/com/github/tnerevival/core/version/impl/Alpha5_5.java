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
import com.github.tnerevival.account.TrackedItems;
import com.github.tnerevival.account.Vault;
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
import com.github.tnerevival.core.material.MaterialHelper;
import com.github.tnerevival.core.shops.ShareEntry;
import com.github.tnerevival.core.shops.Shop;
import com.github.tnerevival.core.shops.ShopEntry;
import com.github.tnerevival.core.signs.ItemSign;
import com.github.tnerevival.core.signs.SignType;
import com.github.tnerevival.core.signs.TNESign;
import com.github.tnerevival.core.signs.item.ItemEntry;
import com.github.tnerevival.core.transaction.Record;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.core.transaction.TransactionHistory;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.core.version.Version;
import com.github.tnerevival.serializable.SerializableItemStack;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by creatorfromhell on 1/17/2017.
 **/
public class Alpha5_5 extends Version {
  @Override
  public double versionNumber() {
    return 5.5;
  }

  @Override
  public void update(double version, String type) {
    if(version < 4.0) return;
    if(version >= 5.2 && version <= 5.4) {
      String table = prefix + "_SIGNS";
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `sign_location` VARCHAR(250) UNIQUE");
      table = prefix + "_SIGN_OFFERS";
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `sign_location` VARCHAR(250)");
      table = prefix + "_TRACKED";
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `location` VARCHAR(250)");
      table = prefix + "_BALANCES";
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `balance` VARCHAR(41)");
      table = prefix + "_BANK_BALANCES";
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `balance` VARCHAR(41)");
      table = prefix + "_TRANSACTIONS";
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `trans_cost` VARCHAR(41)");
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `trans_oldBalance` VARCHAR(41)");
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `trans_balance` VARCHAR(41)");
      table = prefix + "_SIGN_OFFERS";
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `offer_buy` VARCHAR(41)");
      sql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `offer_sell` VARCHAR(41)");
      return;
    }
    if(!type.equalsIgnoreCase("flatfile")) {
      //New Tables
      String table = prefix + "_BALANCES";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`server_name` VARCHAR(250) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`currency` VARCHAR(250) NOT NULL," +
          "`balance` VARCHAR(41)," +
          "PRIMARY KEY(uuid, server_name, world, currency)" +
          ");");

      table = prefix + "_TRACKED";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`material` LONGTEXT," +
          "`location` VARCHAR(250)," +
          "`slot` INT(60) NOT NULL," +
          "PRIMARY KEY(uuid, location, slot)" +
          ");");

      table = prefix + "_SHOP_SHARES";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`percentage` DOUBLE," +
          "PRIMARY KEY(shop_name, shop_world, uuid)" +
          ");");

      table = prefix + "_SHOP_PERMISSIONS";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(shop_name, shop_world, uuid)" +
          ");");

      table = prefix + "_SHOP_ITEMS";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`slot` INT(60) NOT NULL," +
          "`shop_entry` LONGTEXT," +
          "PRIMARY KEY(shop_name, shop_world, slot)" +
          ");");

      table = prefix + "_BANK_BALANCES";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`currency` VARCHAR(250) NOT NULL," +
          "`balance` VARCHAR(41)," +
          "PRIMARY KEY(uuid, world, currency)" +
          ");");

      table = prefix + "_BANK_MEMBERS";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(uuid, world, member)" +
          ");");

      table = prefix + "_VAULT_ITEMS";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`slot` INT(60) NOT NULL," +
          "`amount` INT(60) NOT NULL," +
          "`damage` INT(60) NOT NULL," +
          "`material` LONGTEXT," +
          "`custom_name` LONGTEXT," +
          "`enchantments` LONGTEXT," +
          "`lore` LONGTEXT," +
          "PRIMARY KEY(uuid, world, slot)" +
          ");");

      table = prefix + "_VAULT_MEMBERS";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(uuid, world, member)" +
          ");");

      table = prefix + "_SIGN_OFFERS";
      sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_location` VARCHAR(250)," +
          "`offer_order` INT(60) NOT NULL," +
          "`offer_buy` VARCHAR(40)," +
          "`offer_sell` VARCHAR(40)," +
          "`offer_trade` LONGTEXT," +
          "`offer_amount` INT(60) NOT NULL," +
          "`offer_damage` INT(60) NOT NULL," +
          "`offer_material` LONGTEXT," +
          "`offer_admin` BOOLEAN," +
          "PRIMARY KEY(sign_location, offer_order)" +
          ");");
      
      //Modifications
      table = prefix + "_USERS";
      sql().executeUpdate("ALTER TABLE `" + table + "` DROP COLUMN `balances`");
      sql().executeUpdate("ALTER TABLE `" + table + "` ADD COLUMN `account_special` BOOLEAN AFTER `accountstatus`");

      table = prefix + "_SHOPS";
      sql().executeUpdate("ALTER TABLE `" + table + "` DROP COLUMN `shop_items`," +
          "DROP COLUMN `shop_blacklist`," +
          "DROP COLUMN `shop_whitelist`," +
          "DROP COLUMN `shop_shares`");

      table = prefix + "_BANKS";
      sql().executeUpdate("ALTER TABLE `" + table + "` DROP COLUMN `bank`");

      table = prefix + "_SIGNS";
      mysql().executeUpdate("ALTER TABLE `" + table + "` MODIFY `sign_location` VARCHAR(250) UNIQUE");
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
            new BigDecimal(sql().results(transactionIndex).getString("trans_cost")),
            new BigDecimal(sql().results(transactionIndex).getString("trans_oldBalance")),
            new BigDecimal(sql().results(transactionIndex).getString("trans_balance")),
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
            new BigDecimal(sql().results(transactionIndex).getString("trans_cost")),
            new BigDecimal(sql().results(transactionIndex).getString("trans_oldBalance")),
            new BigDecimal(sql().results(transactionIndex).getString("trans_balance")),
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
              record.getCost().toPlainString(),
              record.getOldBalance().toPlainString(),
              record.getBalance().toPlainString(),
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
        account.setStatus(sql().results(accountIndex).getString("accountstatus"));
        account.setJoined(sql().results(accountIndex).getString("joinedDate"));
        account.creditsFromString(sql().results(accountIndex).getString("inventory_credits"));
        account.commandsFromString(sql().results(accountIndex).getString("command_credits"));
        account.setPin(sql().results(accountIndex).getString("acc_pin"));
        account.setSpecial(sql().results(accountIndex).getBoolean("account_special"));

        String balancesTable = prefix + "_BALANCES";
        int balancesIndex = sql().executePreparedQuery("SELECT * FROM " + balancesTable + " WHERE uuid = ?", new Object[] { account.getUid().toString() });
        while(sql().results(balancesIndex).next()) {
          account.setBalance(sql().results(balancesIndex).getString("world"), new BigDecimal(sql().results(balancesIndex).getString("balance")), sql().results(balancesIndex).getString("currency"));
        }

        table = prefix + "_BANKS";
        int bankIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?;", new Object[] { account.getUid().toString() });
        while(sql().results(bankIndex).next()) {
          Bank bank = new Bank(account.getUid(), sql().results(bankIndex).getString("world"));

          String extraTable = prefix + "_BANK_BALANCES";
          int extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE uuid = ? AND world = ?", new Object[] { account.getUid().toString(), bank.getWorld() });
          while(sql().results(extraIndex).next()) {
            bank.setGold(sql().results(extraIndex).getString("currency"), new BigDecimal(sql().results(extraIndex).getString("balance")));
          }
          extraTable = prefix + "_BANK_MEMBERS";
          extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE uuid = ? AND world = ?", new Object[] { account.getUid().toString(), bank.getWorld() });
          while(sql().results(extraIndex).next()) {
            bank.addMember(UUID.fromString(sql().results(extraIndex).getString("member")));
          }
          account.setBank(bank.getWorld(), bank);
        }

        table = prefix + "_VAULTS";
        int vaultIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?;", new Object[] { account.getUid().toString() });
        while(sql().results(vaultIndex).next()) {
          Vault vault = new Vault(account.getUid(), sql().results(vaultIndex).getString("world"), sql().results(vaultIndex).getInt("size"));

          String extraTable = prefix + "_VAULT_ITEMS";
          int extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE uuid = ? AND world = ?", new Object[] { account.getUid().toString(), vault.getWorld() });
          while(sql().results(extraIndex).next()) {
            int slot = sql().results(extraIndex).getInt("slot");
            ItemStack stack = new ItemStack(MaterialHelper.getMaterial(sql().results(extraIndex).getString("material")));
            stack.setDurability((short)sql().results(extraIndex).getInt("damage"));
            stack.setAmount(sql().results(extraIndex).getInt("amount"));
            SerializableItemStack item = new SerializableItemStack(slot, stack);
            item.setCustomName(sql().results(extraIndex).getString("custom_name"));
            item.addEnchantments(sql().results(extraIndex).getString("enchantments"));
            item.addLore(sql().results(extraIndex).getString("lore"));
          }

          extraTable = prefix + "_VAULT_MEMBERS";
          extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE uuid = ? AND world = ?", new Object[] { account.getUid().toString(), vault.getWorld() });
          while(sql().results(extraIndex).next()) {
            vault.addMember(UUID.fromString(sql().results(extraIndex).getString("member")));
          }
          account.setVault(vault.getWorld(), vault);
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
        account.setStatus(sql().results(accountIndex).getString("accountstatus"));
        account.setJoined(sql().results(accountIndex).getString("joinedDate"));
        account.creditsFromString(sql().results(accountIndex).getString("inventory_credits"));
        account.commandsFromString(sql().results(accountIndex).getString("command_credits"));
        account.setPin(sql().results(accountIndex).getString("acc_pin"));
        account.setSpecial(sql().results(accountIndex).getBoolean("account_special"));

        String balancesTable = prefix + "_BALANCES";
        int balancesIndex = sql().executePreparedQuery("SELECT * FROM " + balancesTable + " WHERE uuid = ?", new Object[] { account.getUid().toString() });
        while(sql().results(balancesIndex).next()) {
          account.setBalance(sql().results(balancesIndex).getString("world"), new BigDecimal(sql().results(balancesIndex).getString("balance")), sql().results(balancesIndex).getString("currency"));
        }

        table = prefix + "_BANKS";
        int bankIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?;", new Object[] { account.getUid().toString() });
        while(sql().results(bankIndex).next()) {
          Bank bank = new Bank(id, sql().results(bankIndex).getString("world"));

          String extraTable = prefix + "_BANK_BALANCES";
          int extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE uuid = ? AND world = ?", new Object[] { account.getUid().toString(), bank.getWorld() });
          while(sql().results(extraIndex).next()) {
            bank.setGold(sql().results(extraIndex).getString("currency"), new BigDecimal(sql().results(extraIndex).getString("balance")));
          }
          extraTable = prefix + "_BANK_MEMBERS";
          extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE uuid = ? AND world = ?", new Object[] { account.getUid().toString(), bank.getWorld() });
          while(sql().results(extraIndex).next()) {
            bank.addMember(UUID.fromString(sql().results(extraIndex).getString("member")));
          }
          account.setBank(bank.getWorld(), bank);
        }

        table = prefix + "_VAULTS";
        int vaultIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?;", new Object[] { account.getUid().toString() });
        while(sql().results(vaultIndex).next()) {
          Vault vault = new Vault(id, sql().results(vaultIndex).getString("world"), sql().results(vaultIndex).getInt("size"));

          String extraTable = prefix + "_VAULT_ITEMS";
          int extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE uuid = ? AND world = ?", new Object[] { account.getUid().toString(), vault.getWorld() });
          while(sql().results(extraIndex).next()) {
            int slot = sql().results(extraIndex).getInt("slot");
            ItemStack stack = new ItemStack(MaterialHelper.getMaterial(sql().results(extraIndex).getString("material")));
            stack.setDurability((short)sql().results(extraIndex).getInt("damage"));
            stack.setAmount(sql().results(extraIndex).getInt("amount"));
            SerializableItemStack item = new SerializableItemStack(slot, stack);
            item.setCustomName(sql().results(extraIndex).getString("custom_name"));
            item.addEnchantments(sql().results(extraIndex).getString("enchantments"));
            item.addLore(sql().results(extraIndex).getString("lore"));
          }

          extraTable = prefix + "_VAULT_MEMBERS";
          extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE uuid = ? AND world = ?", new Object[] { account.getUid().toString(), vault.getWorld() });
          while(sql().results(extraIndex).next()) {
            vault.addMember(UUID.fromString(sql().results(extraIndex).getString("member")));
          }
          account.setVault(vault.getWorld(), vault);
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
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, acc_pin, inventory_credits, command_credits, joinedDate, accountnumber, accountstatus, account_special) VALUES(?, ?, ?, ?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE acc_pin = ?, inventory_credits = ?, command_credits = ?, joinedDate = ?, accountnumber = ?, accountstatus = ?, account_special = ?",
          new Object[]{
              acc.getUid().toString(),
              acc.getPin(),
              acc.creditsToString(),
              acc.commandsToString(),
              acc.getJoined(),
              acc.getAccountNumber(),
              acc.getStatus().getName(),
              acc.isSpecial(),
              acc.getPin(),
              acc.creditsToString(),
              acc.commandsToString(),
              acc.getJoined(),
              acc.getAccountNumber(),
              acc.getStatus().getName(),
              acc.isSpecial(),
          }
      );

      table = prefix + "_BALANCES";
      for (String s : acc.getBalances().keySet()) {
        String[] parts = s.split(":");
        String amount = acc.getBalance(parts[0], parts[1]).toPlainString();
        sql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, server_name, world, currency, balance) " +
                "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?",
            new Object[]{
                acc.getUid().toString(),
                TNE.instance().getServer().getServerName(),
                parts[0],
                parts[1],
                amount,
                amount
            }
        );
      }

      table = prefix + "_BANKS";
      for (Map.Entry<String, Bank> entry : acc.getBanks().entrySet()) {
        sql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, world) VALUES(?, ?)",
            new Object[]{
                acc.getUid().toString(),
                entry.getKey()
            }
        );

        sql().executePreparedUpdate("DELETE FROM " + prefix + "_BANK_BALANCES WHERE uuid = ? ", new Object[] { acc.getUid().toString() });
        for (String s : entry.getValue().getBalances().keySet()) {
          sql().executePreparedUpdate("INSERT INTO `" + prefix + "_BANK_BALANCES` (uuid, world, currency, balance)" +
                  " VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?",
              new Object[]{
                  acc.getUid().toString(),
                  entry.getKey(),
                  s,
                  entry.getValue().getGold(s).toPlainString(),
                  entry.getValue().getGold(s).toPlainString()
              }
          );
        }
        sql().executePreparedUpdate("DELETE FROM " + prefix + "_BANK_MEMBERS WHERE uuid = ? ", new Object[] { acc.getUid().toString() });
        for (UUID id : entry.getValue().getMembers()) {
          sql().executePreparedUpdate("INSERT INTO `" + prefix + "_BANK_MEMBERS` (uuid, world, member, permissions)" +
                  " VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE permissions = ?",
              new Object[]{
                  acc.getUid().toString(),
                  entry.getKey(),
                  id.toString(),
                  "",
                  ""
              }
          );
        }
      }

      table = prefix + "_VAULTS";
      for (Map.Entry<String, Vault> entry : acc.getVaults().entrySet()) {
        sql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, world, size) VALUES(?, ?, ?) ON DUPLICATE" +
                "KEY UPDATE size = ?",
            new Object[]{
                acc.getUid().toString(),
                entry.getKey(),
                entry.getValue().getSize(),
                entry.getValue().getSize()
            }
        );

        sql().executePreparedUpdate("DELETE FROM " + prefix + "_VAULT_ITEMS WHERE uuid = ? ", new Object[] { acc.getUid().toString() });
        for (SerializableItemStack stack : entry.getValue().getItems()) {
          sql().executePreparedUpdate("INSERT INTO `" + prefix + "_VAULT_ITEMS` (uuid, world, slot, amount, " +
                  "damage, material, custom_name, enchantments, lore) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE " +
                  "KEY UPDATE amount = ?, damage = ?, material = ?, custom_name = ?, enchantments = ?, lore = ?",
              new Object[]{
                  acc.getUid().toString(),
                  entry.getKey(),
                  stack.getSlot(),
                  stack.getAmount(),
                  stack.getDamage(),
                  stack.getName(),
                  stack.getCustomName(),
                  stack.enchantmentsToString(),
                  stack.loreToString(),
                  stack.getAmount(),
                  stack.getDamage(),
                  stack.getName(),
                  stack.getCustomName(),
                  stack.enchantmentsToString(),
                  stack.loreToString(),
              }
          );
        }

        sql().executePreparedUpdate("DELETE FROM " + prefix + "_VAULT_MEMBERS WHERE uuid = ? ", new Object[] { acc.getUid().toString() });
        for (UUID id : entry.getValue().getMembers()) {
          sql().executePreparedUpdate("INSERT INTO `" + prefix + "_VAULT_MEMBERS` (uuid, world, member, permissions)" +
                  " VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE permissions = ?",
              new Object[]{
                  acc.getUid().toString(),
                  entry.getKey(),
                  id.toString(),
                  "",
                  ""
              }
          );
        }
      }
      sql().close();
    }
  }

  @Override
  public void deleteAccount(UUID id) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_USERS WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_BALANCES WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_TRACKED WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_BANKS WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_BANK_BALANCES WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_BANK_MEMBERS WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_VAULTS WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_VAULT_MEMBERS WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_VAULT_ITEMS WHERE uuid = ? ", new Object[] { id.toString() });
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

        String extraTable = prefix + "_SHOP_SHARES";
        int extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE shop_name = ? AND shop_world = ?", new Object[] {
            s.getName(),
            s.getWorld()
        });
        while(sql().results(extraIndex).next()) {
          ShareEntry share = new ShareEntry(UUID.fromString(sql().results(extraIndex).getString("uuid")),
              sql().results(extraIndex).getDouble("percentage"));
          s.addShares(share);
        }

        extraTable = prefix + "_SHOP_PERMISSIONS";
        extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE shop_name = ? AND shop_world = ?", new Object[] {
            s.getName(),
            s.getWorld()
        });
        while(sql().results(extraIndex).next()) {
          s.permissionFromString(UUID.fromString(sql().results(extraIndex).getString("uuid")),
              sql().results(extraIndex).getString("permissions"));
        }

        extraTable = prefix + "SHOP_ITEMS";
        extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE shop_name = ? AND shop_world = ?", new Object[] {
            s.getName(),
            s.getWorld()
        });
        while(sql().results(extraIndex).next()) {
          ShopEntry entry = ShopEntry.fromString(sql().results(extraIndex).getString("shop_entry"));
          s.addItem(entry);
        }

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

        String extraTable = prefix + "_SHOP_SHARES";
        int extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE shop_name = ? AND shop_world = ?", new Object[] {
            name,
            world
        });
        while(sql().results(extraIndex).next()) {
          ShareEntry share = new ShareEntry(UUID.fromString(sql().results(extraIndex).getString("uuid")),
              sql().results(extraIndex).getDouble("percentage"));
          s.addShares(share);
        }

        extraTable = prefix + "_SHOP_PERMISSIONS";
        extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE shop_name = ? AND shop_world = ?", new Object[] {
            name,
            world
        });
        while(sql().results(extraIndex).next()) {
          s.permissionFromString(UUID.fromString(sql().results(extraIndex).getString("uuid")),
              sql().results(extraIndex).getString("permissions"));
        }

        extraTable = prefix + "_SHOP_ITEMS";
        extraIndex = sql().executePreparedQuery("SELECT * FROM " + extraTable + " WHERE shop_name = ? AND shop_world = ?", new Object[] {
            name,
            world
        });
        while(sql().results(extraIndex).next()) {
          ShopEntry entry = ShopEntry.fromString(sql().results(extraIndex).getString("shop_entry"));
          s.addItem(entry);
        }
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
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (shop_name, shop_world, shop_owner, shop_hidden, shop_admin) VALUES(?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE shop_owner = ?, shop_hidden = ?, shop_admin = ?",
          new Object[]{
              shop.getName(),
              shop.getWorld(),
              shop.getOwner().toString(),
              SQLDatabase.boolToDB(shop.isHidden()),
              SQLDatabase.boolToDB(shop.isAdmin()),
              shop.getOwner().toString(),
              SQLDatabase.boolToDB(shop.isHidden()),
              SQLDatabase.boolToDB(shop.isAdmin()),
          }
      );

      for(UUID id : shop.getPermissions().keySet()) {
        String permissions = prefix + "_SHOP_PERMISSIONS";
        sql().executePreparedUpdate("INSERT INTO `" + permissions + "` (shop_world, shop_name, uuid, permissions) VALUES(?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE permissions = ?",
            new Object[]{
                shop.getWorld(),
                shop.getName(),
                id.toString(),
                shop.permissionToString(id),
                shop.permissionToString(id)
            }
        );
      }

      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SHOP_ITEMS WHERE shop_name = ? AND shop_world = ?", new Object[] { shop.getName(), shop.getWorld() });
      for(ShopEntry entry : shop.getItems()) {
        String entries = prefix + "_SHOP_ITEMS";
        sql().executePreparedUpdate("INSERT INTO `" + entries + "` (shop_world, shop_name, slot, shop_entry) VALUES(?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE shop_entry = ?",
            new Object[]{
                shop.getWorld(),
                shop.getName(),
                entry.getItem().getSlot(),
                entry.toString(),
                entry.toString(),
            }
        );
      }

      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SHOP_SHARES WHERE shop_name = ? AND shop_world = ?", new Object[] { shop.getName(), shop.getWorld() });
      for(ShareEntry share : shop.getShares()) {
        String shares = prefix + "_SHOP_SHARES";
        sql().executePreparedUpdate("INSERT INTO `" + shares + "` (shop_world, shop_name, uuid, percentage) VALUES(?, ?, ?, ?)" +
                " ON DUPLICATE KEY UPDATE percentage = ?",
            new Object[]{
                shop.getWorld(),
                shop.getName(),
                share.getShareOwner().toString(),
                share.getPercent(),
                share.getPercent()
            }
        );
      }
      sql().close();
    }
  }

  @Override
  public void deleteShop(Shop shop) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SHOPS WHERE shop_name = ? AND shop_world = ?", new Object[] { shop.getName(), shop.getWorld() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SHOP_SHARES WHERE shop_name = ? AND shop_world = ?", new Object[] { shop.getName(), shop.getWorld() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SHOP_PERMISSIONS WHERE shop_name = ? AND shop_world = ?", new Object[] { shop.getName(), shop.getWorld() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SHOP_ITEMS WHERE shop_name = ? AND shop_world = ?", new Object[] { shop.getName(), shop.getWorld() });
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

        if(sign.getType().equals(SignType.ITEM)) {
          String offers = prefix + "_SIGN_OFFERS";
          int offersIndex = sql().executePreparedQuery("SELECT * FROM " + offers + " WHERE sign_location = ?", new Object[] {
              sign.getLocation().toString()
          });

          while(sql().results(offersIndex).next()) {
            int order = sql().results(offersIndex).getInt("offer_order");
            ItemStack stack = new ItemStack(MaterialHelper.getMaterial(sql().results(offersIndex).getString("offer_material")));
            stack.setDurability((short)sql().results(offersIndex).getInt("offer_damage"));
            stack.setAmount(sql().results(offersIndex).getInt("offer_amount"));

            ItemEntry entry = new ItemEntry(order, stack);
            entry.setBuy(new BigDecimal(sql().results(offersIndex).getString("offer_buy")));
            entry.setSell(new BigDecimal(sql().results(offersIndex).getString("offer_sell")));
            entry.setAdmin(sql().results(offersIndex).getBoolean("offer_admin"));
            String trade = sql().results(offersIndex).getString("offer_trade");
            if(!trade.trim().equals("")) {
              entry.setTrade(SerializableItemStack.fromString(trade).toItemStack());
            }
            ((ItemSign)sign).offers.put(entry.getOrder(), entry);
          }
        }
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

        if(sign.getType().equals(SignType.ITEM)) {
          String offers = prefix + "_SIGN_OFFERS";
          int offersIndex = sql().executePreparedQuery("SELECT * FROM " + offers + " WHERE sign_location = ?", new Object[] {
              sign.getLocation().toString()
          });

          while(sql().results(offersIndex).next()) {
            int order = sql().results(offersIndex).getInt("offer_order");
            ItemStack stack = new ItemStack(MaterialHelper.getMaterial(sql().results(offersIndex).getString("offer_material")));
            stack.setDurability((short)sql().results(offersIndex).getInt("offer_damage"));
            stack.setAmount(sql().results(offersIndex).getInt("offer_amount"));

            ItemEntry entry = new ItemEntry(order, stack);
            entry.setBuy(new BigDecimal(sql().results(offersIndex).getString("offer_buy")));
            entry.setSell(new BigDecimal(sql().results(offersIndex).getString("offer_sell")));
            entry.setAdmin(sql().results(offersIndex).getBoolean("offer_admin"));
            String trade = sql().results(offersIndex).getString("offer_trade");
            if(!trade.trim().equals("")) {
              entry.setTrade(SerializableItemStack.fromString(trade).toItemStack());
            }
            ((ItemSign)sign).offers.put(entry.getOrder(), entry);
          }
        }
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

      if(sign.getType().equals(SignType.ITEM)) {
        sql().executePreparedUpdate("DELETE FROM " + prefix + "_SIGN_OFFERS WHERE sign_location = ?", new Object[] { sign.getLocation().toString() });
        for(ItemEntry entry : ((ItemSign)sign).offers.values()) {
          String offers = prefix + "_SIGN_OFFERS";
          sql().executePreparedUpdate("INSERT INTO `" + offers + "` (sign_location, offer_order, offer_buy, " +
                  "offer_sell, offer_trade, offer_amount, offer_damage, offer_material, offer_admin) " +
                  "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE offer_buy = ?, offer_sell = ?, " +
                  "offer_trade = ?, offer_amount = ?, offer_damage = ?, offer_material = ?, offer_admin = ?",
              new Object[]{
                  sign.getLocation().toString(),
                  entry.getOrder(),
                  entry.getBuy().toPlainString(),
                  entry.getSell().toPlainString(),
                  (entry.getTrade().getType().equals(Material.AIR))? "" : new SerializableItemStack(entry.getOrder(), entry.getTrade()).toString(),
                  entry.getItem().getAmount(),
                  entry.getItem().getDurability(),
                  entry.getItem().getType().name(),
                  entry.isAdmin(),
                  entry.getBuy().toPlainString(),
                  entry.getSell().toPlainString(),
                  (entry.getTrade().getType().equals(Material.AIR))? "" : new SerializableItemStack(entry.getOrder(), entry.getTrade()).toString(),
                  entry.getItem().getAmount(),
                  entry.getItem().getDurability(),
                  entry.getItem().getType().name(),
                  entry.isAdmin(),
              }
          );
        }
      }
      sql().close();
    }
  }

  @Override
  public void deleteSign(TNESign sign) {
    if(!TNE.instance().saveManager.type.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_SIGNS WHERE sign_location = ?", new Object[] { sign.getLocation().toString() });

      if(sign.getType().equals(SignType.ITEM)) {
        sql().executePreparedUpdate("DELETE FROM " + prefix + "_SIGN_OFFERS WHERE sign_location = ?", new Object[] { sign.getLocation().toString() });
      }

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
              auction.getCost().getAmount().toPlainString(),
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
              auction.getCost().getAmount().toPlainString(),
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
              claim.getCost().getAmount().toPlainString()
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
    String table = prefix + "_ECOID";
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
      Entry vaults = entry.getValue().getEntry("vaults");
      Entry tracked = entry.getValue().getEntry("tracked");

      Account account = new Account(uid, (Integer) info.getData("accountnumber"));
      Map<String, BigDecimal> balanceMap = new HashMap<>();
      Map<String, Bank> bankMap = new HashMap<>();
      Map<String, Vault> vaultMap = new HashMap<>();
      Map<Location, TrackedItems> trackedMap = new HashMap<>();

      account.setAccountNumber((Integer) info.getData("accountnumber"));
      account.setJoined((String)info.getData("joined"));
      account.setStatus((String) info.getData("status"));
      account.setPin((String) info.getData("pin"));
      account.setSpecial((Boolean)info.getData("special"));
      account.creditsFromString((String)info.getData("inventory_credits"));
      account.commandsFromString((String)info.getData("command_credits"));

      Iterator<Map.Entry<String, Object>> balanceIterator = balances.getData().entrySet().iterator();

      while(balanceIterator.hasNext()) {
        java.util.Map.Entry<String, Object> balanceEntry = balanceIterator.next();

        balanceMap.put(balanceEntry.getKey(), new BigDecimal((String)balanceEntry.getValue()));
      }
      account.setBalances(balanceMap);

      Iterator<java.util.Map.Entry<String, Object>> bankIterator = banks.getData().entrySet().iterator();

      while(bankIterator.hasNext()) {
        java.util.Map.Entry<String, Object> bankEntry = bankIterator.next();
        bankMap.put(bankEntry.getKey(), Bank.fromString((String)bankEntry.getValue()));
      }
      account.setBanks(bankMap);

      Iterator<java.util.Map.Entry<String, Object>> vaultIterator = vaults.getData().entrySet().iterator();

      while(vaultIterator.hasNext()) {
        java.util.Map.Entry<String, Object> vaultEntry = vaultIterator.next();
        vaultMap.put(vaultEntry.getKey(), Vault.fromString((String)vaultEntry.getValue()));
      }
      account.setVaults(vaultMap);

      Iterator<java.util.Map.Entry<String, Object>> trackedIterator = tracked.getData().entrySet().iterator();

      while(trackedIterator.hasNext()) {
        java.util.Map.Entry<String, Object> trackEntry = trackedIterator.next();
        Location location = SerializableLocation.fromString(trackEntry.getKey()).getLocation();
        TrackedItems items = new TrackedItems(location);
        items.materialsFromString((String)trackEntry.getValue());
        trackedMap.put(location, items);
      }
      account.setTrackedItems(trackedMap);

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
      s.permissionsFromString((String)info.getData("permissions"));
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
      auction.setCost(new TransactionCost(new BigDecimal((String)info.getData("cost"))));
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
          new TransactionCost(new BigDecimal((String) info.getData("cost")))
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

      if(signs.hasArticle(sign.getLocation().toString() + "-offers")) {
        Article offers = signs.getArticle(sign.getLocation().toString() + "-offers");

        for(Entry entry : offers.getEntries().values()) {
          ItemStack stack = new ItemStack(MaterialHelper.getMaterial((String)entry.getData("material")));
          stack.setAmount((int)entry.getData("amount"));
          stack.setDurability((short)entry.getData("damage"));
          ItemEntry offer = new ItemEntry((int)entry.getData("order"), stack);
          offer.setAdmin(SQLDatabase.boolFromDB((int)entry.getData("admin")));
          offer.setBuy(new BigDecimal((String) entry.getData("buy")));
          offer.setSell(new BigDecimal((String) entry.getData("sell")));
          if(!((String)entry.getData("trade")).equals("")) {
            SerializableItemStack trade = SerializableItemStack.fromString((String)entry.getData("trade"));
            offer.setTrade(trade.toItemStack());
          }
          ((ItemSign)sign).offers.put(offer.getOrder(), offer);
        }
      }

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
            new TransactionCost(new BigDecimal((String) info.getData("cost"))),
            new BigDecimal((String) info.getData("oldBalance")),
            new BigDecimal((String) info.getData("balance")),
            (Long) info.getData("time")
        );
      }
    }
  }

  @Override
  public void saveFlat(File file) {
    Iterator<java.util.Map.Entry<UUID, Account>> accIT = TNE.instance().manager.accounts.entrySet().iterator();

    Section accounts = new Section("accounts");

    while(accIT.hasNext()) {
      java.util.Map.Entry<UUID, Account> entry = accIT.next();

      Account acc = entry.getValue();
      Article account = new Article(entry.getKey().toString());
      //Info
      Entry info = new Entry("info");
      info.addData("accountnumber", acc.getAccountNumber());
      info.addData("uuid", acc.getUid());
      info.addData("joined", acc.getJoined());
      info.addData("status", acc.getStatus().getName());
      info.addData("inventory_credits", acc.creditsToString());
      info.addData("command_credits", acc.commandsToString());
      info.addData("pin", acc.getPin());
      info.addData("special", acc.isSpecial());
      account.addEntry(info);

      //Balances
      Entry balances = new Entry("balances");
      Iterator<Map.Entry<String, BigDecimal>> balIT = acc.getBalances().entrySet().iterator();

      while(balIT.hasNext()) {
        java.util.Map.Entry<String, BigDecimal> balanceEntry = balIT.next();
        balances.addData(balanceEntry.getKey(), balanceEntry.getValue().toPlainString());
      }
      account.addEntry(balances);

      //Banks
      Entry banks = new Entry("banks");
      Iterator<java.util.Map.Entry<String, Bank>> bankIT = acc.getBanks().entrySet().iterator();

      while(bankIT.hasNext()) {
        java.util.Map.Entry<String, Bank> bankEntry = bankIT.next();
        banks.addData(bankEntry.getKey(), bankEntry.getValue().toString());
      }
      account.addEntry(banks);

      //Vaults
      Entry vaults = new Entry("vaults");
      Iterator<Map.Entry<String, Vault>> vaultIT = acc.getVaults().entrySet().iterator();

      while(vaultIT.hasNext()) {
        Map.Entry<String, Vault> vaultEntry = vaultIT.next();
        vaults.addData(vaultEntry.getValue().getWorld(), vaultEntry.getValue().toString());
      }
      account.addEntry(vaults);

      //Tracked Items
      Entry tracked = new Entry("tracked");
      Iterator<Map.Entry<Location, TrackedItems>> trackedIT = acc.getTrackedItems().entrySet().iterator();

      while(trackedIT.hasNext()) {
        Map.Entry<Location, TrackedItems> trackedEntry = trackedIT.next();
        tracked.addData(new SerializableLocation(trackedEntry.getKey()).toString(), trackedEntry.getValue().materialsToString());
      }
      account.addEntry(tracked);
      accounts.addArticle(entry.getKey().toString(), account);
    }

    Iterator<Map.Entry<String, UUID>> idsIT = TNE.instance().manager.ecoIDs.entrySet().iterator();

    Section ids = new Section("IDS");

    while(idsIT.hasNext()) {
      Map.Entry<String, UUID> idEntry = idsIT.next();

      Article a = new Article(idEntry.getKey());
      Entry e = new Entry("info");

      e.addData("username", idEntry.getKey());
      e.addData("uuid", idEntry.getValue().toString());
      a.addEntry(e);

      ids.addArticle(idEntry.getKey(), a);
    }

    Iterator<Map.Entry<String, Shop>> shopIT = TNE.instance().manager.shops.entrySet().iterator();
    Section shops = new Section("SHOPS");

    while(shopIT.hasNext()) {
      Map.Entry<String, Shop> shopEntry = shopIT.next();
      Shop s = shopEntry.getValue();

      Article a = new Article(s.getName());
      Entry info = new Entry("info");

      info.addData("owner", s.getOwner().toString());
      info.addData("world", s.getWorld());
      info.addData("hidden", s.isHidden());
      info.addData("admin", s.isAdmin());
      MISCUtils.debug("Items:" + s.itemsToString());
      info.addData("items", s.itemsToString());
      info.addData("permissions", s.permissionsToString());
      info.addData("shares", s.sharesToString());
      a.addEntry(info);

      shops.addArticle(s.getName(), a);
    }

    Section auctions = new Section("AUCTIONS");
    for(Auction auction : TNE.instance().manager.auctionManager.getJoined()) {
      Article a = new Article(auction.getLotNumber() + "");
      Entry info = new Entry("info");
      info.addData("lot", auction.getLotNumber());
      info.addData("added", auction.getAdded());
      info.addData("start", auction.getStartTime());
      info.addData("player", auction.getPlayer().toString());
      info.addData("world", auction.getWorld());
      info.addData("silent", auction.getSilent());
      info.addData("item", auction.getItem().toString());
      info.addData("cost", auction.getCost().getAmount().toPlainString());
      info.addData("increment", auction.getIncrement());
      info.addData("global", auction.getGlobal());
      info.addData("time", auction.getTime());
      info.addData("node", auction.getNode());
      a.addEntry(info);
      auctions.addArticle(auction.getLotNumber() + "", a);
    }

    Section claims = new Section("CLAIMS");
    for(Claim claim : TNE.instance().manager.auctionManager.unclaimed) {
      Article a = new Article(claim.getLot() + "");
      Entry info = new Entry("info");
      info.addData("player", claim.getPlayer().toString());
      info.addData("lot", claim.getLot());
      info.addData("item", claim.getItem().toString());
      info.addData("paid", claim.isPaid());
      info.addData("cost", claim.getCost().getAmount().toPlainString());
      a.addEntry(info);
      claims.addArticle(claim.getLot() + "", a);
    }

    Iterator<Map.Entry<SerializableLocation, TNESign>> signIT = TNE.instance().manager.signs.entrySet().iterator();
    Section signs = new Section("SIGNS");

    while(signIT.hasNext()) {
      Map.Entry<SerializableLocation, TNESign> signEntry = signIT.next();
      TNESign sign = signEntry.getValue();

      Article a = new Article(sign.getLocation().toString());
      Entry info = new Entry("info");

      info.addData("owner", sign.getOwner().toString());
      info.addData("type", sign.getType().getName());
      info.addData("extra", sign.getMeta());
      info.addData("location", sign.getLocation().toString());
      a.addEntry(info);

      if(sign.getType().equals(SignType.ITEM)) {
        Article offers = new Article(sign.getLocation().toString() + "-offers");

        for(ItemEntry entry : ((ItemSign)sign).offers.values()) {
          Entry offer = new Entry(entry.getOrder() + "");
          offer.addData("order", entry.getOrder());
          offer.addData("location", sign.getLocation().toString());
          offer.addData("buy", entry.getBuy().toPlainString());
          offer.addData("sell", entry.getSell().toPlainString());
          String trade = (entry.getTrade().getType().equals(Material.AIR))? "" : new SerializableItemStack(entry.getOrder(), entry.getTrade()).toString();
          offer.addData("trade", trade);
          offer.addData("amount", entry.getItem().getAmount());
          offer.addData("damage", entry.getItem().getDurability());
          offer.addData("material", entry.getItem().getType().toString());
          offer.addData("admin", SQLDatabase.boolToDB(entry.isAdmin()));
          offers.addEntry(offer);
        }
        signs.addArticle(offers.getName(), offers);
      }

      signs.addArticle(sign.getLocation().toString(), a);
    }

    Section transactions = new Section("TRANSACTIONS");
    for(Map.Entry<String, TransactionHistory> entry : TNE.instance().manager.transactions.transactionHistory.entrySet()) {
      for(Record r : entry.getValue().getRecords()) {
        Article a = new Article(r.getId());
        Entry info = new Entry("info");
        info.addData("id", r.getId());
        info.addData("initiator", r.getInitiator());
        info.addData("player", r.getPlayer());
        info.addData("world", r.getWorld());
        info.addData("type", r.getType());
        info.addData("cost", r.getCost().toPlainString());
        info.addData("oldBalance", r.getOldBalance().toPlainString());
        info.addData("balance", r.getBalance().toPlainString());
        info.addData("time", r.getTime());
        a.addEntry(info);
        transactions.addArticle(r.getId(), a);
      }
    }

    try {
      db = new FlatFile(TNE.instance().getDataFolder() + File.separator + TNE.configurations.getString("Core.Database.FlatFile.File"));
      FlatFileConnection connection = (FlatFileConnection)db.connection();
      connection.getOOS().writeDouble(versionNumber());
      connection.getOOS().writeObject(accounts);
      connection.getOOS().writeObject(ids);
      connection.getOOS().writeObject(shops);
      connection.getOOS().writeObject(auctions);
      connection.getOOS().writeObject(claims);
      connection.getOOS().writeObject(signs);
      connection.getOOS().writeObject(transactions);
      connection.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void loadMySQL() {
    if(TNE.instance().saveManager.cache) {
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
    createTables("mysql");
    String table = prefix + "_INFO";
    db = new MySQL(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword);
    mysql().executePreparedUpdate("Update " + table + " SET version = ?, server_name = ? WHERE id = 1;", new Object[] { String.valueOf(versionNumber()), TNE.instance().getServer().getServerName() });

    if(TNE.instance().saveManager.cache) {
      if(TNE.instance() != null) {
        TNE.instance().cacheWorker.run();
      } else {
        for (Account acc : TNE.instance().manager.accounts.values()) {
          saveAccount(acc);
        }

        for (Map.Entry<String, UUID> entry : TNE.instance().manager.ecoIDs.entrySet()) {
          saveID(entry.getKey(), entry.getValue());
        }

        for (Shop s : TNE.instance().manager.shops.values()) {
          saveShop(s);
        }

        for (Auction auction : TNE.instance().manager.auctionManager.getJoined()) {
          saveAuction(auction);
        }

        for (Claim claim : TNE.instance().manager.auctionManager.unclaimed) {
          saveClaim(claim);
        }

        for (TNESign sign : TNE.instance().manager.signs.values()) {
          saveSign(sign);
        }

        for (Map.Entry<String, TransactionHistory> entry : TNE.instance().manager.transactions.transactionHistory.entrySet()) {
          for (Record r : entry.getValue().getRecords()) {
            saveTransaction(r);
          }
        }
      }
    }
    mysql().close();
  }

  @Override
  public void loadH2() {
    if(TNE.instance().saveManager.cache) {
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
    createTables("h2");
    String table = prefix + "_INFO";
    db = new H2(h2File, mysqlUser, mysqlPassword);
    h2().executePreparedUpdate("Update " + table + " SET version = ? WHERE id = 1;", new Object[] { String.valueOf(versionNumber()) });

    if(TNE.instance().saveManager.cache) {
      if (TNE.instance().cacheWorker != null) {
        TNE.instance().cacheWorker.run();
      } else {
        for (Account acc : TNE.instance().manager.accounts.values()) {
          saveAccount(acc);
        }

        for (Map.Entry<String, UUID> entry : TNE.instance().manager.ecoIDs.entrySet()) {
          saveID(entry.getKey(), entry.getValue());
        }

        for (Shop s : TNE.instance().manager.shops.values()) {
          saveShop(s);
        }

        for (Auction auction : TNE.instance().manager.auctionManager.getJoined()) {
          saveAuction(auction);
        }

        for (Claim claim : TNE.instance().manager.auctionManager.unclaimed) {
          saveClaim(claim);
        }

        for (TNESign sign : TNE.instance().manager.signs.values()) {
          saveSign(sign);
        }

        for (Map.Entry<String, TransactionHistory> entry : TNE.instance().manager.transactions.transactionHistory.entrySet()) {
          for (Record r : entry.getValue().getRecords()) {
            saveTransaction(r);
          }
        }
      }
    }

    h2().close();
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
          "`joinedDate` VARCHAR(60)," +
          "`accountnumber` INTEGER," +
          "`accountstatus` VARCHAR(60)," +
          "`account_special` BOOLEAN" +
          ");");

      table = prefix + "_BALANCES";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`server_name` VARCHAR(250) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`currency` VARCHAR(250) NOT NULL," +
          "`balance` VARCHAR(41)," +
          "PRIMARY KEY(uuid, server_name, world, currency)" +
          ");");

      table = prefix + "_TRACKED";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`material` LONGTEXT," +
          "`location` VARCHAR(250)," +
          "`slot` INT(60) NOT NULL," +
          "PRIMARY KEY(uuid, location, slot)" +
          ");");

      table = prefix + "_SHOPS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_owner` VARCHAR(36)," +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`shop_hidden` TINYINT(1)," +
          "`shop_admin` TINYINT(1)," +
          "PRIMARY KEY(shop_name, shop_world)" +
          ");");

      table = prefix + "_SHOP_SHARES";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`percentage` DOUBLE," +
          "PRIMARY KEY(shop_name, shop_world, uuid)" +
          ");");

      table = prefix + "_SHOP_PERMISSIONS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(shop_name, shop_world, uuid)" +
          ");");

      table = prefix + "_SHOP_ITEMS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`slot` INT(60) NOT NULL," +
          "`shop_entry` LONGTEXT," +
          "PRIMARY KEY(shop_name, shop_world, slot)" +
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
          "PRIMARY KEY(uuid, world)" +
          ");");

      table = prefix + "_BANK_BALANCES";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`currency` VARCHAR(250) NOT NULL," +
          "`balance` VARCHAR(41)," +
          "PRIMARY KEY(uuid, world, currency)" +
          ");");

      table = prefix + "_BANK_MEMBERS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(uuid, world, member)" +
          ");");

      table = prefix + "_VAULTS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`size` INT(60) NOT NULL," +
          "PRIMARY KEY(uuid, world)" +
          ");");

      table = prefix + "_VAULT_ITEMS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`slot` INT(60) NOT NULL," +
          "`amount` INT(60) NOT NULL," +
          "`damage` INT(60) NOT NULL," +
          "`material` LONGTEXT," +
          "`custom_name` LONGTEXT," +
          "`enchantments` LONGTEXT," +
          "`lore` LONGTEXT," +
          "PRIMARY KEY(uuid, world, slot)" +
          ");");

      table = prefix + "_VAULT_MEMBERS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(uuid, world, member)" +
          ");");

      table = prefix + "_SIGNS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_owner` VARCHAR(36)," +
          "`sign_type` VARCHAR(30) NOT NULL," +
          "`sign_location` VARCHAR(250)  NOT NULL UNIQUE," +
          "`sign_meta` LONGTEXT" +
          ");");

      table = prefix + "_SIGN_OFFERS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_location` VARCHAR(250) NOT NULL," +
          "`offer_order` INT(60) NOT NULL," +
          "`offer_buy` VARCHAR(41)," +
          "`offer_sell` VARCHAR(41)," +
          "`offer_trade` LONGTEXT," +
          "`offer_amount` INT(60) NOT NULL," +
          "`offer_damage` INT(60) NOT NULL," +
          "`offer_material` LONGTEXT," +
          "`offer_admin` BOOLEAN," +
          "PRIMARY KEY(sign_location, offer_order)" +
          ");");

      table = prefix + "_TRANSACTIONS";
      mysql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`trans_id` VARCHAR(36)," +
          "`trans_initiator` VARCHAR(36)," +
          "`trans_player` VARCHAR(36)," +
          "`trans_world` VARCHAR(36)," +
          "`trans_type` VARCHAR(36)," +
          "`trans_cost` VARCHAR(41)," +
          "`trans_oldBalance` VARCHAR(41)," +
          "`trans_balance` VARCHAR(41)," +
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
          "`joinedDate` VARCHAR(60)," +
          "`accountnumber` INTEGER," +
          "`accountstatus` VARCHAR(60)," +
          "`account_special` BOOLEAN" +
          ");");

      table = prefix + "_BALANCES";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`server_name` VARCHAR(250) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`currency` VARCHAR(250) NOT NULL," +
          "`balance` VARCHAR(41)," +
          "PRIMARY KEY(uuid, server_name, world, currency)" +
          ");");

      table = prefix + "_TRACKED";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
              "`uuid` VARCHAR(36) NOT NULL," +
              "`material` LONGTEXT," +
              "`location` VARCHAR(250)," +
              "`slot` INT(60) NOT NULL," +
              "PRIMARY KEY(uuid, location, slot)" +
              ");");

      table = prefix + "_SHOPS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_owner` VARCHAR(36)," +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`shop_hidden` TINYINT(1)," +
          "`shop_admin` TINYINT(1)," +
          "PRIMARY KEY(shop_name, shop_world)" +
          ");");

      table = prefix + "_SHOP_SHARES";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`percentage` DOUBLE," +
          "PRIMARY KEY(shop_name, shop_world, uuid)" +
          ");");

      table = prefix + "_SHOP_PERMISSIONS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(shop_name, shop_world, uuid)" +
          ");");

      table = prefix + "_SHOP_ITEMS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`shop_world` VARCHAR(50) NOT NULL," +
          "`shop_name` VARCHAR(60) NOT NULL," +
          "`slot` INT(60) NOT NULL," +
          "`shop_entry` LONGTEXT," +
          "PRIMARY KEY(shop_name, shop_world, slot)" +
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
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "PRIMARY KEY(uuid, world)" +
          ");");

      table = prefix + "_BANK_BALANCES";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`currency` VARCHAR(250) NOT NULL," +
          "`balance` VARCHAR(41)," +
          "PRIMARY KEY(uuid, world, currency)" +
          ");");

      table = prefix + "_BANK_MEMBERS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(uuid, world, member)" +
          ");");

      table = prefix + "_VAULTS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`size` INT(60) NOT NULL," +
          "PRIMARY KEY(uuid, world)" +
          ");");

      table = prefix + "_VAULT_ITEMS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`slot` INT(60) NOT NULL," +
          "`amount` INT(60) NOT NULL," +
          "`damage` INT(60) NOT NULL," +
          "`material` VARCHAR(80) NOT NULL," +
          "`custom_name` VARCHAR(80) NOT NULL," +
          "`enchantments` LONGTEXT," +
          "`lore` LONGTEXT," +
          "PRIMARY KEY(uuid, world, slot)" +
          ");");

      table = prefix + "_VAULT_MEMBERS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`uuid` VARCHAR(36) NOT NULL," +
          "`world` VARCHAR(50) NOT NULL," +
          "`member` VARCHAR(36) NOT NULL," +
          "`permissions` LONGTEXT," +
          "PRIMARY KEY(uuid, world, member)" +
          ");");

      table = prefix + "_SIGNS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_owner` VARCHAR(36)," +
          "`sign_type` VARCHAR(30) NOT NULL," +
          "`sign_location` VARCHAR(250) NOT NULL UNIQUE," +
          "`sign_meta` LONGTEXT" +
          ");");

      table = prefix + "_SIGN_OFFERS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`sign_location`  VARCHAR(250) NOT NULL," +
          "`offer_order` INT(60) NOT NULL," +
          "`offer_buy` VARCHAR(41)," +
          "`offer_sell` VARCHAR(41)," +
          "`offer_trade` LONGTEXT," +
          "`offer_amount` INT(60) NOT NULL," +
          "`offer_damage` INT(60) NOT NULL," +
          "`offer_material` LONGTEXT," +
          "`offer_admin` BOOLEAN," +
          "PRIMARY KEY(sign_location, offer_order)" +
          ");");

      table = prefix + "_TRANSACTIONS";
      h2().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
          "`trans_id` VARCHAR(36)," +
          "`trans_initiator` VARCHAR(36)," +
          "`trans_player` VARCHAR(36)," +
          "`trans_world` VARCHAR(36)," +
          "`trans_type` VARCHAR(36)," +
          "`trans_cost` VARCHAR(41)," +
          "`trans_oldBalance` VARCHAR(41)," +
          "`trans_balance` VARCHAR(41)," +
          "`trans_time` BIGINT(60)," +
          "PRIMARY KEY(trans_id)" +
          ");");
      h2().close();
    }
  }
}