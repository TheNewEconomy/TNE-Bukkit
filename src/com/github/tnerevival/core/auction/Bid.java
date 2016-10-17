package com.github.tnerevival.core.auction;

import com.github.tnerevival.core.transaction.TransactionCost;

import java.util.UUID;

/**
 * Created by Daniel on 10/17/2016.
 */
public class Bid {

  private UUID bidder;
  private TransactionCost bid;

  public Bid(UUID bidder, TransactionCost bid) {
    this.bidder = bidder;
    this.bid = bid;
  }

  public UUID getBidder() {
    return bidder;
  }

  public void setBidder(UUID bidder) {
    this.bidder = bidder;
  }

  public TransactionCost getBid() {
    return bid;
  }

  public void setBid(TransactionCost bid) {
    this.bid = bid;
  }
}
