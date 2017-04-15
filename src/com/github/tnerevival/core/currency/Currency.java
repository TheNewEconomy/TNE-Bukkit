package com.github.tnerevival.core.currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by creatorfromhell on 10/21/2016.
 */
public class Currency {
  private Map<String, Tier> tiers = new HashMap<>();

  private boolean worldDefault = true;
  private BigDecimal balance;
  private long maxBalance;
  private boolean item;
  private boolean vault;
  private boolean bankChest;
  private boolean enderChest;
  private boolean trackChest;
  private String name;
  private String format;
  private String prefixes;
  private double rate;
  private String decimal;
  private int decimalPlaces;

  //Interest-related configurations
  private boolean interestEnabled = false;
  private double interestRate = 0.2;
  private long interestInterval = 1800;

  public void addTier(Tier tier) {
    addTier(tier.getSingle(), tier);
  }

  public void addTier(String id, Tier tier) {
    tiers.put(id, tier);
  }

  public boolean shorten() {
    return format.contains("<shorten>");
  }

  public boolean isWorldDefault() {
    return worldDefault;
  }

  public void setWorldDefault(boolean worldDefault) {
    this.worldDefault = worldDefault;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public long getMaxBalance() {
    return maxBalance;
  }

  public void setMaxBalance(long maxBalance) {
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

  public boolean canTrackChest() {
    return trackChest;
  }

  public void setTrackChest(boolean trackChest) {
    this.trackChest = trackChest;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  public void setDecimalPlaces(int decimalPlaces) {
    this.decimalPlaces = decimalPlaces;
  }

  public String getMajor() {
    return tiers.get("Major").getSingle();
  }

  public String getMajor(boolean singular) {
    return (singular)? getMajor() : getMajorPlural();
  }

  public void setMajor(String singular) {
    tiers.get("Major").setSingle(singular);
  }

  public String getMajorPlural() {
    return tiers.get("Major").getPlural();
  }

  public void setMajorPlural(String plural) {
    tiers.get("Major").setPlural(plural);
  }

  public String getMinor() {
    return tiers.get("Minor").getSingle();
  }

  public String getMinor(boolean singular) {
    return (singular)? getMinor() : getMinorPlural();
  }

  public void setMinor(String singular) {
    tiers.get("Minor").setSingle(singular);
  }

  public String getMinorPlural() {
    return tiers.get("Minor").getPlural();
  }

  public void setMinorPlural(String plural) {
    tiers.get("Minor").setPlural(plural);
  }

  public Tier getTier(String id) {
    return tiers.get(id);
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