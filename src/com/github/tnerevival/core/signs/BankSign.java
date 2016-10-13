package com.github.tnerevival.core.signs;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BankSign extends TNESign {

	public BankSign(UUID owner) {
		super(owner);
		setType(SignType.BANK);
	}

	@Override
	public boolean onClick(Player player) {
		return false;
	}

	@Override
	public boolean onRightClick(Player player) {
		if(super.onRightClick(player)) {
			if (player.hasPermission(SignType.BANK.getUsePermission())) {

				if (!BankUtils.hasBank(MISCUtils.getID(player))) {
					new Message("Messages.Bank.None").translate(MISCUtils.getWorld(player), player);
					return false;
				}

				if (!BankUtils.sign(MISCUtils.getWorld(player), MISCUtils.getID(player).toString())) {
					new Message("Messages.Bank.NoSign").translate(MISCUtils.getWorld(player), player);
					return false;
				}

				inventory = BankUtils.getBankInventory(MISCUtils.getID(player));
				if (super.onOpen(player)) {
					player.openInventory(inventory);
					return true;
				}
			}
		}
		return false;
	}
}