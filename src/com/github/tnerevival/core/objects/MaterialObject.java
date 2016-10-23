package com.github.tnerevival.core.objects;

public class MaterialObject {

  private String name;
  private Boolean item;
  private Double cost;
  private Double value;
  private Double use;
  private Double crafting;
  private Double enchant;
  private Double place;
  private Double mine;
  private Double smelt;

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

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Double getUse() {
    return use;
  }

  public void setUse(Double use) {
    this.use = use;
  }

  public Double getCrafting() {
    return crafting;
  }

  public void setCrafting(Double crafting) {
    this.crafting = crafting;
  }

  public Double getEnchant() {
    return enchant;
  }

  public void setEnchant(Double enchant) {
    this.enchant = enchant;
  }

  public Double getPlace() {
    return place;
  }

  public void setPlace(Double place) {
    this.place = place;
  }

  public Double getMine() {
    return mine;
  }

  public void setMine(Double mine) {
    this.mine = mine;
  }

  public Double getSmelt() {
    return smelt;
  }

  public void setSmelt(Double smelt) {
    this.smelt = smelt;
  }
}