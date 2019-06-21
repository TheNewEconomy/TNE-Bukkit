package net.tnemc.core.common;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.AccountSession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
public class SessionManager {

  private Map<UUID, AccountSession> sessions = new HashMap<>();

  public boolean hasSession(UUID identifier) {

    Iterator<Map.Entry<UUID, AccountSession>> it = sessions.entrySet().iterator();

    while(it.hasNext()) {
      Map.Entry<UUID, AccountSession> entry = it.next();

      if(entry.getValue().expired()) {
        it.remove();
        continue;
      }

      if(entry.getValue().getAccount().equals(identifier)) return true;
    }
    return false;
  }

  public boolean sessionIDValid(UUID id, UUID account) {
    return sessions.containsKey(id) && sessions.get(id).getAccount().equals(account) && !sessions.get(id).expired();
  }

  public boolean createSession(UUID account, String pin) {
    if(!TNE.manager().exists(account) || TNE.manager().getAccount(account).getPin().trim().equals("TNEPIN")
        || !TNE.manager().getAccount(account).getPin().equals(pin)) return false;

    final UUID sessionID = generateUUID();
    sessions.put(sessionID, new AccountSession(generateUUID(), account));
    return true;
  }

  private UUID generateUUID() {
    UUID id = UUID.randomUUID();

    while(sessions.containsKey(id)) {
      id = UUID.randomUUID();
    }
    return id;
  }
}