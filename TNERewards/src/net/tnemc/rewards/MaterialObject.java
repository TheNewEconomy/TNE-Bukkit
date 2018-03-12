package net.tnemc.rewards;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
