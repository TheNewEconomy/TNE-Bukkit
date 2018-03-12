package net.tnemc.flatfile;

import com.github.tnerevival.core.DataManager;
import com.github.tnerevival.core.db.DatabaseConnector;
import com.github.tnerevival.core.db.FlatFile;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.AccountStatus;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldHoldings;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.data.TNEDataProvider;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.currency.CurrencyEntry;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.type.TransactionType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 9/7/2017.
 */
public class FlatFileProvider extends TNEDataProvider {

  private static final File directory = new File(TNE.instance().getDataFolder(), "/tne/");

  public FlatFileProvider(DataManager manager) {
    super(manager);
  }

  @Override
  public Boolean backupData() {
    Utils.zipDirectory(directory, new File("TheNewEconomy"));
    return true;
  }

  @Override
  public void load(Double version) {
    Collection<TNEAccount> accs = loadAccounts();
    for(TNEAccount account : accs) {
      if(account == null || account.identifier() == null) continue;
      TNE.manager().getAccounts().put(account.identifier(), account, true);
    }

    Collection<TNETransaction> trans = loadTransactions();
    for(TNETransaction transaction : trans) {
      if(transaction == null || transaction.transactionID() == null) continue;
      TNE.transactionManager().getTransactions().put(transaction.transactionID(), transaction, true);
    }
    TNE.instance().setUUIDS(loadEconomyIDS());
  }

  @Override
  public void delete(Double version) {

  }

  @Override
  public UUID loadID(String username) {
    UUID id = null;
    File file = new File(directory, "ids.yml");
    if(file.exists()) {
      YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
      if(configuration.contains("ID." + username)) {
        id = UUID.fromString(configuration.getString("ID." + username));
        TNE.uuidManager().addUUID(username, id);
      }
    }
    return id;
  }

  @Override
  public Map<String, UUID> loadEconomyIDS() {
    File file = new File(directory, "ids.yml");
    if(file.exists()) {
      YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
      if(configuration.contains("ID")) {
        Set<String> usernames = configuration.getConfigurationSection("ID").getKeys(false);

        for (String username : usernames) {
          TNE.uuidManager().addUUID(username, UUID.fromString(configuration.getString("ID." + username)));
        }
      }
    }
    return new HashMap<>();
  }

