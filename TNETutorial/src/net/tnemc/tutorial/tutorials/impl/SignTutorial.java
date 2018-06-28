package net.tnemc.tutorial.tutorials.impl;

import net.tnemc.core.TNE;
import net.tnemc.tutorial.TutorialModule;
import net.tnemc.tutorial.tutorials.Tutorial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
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
public class SignTutorial implements Tutorial {
  @Override
  public String name() {
    return "sign";
  }

  @Override
  public Location startingLocation(Player player) {
    return TutorialModule.lookAt(player.getLocation(), player.getLocation().add(0, 0, 3));
  }

  @Override
  public void run(Player player) {
    final Location blockLocation = player.getLocation().add(0, 1, 3);
    final Location signLocation = blockLocation.clone().add(0, 0, -1);

    player.sendTitle("TNE Sign Tutorial.", "By creatorfromhell.", 5, 50, 10);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        blockLocation.getBlock().setType(Material.WOOD);
        signLocation.getBlock().setType(Material.WALL_SIGN);
        reset(player);
        player.sendTitle(null, ChatColor.RED + "This is a sign.", 5, 50, 10);
      });
    }, 50);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(null, ChatColor.RED + "TNE has custom sign types.", 5, 50, 10);
    }, 100);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(null, ChatColor.RED + "Today we'll walk through them.", 5, 50, 10);
    }, 150);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        final Sign sign = (Sign)signLocation.getBlock().getState();
        sign.setLine(0, ChatColor.DARK_PURPLE + "[safe]");
        sign.setLine(1, "playername");
        sign.update(true);
      });
      reset(player);
      player.sendTitle(null, ChatColor.RED + "This is a safe sign.", 5, 50, 10);
    }, 200);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "Attach to a chest.", ChatColor.RED + "Used to store your gold.", 5, 50, 10);
    }, 250);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(null, ChatColor.RED + "The second line is the owners's name.", 5, 50, 10);
    }, 300);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        final Sign sign = (Sign)signLocation.getBlock().getState();
        sign.setLine(0, ChatColor.DARK_PURPLE + "[nation]");
        sign.setLine(1, "TestNation");
        sign.update(true);
      });
      reset(player);
      player.sendTitle(null, ChatColor.RED + "This is a nation sign.", 5, 50, 10);
    }, 350);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "Attach to a chest.", ChatColor.RED + "Used to store your nation's gold.", 5, 50, 10);
    }, 400);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(null, ChatColor.RED + "The second line is the nation's name.", 5, 50, 10);
    }, 450);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        final Sign sign = (Sign)signLocation.getBlock().getState();
        sign.setLine(0, ChatColor.DARK_PURPLE + "[town]");
        sign.setLine(1, "TestTown");
        sign.update(true);
      });
      reset(player);
      player.sendTitle(null, ChatColor.RED + "This is a town sign.", 5, 50, 10);
    }, 500);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "Attach to a chest.", ChatColor.RED + "Used to store your town's gold.", 5, 50, 10);
    }, 550);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(null, ChatColor.RED + "The second line is the towns's name.", 5, 50, 10);
    }, 600);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        final Sign sign = (Sign)signLocation.getBlock().getState();
        sign.setLine(0, ChatColor.DARK_PURPLE + "[item]");
        sign.setLine(1, "1:Gold");
        sign.setLine(2, "$20");
        sign.update(true);
      });
      reset(player);
      player.sendTitle(null, ChatColor.RED + "This is an item sign.", 5, 50, 10);
    }, 650);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "Attach to any block.", ChatColor.RED + "Used to sell items.", 5, 50, 10);
    }, 700);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "Line 2, your offer.", ChatColor.RED + "Quantity : Item Name, or $amount.", 5, 50, 10);
    }, 750);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "Line 3, your request.", ChatColor.RED + "Quantity : Item Name, or $amount.", 5, 50, 10);
    }, 800);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        final Sign sign = (Sign)signLocation.getBlock().getState();
        sign.setLine(0, ChatColor.DARK_PURPLE + "[sitem]");
        sign.update(true);
      });
      reset(player);
      player.sendTitle(null, ChatColor.RED + "This is a sitem sign.", 5, 50, 10);
    }, 850);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "Attach to any block.", ChatColor.RED + "Used to sell special items.", 5, 50, 10);
    }, 900);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "Attach to any block.", ChatColor.RED + "These include enchanted, and named.", 5, 50, 10);
    }, 950);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      reset(player);
      player.sendTitle(ChatColor.RED + "After placing.", ChatColor.RED + "Right click to setup.", 5, 50, 10);
    }, 1000);
    Bukkit.getScheduler().runTaskLaterAsynchronously(TNE.instance(), ()->{
      Bukkit.getScheduler().runTask(TNE.instance(), ()->{
        signLocation.getBlock().setType(Material.AIR);
        blockLocation.getBlock().setType(Material.AIR);
      });
      reset(player);
      player.sendTitle(null, ChatColor.RED + "That concludes this tutorial.", 5, 50, 10);
      TutorialModule.manager().removeLearner(player.getUniqueId());
    }, 1050);
  }
}