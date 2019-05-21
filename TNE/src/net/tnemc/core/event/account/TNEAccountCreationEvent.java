package net.tnemc.core.event.account;

import net.tnemc.core.common.account.TNEAccount;
import org.bukkit.event.Cancellable;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/7/2017.
 */
public class TNEAccountCreationEvent extends TNEAccountEvent implements Cancellable {

  private TNEAccount account;
  private boolean cancelled = false;

  public TNEAccountCreationEvent(UUID id, TNEAccount account, boolean async) {
    super(id, async);

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