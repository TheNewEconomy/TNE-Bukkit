package net.tnemc.core.common.account.history;

import net.tnemc.core.common.transaction.TNETransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 07/05/2017.
 */
public class WorldHistory {

  private List<UUID> history = new ArrayList<>();

  private String world;

  public WorldHistory(String world) {
    this.world = world;
  }

  public void addTransaction(TNETransaction transaction) {
    history.add(transaction.transactionID());
  }

  public List<UUID> getTransactions() {
    return history;
  }

  public String getWorld() {
    return world;
  }
}