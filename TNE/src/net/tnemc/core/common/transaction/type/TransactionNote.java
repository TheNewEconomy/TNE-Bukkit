package net.tnemc.core.common.transaction.type;

import net.tnemc.core.TNE;
import net.tnemc.core.economy.tax.TaxEntry;
import net.tnemc.core.economy.tax.type.FlatType;
import net.tnemc.core.economy.transaction.TransactionAffected;
import net.tnemc.core.economy.transaction.result.TransactionResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 8/24/2017.
 */
public class TransactionNote implements TNETransactionType {
  @Override
  public Map<String, TaxEntry> taxExceptions() {
    return new HashMap<>();
  }

  @Override
  public String name() {
    return "note";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public Optional<TaxEntry> initiatorTax() {
    return Optional.of(new TaxEntry(new FlatType(), "Default", TNE.instance().defaultWorld, BigDecimal.ZERO));
  }

  @Override
  public Optional<TaxEntry> recipientTax() {
    return Optional.of(new TaxEntry(new FlatType(), "Default", TNE.instance().defaultWorld, BigDecimal.ZERO));
  }

  @Override
  public TransactionResult success() {
    return TNE.transactionManager().getResult("noted");
  }

  @Override
  public TransactionResult fail() {
    return TNE.transactionManager().getResult("failed");
  }

  @Override
  public TransactionAffected affected() {
    return TransactionAffected.RECIPIENT;
  }
}
