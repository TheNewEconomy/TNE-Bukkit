package com.github.tnerevival.core.currency;

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
  private boolean item;
  private String name;
  private String format;
  private double rate;
  private String decimal;

  public void addTier(Tier tier) {
    addTier(tier.getSingle(), tier);
  }

  public void addTier(String id, Tier tier) {
    tiers.put(id, tier);
  }

  public boolean isWorldDefault() {
    return worldDefault;
  }

  public void setWorldDefault(boolean worldDefault) {
    this.worldDefault = worldDefault;
  }

  public boolean isItem() {
    return item;
  }

  public void setItem(boolean item) {
    this.item = item;
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

  public String getMajor() {
    return tiers.get("Major").getSingle();
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
}