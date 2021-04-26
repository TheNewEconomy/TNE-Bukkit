package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.calculations.CalculationProvider;
import net.tnemc.core.common.currency.calculations.impl.BasicCalculation;
import net.tnemc.core.economy.currency.Currency;
import net.tnemc.core.economy.currency.Tier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

  private TreeMap<BigDecimal, TNETier> tiers = new TreeMap<>(Collections.reverseOrder());

  private List<String> worlds = new ArrayList<>();

  private CurrencyNote note = new CurrencyNote("PAPER");

  private CalculationProvider calculationProvider = new BasicCalculation();

  private boolean worldDefault = true;
  private boolean global = true;
  private BigDecimal balance;
  private BigDecimal maxBalance;
  private String type;
  private boolean notable;
  private BigDecimal fee;
  private BigDecimal minimum;
  private boolean enderChest;
  private boolean separateMajor;
  private String majorSeparator;
  private Integer minorWeight;
  private String identifier;
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
  private boolean decimalSupport = false;

  public TreeMap<BigDecimal, TNETier> getTiers() {
    return tiers;
  }

  public void setTiers(TreeMap<BigDecimal, TNETier> tiers) {
    this.tiers = tiers;
  }

  //TNETier-related methods.
  public Set<TNETier> getTNETiersSet() {
    Set<TNETier> tiers = new HashSet<>();
    tiers.addAll(this.tiers.values());
    return tiers;
  }

  public void addTier(TNETier tier) {
    tiers.put(tier.getTNEWeight(), tier);
    if(tier.getTNEWeight().compareTo(BigDecimal.ONE) < 0) decimalSupport = true;
  }

  public Optional<TNETier> getTier(BigDecimal weight) {
    return Optional.of(tiers.get(weight));
  }

  public Set<Tier> getTiersSet() {
    Set<Tier> tiers = new HashSet<>();
    tiers.addAll(this.tiers.values());

    return tiers;
  }

  public Optional<TNETier> getTier(String name) {
    for(TNETier tier : tiers.values()) {
      if(tier.singular().equalsIgnoreCase(name) || tier.plural().equalsIgnoreCase(name))
        return Optional.of(tier);
    }
    return Optional.empty();
  }

  public Optional<TNETier> getTierByMaterial(String material) {
    for(TNETier tier : tiers.values()) {
      if(tier.getItemInfo() != null) {
        if (tier.getItemInfo().getMaterial().equalsIgnoreCase(material))
          return Optional.of(tier);
      }
    }
    return Optional.empty();
  }

  public boolean hasTier(String name) {

    for(TNETier tier : tiers.values()) {
      if(tier.singular().equals(name)) return true;
    }
    return false;
  }

  public static TNECurrency fromReserve(Currency currency) {
    TNECurrency tneCurrency = new TNECurrency();

    tneCurrency.setIdentifier(currency.name());
    tneCurrency.setSingle(currency.name());
    tneCurrency.setPlural(currency.plural());
    tneCurrency.setDecimalPlaces(currency.decimalPlaces());
    tneCurrency.setSymbol(currency.symbol());
    tneCurrency.setBalance(currency.defaultBalance());
    tneCurrency.setWorldDefault(currency.isDefault());
    tneCurrency.setFormat("<symbol><major.amount><decimal><minor.amount>");

    if(currency.getMajorTiers() != null) {
      currency.getMajorTiers().forEach((weight, tier) -> tneCurrency.addTier(TNETier.fromReserve(tier)));
    }

    if(currency.getMinorTiers() != null) {
      currency.getMinorTiers().forEach((weight, tier) -> tneCurrency.addTier(TNETier.fromReserve(tier)));
    }

    return tneCurrency;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public CurrencyNote getNote() {
    return note;
  }

  public void setNote(CurrencyNote note) {
    this.note = note;
  }

  @Override
  public String name() {
    return identifier;
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

  public boolean hasDecimalSupport() {
    return decimalSupport;
  }

  public boolean shorten() {
    return format.contains("<shorten>");
  }

  public void setWorldDefault(boolean worldDefault) {
    this.worldDefault = worldDefault;
  }

  public boolean isGlobal() {
    return global;
  }

  public void setGlobal(boolean global) {
    this.global = global;
  }

  public List<String> getWorlds() {
    return worlds;
  }

  public void setWorlds(List<String> worlds) {
    this.worlds = worlds;
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
    return type.equalsIgnoreCase("item");
  }

  public boolean isNotable() {
    return notable;
  }

  public void setNotable(boolean notable) {
    this.notable = notable;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

  public BigDecimal getMinimum() {
    return minimum;
  }

  public void setMinimum(BigDecimal minimum) {
    this.minimum = minimum;
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
    TNE.debug("Set Symbol: " + symbol);
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

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  public CurrencyType getCurrencyType() {
    return TNE.manager().currencyManager().getType(type.toLowerCase());
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isXp() {
    return type.equalsIgnoreCase("experience");
  }

  public CalculationProvider calculation() {
    return calculationProvider;
  }

  public void setCalculationProvider(CalculationProvider calculationProvider) {
    this.calculationProvider = calculationProvider;
  }
}