package net.tnemc.core.common.menu.icon;

import java.util.function.Predicate;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/5/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ChatResponse {

  private final String dataName;
  private String menu = "";
  private final Predicate<String> validator;

  public ChatResponse(String dataName, Predicate<String> validator) {
    this.dataName = dataName;
    this.validator = validator;
  }

  public ChatResponse(String dataName, String menu, Predicate<String> validator) {
    this.dataName = dataName;
    this.menu = menu;
    this.validator = validator;
  }

  public String getDataName() {
    return dataName;
  }

  public String getMenu() {
    return menu;
  }

  public void setMenu(String menu) {
    this.menu = menu;
  }

  public Predicate<String> getValidator() {
    return validator;
  }
}