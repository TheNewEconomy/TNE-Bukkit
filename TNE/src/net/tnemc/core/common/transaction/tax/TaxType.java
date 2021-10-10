package net.tnemc.core.common.transaction.tax;

import java.math.BigDecimal;

public interface TaxType {

  String name();

  BigDecimal handleTaxation(BigDecimal amount, BigDecimal tax);
}