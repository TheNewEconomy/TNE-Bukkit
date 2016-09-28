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
		if(player.hasPermission(SignType.BANK.getUsePermission())) {

		  if(!BankUtils.hasBank(MISCUtils.getID(player))) {
        player.sendMessage(new Message("Messages.Bank.None").translate());
        return false;
      }

      if(!BankUtils.sign(MISCUtils.getWorld(player))) {
        player.sendMessage(new Message("Messages.Bank.NoSign").translate());
        return false;
      }

			inventory = BankUtils.getBankInventory(MISCUtils.getID(player));
			if(super.onOpen(player)) {
				player.openInventory(inventory);
				return true;
			}
		}
		return false;
	}
}