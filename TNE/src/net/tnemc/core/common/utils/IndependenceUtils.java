package net.tnemc.core.common.utils;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import net.tnemc.core.TNE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class IndependenceUtils {

  public static short duration = 0;
  public static BukkitTask task = null;

  public static void play(final Location location) {
    final Song song = NBSDecoder.parse(TNE.instance().getResource("song.nbs"));
    duration = 0;

    final short length = 117;
    task = Bukkit.getScheduler().runTaskTimerAsynchronously(TNE.instance(), new Runnable() {
      @Override
      public void run() {
        duration++;
        Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));

        if(duration % 2 == 0) {
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
        }

        if(duration % 6 == 0) {

          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
        }

        TNE.debug("Length: " + length);
        TNE.debug("Duration: " + duration);

        if(duration == length) {
          Bukkit.broadcastMessage(ChatColor.RED + "Happy " + ChatColor.WHITE + "Independence " + ChatColor.BLUE + "Day!");
          Bukkit.broadcastMessage(ChatColor.RED + "Happy " + ChatColor.WHITE + "Independence " + ChatColor.BLUE + "Day!");
          Bukkit.broadcastMessage(ChatColor.RED + "Happy " + ChatColor.WHITE + "Independence " + ChatColor.BLUE + "Day!");
          Bukkit.broadcastMessage(ChatColor.RED + "Happy " + ChatColor.WHITE + "Independence " + ChatColor.BLUE + "Day!");
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          Bukkit.getScheduler().runTask(TNE.instance(), ()->MaterialUtils.spawnRandomFirework(location));
          kill();
        }
      }
    }, 0L, 20);

    RadioSongPlayer player = new RadioSongPlayer(song);
    Bukkit.getOnlinePlayers().forEach(player::addPlayer);
    player.setPlaying(true);
    player.setRepeatMode(RepeatMode.NO);
  }

  public static void kill() {
    if(task != null) {
      task.cancel();
    }
  }
}