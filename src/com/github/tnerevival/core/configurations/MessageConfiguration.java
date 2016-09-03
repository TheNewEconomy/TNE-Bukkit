package com.github.tnerevival.core.configurations;

import org.bukkit.configuration.file.FileConfiguration;

public class MessageConfiguration extends Configuration {

	@Override
	public void load(FileConfiguration configurationFile) {
		configurations.put("Messages.Command.Unable", "<red>I'm sorry, but you're not allowed to use that command.");
		configurations.put("Messages.Command.None", "<yellow>Command $command $arguments could not be found! Try using $command help.");
		configurations.put("Messages.Command.Charge", "<white>You have been charged <gold>$amount<white> for using $command.");
		configurations.put("Messages.Inventory.Charge", "<white>You have been charged <gold>$amount<white> for opening inventory of type \"$type.\"");
		configurations.put("Messages.Inventory.NoTime", "<white>You have run out of time for using inventory of type \"$type.\"");
		configurations.put("Messages.Inventory.TimeRemoved", "<white>You used $amount of time for inventory of type \"$type.\"");
		configurations.put("Messages.Credit.Empty", "<white>You currently have no inventory time credits for type \"$type.\"");
		configurations.put("Messages.Package.Empty", "<white>No packages to display for inventory of type \"$type\".");
		configurations.put("Messages.Package.None", "<white>Package \"$name\" does not exist for inventory of type \"$type\".");
		configurations.put("Messages.Package.Bought", "<white> Successfully purchased package \"$name\" for inventory of type $type.");
		configurations.put("Messages.Package.Unable", "<red>I'm sorry, but you have no time credits for inventory of type $type.");
		configurations.put("Messages.General.NoPerm", "<red>I'm sorry, but you do not have permission to do that.");
		configurations.put("Messages.General.NoPlayer", "<red>Unable to locate player \"$player\"!");
		configurations.put("Messages.General.Saved", "<yellow>Successfully saved all TNE Data!");
		configurations.put("Messages.Admin.NoBalance", "<red>$player has no balance data for the world \"$world\"!");
		configurations.put("Messages.Admin.NoBank", "<red>$player has no bank data for the world \"$world\"!");
		configurations.put("Messages.Admin.Balance", "<white>$player currently has <gold>$amount <white>for world \"$world\"!");
		configurations.put("Messages.Account.Locked", "<red>You can't do that with a locked account($player)!");
		configurations.put("Messages.Account.Set", "<yellow>You must use /pin set before accessing your money and/or bank.");
		configurations.put("Messages.Account.Confirm", "<yellow>You must use /pin confirm before accessing your money and/or bank.");
		configurations.put("Messages.Pin.Set", "<white>Your pin has been set successfully.");
		configurations.put("Messages.Pin.Confirmed", "<white>Your pin has been confirmed successfully.");
		configurations.put("Messages.Pin.Already", "<white>Your pin has already been confirmed.");
		configurations.put("Messages.Money.Given", "<white>You were given <gold>$amount<white>.");
		configurations.put("Messages.Money.Received", "<white>You were paid <gold>$amount <white> by <white> $from.");
		configurations.put("Messages.Money.Taken", "<white>$from took <gold>$amount<white> from you.");
		configurations.put("Messages.Money.Insufficient", "<red>I'm sorry, but you do not have <gold>$amount<red>.");
		configurations.put("Messages.Money.Balance", "<white>You currently have <gold>$amount<white> on you.");
		configurations.put("Messages.Money.Gave", "<white>Successfully gave $player <gold>$amount<white>.");
		configurations.put("Messages.Money.Paid", "<white>Successfully paid $player <gold>$amount<white>.");
		configurations.put("Messages.Money.Took", "<white>Successfully took <gold>$amount<white> from $player.");
		configurations.put("Messages.Money.Negative", "<red>Amount cannot be a negative value!");
		configurations.put("Messages.Money.SelfPay", "<red>You can't pay yourself!");
		configurations.put("Messages.Auction.None", "<white>Auction for lot $lot has ended with no bids.");
		configurations.put("Messages.Auction.Start", "<white>Auction for lot $lot has started! $command_string");
		configurations.put("Messages.Auction.Announce", "<white>Auction for lot #lot ends in $time.");
		configurations.put("Messages.Auction.Winner", "<white>$player has won the auction for lot $lot for <gold>$amount<white>.");
		configurations.put("Messages.Auction.Win", "<white>Congratulations! You won the auction for lot $lot for <gold>$amount<white>.");
		configurations.put("Messages.Bank.Added", "<white>$player has been added to your bank!");
		configurations.put("Messages.Bank.Removed", "<white>$player has been removed from your bank!");
		configurations.put("Messages.Bank.Already", "<red>You already own a bank!");
		configurations.put("Messages.Bank.Bought", "<white>Congratulations! You have successfully purchased a bank!");
		configurations.put("Messages.Bank.Insufficient", "<red>I'm sorry, but you need at least <gold>$amount<red> to create a bank.");
		configurations.put("Messages.Bank.Overdraw", "<red>I'm sorry, but the bank of $name does not have <gold>$amount<red>.");
		configurations.put("Messages.Bank.None", "<red>I'm sorry, but you do not own a bank. Please try /bank buy to buy one.");
		configurations.put("Messages.Bank.NoNPC", "<red>I'm sorry, but accessing banks via NPCs has been disabled in this world!");
		configurations.put("Messages.Bank.NoSign", "<red>I'm sorry, but accessing banks via signs has been disabled in this world!");
		configurations.put("Messages.Bank.NoCommand", "<red>I'm sorry, but accessing banks via /bank has been disabled in this world!");
		configurations.put("Messages.Bank.Disabled", "<red>I'm sorry, but banks are disabled in this world.");
		configurations.put("Messages.Bank.Balance", "<white>There is currently <gold>$amount<white> in the bank of $name.");
		configurations.put("Messages.Bank.Deposit", "<white>You have deposited <gold>$amount<white> in the bank of $name.");
		configurations.put("Messages.Bank.Withdraw", "<white>You have withdrawn <gold>$amount<gold> from the bank of $name.");
		configurations.put("Messages.Bank.Cost", "<white>A bank is currently <gold>$amount<white>.");
		configurations.put("Messages.Bank.Invalid", "<red>I'm sorry, but you don't have access to $owner's bank!");
		configurations.put("Messages.Objects.PotionWarning", "<white>Once finished, you'll be charged <gold>$amount<white> for removing your potions from the brewing stand.");
		configurations.put("Messages.Objects.CraftingCharged", "<white> You were charged <gold>$amount<white> for crafting $stack_size \"$item\".");
		configurations.put("Messages.Objects.CraftingPaid", "<white> You were given <gold>$amount<white> for crafting $stack_size \"$item\".");
		configurations.put("Messages.Objects.SmeltingCharged", "<white> You were charged <gold>$amount<white> for smelting $stack_size \"$item\".");
		configurations.put("Messages.Objects.SmeltingPaid", "<white> You were given <gold>$amount<white> for smelting $stack_size \"$item\".");
		configurations.put("Messages.Objects.EnchantingCharged", "<white> You were charged <gold>$amount<white> for enchanting $stack_size \"$item\".");
		configurations.put("Messages.Objects.EnchantingPaid", "<white> You were given <gold>$amount<white> for enchanting $stack_size \"$item\".");
		configurations.put("Messages.Objects.BrewingCharged", "<white> You were charged <gold>$amount<white> for brewing $stack_size \"$item\".");
		configurations.put("Messages.Objects.BrewingPaid", "<white> You were given <gold>$amount<white> for brewing $stack_size \"$item\".");
		configurations.put("Messages.Objects.MiningCharged", "<white> You were charged <gold>$amount<white> for mining one \"$name\" block.");
		configurations.put("Messages.Objects.MiningPaid", "<white> You were given <gold>$amount<white> for mining one \"$name\" block.");
		configurations.put("Messages.Objects.PlacingCharged", "<white> You were charged <gold>$amount<white> for placing one \"$name\" block.");
		configurations.put("Messages.Objects.PlacingPaid", "<white> You were given <gold>$amount<white> for placing one \"$name\" block.");
		configurations.put("Messages.Objects.ItemUseCharged", "<white> You were charged <gold>$amount<white> for using item \"$name\".");
		configurations.put("Messages.Objects.ItemUsePaid", "<white> You were given <gold>$amount<white> for using item \"$name\".");
		configurations.put("Messages.Objects.PotionUseCharged", "<white> You were charged <gold>$amount<white> for using a \"$name\" potion.");
		configurations.put("Messages.Objects.PotionUsePaid", "<white> You were given <gold>$amount<white> for using a \"$name\" potion.");
		configurations.put("Messages.Mob.Killed", "<white>You received $reward <white>for killing a <green>$mob<white>.");
		configurations.put("Messages.Mob.KilledVowel", "<white>You received $reward <white>for killing an <green>$mob<white>.");
		configurations.put("Messages.World.Change", "<white>You have been charged <gold> $amount<white> for changing worlds.");
		configurations.put("Messages.World.ChangeFailed", "<red>I'm sorry, but you need at least <gold>$amount<red> to change worlds.");
		
		super.load(configurationFile);
	}
}