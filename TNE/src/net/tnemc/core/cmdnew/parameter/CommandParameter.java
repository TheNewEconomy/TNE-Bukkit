package net.tnemc.core.cmdnew.parameter;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/12/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandParameter {

  private String name;
  private boolean optional;
  private boolean tabComplete;
  private String completeType;

  public CommandParameter(String name) {
    this.name = name;
  }

  public CommandParameter(String name, boolean optional, boolean tabComplete, String completeType) {
    this.name = name;
    this.optional = optional;
    this.tabComplete = tabComplete;
    this.completeType = completeType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isOptional() {
    return optional;
  }

  public void setOptional(boolean optional) {
    this.optional = optional;
  }

  public boolean isTabComplete() {
    return tabComplete;
  }

  public void setTabComplete(boolean tabComplete) {
    this.tabComplete = tabComplete;
  }

  public String getCompleteType() {
    return completeType;
  }

  public void setCompleteType(String completeType) {
    this.completeType = completeType;
  }
}