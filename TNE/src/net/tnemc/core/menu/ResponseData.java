package net.tnemc.core.menu;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/31/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ResponseData {

  private final String chat;
  private final String menu;

  public ResponseData(String chat, String menu) {
    this.chat = chat;
    this.menu = menu;
  }

  public String getChat() {
    return chat;
  }

  public String getMenu() {
    return menu;
  }
}