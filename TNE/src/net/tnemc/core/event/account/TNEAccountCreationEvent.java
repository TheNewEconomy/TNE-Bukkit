package net.tnemc.core.event.account;

import net.tnemc.core.common.account.TNEAccount;
import org.bukkit.event.Cancellable;

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
 * Created by Daniel on 7/7/2017.
 */
public class TNEAccountCreationEvent extends TNEAccountEvent implements Cancellable {

  private TNEAccount account;
  private boolean cancelled = false;

  public TNEAccountCreationEvent(UUID id, TNEAccount account) {
    super(id);

    this.account = account;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public TNEAccount getAccount() {
    return account;
  }

  public void setAccount(TNEAccount account) {
    this.account = account;
  }
}