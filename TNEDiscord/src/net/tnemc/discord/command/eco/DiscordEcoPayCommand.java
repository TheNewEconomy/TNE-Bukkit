package net.tnemc.discord.command.eco;

import github.scarsz.discordsrv.dependencies.jda.core.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.core.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.core.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.core.entities.User;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.discord.command.DiscordCommand;
import net.tnemc.discord.transaction.MultiTransactionHandler;
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
public class DiscordEcoPayCommand extends DiscordCommand {
  @Override
  public String name() {
    return "pay";
  }

  @Override
  public String role() {
    return TNE.instance().api().getString("Discord.Roles.Pay");
  }

  @Override
  public EmbedBuilder help(String command) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setTitle(command + " pay");
    builder.setDescription("Usage: " + command + " pay <discord id> <amount> [world] [currency]");
    final String id = (command.contains("deco")) ? "Discord" : "Minecraft";
    builder.addField("Description", "Pays the " + id + " + account an amount from your own funds.", false);
    builder.setColor(new Color(190, 57, 0));
    return builder;
  }

  @Override
  public boolean execute(User user, Guild guild, UUID minecraft, boolean fake, MessageChannel channel, String command, String[] arguments) {
    if(arguments.length < 2) {
      channel.sendMessage(help(command).build()).queue();
      return false;
    }
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      TNE.debug("===START MoneyPayCommand ===");
      final String world = (arguments.length >= 3)? TNE.instance().sanitizeWorld(arguments[2]) : TNE.instance().defaultWorld;
      final String currencyName = (arguments.length >= 4)? arguments[3] : TNE.manager().currencyManager().get(world).name();


      if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
        channel.sendMessage(Message.replaceColours(new Message("Messages.General.Disabled").grab(world, Bukkit.getConsoleSender()), true)).queue();
        return;
      }

      if(!arguments[0].contains(",") && !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Faction")) &&
          !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Town")) &&
          !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Nation")) && IDFinder.getOffline(arguments[0], true) == null) {
        channel.sendMessage(Message.replaceColours(new Message(TNE.transactionManager().getResult("failed").initiatorMessage()).grab(world, Bukkit.getConsoleSender()), true)).queue();
        return;
      }

      if (!TNE.manager().currencyManager().contains(world, currencyName)) {
        Message m = new Message("Messages.Money.NoCurrency");
        m.addVariable("$currency", currencyName);
        m.addVariable("$world", world);
        channel.sendMessage(Message.replaceColours(m.grab(world, Bukkit.getConsoleSender()), true)).queue();
        return;
      }

      TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

      String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
      if(parsed.contains("Messages")) {
        Message msg = new Message(parsed);
        msg.addVariable("$currency", currency.name());
        msg.addVariable("$world", world);
        msg.addVariable("$player", IDFinder.getUsername(minecraft.toString()));
        channel.sendMessage(Message.replaceColours(msg.grab(world, Bukkit.getConsoleSender()), true)).queue();
        TNE.debug("===END MoneyPayCommand ===");
        return;
      }

      BigDecimal value = new BigDecimal(parsed);

      if(value.compareTo(BigDecimal.ZERO) < 0) {
        Message msg = new Message("Messages.Money.Negative");
        msg.addVariable("$currency", currency.name());
        msg.addVariable("$world", world);
        msg.addVariable("$player", IDFinder.getUsername(minecraft.toString()));
        channel.sendMessage(Message.replaceColours(msg.grab(world, Bukkit.getConsoleSender()), true)).queue();
        return;
      }

      MultiTransactionHandler handler = new MultiTransactionHandler(channel, MultiTransactionHandler.parsePlayerArgument(arguments[0], true, fake),
          "pay", value, currency, world,
          TNE.manager().getAccount(minecraft));
      handler.handle(true);
      TNE.debug("===END MoneyPayCommand ===");
    });
    return true;
  }
}