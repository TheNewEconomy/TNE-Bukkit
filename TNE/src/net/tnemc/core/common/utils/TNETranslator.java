package net.tnemc.core.common.utils;

import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.commands.core.utils.CommandTranslator;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/16/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TNETranslator implements CommandTranslator {
  @Override
  public Optional<List<String>> translateToList(String text, Optional<PlayerProvider> sender) {
    System.out.println("Translate2: " + text);
    if(sender.isPresent()) {

      System.out.println("made it2");
      CommandSender bukkitSender = (sender.get().isPlayer())? Bukkit.getPlayer(sender.get().getUUID())
          : Bukkit.getConsoleSender();
      return Optional.of(Arrays.asList(new Message(text).format(TNE.instance().defaultWorld, bukkitSender, "")));
    }
    return Optional.empty();
  }

  @Override
  public Optional<String> translateText(String text, Optional<PlayerProvider> sender) {
    System.out.println("Translate: " + text);
    if(sender.isPresent()) {

      System.out.println("made it");
      CommandSender bukkitSender = (sender.get().isPlayer())? Bukkit.getPlayer(sender.get().getUUID())
          : Bukkit.getConsoleSender();
      bukkitSender.sendMessage(Message.replaceColours(new Message(text).grab(TNE.instance().defaultWorld, bukkitSender), false));
      return Optional.of(Message.replaceColours(new Message(text).grab(TNE.instance().defaultWorld, bukkitSender), false));
    }
    return Optional.empty();
  }
}