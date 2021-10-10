package net.tnemc.core.common.transaction.tax;

import java.math.BigDecimal;

public class TaxEntry {

  /**
   * The {@link TaxType} associated with this {@link TaxEntry}.
   */
  private TaxType type;

  /**
   * The currency to utilize for balances for this tax.
   */
  private String currency;

  /**
   * The world to utilize for balances for this tax.
   */
  private String world;

  /**
   * The value of the taxes to be imposed.
   */
  private BigDecimal tax;

  public TaxEntry(TaxType type, String currency, String world, BigDecimal tax) {
    this.type = type;
    this.currency = currency;
    this.world = world;
    this.tax = tax;
  }

  public TaxType getType() {
    return type;
  }

  public void setType(TaxType type) {
    this.type = type;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }
}