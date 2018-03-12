package net.tnemc.signs.impl.offer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/13/2018.
 */
public class ItemEntry {

  private int order;
  private ItemStack item = new ItemStack(Material.AIR);
  private ItemStack trade = new ItemStack(Material.AIR);
  private BigDecimal sell = new BigDecimal(-1.0);
  private BigDecimal buy = new BigDecimal(-1.0);
  private boolean unlimited;

  public ItemEntry(int order, ItemStack item) {
    this.order = order;
    this.item = item;
  }

  public ItemEntry reorder(int order) {
    ItemEntry entry = new ItemEntry(order, item);
    entry.setTrade(trade);
    entry.setSell(sell);
    entry.setBuy(buy);
    return entry;
  }

  public int getOrder() {
    return order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  public ItemStack getItem() {
    return item;
  }

  public void setItem(ItemStack item) {
    this.item = item;
  }

  public ItemStack getTrade() {
    return trade;
  }

  public void setTrade(ItemStack trade) {
    this.trade = trade;
  }

  public BigDecimal getSell() {
    return sell;
  }

  public void setSell(BigDecimal sell) {
    this.sell = sell;
  }

  public BigDecimal getBuy() {
    return buy;
  }

  public void setBuy(BigDecimal buy) {
    this.buy = buy;
  }

  public boolean isUnlimited() {
    return unlimited;
  }

  public void setUnlimited(boolean unlimited) {
    this.unlimited = unlimited;
  }
}