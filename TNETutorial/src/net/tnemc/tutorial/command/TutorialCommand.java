package net.tnemc.tutorial.command;

import com.github.tnerevival.TNELib;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.tutorial.TutorialModule;
import net.tnemc.tutorial.tutorials.Learner;
import net.tnemc.tutorial.tutorials.Tutorial;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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
public class TutorialCommand extends TNECommand {

  public TutorialCommand(TNELib plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "tutorial";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "tut"
    };
  }

  @Override
  public String getNode() {
    return "tne.command.tutorial";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(TutorialModule.manager().getLearners().containsKey(getPlayer(sender).getUniqueId())) {
      help(sender);
      return false;
    }
    if(arguments.length >= 1) {
      String tutorial = arguments[0].toLowerCase();
      Tutorial tutorialInstance = TutorialModule.manager().getTutorials().get(tutorial);
      if(tutorialInstance == null) {
        sender.sendMessage(ChatColor.RED + "No tutorial with that name exists.");
        return false;
      }
      if(tutorialInstance.startingLocation(getPlayer(sender)) != null) {
        getPlayer(sender).teleport(tutorialInstance.startingLocation(getPlayer(sender)));
      }
      Learner learner = new Learner(getPlayer(sender).getUniqueId(), getPlayer(sender).isOp());
      TutorialModule.manager().addLearner(learner);
      tutorialInstance.run(getPlayer(sender));
      return true;
    }
    help(sender);
    return false;
  }
}