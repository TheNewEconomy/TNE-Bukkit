package net.tnemc.core.common.account;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 12/7/2017.
 */
public class AccountAccessor {

  private UUID id;
  private boolean deposit;
  private boolean withdraw;
  private boolean addAccessor;
  private boolean removeAccessor;

  public AccountAccessor(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public boolean canDeposit() {
    return deposit;
  }

  public void setDeposit(boolean deposit) {
    this.deposit = deposit;
  }

  public boolean canWithdraw() {
    return withdraw;
  }

  public void setWithdraw(boolean withdraw) {
    this.withdraw = withdraw;
  }

  public boolean canAddAccessor() {
    return addAccessor;
  }

  public void setAddAccessor(boolean addAccessor) {
    this.addAccessor = addAccessor;
  }

  public boolean canRemoveAccessor() {
    return removeAccessor;
  }

  public void setRemoveAccessor(boolean removeAccessor) {
    this.removeAccessor = removeAccessor;
  }
}