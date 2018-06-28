package net.tnemc.tutorial.tutorials.impl;

import net.tnemc.core.TNE;
import net.tnemc.tutorial.TutorialModule;
import net.tnemc.tutorial.tutorials.Tutorial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/29/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MoneyTutorial implements Tutorial {
  @Override
  public String name() {
    return "money";
  }

  @Override
  public void run(Player player) {
    player.sendTitle("Money Tutorial.", "By creatorfromhell.", 5, 50, 10);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "/money", ChatColor.RED + "The command to your future.", 5, 50, 10);
      });
    }, 50);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle("Starting funds.", ChatColor.RED + "These funds are for the tutorial.", 5, 50, 10);
      });
    }, 100);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(null, ChatColor.RED + "We'll start you with 1k.", 5, 50, 10);
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "money give " + player.getName() + " 1000");
      });
    }, 150);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "/bal, /balance, /money", ChatColor.RED + "Check your funds.", 5, 50, 10);
      });
    }, 200);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendMessage("/bal");
        Bukkit.dispatchCommand(player, "bal");
      });
    }, 250);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "/money pay, /pay", ChatColor.RED + "Pay players money.", 5, 50, 10);
      });
    }, 300);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendMessage("/pay Server_Account 50");
        Bukkit.dispatchCommand(player, "pay Server_Account 50");
      });
    }, 350);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "Pay Nations", ChatColor.RED + "/money pay nation-<nation name>.", 5, 50, 10);
      });
    }, 400);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendMessage("/pay nation-test 50");
      });
    }, 450);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "Pay Towns", ChatColor.RED + "/money pay town-<nation name>.", 5, 50, 10);
      });
    }, 500);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendMessage("/pay town-qaf 50");
      });
    }, 550);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "Multi-Player pay", ChatColor.RED + "/pay player1,player2 pays multiple players.", 5, 50, 10);
      });
    }, 600);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tne create test_player");
        player.sendMessage("/pay Server_Account,test_player 50");
        Bukkit.dispatchCommand(player, "pay Server_Account,test_player 50");
      });
    }, 650);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "/money top, /baltop", ChatColor.RED + "See top balance holders.", 5, 50, 10);
      });
    }, 700);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendMessage("/baltop");
        Bukkit.dispatchCommand(player, "baltop");
      });
    }, 750);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "/money note", ChatColor.RED + "Convert digital currencies to physical form.", 5, 50, 10);
      });
    }, 800);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "/money set", ChatColor.RED + "Set a player's balance.", 5, 50, 10);
      });
    }, 850);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendMessage("/money set test_player 500");
        Bukkit.dispatchCommand(player, "money set test_player 500");
      });
    }, 900);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "/money give", ChatColor.RED + "Give a player funds.", 5, 50, 10);
      });
    }, 950);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendMessage("/money give test_player 500");
        Bukkit.dispatchCommand(player, "money give test_player 500");
      });
    }, 1000);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendTitle(ChatColor.RED + "/money take", ChatColor.RED + "Take funds from a player.", 5, 50, 10);
      });
    }, 1050);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        player.sendMessage("/money take test_player 500");
        Bukkit.dispatchCommand(player, "money take test_player 500");
      });
    }, 1100);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        reset(player);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "money take " + player.getName() + " 850");
        player.sendTitle(null, ChatColor.RED + "That concludes this tutorial.", 5, 50, 10);
        TutorialModule.manager().removeLearner(player.getUniqueId());
      });
    }, 1150);
  }
}