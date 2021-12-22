package net.tnemc.discord;

import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.tnemc.core.TNE;
import net.tnemc.core.event.transaction.TNETransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.awt.*;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/6/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TransactionListener implements Listener {

  private TNE plugin;

  public TransactionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onEvent(TNETransactionEvent event) {

    if(TNE.instance().api().getBoolean("Discord.Transactions.Log")) {
      if (event.getResult().proceed()) {
        final String initiator = (event.getTransaction().getInitiator() != null) ? event.getTransaction().getInitiator().displayName() : "Not Logged";
        final String initiatorAmount = (event.getTransaction().initiatorBalance() != null) ? event.getTransaction().initiatorBalance().getAmount().toPlainString() : "Not Logged";
        final String initiatorCharge = (event.getTransaction().initiatorCharge() != null) ? event.getTransaction().initiatorCharge().getAmount().toPlainString() : "Not Logged";
        final String recipient = (event.getTransaction().getRecipient() != null) ? event.getTransaction().getRecipient().displayName() : "Not Logged";
        final String recipientAmount = (event.getTransaction().recipientBalance() != null) ? event.getTransaction().recipientBalance().getAmount().toPlainString() : "Not Logged";
        final String recipientCharge = (event.getTransaction().recipientCharge() != null) ? event.getTransaction().recipientCharge().getAmount().toPlainString() : "Not Logged";

        StringBuilder description = new StringBuilder();
        description.append("Type: " + event.getTransaction().type().name());
        description.append(System.lineSeparator());
        description.append("Successful: " + event.getResult().proceed());
        description.append(System.lineSeparator());
        description.append("World: " + event.getTransaction().getWorld());

        StringBuilder initiatorBuilder = new StringBuilder();
        initiatorBuilder.append("Name: " + initiator);
        initiatorBuilder.append(System.lineSeparator());
        initiatorBuilder.append("Initial Balance: " + initiatorAmount);
        initiatorBuilder.append(System.lineSeparator());
        initiatorBuilder.append("Charge Amount: " + initiatorCharge);


        StringBuilder recipientBuilder = new StringBuilder();
        recipientBuilder.append("Name: " + recipient);
        recipientBuilder.append(System.lineSeparator());
        recipientBuilder.append("Initial Balance: " + recipientAmount);
        recipientBuilder.append(System.lineSeparator());
        recipientBuilder.append("Charge Amount: " + recipientCharge);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Transaction: " + event.getTransaction().transactionID().toString());
        embed.setDescription(description.toString());
        embed.addField("Initiator", initiatorBuilder.toString(), false);
        embed.addField("Recipient", recipientBuilder.toString(), false);
        embed.setColor(new Color(190, 57, 0));
        try {
          DiscordUtil.getJda().getTextChannelById(TNE.instance().api().getString("Discord.Transactions.Channel")).sendMessage(embed.build()).queue();
        } catch(Exception ignore) {
          TNE.logger().warning("Attempted to log TNE transaction to discord, but an invalid channel ID was specified in discord.yml. To remove this message remove plugins/TheNewEconomy/modules/Discord.jar, or configure it properly.");
        }
      }
    }
  }
}