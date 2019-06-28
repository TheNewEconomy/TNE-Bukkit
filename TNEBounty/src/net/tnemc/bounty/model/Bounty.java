package net.tnemc.bounty.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/28/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class Bounty {

  //Our standard data for each bounty
  private UUID id;
  private UUID target;
  private UUID benefactor;
  private long created;

  //Our bounty claim data for each bounty
  private boolean requireHead = false;
  private long claimedTime = 0;
  private boolean claimed = false;
  private UUID claimant = null;

  //Our reward data for each bounty
  private boolean currencyReward = true;
  private BigDecimal amount = BigDecimal.ZERO;
  private String itemReward = null;

  public Bounty(UUID target, UUID benefactor) {
    this.id = UUID.randomUUID();
    this.target = target;
    this.benefactor = benefactor;
    this.created = new Date().getTime();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getTarget() {
    return target;
  }

  public void setTarget(UUID target) {
    this.target = target;
  }

  public UUID getBenefactor() {
    return benefactor;
  }

  public void setBenefactor(UUID benefactor) {
    this.benefactor = benefactor;
  }

  public long getCreated() {
    return created;
  }

  public void setCreated(long created) {
    this.created = created;
  }

  public boolean isRequireHead() {
    return requireHead;
  }

  public void setRequireHead(boolean requireHead) {
    this.requireHead = requireHead;
  }

  public long getClaimedTime() {
    return claimedTime;
  }

  public void setClaimedTime(long claimedTime) {
    this.claimedTime = claimedTime;
  }

  public boolean isClaimed() {
    return claimed;
  }

  public void setClaimed(boolean claimed) {
    this.claimed = claimed;
  }

  public UUID getClaimant() {
    return claimant;
  }

  public void setClaimant(UUID claimant) {
    this.claimant = claimant;
  }

  public boolean isCurrencyReward() {
    return currencyReward;
  }

  public void setCurrencyReward(boolean currencyReward) {
    this.currencyReward = currencyReward;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getItemReward() {
    return itemReward;
  }

  public void setItemReward(String itemReward) {
    this.itemReward = itemReward;
  }
}