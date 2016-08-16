package com.github.tnerevival.core.event.account;

import com.github.tnerevival.account.Account;

import java.util.UUID;

/**
 * Created by Daniel on 8/15/2016.
 */
public class TNEAccountDeletionEvent extends TNEAccountEvent {

  private Account account;

  public TNEAccountDeletionEvent(UUID id, Account account) {
    super(id);
    this.account = account;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }
}