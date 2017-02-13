package com.github.tnerevival.account;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.credits.CreditsEntry;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Location;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Account implements Serializable {

  private static final long serialVersionUID = 1L;

  private Map<Location, TrackedItems> trackedItems = new HashMap<>();

  /**
   * A HashMap of this account's balances from every world that the player has visited.
   */
  private Map<String, BigDecimal> balances = new HashMap<>();

  /**
   * A HashMap of this account's banks from every world that the player has visited.
   */
  private Map<String, Bank> banks = new HashMap<>();

  private Map<String, Vault> vaults = new HashMap<>();

  private Map<String, CreditsEntry> credits = new HashMap<>();

  private Map<String, Integer> commands = new HashMap<>();

  private String joined;

  /**
   * The account number for this account.
   * This number is unique to the account.
   */
  private int accountNumber = 0;

  private UUID uid;

  private AccountStatus status;

  private String pin;

  public Account(UUID uid) {
    this(uid, TNE.instance.manager.accounts.size() + 1);
  }

  public Account(UUID uid, int accountNumber) {
    this.uid = uid;
    this.joined = TNE.instance.dateFormat.format(new Date());
    this.accountNumber = accountNumber;
    this.status = AccountStatus.NORMAL;
    this.pin = "TNENOSTRINGVALUE";
    setBalance(TNE.instance.defaultWorld, BigDecimal.ZERO, TNE.instance.manager.currencyManager.get(TNE.instance.defaultWorld).getName());
  }

  public String balancesToString() {
    int count = 0;
    String toReturn = "";
    for(Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
        if(count > 0) toReturn += "-";
        toReturn += entry.getKey() + "," + entry.getValue().doubleValue();
        count++;
    }
    return toReturn;
  }

  public void balancesFromString(String from) {
    String[] worlds = from.split("-");

    List<Integer> combine = new ArrayList<>();
    for(int i = 0; i < worlds.length; i++) {
      String world = worlds[i];
      if(combine.size() > 0 && world.contains(":")) {
        combine.add(i);
        world = combine(worlds, combine);
        combine = new ArrayList<>();
      }

      if(!world.contains(":")) {
        combine.add(i);
        continue;
      }
      String[] balance = world.split(",");
      if(balance.length == 2) {
        balances.put(balance[0], new BigDecimal(balance[1]));
      }
    }
  }

  private String combine(String[] values, List<Integer> indexes) {
    StringBuilder builder = new StringBuilder();

    int i = 0;
    for(Integer in : indexes) {
      if(i > 0) builder.append("-");
      builder.append(values[in]);
      i++;
    }
    return builder.toString();
  }

  public String commandsToString() {
    Iterator<Map.Entry<String, Integer>> commandsIterator = commands.entrySet().iterator();
    StringBuilder builder = new StringBuilder();

    while(commandsIterator.hasNext()) {
      Map.Entry<String, Integer> commandEntry = commandsIterator.next();

      if(builder.length() > 0) {
        builder.append(",");
      }
      builder.append(commandEntry.getKey() + "=" + commandEntry.getValue());
    }
    return builder.toString();
  }

  public void commandsFromString(String value) {
    String[] values = value.split(",");

    for(String s : values) {
      String[] data = s.split("=");
      if(data.length == 2) {
        commands.put(data[0], Integer.valueOf(data[1]));
      }
    }
  }

  public String creditsToString() {
    Iterator<Map.Entry<String, CreditsEntry>> creditsIterator = credits.entrySet().iterator();
    StringBuilder builder = new StringBuilder();

    while(creditsIterator.hasNext()) {
      Map.Entry<String, CreditsEntry> creditsEntry = creditsIterator.next();
      if(builder.length() > 0) {
        builder.append(",");
      }
      builder.append(creditsEntry.getKey() + "=" + creditsEntry.getValue().toString());
    }

    return builder.toString();
  }

  public void creditsFromString(String value) {
    String[] values = value.split(",");

    for(String s : values) {
      String[] data = s.split("=");
      if(data.length == 2) {
        credits.put(data[0], CreditsEntry.fromString(data[1]));
      }
    }
  }

  /*
   * Inventory Time Credits
   */
  public Map<String, Long> getTimes(String inventory) {
    if(credits.get(inventory) != null) {
      return credits.get(inventory).getCredits();
    }
    return new HashMap<>();
  }

  public void addTime(String world, String inventory, Long time) {
    setTime(world, inventory, getTimeLeft(world, inventory) + time);
  }

  public Long getTimeLeft(String world, String inventory) {
    if(credits.get(inventory) != null) {
      return credits.get(inventory).getRemaining(world);
    }
    return 0L;
  }

  public void setTime(String world, String inventory, long time) {
    Map<String, Long> inventoryCredits = (credits.get(inventory) != null) ? credits.get(inventory).getCredits() : new HashMap<String, Long>();
    inventoryCredits.put(world, time);
    credits.put(inventory, new CreditsEntry(inventoryCredits));
  }

  /*
   * Command Credits
   */
  public void addCredit(String command) {
    if(commands.containsKey(command)) {
      commands.put(command, commands.get(command) + 1);
    }
  }

  public void removeCredit(String command) {
    if(commands.containsKey(command)) {
      commands.put(command, commands.get(command) - 1);
    }
  }

  public Boolean hasCredit(String command) {
    return (commands.containsKey(command) && commands.get(command) > 0);
  }

  public Map<String, Integer> getCredits() {
    return commands;
  }

  /*
   * MISC Methods/Getters & Setters
   */

  /**
   * @return the accountNumber
   */
  public int getAccountNumber() {
    return accountNumber;
  }

  /**
   * @param accountNumber the accountNumber to set
   */
  public void setAccountNumber(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  public UUID getUid() {
    return uid;
  }

  /**
   * @return the joined
   */
  public String getJoined() {
    return joined;
  }

  /**
   * @param joined the joined to set
   */
  public void setJoined(String joined) {
    this.joined = joined;
  }

  /**
   * @return the status
   */
  public AccountStatus getStatus() {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = AccountStatus.fromName(status);
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public Map<Location, TrackedItems> getTrackedItems() {
    return trackedItems;
  }

  public void setTrackedItems(Map<Location, TrackedItems> trackedItems) {
    this.trackedItems = trackedItems;
  }

  public Map<String, BigDecimal> getBalances() {
    return balances;
  }

  public void setBalances(Map<String, BigDecimal> balances) {
    this.balances = balances;
  }

  public BigDecimal getBalance(String world, String currency) {
    MISCUtils.debug("Returning balance for " + world + ":" + currency);
    return balances.get(world + ":" + currency);
  }

  public void setBalance(String world, BigDecimal balance, String currency) {
    MISCUtils.debug("Setting balance for " + world + ":" + currency);
    this.balances.put(world + ":" + currency, balance);
  }

  public Map<String, Bank> getBanks() {
    return banks;
  }

  public void setBanks(Map<String, Bank> banks) {
    this.banks = banks;
  }

  public void setBank(String world, Bank bank) {
    this.banks.put(world, bank);
  }

  public Bank getBank(String world) {
    return this.banks.get(world);
  }

  public boolean hasBank(String world) {
    return this.banks.containsKey(world);
  }

  public Map<String, Vault> getVaults() {
    return vaults;
  }

  public void setVault(Map<String, Vault> vaults) {
    this.vaults = vaults;
  }

  public void setVault(String world, Vault vault) {
    this.vaults.put(world, vault);
  }

  public Vault getVault(String world) {
    return this.vaults.get(world);
  }

  public boolean hasVault(String world) {
    MISCUtils.debug("Account.hasVault(" + world + ")");
    MISCUtils.debug("Outcome: " + vaults.containsKey(world));
    return vaults.containsKey(world);
  }
}