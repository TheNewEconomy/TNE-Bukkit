package net.tnemc.banks.regional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/2/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class RegionalBankAccount {

  private Map<String, BigDecimal> holdings = new HashMap<>();

  private UUID owner;
  private long created;
  private UUID regionalBank;

  private boolean doInterest;
  private double interest;

  public RegionalBankAccount(UUID owner, long created, UUID regionalBank) {
    this.owner = owner;
    this.created = created;
    this.regionalBank = regionalBank;
  }
}