package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.auction.Auction;
import com.github.tnerevival.core.auction.Bid;
import com.github.tnerevival.core.auction.Claim;
import com.github.tnerevival.core.collection.EventList;
import com.github.tnerevival.core.collection.EventMap;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Daniel on 10/17/2016.
 */
public class AuctionManager {
  private Map<Integer, Auction> auctionQueue = new EventMap<>();
  private Map<Integer, Auction> active = new EventMap<>();
  public List<Claim> unclaimed = new EventList<>();

  private int lastLot = 0;

  public void auctionMessage(CommandSender sender, String message, Auction auction, boolean check) {
    String id = (sender instanceof Player)? IDFinder.getID((Player)sender).toString() : "";
    String world = (sender instanceof Player)? MISCUtils.getWorld((Player)sender) : TNE.instance.defaultWorld;

    Message send = new Message(message);
    send.addVariable("$start", CurrencyFormatter.format(auction.getWorld(), auction.getCost().getAmount()));
    send.addVariable("$item", auction.getItem().toItemStack().getType().name());
    send.addVariable("$lot", auction.getLotNumber() + "");
    if(auction.getHighestBid() != null) {
      send.addVariable("$amount", CurrencyFormatter.format(world, auction.getHighestBid().getBid().getAmount()));
      send.addVariable("$player", MISCUtils.getPlayer(auction.getHighestBid().getBidder()).getDisplayName());
    }

    MISCUtils.debug("Auction Message");
    MISCUtils.debug(message);
    if(!check || TNE.instance.api.getBoolean("Core.Auctions.Announce", auction.getWorld(), id)) {
      if (auction.getNode().equalsIgnoreCase("") || sender.hasPermission(auction.getNode())) {
        send.translate(world, sender);
      }
    }
  }

  public void notifyPlayers(String world, Integer lot, Boolean ignore) {
    Auction a = getActive(world, lot);
    String message = ChatColor.WHITE + "Auction started for " + a.getItem().getName() + " starting bid is " + ChatColor.GOLD + CurrencyFormatter.format(world, a.getCost().getAmount()) + ChatColor.WHITE + ".";
    if(a.getStartTime() != System.nanoTime()) {
      message = ChatColor.WHITE + "The auction for " + a.getItem().getName() + " will end in " + ChatColor.GREEN + a.remaining() + ChatColor.WHITE + ".";
    }
    notifyPlayers(world, lot, message, ignore, true);
    notifyPlayers(world, lot, ChatColor.WHITE + "Type /auction info " + ChatColor.GREEN + a.getLotNumber() + ChatColor.WHITE + " for more information.", ignore, true);
  }

  public void notifyPlayers(String world, Integer lot, String message, Boolean ignore) {
    notifyPlayers(world, lot, message, ignore, false);
  }

  public void notifyPlayers(String world, Integer lot, String message, Boolean ignore, Boolean check) {
    Auction a = getActive(world, lot);
    if(ignore || !a.getSilent()) {
      Collection<? extends Player> players = (world.equalsIgnoreCase("Global")) ? Bukkit.getOnlinePlayers() : Bukkit.getWorld(world).getPlayers();
      for (Player p : players) {
        auctionMessage(p, message, a, check);
      }
    }
  }