  @Override
  public void saveID(String username, UUID id) {
    if(id == null) return;
    File file = new File(directory, "ids.yml");
    try {
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    TNE.debug("FlatFileProvider.save " + username + ", " + id.toString());
    configuration.set("ID." + username, id.toString());
    try {
      configuration.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeID(String username) {
    TNE.uuidManager().remove(username);
    File file = new File(directory, "ids.yml");
    if(file.exists()) {
      YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
      configuration.set("ID." + username, null);
      try {
        configuration.save(file);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void removeID(UUID id) {
    String username = "";
    TNE.uuidManager().getUuids().entrySet().removeIf(entry->entry.getValue().equals(id));
    if(!username.equalsIgnoreCase("")) {
      File file = new File(directory, "ids.yml");
      if(file.exists()) {
        try {
          file.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.set("ID." + username, null);
        try {
          configuration.save(file);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  @Override
  public Collection<TNEAccount> loadAccounts() {
    List<TNEAccount> accounts = new ArrayList<>();
    File file = new File(directory, "accounts/");

    for(File f : file.listFiles()) {
      if(f.getName().contains(".yml")) {
        TNEAccount account = loadAccount(UUID.fromString(f.getName().replace(".yml", "").trim()));
        if(account != null) accounts.add(account);
      }
    }
    return accounts;
  }

  @Override
  public TNEAccount loadAccount(UUID id) {
    TNEAccount account = null;
    File file = new File(directory, "accounts/" + id.toString() + ".yml");
    if(file.exists()) {
      TNE.debug("Loading account: " + id.toString());
      YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
      account = new TNEAccount(id, configuration.getString("Account.Display"));
      account.setAccountNumber(configuration.getInt("Account.Number"));
      account.setId(UUID.fromString(configuration.getString("Account.ID")));
      account.setStatus(AccountStatus.fromName(configuration.getString("Account.Status")));
      account.setLanguage(configuration.getString("Account.Language"));
      account.setPlayerAccount(configuration.getBoolean("Account.Player"));
      account.setJoined(configuration.getLong("Account.Joined"));
      account.setLastOnline(configuration.getLong("Account.LastOnline"));


      if(configuration.contains("Account.Holdings")) {
        Set<String> worlds = configuration.getConfigurationSection("Account.Holdings").getKeys(false);

        for (String world : worlds) {
          Set<String> currencies = configuration.getConfigurationSection("Account.Holdings." + world).getKeys(false);

          for (String currency : currencies) {
            account.setHoldings(world, currency, new BigDecimal(configuration.getString("Account.Holdings." + world + "." + currency)), true);
          }
        }
      }
    }

    return account;
  }

  @Override
  public void saveAccount(TNEAccount account) {
    TNE.manager().getAccounts().put(account.identifier(), account);
    File file = new File(directory, "accounts/" + account.identifier().toString() + ".yml");
    if(TNE.uuidManager().containsUUID(account.identifier())) {
      saveID(IDFinder.getUsername(account.identifier().toString()), account.identifier());
    }
    if(!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    configuration.set("Account.Number", account.getAccountNumber());
    configuration.set("Account.ID", account.identifier().toString());
    configuration.set("Account.Display", account.displayName());
    configuration.set("Account.Status", account.getStatus().getName());
    configuration.set("Account.Language", account.getLanguage());
    configuration.set("Account.Player", account.playerAccount());
    configuration.set("Account.Joined", account.getJoined());
    configuration.set("Account.LastOnline", account.getLastOnline());

    for(Map.Entry<String, WorldHoldings> entry : account.getWorldHoldings().entrySet()) {
      for(Map.Entry<String, BigDecimal> balEntry : entry.getValue().getHoldings().entrySet()) {
        configuration.set("Account.Holdings." + entry.getKey() + "." + balEntry.getKey(), balEntry.getValue().toPlainString());
      }
    }
    try {
      configuration.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteAccount(UUID id) {
    TNE.manager().getAccounts().remove(id, false);
    File file = new File(directory, "accounts/" + id.toString() + ".yml");
    if(file.exists()) {
      file.delete();
    }
  }

  @Override
  public TNETransaction loadTransaction(UUID id) {
    TNE.debug("=====START FlatFileProvider.loadTransaction =====");
    TNE.debug("UUID: " + id.toString());
    TNETransaction transaction = null;
    File file = new File(directory, "transactions/" + id.toString() + ".yml");
    if(file.exists()) {
      YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
      TransactionType type = TNE.transactionManager().getType(configuration.getString("Transaction.Type").toLowerCase());
      String world = configuration.getString("Transaction.World");
      Long time = configuration.getLong("Transaction.Time");
      Boolean voided = configuration.getBoolean("Transaction.Voided");
      UUID initiator = null;
      UUID recipient = null;

      if(configuration.contains("Transaction.Initiator.ID"))
          initiator = UUID.fromString(configuration.getString("Transaction.Initiator.ID"));

      if(configuration.contains("Transaction.Recipient.ID"))
          recipient = UUID.fromString(configuration.getString("Transaction.Recipient.ID"));

      transaction = new TNETransaction(id, initiator, recipient, world, type, time);
      transaction.setVoided(voided);

      if(configuration.contains("Transaction.Initiator.Balance")) {
        String w = configuration.getString("Transaction.Initiator.Balance.World");
        TNECurrency currency = TNE.manager().currencyManager().get(
            w,
            configuration.getString("Transaction.Initiator.Balance.Currency")
        );
        BigDecimal amount = new BigDecimal(configuration.getString("Transaction.Initiator.Balance.Amount"));
        CurrencyEntry entry = new CurrencyEntry(world, currency, amount);
        transaction.setInitiatorBalance(entry);
      }

      if(configuration.contains("Transaction.Initiator.Charge")) {
        TransactionChargeType chargeType = TransactionChargeType.valueOf(configuration.getString("Transaction.Initiator.Charge.Type"));
        String w = configuration.getString("Transaction.Initiator.Charge.World");
        TNECurrency currency = TNE.manager().currencyManager().get(
            w,
            configuration.getString("Transaction.Initiator.Charge.Currency")
        );
        BigDecimal amount = new BigDecimal(configuration.getString("Transaction.Initiator.Charge.Amount"));

        TransactionCharge charge = new TransactionCharge(world, currency, amount, chargeType);
        transaction.setInitiatorCharge(charge);
      }

      if(configuration.contains("Transaction.Recipient.Balance")) {
        String w = configuration.getString("Transaction.Recipient.Balance.World");
        TNECurrency currency = TNE.manager().currencyManager().get(
            w,
            configuration.getString("Transaction.Recipient.Balance.Currency")
        );
        BigDecimal amount = new BigDecimal(configuration.getString("Transaction.Recipient.Balance.Amount"));
        CurrencyEntry entry = new CurrencyEntry(world, currency, amount);
        transaction.setRecipientBalance(entry);
      }

      if(configuration.contains("Transaction.Recipient.Charge")) {
        TransactionChargeType chargeType = TransactionChargeType.valueOf(configuration.getString("Transaction.Recipient.Charge.Type"));
        String w = configuration.getString("Transaction.Recipient.Charge.World");
        TNECurrency currency = TNE.manager().currencyManager().get(
            w,
            configuration.getString("Transaction.Recipient.Charge.Currency")
        );
        BigDecimal amount = new BigDecimal(configuration.getString("Transaction.Recipient.Charge.Amount"));

        TransactionCharge charge = new TransactionCharge(world, currency, amount, chargeType);
        transaction.setRecipientCharge(charge);
      }
    }
    TNE.debug("=====END FlatFileProvider.loadTransaction =====");
    return transaction;
  }

  @Override
  public Collection<TNETransaction> loadTransactions() {
    List<TNETransaction> transactions = new ArrayList<>();
    File file = new File(directory, "transactions/");

    for(File f : file.listFiles()) {
      if(f.getName().contains(".yml")) {
        TNETransaction transaction = loadTransaction(UUID.fromString(f.getName().replace(".yml", "").trim()));
        if(transaction != null) transactions.add(transaction);
      }
    }
    return transactions;
  }

  @Override
  public void saveTransaction(TNETransaction transaction) {
    TNE.transactionManager().getTransactions().put(transaction.transactionID(), transaction);
    File file = new File(directory, "transactions/" + transaction.transactionID().toString() + ".yml");
    if(!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    configuration.set("Transaction.ID", transaction.transactionID().toString());
    configuration.set("Transaction.Type", transaction.type().name());
    configuration.set("Transaction.World", transaction.getWorld());
    configuration.set("Transaction.Time", transaction.time());
    configuration.set("Transaction.Voided", transaction.voided());

    if(transaction.initiator() != null) {
      configuration.set("Transaction.Initiator.ID", transaction.initiator());
      if(transaction.initiatorBalance() != null) {
        configuration.set("Transaction.Initiator.Balance.World", transaction.initiatorBalance().getWorld());
        configuration.set("Transaction.Initiator.Balance.Currency", transaction.initiatorBalance().getCurrency().name());
        configuration.set("Transaction.Initiator.Balance.Amount", transaction.initiatorBalance().getAmount().toPlainString());
      }

      if(transaction.initiatorCharge() != null) {
        configuration.set("Transaction.Initiator.Charge.Type", transaction.initiatorCharge().getType().name());
        configuration.set("Transaction.Initiator.Charge.World", transaction.initiatorCharge().getWorld());
        configuration.set("Transaction.Initiator.Charge.Currency", transaction.initiatorCharge().getCurrency().name());
        configuration.set("Transaction.Initiator.Charge.Amount", transaction.initiatorCharge().getAmount().toPlainString());
      }
    }

    if(transaction.recipient() != null) {
      configuration.set("Transaction.Recipient.ID", transaction.recipient());
      if(transaction.recipientBalance() != null) {
        configuration.set("Transaction.Recipient.Balance.World", transaction.recipientBalance().getWorld());
        configuration.set("Transaction.Recipient.Balance.Currency", transaction.recipientBalance().getCurrency().name());
        configuration.set("Transaction.Recipient.Balance.Amount", transaction.recipientBalance().getAmount().toPlainString());
      }

      if(transaction.recipientCharge() != null) {
        configuration.set("Transaction.Recipient.Charge.Type", transaction.recipientCharge().getType().name());
        configuration.set("Transaction.Recipient.Charge.World", transaction.recipientCharge().getWorld());
        configuration.set("Transaction.Recipient.Charge.Currency", transaction.recipientCharge().getCurrency().name());
        configuration.set("Transaction.Recipient.Charge.Amount", transaction.recipientCharge().getAmount().toPlainString());
      }
    }
    try {
      configuration.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void deleteTransaction(UUID id) {
    TNE.transactionManager().getTransactions().remove(id);
    File file = new File(directory, "transactions/" + id.toString() + ".yml");
    if(file.exists()) {
      file.delete();
    }
  }

  @Override
  public String identifier() {
    return "flatfile";
  }

  @Override
  public boolean supportUpdate() {
    return false;
  }

  @Override
  public Boolean first() {
    File file = new File(directory, "tne.yml");
    return !directory.exists() || !file.exists();
  }

  @Override
  public Double version() {
    if(first()) return 0.0;
    File file = new File(directory, "tne.yml");
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    return configuration.getDouble("TNE.version");
  }

  @Override
  public void initialize() {
    File file = new File(directory, "tne.yml");
    File ids = new File(directory, "ids.yml");
    File transactions = new File(directory, "transactions/");
    File accounts = new File(directory, "accounts/");
    try {
      directory.mkdir();
      ids.createNewFile();
      file.createNewFile();
      transactions.mkdir();
      accounts.mkdir();
    } catch (IOException e) {
      e.printStackTrace();
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    configuration.set("TNE.version", 10.0);
    try {
      configuration.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Double version) {

  }

  @Override
  public DatabaseConnector connector() {
    return new FlatFile();
  }
}