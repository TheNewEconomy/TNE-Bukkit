package net.tnemc.core.common.account;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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