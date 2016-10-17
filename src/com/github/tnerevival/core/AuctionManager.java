package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.auction.Bid;
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
  private Map<Integer, Auction> active = new HashMap<>();

  private int lastLot = 0;

  public void auctionMessage(Player p, String message, Auction auction) {
    Message send = null;

    if(message.contains("Messages.")) {
      send = new Message(message);
      send.addVariable("$start", MISCUtils.formatBalance(auction.getWorld(), auction.getStart()));
      send.addVariable("$lot", auction.getLotNumber() + "");
      if(auction.getHighestBid() != null) {
        send.addVariable("$amount", MISCUtils.formatBalance(MISCUtils.getWorld(p), auction.getHighestBid().getBid().getAmount()));
        send.addVariable("$player", MISCUtils.getPlayer(auction.getHighestBid().getBidder()).getDisplayName());
      }
    }

    if(TNE.instance.api.getBoolean("Core.Auctions.Announce", auction.getWorld(), MISCUtils.getID(p).toString())) {
      if (auction.getNode().equalsIgnoreCase("") || !auction.getNode().equalsIgnoreCase("") && p.hasPermission(auction.getNode())) {
        if(send != null) {
          send.translate(MISCUtils.getWorld(p), p);
          return;
        }
        p.sendMessage(message);
      }
    }
  }

  public void notifyPlayers(String world, Integer lot, Boolean ignore) {
    Auction a = getActive(world, lot);
    notifyPlayers(world, lot, a.getNotification(), ignore);
  }

  public void notifyPlayers(String world, Integer lot, String message, Boolean ignore) {
    Auction a = getActive(world, lot);
    if(!a.getSilent() && !ignore) {
      Collection<? extends Player> players = (world.equalsIgnoreCase("Global")) ? Bukkit.getOnlinePlayers() : Bukkit.getWorld(world).getPlayers();
      for (Player p : players) {
        auctionMessage(p, message, a);
      }
    }
  }

  public void end(String world, Integer lot) {
    Auction a = getActive(world, lot);

    if(a.getHighestBid() != null) {
      UUID winner = a.getHighestBid().getBidder();
      TransactionCost bid = a.getHighestBid().getBid();

      if (AccountUtils.transaction(winner.toString(), null, bid, TransactionType.MONEY_INQUIRY, world)) {
        AccountUtils.transaction(a.getPlayer().toString(), winner.toString(), bid, TransactionType.MONEY_INQUIRY, world);
        MISCUtils.getPlayer(winner).getInventory().addItem(a.getItem().toItemStack());
        Message win = new Message("Messages.Auction.Won");
        win.addVariable("$item", a.getItem().getName());
        win.translate(world, MISCUtils.getPlayer(winner));

        notifyPlayers(world, lot, "Messages.Auction.Winner", true);

        Message paid = new Message("Messages.Auction.Paid");
        paid.addVariable("$amount", MISCUtils.formatBalance(world, bid.getAmount()));
        paid.translate(world, MISCUtils.getPlayer(a.getPlayer()));

        String w = (a.getGlobal())? "Global" : a.getWorld();
        startNext(w);
        return;
      }
      MISCUtils.getPlayer(a.getPlayer()).getInventory().addItem(a.getItem().toItemStack());
      new Message("Messages.Auction.FailedReturn").translate(world, MISCUtils.getPlayer(a.getPlayer()));
      return;
    }
    notifyPlayers(world, lot, "Messages.Auction.NoWinner", true);
    MISCUtils.getPlayer(a.getPlayer()).getInventory().addItem(a.getItem().toItemStack());
    new Message("Messages.Auction.Return").translate(world, MISCUtils.getPlayer(a.getPlayer()));
    active.remove(world);
  }

  public Boolean hasActive(String world) {
    return activeCount(world) > 0;
  }

  public List<Auction> getActive(String world) {
    List<Auction> worldActive = new ArrayList<>();
    for(Auction a : active.values()) {
      if(a.getWorld().equalsIgnoreCase(world) || world.equalsIgnoreCase("global") && a.getGlobal()) {
        worldActive.add(a);
      }
    }
    return worldActive;
  }

  public Auction getActive(String world, Integer lot) {
    if(activeCount(world) > 0) {
      return active.get(lot);
    }
    return null;
  }

  public Integer activeCount(String world) {
    Integer activeCount = 0;

    for(Auction a : active.values()) {
      if(a.getWorld().equalsIgnoreCase(world)) {
        activeCount++;
      }
    }
    return activeCount;
  }

  public Integer getMaxActive(String world, String player) {
    Integer max = 1;
    if(TNE.instance.api.getBoolean("Core.Auctions.Multiple", world, player)) {
      max = TNE.instance.api.getInteger("Core.Auctions.MaxMultiple", world, player);
    }
    return max;
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
      active.put(lot, auction);
      notifyPlayers(auction.getWorld(), auction.getLotNumber(), "Messages.Auction.Start", true);
    }
  }

  public Boolean bid(String world, Integer lot, UUID player, TransactionCost bid) {
    Auction auction = getActive(world, lot);

    Boolean snipeProtection = TNE.instance.api.getBoolean("Core.Auctions.AntiSnipe", world, player.toString());
    Integer snipeTime = TNE.instance.api.getInteger("Core.Auctions.SnipeTime", world, player.toString());
    Integer snipePeriod = TNE.instance.api.getInteger("Core.Auctions.SnipePeriod",world, player.toString());

    Double minimum = auction.getStart();
    if(auction.getHighestBid() != null) {
      minimum = auction.getHighestBid().getBid().getAmount() + auction.getIncrement();
    }

    if(bid.getAmount() < minimum) {
      Message under = new Message("Messages.Auction.Under");
      under.addVariable("$amount", MISCUtils.formatBalance(world, minimum));
      under.translate(world, MISCUtils.getPlayer(player));
      return false;
    }

    if(snipeProtection && auction.remaining() <= snipePeriod) {
      active.get(world).setTime(auction.getTime() + snipeTime);
      notifyPlayers(world, lot, "Messages.Auction.AntiSnipe", false);
    }
    active.get(world).setHighestBid(new Bid(player, bid));
    notifyPlayers(world, lot, "Messages.Auction.Bid", false);
    return true;
  }

  public Integer getQueued(String world) {
    Integer queued = 0;

    for(Auction a : auctionQueue.values()) {
      if(a.getWorld().equalsIgnoreCase(world) || world.equalsIgnoreCase("global") && a.getGlobal()) {
        queued++;
      }
    }
    return queued;
  }

  public void add(Auction auction) {
    if(getQueued(auction.getWorld()) >= TNE.instance.api.getInteger("Core.Auctions.MaxQueue", auction.getWorld(), auction.getPlayer().toString())) {
      return;
    }
    auction.setLotNumber(lastLot + 1);
    if(activeCount(auction.getWorld()) < getMaxActive(auction.getWorld(), auction.getPlayer().toString())) {
      active.put(auction.getLotNumber(), auction);
      notifyPlayers(auction.getWorld(), auction.getLotNumber(), "Messages.Auction.Start", true);
      return;
    }
    auctionQueue.put(auction.getLotNumber(), auction);
  }

  public void remove(Integer lot) {
    auctionQueue.remove(lot);
  }
}