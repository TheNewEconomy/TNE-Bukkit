package com.github.tnerevival.core;

import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Daniel on 10/17/2016.
 */
public class AuctionManager {
  private Map<Integer, Auction> auctionQueue = new HashMap<>();
  private Map<String, Auction> active = new HashMap<>();

  private int lastLot = 0;

  public void notifyPlayers(String world) {
    Auction a = getActive(world);
    if(!a.getSilent()) {
      String message = a.getNotification();
      Collection<? extends Player> players = (world.equalsIgnoreCase("Global")) ? Bukkit.getOnlinePlayers() : Bukkit.getWorld(world).getPlayers();
      for (Player p : players) {
        if(a.getNode().equalsIgnoreCase("") || !a.getNode().equalsIgnoreCase("") && p.hasPermission(a.getNode())) {
          p.sendMessage(message);
        }
      }
    }
  }

  public void end(String world) {
    Auction a = getActive(world);
    active.remove(world);
    UUID winner = a.getHighestBid().getBidder();
    TransactionCost bid = a.getHighestBid().getBid();

    if(AccountUtils.transaction(winner.toString(), null, bid, TransactionType.MONEY_INQUIRY, world)) {
      AccountUtils.transaction(a.getPlayer().toString(), winner.toString(), bid, TransactionType.MONEY_INQUIRY, world);
      MISCUtils.getPlayer(winner).getInventory().addItem(a.getItem().toItemStack());
      Message win = new Message("Messages.Auction.Winner");
      win.addVariable("$item", a.getItem().getName());
      win.translate(world, MISCUtils.getPlayer(winner));

      Message paid = new Message("Messages.Auction.Paid");
      paid.addVariable("$amount", MISCUtils.formatBalance(world, bid.getAmount()));
      paid.translate(world, MISCUtils.getPlayer(a.getPlayer()));

      startNext(world);
      return;
    }
    MISCUtils.getPlayer(a.getPlayer()).getInventory().addItem(a.getItem().toItemStack());
    new Message("Messages.Auction.Return").translate(world, MISCUtils.getPlayer(a.getPlayer()));
  }

  public Boolean hasActive(String world) {
    return active.containsKey(world);
  }

  public Auction getActive(String world) {
    if(active.containsKey(world)) {
      return active.get(world);
    }
    return null;
  }

  public void startNext(String world) {
    Auction auction = null;
    Integer lot = 10000;

    for(Auction a : auctionQueue.values()) {
      String w = (a.getGlobal())? "Global" : world;
      if(w.equalsIgnoreCase(world) && a.getLotNumber() < lot) {
        lot = a.getLotNumber();
        auction = a;
      }
    }

    if(auction != null) {
      auctionQueue.remove(lot);
      active.put(world, auction);
    }
  }

  public void add(Auction auction) {
    auction.setLotNumber(lastLot + 1);
    auctionQueue.put(auction.getLotNumber(), auction);
  }

  public void remove(Integer lot) {
    auctionQueue.remove(lot);
  }
}