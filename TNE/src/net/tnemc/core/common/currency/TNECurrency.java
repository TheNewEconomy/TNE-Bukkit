package net.tnemc.core.common.currency;

import net.tnemc.core.economy.currency.Currency;
import net.tnemc.core.economy.currency.Tier;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 10/21/2016.
 */
public class TNECurrency implements Currency {

  private TreeMap<Integer, TNETier> majorTiers = new TreeMap<>(Collections.reverseOrder());

  private TreeMap<Integer, TNETier> minorTiers = new TreeMap<>(Collections.reverseOrder());

  private boolean worldDefault = true;
  private BigDecimal balance;
  private BigDecimal maxBalance;
  private boolean item;
  private boolean vault;
  private boolean notable;
  private boolean bankChest;
  private boolean enderChest;
  private boolean separateMajor;
  private String majorSeparator;
  private Integer minorWeight;
  private String single;
  private String plural;
  private String singleMinor;
  private String pluralMinor;
  private String server;
  private String symbol;
  private String format;
  private String prefixes;
  private double rate;
  private String decimal;
  private int decimalPlaces;

  //Interest-related configurations
  private boolean interestEnabled = false;
  private double interestRate = 0.2;
  private long interestInterval = 1800;

  //TNETier-related methods.
  public Set<TNETier> getTNETiers() {
    Set<TNETier> tiers = new HashSet<>();
    tiers.addAll(majorTiers.values());
    tiers.addAll(minorTiers.values());
    return tiers;
  }

  public TreeMap<Integer, TNETier> getTNEMajorTiers() {
    return majorTiers;
  }

  public void setTNEMajorTiers(TreeMap<Integer, TNETier> tiers) {
    majorTiers = tiers;
  }

  public void addTNEMajorTier(TNETier tier) {
    majorTiers.put(tier.weight(), tier);
  }

  public Optional<TNETier> getMajorTier(Integer weight) {
    return Optional.of(majorTiers.get(weight));
  }

  public Optional<TNETier> getMajorTier(String name) {
    for(TNETier tier : majorTiers.values()) {
      if(tier.singular().equalsIgnoreCase(name) || tier.plural().equalsIgnoreCase(name))
        return Optional.of(tier);
    }
    return Optional.empty();
  }

  public TreeMap<Integer, TNETier> getTNEMinorTiers() {
    return minorTiers;
  }

  public void setTNEMinorTiers(TreeMap<Integer, TNETier> tiers) {
    minorTiers = tiers;
  }

  public void addTNEMinorTier(TNETier tier) {
    minorTiers.put(tier.weight(), tier);
  }

  public Optional<TNETier> getMinorTier(Integer weight) {
    return Optional.of(minorTiers.get(weight));
  }

  public Optional<TNETier> getMinorTier(String name) {
    for(TNETier tier : minorTiers.values()) {
      if(tier.singular().equalsIgnoreCase(name) || tier.plural().equalsIgnoreCase(name))
        return Optional.of(tier);
    }
    return Optional.empty();
  }

  public Set<Tier> getTiers() {
    Set<Tier> tiers = new HashSet<>();
    tiers.addAll(majorTiers.values());
    tiers.addAll(minorTiers.values());

    return tiers;
  }

  public boolean hasTier(String name) {
    for(TNETier tier : majorTiers.values()) {
      if(tier.singular().equals(name)) return true;
    }

    for(TNETier tier : minorTiers.values()) {
      if(tier.singular().equals(name)) return true;
    }
    return false;
  }

  public static TNECurrency fromReserve(Currency currency) {
    TNECurrency tneCurrency = new TNECurrency();

    tneCurrency.setSingle(currency.name());
    tneCurrency.setPlural(currency.plural());
    tneCurrency.setDecimalPlaces(currency.decimalPlaces());
    tneCurrency.setSymbol(currency.symbol());
    tneCurrency.setBalance(currency.defaultBalance());
    tneCurrency.setWorldDefault(currency.isDefault());

    if(currency.getMajorTiers() != null) {
      currency.getMajorTiers().forEach((weight, tier) -> {
        tneCurrency.addTNEMajorTier(TNETier.fromReserve(tier));
      });
    }

    if(currency.getMinorTiers() != null) {
      currency.getMinorTiers().forEach((weight, tier) -> {
        tneCurrency.addTNEMinorTier(TNETier.fromReserve(tier));
      });
    }

    return tneCurrency;
  }

