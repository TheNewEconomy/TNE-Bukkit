package com.github.tnerevival.core.event.bank;

import com.github.tnerevival.account.Bank;
import com.github.tnerevival.core.event.TNEInventoryEvent;

import java.util.UUID;

/**
 * Created by Daniel on 8/15/2016.
 */
public class TNEBankCloseEvent extends TNEInventoryEvent {

  private Bank bank;

  public TNEBankCloseEvent(UUID player, Bank bank) {
    super(player);
    this.bank = bank;
  }

  public Bank getBank() {
    return bank;
  }
}