  public void end(String world, Integer lot, Boolean pickWinner) {
    Auction a = getActive(world, lot);

    if(pickWinner && a.getHighestBid() != null) {
      UUID winner = a.getHighestBid().getBidder();
      TransactionCost bid = a.getHighestBid().getBid();

      if (AccountUtils.transaction(winner.toString(), null, bid, TransactionType.MONEY_INQUIRY, world)) {
        AccountUtils.transaction(winner.toString(), a.getPlayer().toString(), bid, TransactionType.MONEY_PAY, world);
        if(Bukkit.getOnlinePlayers().contains(MISCUtils.getPlayer(winner))) {
          MISCUtils.getPlayer(winner).getInventory().addItem(a.getItem().toItemStack());
          Message win = new Message("Messages.Auction.Won");
          win.addVariable("$item", a.getItem().getName());
          win.translate(world, MISCUtils.getPlayer(winner));
        } else {
          Claim claim = new Claim(winner, a.getLotNumber(), a.getItem(), a.getCost());
          unclaimed.add(claim);
        }

        notifyPlayers(world, lot, "Messages.Auction.Winner", true);

        Message paid = new Message("Messages.Auction.Paid");
        paid.addVariable("$amount", CurrencyFormatter.format(world, bid.getAmount()));
        paid.translate(world, MISCUtils.getPlayer(a.getPlayer()));
        active.remove(lot);

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
    active.remove(lot);
  }

  public Boolean cancel(Integer lot, UUID id, String world) {
    Auction a = getAuction(lot);
    Player player =  MISCUtils.getPlayer(id);

    boolean requiresAdmin = (isActive(lot) && a.getHighestBid() != null  || !a.getPlayer().equals(id));

    if(requiresAdmin && !player.hasPermission("tne.bypass.auction")) {
      if(isActive(lot) && a.getHighestBid() != null) {
        new Message("Messages.Auction.NoCancel").translate(world, player);
        return false;
      }
      new Message("Messages.General.NoPerm").translate(world, player);
      return false;
    }

    end(world, lot, false);
    Message cancelled = new Message("Messages.Auction.Cancelled");
    cancelled.addVariable("$lot", lot + "");
    cancelled.translate(world, MISCUtils.getPlayer(id));
    return true;
  }

  public void claim(Integer lot, UUID player) {
    Iterator<Claim> i = unclaimed.iterator();

    while(i.hasNext()) {
      Claim claim = i.next();

      if(claim.getLot().equals(lot) && claim.getPlayer().equals(player)) {
        TNE.instance.saveManager.deleteClaim(player, lot);
        claim.claim();
        i.remove();
      }
    }
  }

  public List<Integer> unclaimed(UUID player) {
    List<Integer> lots = new ArrayList<>();
    for(Claim claim : unclaimed) {
      if(claim.getPlayer().equals(player)) {
        lots.add(claim.getLot());
      }
    }
    return lots;
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
      auction.setStartTime(System.nanoTime());
      active.put(lot, auction);
      TNE.instance.saveManager.deleteAuction(lot);
      notifyPlayers(auction.getWorld(), auction.getLotNumber(), "Messages.Auction.Start", true);
    }
  }

  public Boolean bid(String world, Integer lot, UUID player, TransactionCost bid) {
    Auction auction = getActive(world, lot);

    Boolean snipeProtection = TNE.instance.api.getBoolean("Core.Auctions.AntiSnipe", world, player.toString());
    Integer snipeTime = TNE.instance.api.getInteger("Core.Auctions.SnipeTime", world, player.toString());
    Integer snipePeriod = TNE.instance.api.getInteger("Core.Auctions.SnipePeriod",world, player.toString());

    Double minimum = auction.getCost().getAmount();
    if(auction.getHighestBid() != null) {
      minimum = auction.getHighestBid().getBid().getAmount() + auction.getIncrement();
    }

    if(!AccountUtils.transaction(player.toString(), null, bid, TransactionType.MONEY_INQUIRY, world)) {
      Message insufficient = new Message("Messages.Money.Insufficient");
      insufficient.addVariable("$amount", CurrencyFormatter.format(world, AccountUtils.round(bid.getAmount())));
      insufficient.translate(world, MISCUtils.getPlayer(player));
      return false;
    }

    if(bid.getAmount() < minimum) {
      if(!auction.getSilent()) {
        Message under = new Message("Messages.Auction.Under");
        under.addVariable("$amount", CurrencyFormatter.format(world, minimum));
        under.translate(world, MISCUtils.getPlayer(player));
      }
      return false;
    }

    if(snipeProtection && auction.remaining() <= snipePeriod) {
      active.get(lot).setTime(auction.getTime() + snipeTime);
      Message snipe = new Message("Messages.Auction.AntiSnipe");
      snipe.addVariable("$time", snipeTime + "");
      notifyPlayers(world, lot, snipe.grab(world, MISCUtils.getPlayer(player)), false);
    }
    active.get(lot).setHighestBid(new Bid(player, bid));

    Message submitted = new Message("Messages.Auction.Submitted");
    submitted.addVariable("$amount", CurrencyFormatter.format(world, bid.getAmount()));
    submitted.addVariable("$lot", lot + "");
    submitted.translate(world, MISCUtils.getPlayer(player));

    notifyPlayers(world, lot, "Messages.Auction.Bid", false);
    return true;
  }

  public List<Integer> getLots(String scope, Integer page, UUID player) {
    List<Auction> iterate = new ArrayList<>();
    iterate.addAll(active.values());
    iterate.addAll(auctionQueue.values());
    Boolean matchWorld = scope.equals("world");
    Boolean matchPlayer = scope.equals("player");
    Integer max = 5;
    Integer start = (page.equals(0))? 0 : max * (page - 1);
    List<Integer> lots = new ArrayList<>();

    for(int i = start; i < start + max; i++) {
      if(i >= iterate.size()) continue;
      lots.add(iterate.get(i).getLotNumber());
    }

    return lots;
  }

  public Auction getAuction(Integer lot) {
    if(exists(lot)) {
      if(isQueued(lot)) {
        return auctionQueue.get(lot);
      }
      return active.get(lot);
    }
    return null;
  }

  public Boolean exists(Integer lot) {
    return isQueued(lot) || isActive(lot);
  }

  public Boolean isQueued(Integer lot) {
    return auctionQueue.containsKey(lot);
  }

  public Boolean isActive(Integer lot) {
    return active.containsKey(lot);
  }

  public Integer getQueued(UUID id) {
    Integer queued = 0;

    for(Auction a : auctionQueue.values()) {
      if(a.getPlayer().equals(id)) {
        queued++;
      }
    }
    return queued;
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

  public Boolean add(Auction auction) {
    MISCUtils.debug("Auction Add");
    if(auction.getSilent() && !MISCUtils.getPlayer(auction.getPlayer()).hasPermission("tne.auction.sauction")) {
      new Message("Messages.General.NoPerm").translate(auction.getWorld(), auction.getPlayer());
      return false;
    }

    Double cost = TNE.instance.api.getDouble("Core.Auctions.Cost", auction.getWorld(), auction.getPlayer());

    if(cost > 0.0 && !AccountUtils.transaction(auction.getPlayer().toString(), null, cost, TNE.instance.manager.currencyManager.get(auction.getWorld()), TransactionType.MONEY_INQUIRY, auction.getWorld())) {
      Message insufficient = new Message("Messages.Money.Insufficient");
      insufficient.addVariable("$amount", CurrencyFormatter.format(auction.getWorld(), AccountUtils.round(cost)));
      insufficient.translate(auction.getWorld(), MISCUtils.getPlayer(auction.getPlayer()));
      return false;
    }
    MISCUtils.debug("Cost: " + cost);

    if(getQueued(auction.getPlayer()) >= TNE.instance.api.getInteger("Core.Auctions.PersonalQueue", auction.getWorld(), auction.getPlayer())) {
      new Message("Messages.Auction.PersonalQueue").translate(MISCUtils.getWorld(auction.getPlayer()), MISCUtils.getPlayer(auction.getPlayer()));
      return false;
    }

    if(getQueued(auction.getWorld()) >= TNE.instance.api.getInteger("Core.Auctions.MaxQueue", auction.getWorld(), auction.getPlayer())) {
      new Message("Messages.Auction.MaxQueue").translate(MISCUtils.getWorld(auction.getPlayer()), MISCUtils.getPlayer(auction.getPlayer()));
      return false;
    }
    auction.setLotNumber(lastLot + 1);
    MISCUtils.getPlayer(auction.getPlayer()).getInventory().removeItem(auction.getItem().toItemStack());
    AccountUtils.transaction(auction.getPlayer().toString(), null, cost, TNE.instance.manager.currencyManager.get(auction.getWorld()), TransactionType.MONEY_REMOVE, auction.getWorld());
    if(canStart(auction.getWorld(), auction.getPlayer().toString())) {
      MISCUtils.debug("Starting Auction");
      auction.setStartTime(System.nanoTime());
      active.put(auction.getLotNumber(), auction);
      TNE.instance.saveManager.deleteAuction(auction.getLotNumber());
      notifyPlayers(auction.getWorld(), auction.getLotNumber(), "Messages.Auction.Start", true);
      notifyPlayers(auction.getWorld(), auction.getLotNumber(), ChatColor.WHITE + "Type /auction info " + ChatColor.GREEN + auction.getLotNumber() + ChatColor.WHITE + " for more information.", true);
      return true;
    }
    lastLot++;
    MISCUtils.debug("Queueing Auction");
    auctionQueue.put(auction.getLotNumber(), auction);
    Message queued = new Message("Messages.Auction.Queued");
    queued.addVariable("$lot", auction.getLotNumber() + "");
    queued.translate(MISCUtils.getWorld(auction.getPlayer()), MISCUtils.getPlayer(auction.getPlayer()));
    return true;
  }

  public Boolean canStart(String world, String player) {
    Boolean canWorld = TNE.instance.api.getBoolean("Core.Auctions.AllowWorld", world, player);
    Integer activeCount = (canWorld)? activeCount(world) : active.size();
    Integer maxCount = getMaxActive(world, player);
    return activeCount < maxCount;
  }

  public Boolean requireLot(String world) {
    return TNE.instance.api.getBoolean("Core.Auctions.AllowWorld", world, "")
        || TNE.instance.api.getBoolean("Core.Auctions.Multiple", world, "");
  }

  public Integer getLot(String world) {
    Boolean canWorld = TNE.instance.api.getBoolean("Core.Auctions.AllowWorld", world, "");

    for(Auction a : active.values()) {
      if(!canWorld || a.getWorld().equalsIgnoreCase(world)) {
        return a.getLotNumber();
      }
    }
    return(active.size() > 0)? active.get(0).getLotNumber() : 0;
  }

  public void remove(Integer lot) {
    auctionQueue.remove(lot);
  }

  public Collection<Auction> getJoined() {
    Collection<Auction> joined = auctionQueue.values();
    joined.addAll(active.values());

    return joined;
  }
}