package net.tnemc.core.common;

import net.tnemc.core.common.account.WorldBalance;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 11/29/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BalanceManager {

  private Map<String, WorldBalance> worldBalances = new HashMap<>();

  private UUID uuid;

  public BalanceManager(UUID uuid) {
    this.uuid = uuid;
  }

  public Map<String, WorldBalance> getWorldBalances() {
    return worldBalances;
  }

  public void setWorldBalances(Map<String, WorldBalance> worldBalances) {
    this.worldBalances = worldBalances;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public BigDecimal getBalance(String world, String currency) {
    if(worldBalances.containsKey(world)) {
      return worldBalances.get(world).getBalance(currency);
    }
    return BigDecimal.ZERO;
  }

  public void setBalance(String world, String currency, BigDecimal balance) {
    setBalance(world, currency, balance, true);
  }

  public void setBalance(String world, String currency, BigDecimal balance, boolean update) {

    WorldBalance bal = worldBalances.getOrDefault(world, new WorldBalance(uuid, world));
    bal.setBalance(currency, balance, update);
    worldBalances.put(world, bal);
  }
}