package net.tnemc.discord.command.eco;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Guild;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
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

;
;
;
;

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
public class DiscordEcoGiveCommand extends DiscordCommand {
  @Override
  public String name() {
    return "give";
  }

  @Override
  public String role() {
    return TNE.instance().api().getString("Discord.Roles.Give");
  }

  @Override
  public EmbedBuilder help(String command) {
    EmbedBuilder builder = new EmbedBuilder();
    builder.setTitle(command + " give");
    builder.setDescription("Usage: " + command + " give <discord id> <amount> [world] [currency]");
    final String id = (command.contains("deco"))? "Discord" : "Minecraft";
    builder.addField("Description", "Gives a " + id + " user a certain amount of funds.", false);
    builder.setColor(new Color(190, 57, 0));
    return builder;
  }

  @Override
  public boolean execute(User user, Guild guild, UUID minecraft, boolean fake, MessageChannel channel, String command, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      TNE.debug("===START MoneyGiveCommand ===");
      if(arguments.length >= 2) {
        final String world = (arguments.length >= 3)? TNE.instance().sanitizeWorld(arguments[2]) : TNE.instance().defaultWorld;
        final String currencyName = (arguments.length >= 4)? arguments[3] : TNE.manager().currencyManager().get(world).name();

        if (!TNE.manager().currencyManager().contains(world, currencyName)) {
          Message m = new Message("Messages.Money.NoCurrency");
          m.addVariable("$currency", currencyName);
          m.addVariable("$world", world);
          channel.sendMessage(Message.replaceColours(m.grab(world, Bukkit.getConsoleSender()), true)).queue();
          return;
        }

        final TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);

        if(TNE.instance().getWorldManager(world).isEconomyDisabled()) {
          channel.sendMessage(Message.replaceColours(new Message("Messages.General.Disabled").grab(world, Bukkit.getConsoleSender()), true)).queue();
          return;
        }

        if(!arguments[0].contains(",") && !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Faction")) &&
            !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Town")) &&
            !arguments[0].contains(TNE.instance().api().getString("Core.Server.ThirdParty.Nation")) && IDFinder.getOffline(arguments[0], true) == null) {
          channel.sendMessage(new Message(TNE.transactionManager().getResult("failed").initiatorMessage()).grab(world, Bukkit.getConsoleSender())).queue();
          return;
        }

        String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
        if(parsed.contains("Messages")) {
          Message max = new Message(parsed);
          max.addVariable("$currency", currency.name());
          max.addVariable("$world", world);
          max.addVariable("$player", IDFinder.getUsername(minecraft.toString()));
          channel.sendMessage(Message.replaceColours(max.grab(world, Bukkit.getConsoleSender()), true)).queue();
          TNE.debug("===END MoneyGiveCommand ===");
          return;
        }

        final BigDecimal value = new BigDecimal(parsed);

        if(!validateDiscordID(arguments[0])) {
          channel.sendMessage("Invalid discord id").queue();
          return;
        }

        final MultiTransactionHandler handler = new MultiTransactionHandler(channel, MultiTransactionHandler.parsePlayerArgument(arguments[0], false, fake),
            "give", value, currency, world,
            TNE.manager().getAccount(minecraft));
        handler.handle(true);
        TNE.debug("===END MoneyGiveCommand ===");
        return;
      }
      help(command);
      TNE.debug("===END MoneyGiveCommand ===");
    });
    return true;
  }
}