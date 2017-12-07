package net.tnemc.rewards;

import java.math.BigDecimal;

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
 * Created by Daniel on 10/21/2017.
 */
public class MaterialObject {

  private String name;
  private Boolean item;
  private BigDecimal cost;
  private BigDecimal value;
  private BigDecimal use;
  private BigDecimal crafting;
  private BigDecimal enchant;
  private BigDecimal place;
  private BigDecimal mine;
  private BigDecimal smelt;

  public MaterialObject(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean isItem() {
    return item;
  }

  public void setItem(Boolean item) {
    this.item = item;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getUse() {
    return use;
  }

  public void setUse(BigDecimal use) {
    this.use = use;
  }

  public BigDecimal getCrafting() {
    return crafting;
  }

  public void setCrafting(BigDecimal crafting) {
    this.crafting = crafting;
  }

  public BigDecimal getEnchant() {
    return enchant;
  }

  public void setEnchant(BigDecimal enchant) {
    this.enchant = enchant;
  }

  public BigDecimal getPlace() {
    return place;
  }

  public void setPlace(BigDecimal place) {
    this.place = place;
  }

  public BigDecimal getMine() {
    return mine;
  }

  public void setMine(BigDecimal mine) {
    this.mine = mine;
  }

  public BigDecimal getSmelt() {
    return smelt;
  }

  public void setSmelt(BigDecimal smelt) {
    this.smelt = smelt;
  }
}
