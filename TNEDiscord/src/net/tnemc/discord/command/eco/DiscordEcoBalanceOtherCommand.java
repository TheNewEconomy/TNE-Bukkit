package net.tnemc.discord.command.eco;

import github.scarsz.discordsrv.dependencies.jda.core.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.core.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.core.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.core.entities.User;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.discord.command.DiscordCommand;
import org.bukkit.Bukkit;

import java.awt.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/7/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class DiscordEcoBalanceOtherCommand extends DiscordCommand {
  @Override
  public String name() {
    return "balanceother";
  }

  @Override
  public String role() {
    return TNE.instance().api().getString("Discord.Roles.BalanceOther");
  }

  @Override
  public EmbedBuilder help(String command) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setTitle(command + " balanceother");
    builder.setDescription("Usage: " + command + " balanceother <discord id>");
    final String id = (command.contains("deco"))? "Discord" : "Minecraft";
    builder.addField("Description", "Checks another user's " + id + " balance.", false);
    builder.setColor(new Color(190, 57, 0));
    return builder;
  }

  @Override
  public boolean execute(User user, Guild guild, UUID minecraft, boolean fake, MessageChannel channel, String command, String[] arguments) {
    TNE.debug("===START MoneyOtherCommand  ===");
    if(arguments.length >= 1 && arguments.length <= 3) {
      final String world = (arguments.length >= 2)? TNE.instance().sanitizeWorld(arguments[1]) : TNE.instance().defaultWorld;
      final String currencyName = (arguments.length >= 3)? arguments[2] : TNE.manager().currencyManager().get(world).name();

      if(!validateDiscordID(arguments[0])) {
        channel.sendMessage("Invalid discord id").queue();
        return false;
      }

      final UUID id = getID(arguments[0], fake);

      final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        channel.sendMessage(Message.replaceColours(new Message("Messages.General.Disabled").grab(world, Bukkit.getConsoleSender()), true)).queue();
        return false;
      }

      TNETransaction transaction = new TNETransaction(TNE.manager().getAccount(id), TNE.manager().getAccount(id), world, TNE.transactionManager().getType("inquiry"));
      transaction.setRecipientCharge(new TransactionCharge(world, currency, BigDecimal.ZERO));
      TransactionResult result = transaction.perform();
      Message message = new Message(result.initiatorMessage());
      message.addVariable("$player", arguments[0]);
      message.addVariable("$world", world);
      message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(world, transaction.recipientBalance().getCurrency().name()), world, transaction.recipientBalance().getAmount(), transaction.recipient()));
      channel.sendMessage(Message.replaceColours(message.grab(world, Bukkit.getConsoleSender()), true)).queue();
      TNE.debug("===END MoneyOtherCommand  ===");
      return result.proceed();
    }
    channel.sendMessage(help(command).build()).queue();
    TNE.debug("===END MoneyOtherCommand  ===");
    return false;
  }
}