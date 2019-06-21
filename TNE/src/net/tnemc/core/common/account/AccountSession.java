package net.tnemc.core.common.account;

import java.util.Calendar;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/21/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AccountSession {

  private UUID sessionID;
  private UUID account;
  private long expiration;

  public AccountSession(UUID sessionID, UUID account) {
    this.sessionID = sessionID;
    this.account = account;

    Calendar now = Calendar.getInstance();
    now.add(Calendar.MINUTE, 30);

    this.expiration = now.getTimeInMillis();
  }

  public boolean expired() {
    return Calendar.getInstance().getTimeInMillis() >= this.expiration;
  }

  public UUID getSessionID() {
    return sessionID;
  }

  public void setSessionID(UUID sessionID) {
    this.sessionID = sessionID;
  }

  public UUID getAccount() {
    return account;
  }

  public void setAccount(UUID account) {
    this.account = account;
  }
}