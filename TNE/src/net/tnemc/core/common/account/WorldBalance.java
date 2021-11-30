package net.tnemc.core.common.account;

import com.github.tnerevival.core.collection.EventMap;
import net.tnemc.core.TNE;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.sql.SQLException;
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
public class WorldBalance {

  private EventMap<String, BigDecimal> balances = new EventMap<>();

  private UUID uuid;
  private String world;

  public WorldBalance(UUID uuid, String world) {
    this.uuid = uuid;
    this.world = world;
  }

  public EventMap<String, BigDecimal> getBalances() {
    return balances;
  }

  public void setBalance(String currency, BigDecimal balance) {
    setBalance(currency, balance, true);
  }

  public void setBalance(String currency, BigDecimal balance, boolean update) {
    this.balances.put(currency, balance);

    if(update) {
      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{

        try {
          TNE.saveManager().getTNEManager().getTNEProvider().saveBalance(uuid, world, currency, balance);
        } catch (SQLException e) {
          TNE.debug(e);
        }
      });
    }
  }

  public BigDecimal getBalance(String currency) {
    return balances.getOrDefault(currency, BigDecimal.ZERO);
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }
}