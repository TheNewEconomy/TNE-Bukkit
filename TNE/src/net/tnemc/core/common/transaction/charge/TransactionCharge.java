package net.tnemc.core.common.transaction.charge;

import net.tnemc.core.common.currency.CurrencyEntry;
import net.tnemc.core.common.currency.TNECurrency;
import org.bukkit.World;

import java.math.BigDecimal;

/**
 * Created by creatorfromhell on 8/9/2017.
 * All rights reserved.
 **/
public class TransactionCharge {

  /**
   * The {@link TransactionChargeType} of this {@link TransactionCharge}.
   */
  TransactionChargeType type = TransactionChargeType.LOSE;

  /**
   * The {@link CurrencyEntry} for this {@link TransactionCharge}.
   */
  CurrencyEntry entry;

  /**
   * A charge that occurs during a transaction, this may either be a gain, or a loss.
   * @param world The name of the {@link World} this {@link TransactionCharge} takes place in.
   * @param currency The {@link TNECurrency} this {@link TransactionCharge} involves.
   * @param amount The {@link BigDecimal} this {@link TransactionCharge} is for.
   */
  public TransactionCharge(String world, TNECurrency currency, BigDecimal amount) {
    this(world, currency, amount, TransactionChargeType.LOSE);
  }

  public TransactionCharge(String world, TNECurrency currency, BigDecimal amount, TransactionChargeType type) {
    this.entry = new CurrencyEntry(world, currency, amount);
    this.type = type;
  }

  public TransactionChargeType getType() {
    return type;
  }

  public void setType(TransactionChargeType type) {
    this.type = type;
  }

  public CurrencyEntry getEntry() {
    return entry;
  }

  public void setEntry(CurrencyEntry entry) {
    this.entry = entry;
  }

  public String getWorld() {
    return entry.getWorld();
  }

  public TNECurrency getCurrency() {
    return entry.getCurrency();
  }

  public BigDecimal getAmount() {
    return entry.getAmount();
  }

  public TransactionChargeType reverse() {
    return ((type.equals(TransactionChargeType.LOSE))? TransactionChargeType.GAIN : TransactionChargeType.LOSE);
  }

  public TransactionCharge copy(boolean reverse) {
    TransactionChargeType copyType = (reverse)? reverse() : getType();
    return new TransactionCharge(getWorld(), getCurrency(), getAmount(), copyType);
  }
}