  @Override
  public String name() {
    return single;
  }

  @Override
  public String plural() {
    return plural;
  }

  @Override
  public String symbol() {
    return symbol;
  }

  @Override
  public int decimalPlaces() {
    return decimalPlaces;
  }

  @Override
  public boolean isDefault() {
    return worldDefault;
  }

  @Override
  public BigDecimal defaultBalance() {
    return balance;
  }

  @Override
  public TreeMap<Integer, Tier> getMajorTiers() {
    return null;
  }

  @Override
  public TreeMap<Integer, Tier> getMinorTiers() {
    return null;
  }

  public boolean shorten() {
    return format.contains("<shorten>");
  }

  public void setWorldDefault(boolean worldDefault) {
    this.worldDefault = worldDefault;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getMaxBalance() {
    return maxBalance;
  }

  public void setMaxBalance(BigDecimal maxBalance) {
    this.maxBalance = maxBalance;
  }

  public boolean isItem() {
    return item;
  }

  public void setItem(boolean item) {
    this.item = item;
  }

  public boolean canVault() {
    return vault;
  }

  public void setVault(boolean vault) {
    this.vault = vault;
  }

  public boolean isNotable() {
    return notable;
  }

  public void setNotable(boolean notable) {
    this.notable = notable;
  }

  public boolean canBankChest() {
    return bankChest;
  }

  public void setBankChest(boolean bankChest) {
    this.bankChest = bankChest;
  }

  public boolean canEnderChest() {
    return enderChest;
  }

  public void setEnderChest(boolean enderChest) {
    this.enderChest = enderChest;
  }

  public boolean canSeparateMajor() {
    return separateMajor;
  }

  public void setSeparateMajor(boolean separateMajor) {
    this.separateMajor = separateMajor;
  }

  public String getMajorSeparator() {
    return majorSeparator;
  }

  public void setMajorSeparator(String majorSeparator) {
    this.majorSeparator = majorSeparator;
  }

  public Integer getMinorWeight() {
    return minorWeight;
  }

  public void setMinorWeight(Integer minorWeight) {
    this.minorWeight = minorWeight;
  }

  public void setSingle(String single) {
    this.single = single;
  }

  public void setPlural(String plural) {
    this.plural = plural;
  }

  public String getSingleMinor() {
    return singleMinor;
  }

  public void setSingleMinor(String singleMinor) {
    this.singleMinor = singleMinor;
  }

  public String getPluralMinor() {
    return pluralMinor;
  }

  public void setPluralMinor(String pluralMinor) {
    this.pluralMinor = pluralMinor;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getPrefixes() {
    return prefixes;
  }

  public void setPrefixes(String prefixes) {
    this.prefixes = prefixes;
  }

  public double getRate() {
    return rate;
  }

  public void setRate(double rate) {
    this.rate = rate;
  }

  public String getDecimal() {
    return decimal;
  }

  public void setDecimal(String decimal) {
    this.decimal = decimal;
  }

  public void setDecimalPlaces(int decimalPlaces) {
    this.decimalPlaces = decimalPlaces;
  }

  public boolean isInterestEnabled() {
    return interestEnabled;
  }

  public void setInterestEnabled(boolean interestEnabled) {
    this.interestEnabled = interestEnabled;
  }

  public double getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(double interestRate) {
    this.interestRate = interestRate;
  }

  public long getInterestInterval() {
    return interestInterval;
  }

  public void setInterestInterval(long interestInterval) {
    this.interestInterval = interestInterval;
  }
}