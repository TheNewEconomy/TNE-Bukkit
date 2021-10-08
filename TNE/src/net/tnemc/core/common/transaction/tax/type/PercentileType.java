package net.tnemc.core.common.transaction.tax.type;


import net.tnemc.core.common.transaction.tax.TaxType;

import java.math.BigDecimal;

public class PercentileType implements TaxType {
  @Override
  public String name() {
    return "percent";
  }

  @Override
  public BigDecimal handleTaxation(BigDecimal amount, BigDecimal tax) {
    return amount.add(amount.multiply(tax));
  }
}