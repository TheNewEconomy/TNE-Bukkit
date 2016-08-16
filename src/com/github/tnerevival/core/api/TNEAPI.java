package com.github.tnerevival.core.api;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class TNEAPI {
	
	public TNE plugin;
	
	public TNEAPI(TNE plugin) {
		this.plugin = plugin;
	}
	
	@Deprecated
	public Boolean accountExists(String username) {
		return AccountUtils.getAccount(getPlayerID(username)) != null;
	}

	@Deprecated
	public void createAccount(String username) {
		AccountUtils.createAccount(getPlayerID(username));
	}

	@Deprecated
	public void fundsAdd(String username, Double amount) {
		fundsAdd(username, MISCUtils.getWorld(getPlayerID(username)), amount);
	}

	@Deprecated
	public void fundsAdd(String username, String world, Double amount) {
    AccountUtils.transaction(getPlayerID(username).toString(), null, amount, TransactionType.MONEY_GIVE, world);
	}

	@Deprecated
	public Boolean fundsHas(String username, Double amount) {
    return fundsHas(username, MISCUtils.getWorld(getPlayerID(username)), amount);
	}

	@Deprecated
	public Boolean fundsHas(String username, String world, Double amount) {
    return AccountUtils.transaction(getPlayerID(username).toString(), null, amount, TransactionType.MONEY_INQUIRY, world);
	}

	@Deprecated
	public void fundsRemove(String username, Double amount) {
		fundsRemove(username, MISCUtils.getWorld(getPlayerID(username)), amount);
	}

	@Deprecated
	public void fundsRemove(String username, String world, Double amount) {
		AccountUtils.transaction(getPlayerID(username).toString(), null, amount, TransactionType.MONEY_REMOVE, world);
	}

	@Deprecated
	public Account getAccount(String username) {
		Account account = AccountUtils.getAccount(getPlayerID(username));
		return account;
	}

	@Deprecated
	public Double getBalance(String username) {
		return AccountUtils.getFunds(getPlayerID(username));
	}

	@Deprecated
	public Double getBalance(String username, String world) {
		return AccountUtils.getFunds(getPlayerID(username), world);
	}
	
	public Boolean accountExists(OfflinePlayer player) {
		return getAccount(player) != null;
	}

	public void createAccount(OfflinePlayer player) {
		AccountUtils.createAccount(MISCUtils.getID(player));
	}

	public void fundsAdd(OfflinePlayer player, Double amount) {
    fundsAdd(player, MISCUtils.getWorld(getPlayerID(player.getName())), amount);
	}

	public void fundsAdd(OfflinePlayer player, String world, Double amount) {
    AccountUtils.transaction(MISCUtils.getID(player.getPlayer()).toString(), null, amount, TransactionType.MONEY_GIVE, world);
	}

	public Boolean fundsHas(OfflinePlayer player, Double amount) {
		return fundsHas(player, MISCUtils.getWorld(getPlayerID(player.getName())), amount);
	}

	public Boolean fundsHas(OfflinePlayer player, String world, Double amount) {
		return AccountUtils.transaction(MISCUtils.getID(player.getPlayer()).toString(), null, amount, TransactionType.MONEY_INQUIRY, world);
	}

	public void fundsRemove(OfflinePlayer player, Double amount) {
		fundsRemove(player, MISCUtils.getWorld(getPlayerID(player.getName())), amount);
	}

	public void fundsRemove(OfflinePlayer player, String world, Double amount) {
    AccountUtils.transaction(MISCUtils.getID(player.getPlayer()).toString(), null, amount, TransactionType.MONEY_REMOVE, world);
	}

	public Account getAccount(OfflinePlayer player) {
		return AccountUtils.getAccount(MISCUtils.getID(player));
	}

	public Double getBalance(OfflinePlayer player) {
		return AccountUtils.getFunds(MISCUtils.getID(player));
	}

	public Double getBalance(OfflinePlayer player, String world) {
		return AccountUtils.getFunds(MISCUtils.getID(player), world);
	}

	public String format(Double amount) {
		return MISCUtils.formatBalance(plugin.defaultWorld, amount);
	}

	public String format(String world, Double amount) {
		return MISCUtils.formatBalance(world, amount);
	}

	public String format(String world, Double amount, Boolean shorten) {
		return MISCUtils.formatBalance(world, amount, shorten);
	}

	public String getCurrencyName(Boolean major, Boolean singular) {
		return getCurrencyName(major, singular, plugin.defaultWorld);
	}

	public String getCurrencyName(Boolean major, Boolean singular, String world) {
		return (major) ? MISCUtils.getMajorCurrencyName(world, singular) : MISCUtils.getMinorCurrencyName(world, singular);
	}
	
	public Boolean getShorten() {
		return getShorten(plugin.defaultWorld);
	}
	
	public Boolean getShorten(String world) {
		return MISCUtils.shorten(world);
	}
	
	public UUID getPlayerID(String username) {
		if(username.contains("faction-")) {
			return UUID.fromString(username.substring(8, username.length() - 1));
		}
		return MISCUtils.getID(username);
	}
}